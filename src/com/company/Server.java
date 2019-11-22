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
    //streams connected to the remote client
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private DataInputStream dataInputStream = null;

    //file metadata to help processing the data coming over the socket
    private String fileName = null;
    private long fileSize;
    private byte[] buffer = new byte[4096];

    //output stream for writing the client's data to the local storage
    private OutputStream localFileOutput = null;

    public Server(int port) throws IOException {
        this.ip = InetAddress.getLocalHost().toString();
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    //This method waits until a client connects and then processes the incoming data
    //ToDo: spawn a thread that handles each client separately and move the I/O processing to those threads
    public void listen(){
        try {
            socket = serverSocket.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            readMetaData();
            System.out.println(fileName);
            createFile();
            processIO();
;        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    This method reads the file name from the client as String using readUTF and the file size as long using readLong

     */
    public void readMetaData() throws IOException {
        fileName = dataInputStream.readUTF();
        fileSize = dataInputStream.readLong();
    }

    /*
    This method creates a local file and opens an output stream to write to the file
     */
    public void createFile() throws FileNotFoundException {
        File file = new File("C:\\Users\\trist\\Documents\\" + fileName);
        localFileOutput = new FileOutputStream(file);

    }

    /*
    This method reads the input stream from the client and writes the data to the output stream for the local file.
    Continues this process until the socket reaches end of stream and the total size of the local file is the expected size.

     */
    public void processIO() throws IOException {
        int bytesRead;

        /*
        conditioning the loop to continue until end of stream (EOS) may have unintended errors.
        For example if the client sends some of the file now and the rest later the server may end the loop prematurely,
        Next time I read this research if EOS indicates the closing of the stream or rather that there is simply
        nothing currently in the stream.
        If it's the latter then I should implement a block until more data arrives in the stream,
        assuming the API class doesn't already have that feature.
        */

        while (fileSize > 0 && (bytesRead =
            dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                localFileOutput.write(buffer, 0 , bytesRead);
                fileSize -= bytesRead;
        }
        localFileOutput.close();
    }

}
