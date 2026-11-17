package com.mycompany.progassignmentprt1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MessageManager {

    private static final int START_SIZE = 10;

    // the five required arrays
    public static Message[] sentMessages = new Message[START_SIZE];
    public static Message[] disregardedMessages = new Message[START_SIZE];
    public static Message[] storedMessages = new Message[START_SIZE];
    public static String[] messageHashes = new String[START_SIZE];
    public static String[] messageIDs = new String[START_SIZE];

    // how many slots of each array are in use
    public static int sentCount = 0;
    public static int disregardedCount = 0;
    public static int storedCount = 0;
    public static int hashCount = 0;   // messageHashes and messageIDs always stay in step
    
    // ---------- populating the arrays ----------

    public static void addSent(Message m) {
        if (sentCount == sentMessages.length) {
            sentMessages = growMessages(sentMessages);
        }
        sentMessages[sentCount++] = m;
        register(m);
    }

    public static void addDisregarded(Message m) {
        if (disregardedCount == disregardedMessages.length) {
            disregardedMessages = growMessages(disregardedMessages);
        }
        disregardedMessages[disregardedCount++] = m;
        register(m);
    }

    public static void addStored(Message m) {
        if (storedCount == storedMessages.length) {
            storedMessages = growMessages(storedMessages);
        }
        storedMessages[storedCount++] = m;
        register(m);
    }

    // hash and ID arrays stay in step with each other
    private static void register(Message m) {
        if (hashCount == messageHashes.length) {
            messageHashes = growStrings(messageHashes);
            messageIDs = growStrings(messageIDs);
        }
        messageHashes[hashCount] = m.getMessageHash();
        messageIDs[hashCount] = m.getMessageID();
        hashCount++;
    }

    public static void clearAll() {
        sentMessages = new Message[START_SIZE];
        disregardedMessages = new Message[START_SIZE];
        storedMessages = new Message[START_SIZE];
        messageHashes = new String[START_SIZE];
        messageIDs = new String[START_SIZE];
        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;
        hashCount = 0;
    }

    private static Message[] growMessages(Message[] old) {
        Message[] bigger = new Message[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            bigger[i] = old[i];
        }
        return bigger;
    }

    private static String[] growStrings(String[] old) {
        String[] bigger = new String[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            bigger[i] = old[i];
        }
        return bigger;
    }

    // Reads storedMessages.json into the Stored Messages array. 
    public static void loadStoredMessages(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            return;
        }
        try (JsonReader reader = new JsonReader(new FileReader(f))) {
            reader.setStrictness(Strictness.LENIENT); // copes with an array OR a run of separate objects
            while (reader.peek() != JsonToken.END_DOCUMENT) {
                JsonElement el = JsonParser.parseReader(reader);
                if (el.isJsonArray()) {
                    JsonArray arr = el.getAsJsonArray();
                    for (int i = 0; i < arr.size(); i++) {
                        loadOne(arr.get(i));
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
        for (int i = 0; i < names.length; i++) {
            String n = names[i];
            if (o.has(n) && !o.get(n).isJsonNull()) {
                return o.get(n).getAsString();
            }
        }
        return null;
    }

    // ---------- menu operations ----------

    // a) sender and recipient of all sent messages
    public static String displaySendersAndRecipients(String sender) {
        if (sentCount == 0) {
            return "No sent messages.";
        }
        String result = "";
        for (int i = 0; i < sentCount; i++) {
            result += "Sender: " + sender + " | Recipient: " + sentMessages[i].getRecipient() + "\n";
        }
        return result.trim();
    }

    // b) longest message (sent + stored, which is what the test data expects)
    public static String longestMessage() {
        Message[] all = activeMessages();
        String longest = "";
        for (int i = 0; i < all.length; i++) {
            if (all[i].getText().length() > longest.length()) {
                longest = all[i].getText();
            }
        }
        return longest;
    }

    // c) search by message ID
    public static Message findByID(String id) {
        Message[] all = activeMessages();
        for (int i = 0; i < all.length; i++) {
            if (all[i].getMessageID().equals(id)) {
                return all[i];
            }
        }
        return null;
    }

    // d) all messages sent/stored for a recipient
    public static String[] messagesForRecipient(String recipient) {
        Message[] all = activeMessages();
        int matches = 0;
        for (int i = 0; i < all.length; i++) {
            if (all[i].getRecipient().equals(recipient)) {
                matches++;
            }
        }
        String[] result = new String[matches];
        int pos = 0;
        for (int i = 0; i < all.length; i++) {
            if (all[i].getRecipient().equals(recipient)) {
                result[pos++] = all[i].getText();
            }
        }
        return result;
    }

    // e) delete by message hash
    public static String deleteByHash(String hash) {
        int hashIdx = -1;
        for (int i = 0; i < hashCount; i++) {
            if (messageHashes[i].equals(hash)) {
                hashIdx = i;
                break;
            }
        }
        if (hashIdx == -1) {
            return "Message hash not found.";
        }

        String text = null;
        int idx = indexOfHash(sentMessages, sentCount, hash);
        if (idx != -1) {
            text = sentMessages[idx].getText();
            removeMessageAt(sentMessages, sentCount, idx);
            sentCount--;
        } else if ((idx = indexOfHash(storedMessages, storedCount, hash)) != -1) {
            text = storedMessages[idx].getText();
            removeMessageAt(storedMessages, storedCount, idx);
            storedCount--;
        } else if ((idx = indexOfHash(disregardedMessages, disregardedCount, hash)) != -1) {
            text = disregardedMessages[idx].getText();
            removeMessageAt(disregardedMessages, disregardedCount, idx);
            disregardedCount--;
        }

        removeStringAt(messageHashes, hashCount, hashIdx);
        removeStringAt(messageIDs, hashCount, hashIdx);
        hashCount--;
        return "Message '" + text + "' successfully deleted.";
    }

    private static int indexOfHash(Message[] arr, int count, String hash) {
        for (int i = 0; i < count; i++) {
            if (arr[i].getMessageHash().equals(hash)) {
                return i;
            }
        }
        return -1;
    }

    // shifts everything after idx one place left
    private static void removeMessageAt(Message[] arr, int count, int idx) {
        for (int i = idx; i < count - 1; i++) {
            arr[i] = arr[i + 1];
        }
        arr[count - 1] = null;
    }

    private static void removeStringAt(String[] arr, int count, int idx) {
        for (int i = idx; i < count - 1; i++) {
            arr[i] = arr[i + 1];
        }
        arr[count - 1] = null;
    }

    // f) report of all sent messages
    public static String displayReport() {
        if (sentCount == 0) {
            return "No sent messages.";
        }
        String report = "";
        for (int i = 0; i < sentCount; i++) {
            report += "Message Hash: " + sentMessages[i].getMessageHash() + "\n"
                    + "Recipient: " + sentMessages[i].getRecipient() + "\n"
                    + "Message: " + sentMessages[i].getText() + "\n\n";
        }
        return report.trim();
    }

    // sent + stored messages in one exactly-sized array
    private static Message[] activeMessages() {
        Message[] all = new Message[sentCount + storedCount];
        for (int i = 0; i < sentCount; i++) {
            all[i] = sentMessages[i];
        }
        for (int i = 0; i < storedCount; i++) {
            all[sentCount + i] = storedMessages[i];
        }
        return all;
    }
}