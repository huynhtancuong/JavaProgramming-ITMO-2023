package server.utility;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.utility.Outputer;
import server.App;
import server.Server;

import java.io.*;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Handles user connection.
 */
public class ConnectionHandler implements Runnable {
    private final Server server;
    private final HandleResquestTask handleResquestTask;

    // NonBlocking IO
    private final DatagramChannel datagramChannel;
    private SocketAddress addr;
    public Request userRequest;
    public Response responseToUser;
    public ReadWriteLock readWriteLock;


    public ConnectionHandler(Server server, DatagramChannel datagramChannel, HandleResquestTask handleResquestTask) {
        this.server = server;
        this.datagramChannel = datagramChannel;
        this.handleResquestTask = handleResquestTask;
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    /**
     * Main handling cycle.
     */
    @Override
    public void run() {
        while (!server.isStopped) {
            try {
                // Read and process the user request
                Thread readRequestThread = new Thread(new ReadRequest(this));
                readRequestThread.start();
                readRequestThread.join();

                if (userRequest != null) {
                    Thread processRequestThread = new Thread(new ProcessRequest(this, userRequest));
                    processRequestThread.start();
                    processRequestThread.join();
                }

                // Write back the response to the client
                try {
                    if (responseToUser != null && userRequest != null) {
                        Thread writeResponseThread = new Thread(new WriteResponse(responseToUser));
                        writeResponseThread.start();
                        writeResponseThread.join();
                        // When client type server_exit
                        if (responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT) {
                            server.isStopped = true;
                            server.stop();
                        }
                    }
                } catch (NullPointerException e) {
                    Outputer.printerror("An error occurred while sending data to the client!");
                    App.logger.error("An error occurred while sending data to the client!");
                }

            } catch (InterruptedException e) {
                Outputer.printerror("Server is interrupted!");
                App.logger.error("Server is interrupted!");
            }
        }
    }

    class ReadRequest implements Runnable {
        private final ConnectionHandler connectionHandler;
        private Request read() throws IOException {

            ByteBuffer buffer = ByteBuffer.allocate(1024*16);

            // Nhan request tu client
            addr = datagramChannel.receive(buffer);

            Object obj = null;

            if (addr != null) {
                try {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                    obj = objectInputStream.readObject();
                } catch (Exception e) {
                    App.logger.error("An error had occurred.");
                }
            }

            Request userRequest = null;
            if (obj instanceof Request) {
                userRequest = (Request) obj;
            }

            return userRequest;
        }

        public ReadRequest(ConnectionHandler connectionHandler) {
            this.connectionHandler = connectionHandler;
        }

        @Override
        public void run() {
            try {
                this.connectionHandler.userRequest = read();
            } catch (IOException e) {
//                e.printStackTrace();
                Outputer.printerror("An error occurred while reading received data!");
                App.logger.error("An error occurred while reading received data!");
            }
        }
    }

    class ProcessRequest implements Runnable {
        private final ConnectionHandler connectionHandler;
        private final Request userRequest;

        private Response processRequest(Request userRequest) {
            // Xử lý yêu cầu -> trả lại response
            Response responseToUser;
            try {
                // Lock the write lock
                connectionHandler.readWriteLock.writeLock().lock();
                // Xu ly request cua client va tao ra responseToUser
                responseToUser = handleResquestTask.handle(userRequest);
                App.logger.info("Request '" + userRequest.getCommandName() + "' successfully processed.");
            } finally {
                connectionHandler.readWriteLock.writeLock().unlock();
            }
            return responseToUser;
        }

        public ProcessRequest(ConnectionHandler connectionHandler, Request userRequest) {
            this.connectionHandler = connectionHandler;
            this.userRequest = userRequest;
        }
        @Override
        public void run() {
            connectionHandler.responseToUser = processRequest(userRequest);
        }
    }

    class WriteResponse implements Runnable {
        private final Response responseToUser;

        private void write(Response responseToUser) throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream clientWriter = new ObjectOutputStream(byteArrayOutputStream);
            clientWriter.flush();

            clientWriter.writeObject(responseToUser);

            ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
            //        buffer.flip();
            datagramChannel.send(buffer, addr);
        }

        public WriteResponse(Response responseToUser){
            this.responseToUser = responseToUser;
        }

        @Override
        public void run() {
            try {
                if (responseToUser != null) {
                    write(responseToUser);
                }
            } catch (IOException e) {
                Outputer.printerror("An error occurred while sending data to the client!");
                App.logger.error("An error occurred while sending data to the client!");
            }
        }
    }
}
