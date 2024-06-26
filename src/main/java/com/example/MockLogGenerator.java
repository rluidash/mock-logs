package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MockLogGenerator {

    private static final Logger logger = LogManager.getLogger(MockLogGenerator.class);
    private static final Random random = new Random();

    public static void main(String[] args) {
        String[] messages = {
                "User %s logged in",
                "User %s logged out",
                "User %s updated profile",
                "User %s deleted account",
                "User %s added an item to cart",
                "User %s completed purchase"
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
        String message = String.format(messages[index], generateRandomUserId());
        logger.info(message);
    }

    private static void logWarning() {
        String[] warnings = {
                "Disk space running low",
                "High memory usage detected",
                "User session nearing timeout for %s"
        };
        int index = random.nextInt(warnings.length);
        String message = String.format(warnings[index], generateRandomUserId());
        logger.warn(message);
    }

    private static void logError() {
        String[] errors = {
                "Failed to load user profile for %s",
                "Error processing payment for %s",
                "Database connection lost for %s"
        };
        int index = random.nextInt(errors.length);
        String message = String.format(errors[index], generateRandomUserId());
        logger.error(message);
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

    private static String generateRandomUserId() {
        return UUID.randomUUID().toString().replace("-", "_");
    }
}

