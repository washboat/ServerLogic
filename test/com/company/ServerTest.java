package com.company;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    Server server = null;
    @BeforeEach
    void setUp() {
        try {
            server = new Server(32581);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void listen() {
        server.listen();
    }

    @Test
    void readMetaData() {
        try {
            server.readMetaData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createFile() {
    }

    @Test
    void processIO() {
    }
}