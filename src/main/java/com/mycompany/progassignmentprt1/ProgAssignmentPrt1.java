/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.progassignmentprt1;
 
import java.util.Scanner;
 
/**
 * @author Student
 */
public class ProgAssignmentPrt1 {
 
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login obj = new Login();
 
        //register
        System.out.println("-----------------------------");
        System.out.println("Enter your first name");
        String firstName = input.nextLine();
        System.out.println("-----------------------------");
        System.out.println("Enter last name");
        String lastName = input.nextLine();
        System.out.println("-----------------------------");
        obj.setName(firstName, lastName);
        String results;
        do {
            System.out.println("Enter a username (must contain _ and be at least 5 characters):");
            String username = input.nextLine();
            System.out.println("-----------------------------");
            System.out.println("Enter a password (8+ characters, capital letter, number, special character):");
            String password = input.nextLine();
            System.out.println("-----------------------------");
            System.out.println("Enter your cell number (international code, e.g. +27...):");
            String cellNumber = input.nextLine();
            System.out.println("-----------------------------");
            results = obj.RegisterUser(username, password, cellNumber);
            System.out.println(results);
        } while (!results.equals("Your registration was succesfull"));
 
        // Login
        boolean loggedIn = false;
        for (int attempt = 0; attempt < 3 && !loggedIn; attempt++) {
            System.out.println("-----------------------------");
            System.out.println("Login - enter username:");
            String u = input.nextLine();
            System.out.println("-----------------------------");
            System.out.println("Login - enter password:");
            String p = input.nextLine();
            System.out.println("-----------------------------");
            loggedIn = obj.UserLogin(u, p, "");
            System.out.println(obj.returnUserLoginStatus(loggedIn, !loggedIn));
        }
        if (!loggedIn) {
            System.out.println("Too many failed attempts. Exiting.");
            return;
        }
 
        //Quick Chat
        System.out.println("-----------------------------");
        System.out.println("Welcome to QuickChat.");
        System.out.println("-----------------------------");
        int maxMessages = readPositiveInt(input, "How many messages do you wish to enter?");
        int entered = 0;
        
        boolean running = true;
        while (running) {
            System.out.println("-----------------------------");
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("-----------------------------");
            String choice = input.nextLine().trim();
            
            switch (choice) {
                case "1":
                    if (entered >= maxMessages) {
                        System.out.println("You have already entered all " + maxMessages + " messages.");
                        break;
                    }
                    while (entered < maxMessages) {
                        Message msg = createMessage(input);
                        entered++;
                        String outcome = msg.SentMessage(input);
                        System.out.println(outcome);
                        if (outcome.equals(Message.MSG_DISREGARD)) {
                            while (!input.nextLine().trim().equals("0")) {
                                System.out.println(Message.MSG_DISREGARD);
                            }
                            System.out.println("Message deleted.");
                        } else if (outcome.equals(Message.MSG_SENT)) {
                            System.out.println(msg.details());
                        }
                    }
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please enter 1, 2 or 3.");
            }
        }
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
    }
 
    // asks for recipient + text until both are valid
    private static Message createMessage(Scanner input) {
        String recipient;
        while (true) {
            System.out.println("-----------------------------");
            System.out.println("Enter recipient cell number (starting with international code e.g. +27...):");
            recipient = input.nextLine().trim();
            String cellResult = Message.checkRecipientCell(recipient);
            System.out.println(cellResult);
            if (cellResult.equals(Message.CELL_OK)) {
                break;
            }
            System.out.println("-----------------------------");
        }
        String text;
        while (true) {
            System.out.println("-----------------------------");
            System.out.println("Enter your message:");
            text = input.nextLine();
            if (text.trim().isEmpty()) {
                System.out.println("Message cannot be empty.");
            } else {
                String lengthResult = Message.checkMessageLength(text);
                System.out.println(lengthResult);
                if (lengthResult.equals(Message.MSG_READY)) {
                    break;
                }
            }
            System.out.println("-----------------------------");
        }
        return new Message(recipient, text);
    }
 
    private static int readPositiveInt(Scanner input, String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                int n = Integer.parseInt(input.nextLine().trim());
                if (n > 0) return n;
            } catch (NumberFormatException ignored) { }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }
}