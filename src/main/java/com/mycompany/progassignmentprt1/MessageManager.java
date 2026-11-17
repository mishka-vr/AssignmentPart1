package com.mycompany.progassignmentprt1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Part 3: holds the arrays and the operations used by the "Stored Messages" menu.
 * Nothing is hard-coded: every array is filled while the program runs.
 */
public class MessageManager {

    // the five required arrays
    public static final ArrayList<Message> sentMessages = new ArrayList<>();
    public static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static final ArrayList<Message> storedMessages = new ArrayList<>();
    public static final ArrayList<String> messageHashes = new ArrayList<>();
    public static final ArrayList<String> messageIDs = new ArrayList<>();

    // ---------- populating the arrays ----------

    public static void addSent(Message m) {
        sentMessages.add(m);
        register(m);
    }

    public static void addDisregarded(Message m) {
        disregardedMessages.add(m);
        register(m);
    }

    public static void addStored(Message m) {
        storedMessages.add(m);
        register(m);
    }

    // hash and ID arrays stay in step with each other
    private static void register(Message m) {
        messageHashes.add(m.getMessageHash());
        messageIDs.add(m.getMessageID());
    }

    public static void clearAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }

    // Reads storedMessages.json into the Stored Messages array. 
    public static void loadStoredMessages(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            return;
        }
        try (JsonReader reader = new JsonReader(new FileReader(f))) {
            reader.setLenient(true); // copes with an array OR a run of separate objects
            while (reader.peek() != JsonToken.END_DOCUMENT) {
                JsonElement el = JsonParser.parseReader(reader);
                if (el.isJsonArray()) {
                    JsonArray arr = el.getAsJsonArray();
                    for (JsonElement item : arr) {
                        loadOne(item);
                    }
                } else {
                    loadOne(el);
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not read " + fileName + ": " + e.getMessage());
        }
    }

    private static void loadOne(JsonElement el) {
        if (!el.isJsonObject()) {
            return;
        }
        JsonObject o = el.getAsJsonObject();
        String id = field(o, "messageID", "messageId", "id");
        String recipient = field(o, "recipient", "Recipient");
        String text = field(o, "message", "text", "messageText", "Message");
        if (recipient == null || text == null) {
            return;
        }
        String num = field(o, "messageNumber", "number");
        Message m;
        if (id != null && id.length() >= 2) {
            int n = 0;
            try {
                n = num == null ? 0 : Integer.parseInt(num);
            } catch (NumberFormatException ignored) { }
            m = new Message(id, n, recipient, text);
        } else {
            m = new Message(recipient, text);
        }
        addStored(m);
    }

    private static String field(JsonObject o, String... names) {
        for (String n : names) {
            if (o.has(n) && !o.get(n).isJsonNull()) {
                return o.get(n).getAsString();
            }
        }
        return null;
    }

    //  menu operations 

    // a) sender and recipient of all sent messages
    public static String displaySendersAndRecipients(String sender) {
        if (sentMessages.isEmpty()) {
            return "No sent messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Sender: ").append(sender)
              .append(" | Recipient: ").append(m.getRecipient()).append("\n");
        }
        return sb.toString().trim();
    }

    // b) longest message (sent + stored, which is what the test data expects)
    public static String longestMessage() {
        String longest = "";
        for (Message m : activeMessages()) {
            if (m.getText().length() > longest.length()) {
                longest = m.getText();
            }
        }
        return longest;
    }

    // c) search by message ID
    public static Message findByID(String id) {
        for (Message m : activeMessages()) {
            if (m.getMessageID().equals(id)) {
                return m;
            }
        }
        return null;
    }

    // d) all messages sent/stored for a recipient
    public static ArrayList<String> messagesForRecipient(String recipient) {
        ArrayList<String> result = new ArrayList<>();
        for (Message m : activeMessages()) {
            if (m.getRecipient().equals(recipient)) {
                result.add(m.getText());
            }
        }
        return result;
    }

    // e) delete by message hash
    public static String deleteByHash(String hash) {
        int idx = messageHashes.indexOf(hash);
        if (idx == -1) {
            return "Message hash not found.";
        }
        String text = removeByHash(sentMessages, hash);
        if (text == null) {
            text = removeByHash(storedMessages, hash);
        }
        if (text == null) {
            text = removeByHash(disregardedMessages, hash);
        }
        messageHashes.remove(idx);
        messageIDs.remove(idx);
        return "Message '" + text + "' successfully deleted.";
    }

    private static String removeByHash(ArrayList<Message> list, String hash) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMessageHash().equals(hash)) {
                return list.remove(i).getText();
            }
        }
        return null;
    }

    // f) report of all sent messages
    public static String displayReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getText()).append("\n\n");
        }
        return sb.toString().trim();
    }

    private static ArrayList<Message> activeMessages() {
        ArrayList<Message> all = new ArrayList<>(sentMessages);
        all.addAll(storedMessages);
        return all;
    }
}