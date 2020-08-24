package com.company;

import javax.swing.SwingWorker;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
    The Server class consists of methods which handle the connection and communication with a remote client.
    The client uses a buffer size of 4096 since that is the block size for the file system being used.
    IOExceptions are thrown from majority of the methods if predictable exceptions occur such as a socket being closed unexpectedly
    or accessing a which already exists.

    @author Triston Gregoire

    Currently the server handles one client and terminates. Add multi threading later

 */
public class Server extends SwingWorker {
    private ServerSocket serverSocket;
    private File repository;
    private volatile boolean serverState;
    public Server(int port, File repository) throws IOException {
        setServerState(false);
        setServerSocket(new ServerSocket(port));
        setRepository(repository);
    }

    @Override
    protected Object doInBackground() throws Exception {
        serverState = true;
        System.out.format("Listening on port %s...\n", serverSocket.getLocalPort());
        while(isServerState()) {
            try {
                ServerSocket serverSocket = getServerSocket();
                File repository = getRepository();
                Socket socket = serverSocket.accept();
                Thread thread = new ClientHandler(socket, repository);
                thread.start();
            } catch (IOException e) {
                System.err.println("Socket Closed");
            }
        }
        serverSocket.close();
        System.out.println("Server stopped...");
        return null;
    }

    @Override
    protected void done() {
        System.out.println("Server shutting down...");
        try {
            getServerSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setServerState(false);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public boolean isServerState() {
        return serverState;
    }

    public void setServerState(boolean serverState) {
        this.serverState = serverState;
    }

    public void setRepository(File repository) {
        this.repository = repository;
    }

    public File getRepository(){
        return repository;
    }
}
