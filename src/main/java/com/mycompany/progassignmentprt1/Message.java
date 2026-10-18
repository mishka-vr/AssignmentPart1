/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progassignmentprt1;

import java.util.Random;

/**
 *
 * @author Student
 */
public class Message {
   public static final int MAX_MESSAGE_LENGTH = 250; //sets the max length for the message
    private static final int MAX_ID_LENGTH = 10;     //sets the max length for the id
    private static final int MAX_CELL_LENGTH = 10;
    private static final String STORE_FILE = "storedMessages.json";
    private static final Random random = new Random();
    
    private static String sentLog = "";   // details of every message sent this run
    private static String storedEntries = "";   // JSON objects of every message stored this run
    private static int totalSent = 0;
 
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String text;
    private final String messageHash;
 
    public Message(String recipient, String text) {
        this.messageID = generateMessageID();
        this.messageNumber = totalSent + 1;   // "Num messages sent" starts at 1 and auto-increments
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }
    
    public String generateTaskID(){
        Random obj = new Random();
        long taskID = random.nextLong(10_000_000_000L);
        return String.valueOf(taskID);
    }
     public boolean checkMessageID() {
        return messageID != null && messageID.length() <= MAX_ID_LENGTH;
     }
     
     public boolean checkRecipientCell() {
        return recipient != null
                && recipient.length() <= MAX_CELL_LENGTH
                && recipient.matches("^\\+\\d+$");
    }
    
     
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
                    return "Message successfully sent";
                case "2":
                    System.out.println("Press 0 to delete the message");
                    while (!input.nextLine().trim().equals("0")) {
                        System.out.println("Press 0 to delete the message");
                    }
                    return "Message disregarded";
                case "3":
                    storeMessage();
                    return "Message successfully stored";
                default:
                    System.out.println("Invalid option, please enter 1, 2 or 3.");
            }
        }
    }
   
   
}
