package com.company;

import java.io.*;
import java.net.Socket;

/*
    The client class consists of methods which handle the connection and communication with a remote server.
    The client uses a buffer size of 4096 since that is the block size for the file system being used.
    IOExceptions are thrown from majority of the methods if predictable exceptions occur such as a socket being closed unexpectedly
    or accessing a file before checking if it's available.

    @author Triston Gregoire

 */

//FIXME:
// Benchmark performance of different buffer sizes
// Guard against IOExceptions before attemting to access local files
// Implement ability for client to send multiple files
// Actually handle exceptions
// Add exception comments to documentation
public class Client {

    //The IP address of the server and the port on which it is listening
    private String ip = null;
    private int port;
    //Stream used to communicate with server
    private Socket socket = null;
    private DataOutputStream outputStream= null;
    //File I/O streams
    private File file;
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;
    //buffer used to avoid reading entire files into memory at once. size of 4096 is set to match the block size of the NTFS
    private byte[] buffer = new byte[4096];
    private int fileLength;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /*
    This method establishes the connection with the server and creates an output stream
     */
    public void connect() throws IOException {
        this.socket = new Socket(ip, port);
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    /*
    This method closes the streams associated with the network and file resources
     */
    public void close() throws IOException {
        socket.close();
        fileInputStream.close();
    }

    /*
    This method sends the file name, file length measured in bytes, and the entire file, in that order

    @param message the name the server will use when saving the file
     */
    public void send(String message) throws IOException {
        outputStream.writeUTF(message);
        outputStream.writeLong(fileLength);
        int bytesWritten;
        //while the virtual file's length > 0 and the logical file still has unread bytes
        while(fileLength > 0 && (fileInputStream.read(buffer) != -1) ) {
            bytesWritten = Math.min(buffer.length, fileLength);
            outputStream.write(buffer, 0, bytesWritten);
            outputStream.flush();
            fileLength -= bytesWritten;
        }
        //ToDo: move this close elsewhere
        close();
    }

    /*
    This method creates a representation of a file using the File class and extracts some metadata about the file
    @param filePath The string representation of the file's location in the local file system
     */
    public void loadFile(String filePath) throws IOException {
        this.file = new File(filePath);
        fileLength = (int) file.length();
        fileInputStream = new FileInputStream(file);
    }

    /*
    This class uses an already instantiated File to create an input stream and extract metadata from the file
    @param representation of a file in the local file system
     */
    public void loadFile(File file) throws IOException {
        this.file = file;
        fileLength = (int) this.file.length();
        fileInputStream = new FileInputStream(this.file);
    }
}
