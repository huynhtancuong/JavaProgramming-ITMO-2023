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

/**
 * Handles user connection.
 */
public class ConnectionHandler implements Runnable {
    private Server server;
    private CommandManager commandManager;
    private HandleResquestTask handleResquestTask;

    // NonBlocking IO
    private DatagramChannel datagramChannel;
    private SocketAddress addr;
    public Object userRequest = null;
    public Response responseToUser = null;


    public ConnectionHandler(Server server, DatagramChannel datagramChannel, HandleResquestTask handleResquestTask) {
        this.server = server;
        this.datagramChannel = datagramChannel;
        this.handleResquestTask = handleResquestTask;
    }

    /**
     * Main handling cycle.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Read and process the user request
                Thread readRequestThread = new Thread(new ReadRequest(this));
                readRequestThread.start();
                readRequestThread.join();

                Thread processRequestThread = new Thread(new ProcessRequest(this, userRequest));
                processRequestThread.start();
                processRequestThread.join();

                // Write back the response to the client
                try {
                    if (responseToUser != null) {
                        Thread writeResponseThread = new Thread(new WriteResponse(responseToUser));
                        writeResponseThread.start();
                        writeResponseThread.join();
                        // Khi client go lenh server_exit
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
                e.printStackTrace();
            }
        }
    }

    class ReadRequest implements Runnable {
        private final ConnectionHandler connectionHandler;
        private Object userRequest;

        private Object read() throws IOException {

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

            return obj;
        }

        public ReadRequest(ConnectionHandler connectionHandler) {
            this.connectionHandler = connectionHandler;
        }

        @Override
        public void run() {
            try {
                this.connectionHandler.userRequest = read();
            } catch (IOException e) {
                e.printStackTrace();
                Outputer.printerror("An error occurred while reading received data!");
                App.logger.error("An error occurred while reading received data!");
            }
        }
    }

    class ProcessRequest implements Runnable {
        private final ConnectionHandler connectionHandler;
        private final Object userRequest;

        private Response processRequest(Object obj) {
            // Xử lý yêu cầu -> trả lại response
            Response responseToUser = null;
            if (obj instanceof Request userRequest) {
                // Xu ly request cua client va tao ra responseToUser
                responseToUser = handleResquestTask.handle(userRequest);
                App.logger.info("Request '" + userRequest.getCommandName() + "' successfully processed.");

            }
            return responseToUser;
        }

        public ProcessRequest(ConnectionHandler connectionHandler, Object userRequest) {
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
                write(responseToUser);
            } catch (IOException e) {
                Outputer.printerror("An error occurred while sending data to the client!");
                App.logger.error("An error occurred while sending data to the client!");
            }
        }
    }
}
