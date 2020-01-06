package com.company;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryMonitorTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void cast() {

    }

    @Test
    void processEvents() {
        Path dir = Paths.get("C:\\Users\\trist\\Documents\\TestFiles");
        try {
            new DirectoryMonitor(dir, false, "192.168.1.76", 32581 ).processEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}