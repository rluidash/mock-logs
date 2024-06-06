package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MockLogGenerator {

    private static final Logger logger = LogManager.getLogger(MockLogGenerator.class);
    private static final Random random = new Random();

    public static void main(String[] args) {
        String[] messages = {
                "User logged in",
                "User logged out",
                "User updated profile",
                "User deleted account",
                "User added an item to cart",
                "User completed purchase"
        };

        while (true) {
            try {
                generateLogs(messages);
                TimeUnit.SECONDS.sleep(1); // Generate a log every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private static void generateLogs(String[] messages) {
        int action = random.nextInt(4);
        switch (action) {
            case 0:
                logInfo(messages);
                break;
            case 1:
                logWarning();
                break;
            case 2:
                logError();
                break;
            case 3:
                logException();
                break;
        }
    }

    private static void logInfo(String[] messages) {
        int index = random.nextInt(messages.length);
        String message = messages[index];
        logger.info(message);
    }

    private static void logWarning() {
        String[] warnings = {
                "Disk space running low",
                "High memory usage detected",
                "User session nearing timeout"
        };
        int index = random.nextInt(warnings.length);
        logger.warn(warnings[index]);
    }

    private static void logError() {
        String[] errors = {
                "Failed to load user profile",
                "Error processing payment",
                "Database connection lost"
        };
        int index = random.nextInt(errors.length);
        logger.error(errors[index]);
    }

    private static void logException() {
        try {
            performIOOperation();
        } catch (IOException e) {
            logger.error("IOException occurred while performing I/O operation", e);
        }
    }

    private static void performIOOperation() throws IOException {
        String filePath = "non_existing_file.txt";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("Read line: {}", line);
            }
        } catch (IOException e) {
            logger.error("Error reading file: " + filePath, e);
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("Error closing file reader", e);
                }
            }
        }
    }
}
