package com.mycompany.progassignmentprt1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageManagerTest {

    private Message m1, m2, m3, m4, m5;

    @BeforeEach
    public void setUp() {
        MessageManager.clearAll();

        m1 = new Message("1000000001", 0, "+27834557896", "Did you get the cake?");
        m2 = new Message("2000000002", 1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m3 = new Message("3000000003", 2, "+27834484567", "Yohoooo, I am at your gate.");
        m4 = new Message("4000000004", 3, "0838884567", "It is dinner time!");
        m5 = new Message("5000000005", 4, "+27838884567", "Ok, I am leaving without you.");

        MessageManager.addSent(m1);          // Sent
        MessageManager.addStored(m2);        // Stored
        MessageManager.addDisregarded(m3);   // Disregard
        MessageManager.addSent(m4);          // Sent
        MessageManager.addStored(m5);        // Stored
    }

    @Test
    public void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(2, MessageManager.sentCount);
        assertEquals("Did you get the cake?", MessageManager.sentMessages[0].getText());
        assertEquals("It is dinner time!", MessageManager.sentMessages[1].getText());
    }

    @Test
    public void displayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                MessageManager.longestMessage());
    }

    @Test
    public void searchByMessageID() {
        Message found = MessageManager.findByID("4000000004");
        assertNotNull(found);
        assertEquals("0838884567", found.getRecipient());
        assertEquals("It is dinner time!", found.getText());
    }

    @Test
    public void searchByRecipient() {
        String[] expected = {
            "Where are you? You are late! I have asked you to be on time.",
            "Ok, I am leaving without you."
        };
        assertArrayEquals(expected, MessageManager.messagesForRecipient("+27838884567"));
    }

    @Test
    public void deleteMessageUsingHash() {
        String hash = m2.getMessageHash();
        assertEquals("Message 'Where are you? You are late! I have asked you to be on time.' successfully deleted.",
                MessageManager.deleteByHash(hash));
        assertEquals(4, MessageManager.hashCount);
        assertEquals(1, MessageManager.storedCount);
    }

    @Test
    public void displayReport() {
        String report = MessageManager.displayReport();
        assertTrue(report.contains(m1.getMessageHash()));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains(m4.getMessageHash()));
        assertTrue(report.contains("0838884567"));
        assertTrue(report.contains("It is dinner time!"));
    }
}
