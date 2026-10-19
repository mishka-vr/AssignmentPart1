package com.mycompany.progassignmentprt1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * A single QuickChat message plus the running totals for the session.
 */
public class Message {

    public static final int MAX_MESSAGE_LENGTH = 250;
    private static final int MAX_ID_LENGTH = 10;

    // result messages (public so the unit tests and main use exactly the same text)
    public static final String MSG_READY = "Message ready to send.";
    public static final String CELL_OK = "Cell phone number successfully captured.";
    public static final String CELL_BAD = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    public static final String MSG_SENT = "Message successfully sent.";
    public static final String MSG_DISREGARD = "Press 0 to delete the message.";
    public static final String MSG_STORED = "Message successfully stored.";

    private static final String STORE_FILE = "storedMessages.json";
    private static final Random random = new Random();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // shared across the whole program run
    private static String sentLog = "";   // details of every message sent this run
    private static String storedEntries = "";   // JSON objects of every message stored this run
    private static int totalSent = 0;

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String text;
    private final String messageHash;

    // used by the program: ID is random, message number is how many messages have been sent so far (starts at 0)
    public Message(String recipient, String text) {
        this(generateMessageID(), totalSent, recipient, text);
    }

    // lets the unit tests supply the ID and message number
    public Message(String messageID, int messageNumber, String recipient, String text) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }

    // ten-digit random number (leading zeros allowed, e.g. 0012345678)
    private static String generateMessageID() {
        long n = random.nextLong(10_000_000_000L);
        return String.format("%010d", n);
    }

    
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= MAX_ID_LENGTH;
    }

    // Returns the "Message ID generated: ..." message. 
    public String createMessageID() {
        return "Message ID generated: " + messageID;
    }

    // Checks the message is no more than 250 characters. 
    public static String checkMessageLength(String text) {
        if (text.length() <= MAX_MESSAGE_LENGTH) {
            return MSG_READY;
        }
        int over = text.length() - MAX_MESSAGE_LENGTH;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    // Cell number must start with an international code (+) followed by digits only (at most ten after the code).
    public static String checkRecipientCell(String recipient) {
        if (recipient != null && recipient.matches("^\\+\\d{5,13}$")) {
            return CELL_OK;
        }
        return CELL_BAD;
    }

    // Hash = first two digits of ID : message number : FIRSTWORDLASTWORD (all caps). 
    public String createMessageHash() {
        String trimmed = text.trim();

        // first word: characters up to the first whitespace
        int firstEnd = 0;
        while (firstEnd < trimmed.length() && !Character.isWhitespace(trimmed.charAt(firstEnd))) {
            firstEnd++;
        }
        // last word: characters after the last whitespace
        int lastStart = trimmed.length();
        while (lastStart > 0 && !Character.isWhitespace(trimmed.charAt(lastStart - 1))) {
            lastStart--;
        }

        String first = trimmed.substring(0, firstEnd).replaceAll("[^A-Za-z0-9]", "");
        String last = trimmed.substring(lastStart).replaceAll("[^A-Za-z0-9]", "");
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    
    public String SentMessage(Scanner input) {
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    sentLog += details() + "\n\n";
                    totalSent++;
                    return MSG_SENT;
                case "2":
                    return MSG_DISREGARD;
                case "3":
                    storeMessage();
                    return MSG_STORED;
                default:
                    System.out.println("Invalid option, please enter 1, 2 or 3.");
            }
        }
    }

    
    public static String printMessages() {
        if (sentLog.isEmpty()) {
            return "No messages have been sent yet.";
        }
        return sentLog;
    }

    
    public static int returnTotalMessages() {
        return totalSent;
    }

    // Clears the running totals (used by the unit tests so each test starts fresh).
    public static void resetMessages() {
        sentLog = "";
        storedEntries = "";
        totalSent = 0;
    }

    //Stores this message in storedMessages.json (a JSON array of message objects) using Gson. 
    public void storeMessage() {
        // Gson turns this Message object (its fields) into a JSON object
        String entry = gson.toJson(this);
        if (!storedEntries.isEmpty()) {
            storedEntries += ",\n";
        }
        storedEntries += entry;

        // rewrite the file with every message stored so far, so it is always valid JSON
        try (FileWriter writer = new FileWriter(STORE_FILE)) {
            writer.write("[\n" + storedEntries + "\n]");
        } catch (IOException e) {
            System.out.println("Could not store the message: " + e.getMessage());
        }
    }

    
    public String details() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + text;
    }

    public String getMessageHash() { return messageHash; }
    public String getMessageID() { return messageID; }
}