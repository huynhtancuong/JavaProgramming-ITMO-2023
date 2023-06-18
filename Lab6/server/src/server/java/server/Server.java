package server;

import common.exceptions.ClosingSocketException;
import common.exceptions.OpeningServerSocketException;
import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.utility.Outputer;
import server.utility.RequestHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Runs the server.
 */
public class Server {
    private final int port;

    private final RequestHandler requestHandler;
    // NonBlocking IO
    private DatagramChannel datagramChannel;
    private SocketAddress addr;
    private Request userRequest = null;
//     private Response responseToUser = null;

    public Server(int port, int soTimeout, RequestHandler requestHandler) {
        this.port = port;
        this.requestHandler = requestHandler;
    }

    /**
     * Begins server operation.
     */
    public void run() {

        try {
            openServerSocket();
            boolean processingStatus = true;
            while (processingStatus) {
                Response responseToUser = null;
                try {
                    responseToUser = read();

                }
                catch (StreamCorruptedException | ClassNotFoundException e) {
                    Outputer.printerror("An error occurred while reading received data!");
                    App.logger.error("An error occurred while reading received data!");
                } catch (InvalidClassException | NotSerializableException exception) {
                    Outputer.printerror("An error occurred while reading received data!");
                    App.logger.error("An error occurred while reading received data!");
                } catch (IOException exception) {
                    Outputer.printerror("An error occurred while trying to terminate the connection with the client!");
                    App.logger.error("An error occurred while trying to terminate the connection with the client!");
                }

                try {
                    if (responseToUser != null) {
                        write(responseToUser);
                        if (responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT) {
                            stop();
                        }
                    }
                } catch (StreamCorruptedException | NullPointerException e) {

                } catch (InvalidClassException | NotSerializableException exception) {
                    Outputer.printerror("An error occurred while sending data to the client!");
                    App.logger.error("An error occurred while sending data to the client!");
                } catch (IOException exception) {
                    if (userRequest == null) {
                        Outputer.printerror("Unexpected loss of connection with the client!");
                        App.logger.warn("Unexpected loss of connection with the client!");
                    } else {
                        Outputer.println("Client successfully disconnected from server!");
                        App.logger.info("Client successfully disconnected from server!");
                    }
                }
            }
            stop();
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("Server cannot be started!");
            App.logger.error("Server cannot be started!");
        }
    }


    private Response read() throws IOException, ClassNotFoundException {

        ByteBuffer buffer = ByteBuffer.allocate(1024*16);

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

        Response responseToUser = null;
        if (obj instanceof Request) {
            userRequest = (Request) obj;
            responseToUser = requestHandler.handle(userRequest);
            App.logger.info("Request '" + userRequest.getCommandName() + "' successfully processed.");

        }
        return responseToUser;
    }

    private void write(Response responseToUser) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream clientWriter = new ObjectOutputStream(byteArrayOutputStream);
        clientWriter.flush();

        clientWriter.writeObject(responseToUser);

        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
//        buffer.flip();
        datagramChannel.send(buffer, addr);
    }

    /**
     * Finishes server operation.
     */
    private void stop() {
        try {
            App.logger.info("Shutting down the server...");
//            if (serverSocketChannel == null) throw new ClosingSocketException();
//            serverSocketChannel.close();
            if (datagramChannel == null) throw new ClosingSocketException();
            datagramChannel.close();

            Outputer.println("Server completed successfully.");
            App.logger.info("Server completed successfully.");
            System.exit(0);
        } catch (ClosingSocketException exception) {
            Outputer.printerror("Unable to shut down server not yet running!");
            App.logger.error("Unable to shut down server not yet running!");
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while shutting down the server!");
            App.logger.error("An error occurred while shutting down the server!");
        }
    }

    /**
     * Open server socket.
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            App.logger.info("Server start...");

            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);

            addr = new InetSocketAddress("localhost", port);

            datagramChannel.bind(addr);

            App.logger.info("Server started successfully.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Port '" + port + "' is out of range!");
            Outputer.printerror(exception.toString());
            App.logger.error("Port '" + port + "' is out of range!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while trying to use the port '" + port + "'!");
            App.logger.error("An error occurred while trying to use the port '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }



}
