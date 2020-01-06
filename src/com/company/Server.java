package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/*
    The Server class consists of methods which handle the connection and communication with a remote client.
    The client uses a buffer size of 4096 since that is the block size for the file system being used.
    IOExceptions are thrown from majority of the methods if predictable exceptions occur such as a socket being closed unexpectedly
    or accessing a which already exists.

    @author Triston Gregoire

    Currently the server handles one client and terminates. Add multi threading later

 */
public class Server {
    //unused for now
    private String ip = null;
    private int port;
    private ServerSocket serverSocket = null;

    public Server(int port) throws IOException {
        this.ip = InetAddress.getLocalHost().toString();
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    //This method waits until a client connects and then processes the incoming data
    //ToDo: spawn a thread that handles each client separately and move the I/O processing to those threads
    public void listen(){
        while(true) {
            try {
                //streams connected to the remote client
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                Thread thread = new ClientHandler(socket, dataInputStream, dataOutputStream);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
