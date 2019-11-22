package com.company;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ClientTest {
    Client client = null;
    @BeforeEach
    void setUp() {
        String ip = "192.168.1.76";
        int port = 32581;
        client = new Client(ip, port);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void connect() {
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void close() {
    }

    @Test
    void send() {
        connect();
        openFile();
        try {
            client.send("Control.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void openFile() {
        try {
            client.loadFile("C:\\Users\\trist\\IdeaProjects\\ServerLogic\\res\\Control.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testOpenFile() {
    }
}