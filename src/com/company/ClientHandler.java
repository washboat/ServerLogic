package com.company;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread{
    DataOutputStream outputStream= null;
    DataInputStream inputStream = null;
    Socket socket = null;

    private String fileName = null;
    private long fileSize;
    //private OutputStream localFileOutput = null;
    private byte[] buffer = new byte[4096];


    public ClientHandler( Socket socket, DataInputStream inputStream, DataOutputStream outputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.socket = socket;
        readMetaData();
//        OutputStream localFileOutput = createFile();
        processIO();
    }

    /*
    This method creates a local file and opens an output stream to write to the file
     */
//    public OutputStream createFile() {
//        File file = new File("C:\\Users\\trist\\Documents\\" + fileName);
//        try {
//            localFileOutput = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return localFileOutput;
//    }

    /*
    This method reads the file name from the client as String using readUTF and the file size as long using readLong
     */
    public void readMetaData() {
        try {
            fileName = inputStream.readUTF();
            fileSize = inputStream.readLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    This method reads the input stream from the client and writes the data to the output stream for the local file.
    Continues this process until the socket reaches end of stream and the total size of the local file is the expected size.

     */
    public void processIO() {
        /*
        conditioning the loop to continue until end of stream (EOS) may have unintended errors.
        For example if the client sends some of the file now and the rest later the server may end the loop prematurely,
        Next time I read this research if EOS indicates the closing of the stream or rather that there is simply
        nothing currently in the stream.
        If it's the latter then I should implement a block until more data arrives in the stream,
        assuming the API class doesn't already have that feature.
        */
        int bytesRead = -1;
        File file = new File("C:\\Users\\trist\\Documents\\" + fileName);
        try (OutputStream localFileOutput = new FileOutputStream(file)){
            while (true) {
                if (!(fileSize > 0 && (bytesRead = inputStream.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1))
                    break;
                localFileOutput.write(buffer, 0 , bytesRead);
            fileSize -= bytesRead;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("hello");
    }
}
