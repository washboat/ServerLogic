package com.company;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.file.Path;
import java.util.*;

/*
    The client class consists of methods which handle the connection and communication with a remote server.
    The client uses a buffer size of 4096 since that is the block size for the file system being used.
    IOExceptions are thrown from majority of the methods if predictable exceptions occur such as a socket being closed unexpectedly
    or accessing a file before checking if it's available.

    @author Triston Gregoire

 */

//FIXME:
// Benchmark performance of different buffer sizes
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
        BigInteger bigFileLength = BigInteger.valueOf(fileLength);
        BigInteger bigBufferLength = BigInteger.valueOf(buffer.length);
        BigInteger bigBytesWritten;

        //while the virtual file's length > 0 and the logical file still has unread bytes
        while(bigFileLength.signum() > 0 && (fileInputStream.read(buffer) != -1) ) {
//            bytesWritten = Math.min(buffer.length, (int)fileLength);
            bigBytesWritten =  bigFileLength.min(bigBufferLength);
            outputStream.write(buffer, 0, bigBytesWritten.intValue());
            outputStream.flush();
            bigFileLength = bigFileLength.subtract(bigBytesWritten);
        }
    }

    public static List<InetAddress> pingNetwork(String networkID) throws IOException {
        //254 is reserved for modem
        List<InetAddress> addressList = new ArrayList<InetAddress>();
        for (int i = 1; i < 254; i++){
            String ip = networkID + "." + i;
            InetAddress address = InetAddress.getByName(ip);
            if (address.isReachable(250))
                addressList.add(address);
            System.out.println(i);
        }
        return addressList;
    }
}
