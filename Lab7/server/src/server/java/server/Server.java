package server;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.utility.Outputer;
import server.utility.CommandManager;
import server.utility.ConnectionHandler;
import server.utility.HandleResquestTask;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Runs the server.
 */
public class Server {
    private final int port;
    private final HandleResquestTask handleResquestTask;
    // NonBlocking IO
    private DatagramChannel datagramChannel;
    private SocketAddress addr;
    private CommandManager commandManager;
    public boolean isStopped = false;

    private Thread readRequestThread;

    public Server(int port, int soTimeout, HandleResquestTask handleResquestTask, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.handleResquestTask = handleResquestTask;
    }

    /**
     * Begins server operation.
     */
    public void run() {
        try {
            openServerSocket();
            while (!isStopped()) {
                try {
                    if (isStopped()) throw new ConnectionErrorException();
                    readRequestThread = new Thread(new ConnectionHandler(this, datagramChannel, handleResquestTask));
                    readRequestThread.start();
                }
                catch (ConnectionErrorException exception) {
                    if (!isStopped()) {
                        Outputer.printerror("An error occurred while connecting to the client!");
                        App.logger.error("An error occurred while connecting to the client!");
                    } else break;
                }
                readRequestThread.join();
                Outputer.println("Server shut down.");
            }

        } catch (OpeningServerSocketException e) {
            Outputer.printerror("Server cannot be started!");
            App.logger.error("Server cannot be started!");
        } catch (InterruptedException e) {
            Outputer.printerror("Server is interrupted!");
            App.logger.error("Server is interrupted!");
        }
    }


    /**
     * Finishes server operation.
     */
    public void stop() {
        try {
            App.logger.info("Shutting down the server...");
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
     * Checked stops of server.
     *
     * @return Status of server stop.
     */
    private synchronized boolean isStopped() {
        return isStopped;
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
