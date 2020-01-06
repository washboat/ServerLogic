package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

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
public class Client extends Thread{

    //buffer used to avoid reading entire files into memory at once. size of 4096 is set to match the block size of the NTFS
    final private byte[] buffer = new byte[4096];

    public Client(String ip, int port, Path path) {
        File file;
        long fileLength;
        String fileName;
        file = path.toFile();
        fileLength = file.length();
        fileName = path.getFileName().toString();
        try (
                Socket socket = new Socket(ip, port);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                FileInputStream fileInputStream = new FileInputStream(file);
        ) {
            send(fileName, outputStream, fileLength, fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    This method sends the file name, file length measured in bytes, and the entire file, in that order

    @param message the name the server will use when saving the file
     */
    public void send(String fileName, DataOutputStream outputStream, long fileLength, FileInputStream fileInputStream) throws IOException {
        outputStream.writeUTF(fileName);
        outputStream.writeLong(fileLength);
        int bytesWritten;
        //while the virtual file's length > 0 and the logical file still has unread bytes
        while(fileLength > 0 && (fileInputStream.read(buffer) != -1) ) {
            bytesWritten = Math.min(buffer.length, (int)fileLength);
            outputStream.write(buffer, 0, bytesWritten);
            outputStream.flush();
            fileLength -= bytesWritten;
        }
    }
}
