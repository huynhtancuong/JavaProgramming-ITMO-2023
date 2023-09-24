package client;

import client.utility.AuthHandler;
import client.utility.UserHandler;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotInDeclaredLimitsException;
import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.interaction.User;
import common.utility.Outputer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Runs the client.
 */
public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private UserHandler userHandler;
//    private SocketChannel socketChannel;
    private DatagramSocket datagramSocket;
    private SocketAddress addr;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private AuthHandler authHandler;
    private User user;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler,
                  AuthHandler authHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
        this.authHandler = authHandler;
    }

    /**
     * Begins client operation.
     */
    public void run() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                try {
                    connectToServer();
                    processAuthentication();
                    processingStatus = processRequestToServer();
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        Outputer.printerror("Connection attempts exceeded!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        Outputer.printerror("Connection timeout '" + reconnectionTimeout +
                                "' is out of range!");
                        Outputer.println("Reconnection will be done immediately.");
                    } catch (Exception timeoutException) {
                        Outputer.printerror("An error occurred while trying to connect!");
                        Outputer.println("Reconnection will be done immediately.");
                    }
                }
                reconnectionAttempts++;
            }
            if (datagramSocket != null) datagramSocket.close();
            Outputer.println("Client job completed successfully.");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Client cannot be started!");
        }
    }

    /**
     * Connecting to server.
     */
    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) Outputer.println("Reconnecting to the server...");

            addr = new InetSocketAddress(host, port);
            datagramSocket = new DatagramSocket();
//            datagramChannel.configureBlocking(false);
            datagramSocket.setSoTimeout(10000);
            
            if (datagramSocket != null) {
                Outputer.println("Connected to server.");
            } else {
                Outputer.println("Reconnecting to the server.");
            }
            Outputer.println("Waiting for permission to communicate...");
//            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
//            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            Outputer.println("Permission to share data received.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Server address entered incorrectly!");
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while connecting to the server!");
            throw new ConnectionErrorException();
        }
    }

    /**
     * Server request process.
     */
    private boolean processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getResponseCode(), user) :
                        userHandler.handle(null, user);
                if (requestToServer.isEmpty()) continue;

                // writing Request object here
                myWriteObject(requestToServer);
                // reading Response object here
                serverResponse = myReadObject();

                Outputer.print(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                Outputer.printerror(exception);
                Outputer.printerror("An error occurred while sending data to the server!");
            } catch (ClassNotFoundException exception) {
                Outputer.printerror("An error occurred while reading received data!");
            } catch (IOException exception) {
                exception.printStackTrace();
                Outputer.printerror("Server connection lost!");
                try {
                    reconnectionAttempts++;
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    if (requestToServer.getCommandName().equals("exit"))
                        Outputer.println("The command will not be registered on the server.");
                    else Outputer.println("Try repeating the command later.");
                }
            } catch (NullPointerException e) {
                Outputer.printerror("Catched NullPointerExeption");
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return false;
    }

    /**
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Response myReadObject() throws IOException, ClassNotFoundException {
        Response serverResponse = new Response(ResponseCode.ERROR, "");
        ByteBuffer buffer = ByteBuffer.allocate(1024*16);

        buffer.clear();

        DatagramPacket datagramPacket = new DatagramPacket(buffer.array(), buffer.array().length);

        datagramSocket.receive(datagramPacket);

//        int read = datagramChannel.read(buffer);
        if (addr == null) return serverResponse;

//        buffer.flip();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
        serverReader = new ObjectInputStream(byteArrayInputStream);

        Object obj = serverReader.readObject();

        if (obj instanceof Response) {
            serverResponse = (Response) obj;
        }
        return serverResponse;
    }

    private void myWriteObject(Request requestToServer) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        serverWriter = new ObjectOutputStream(byteArrayOutputStream);
        serverWriter.flush();

        serverWriter.writeObject(requestToServer);

        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

//        datagramSocket.send(buffer, addr);
        datagramSocket.send(new DatagramPacket(buffer.array(), buffer.array().length, addr));
    }

    /**
     * Handle process authentication.
     */
    private void processAuthentication() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = authHandler.handle();
                if (requestToServer.isEmpty()) continue;
                myWriteObject(requestToServer);
                serverResponse = (Response) myReadObject();
                Outputer.print(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                Outputer.printerror("An error occurred while sending data to the server!");
            } catch (ClassNotFoundException exception) {
                Outputer.printerror("An error occurred while reading received data!");
            } catch (IOException exception) {
                exception.printStackTrace();
                Outputer.printerror("Server connection lost!");
                try {
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    Outputer.println("Please try again later.");
                }
            }
        } while (serverResponse == null || !serverResponse.getResponseCode().equals(ResponseCode.OK));
        user = requestToServer.getUser();
    }
}

