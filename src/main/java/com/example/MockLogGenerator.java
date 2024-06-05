package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MockLogGenerator {

    private static final Logger logger = LogManager.getLogger(MockLogGenerator.class);

    public static void main(String[] args) {
        Random random = new Random();
        String[] messages = {"User logged in", "User logged out", "User updated profile", "User deleted account"};

        while (true) {
            try {
                int index = random.nextInt(messages.length);
                String message = messages[index];
                logger.info(message);
                TimeUnit.SECONDS.sleep(1); // Generate a log every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

