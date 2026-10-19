package com.mycompany.progassignmentprt1;

import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MessageTest {

    // Test data - message 1 (user selects Send)
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";

    // Test data - message 2 (user selects Discard)
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    @BeforeEach
    public void setUp() {
        Message.resetMessages();   // each test starts with no messages sent
    }

    // ---------- message length ----------
    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", Message.checkMessageLength(TEXT_1));
    }

    @Test
    public void testMessageLengthFailure() {
        String tooLong = "a".repeat(260);   // 10 characters over the limit
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(tooLong));
    }

    // ---------- recipient number ----------
    @Test
    public void testRecipientNumberSuccess() {
        assertEquals("Cell phone number successfully captured.", Message.checkRecipientCell(RECIPIENT_1));
    }

    @Test
    public void testRecipientNumberFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", Message.checkRecipientCell(RECIPIENT_2));
    }

    // ---------- message hash ----------
    @Test
    public void testMessageHashTestCase1() {
        Message msg = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        assertEquals("00:0:HITONIGHT", msg.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        // loop through both messages in the test data and check each hash
        for (int i = 0; i < 2; i++) {
            String id;
            String recipient;
            String text;
            String expectedHash;

            if (i == 0) {
                id = "0012345678";
                recipient = RECIPIENT_1;
                text = TEXT_1;
                expectedHash = "00:0:HITONIGHT";
            } else {
                id = "0123456789";
                recipient = RECIPIENT_2;
                text = TEXT_2;
                expectedHash = "01:1:HIPAYMENT";
            }

            Message msg = new Message(id, i, recipient, text);
            assertEquals(expectedHash, msg.createMessageHash());
        }
    }

    // ---------- message ID ----------
    @Test
    public void testMessageIDCreated() {
        Message msg = new Message(RECIPIENT_1, TEXT_1);
        assertTrue(msg.checkMessageID());
        assertEquals("Message ID generated: " + msg.getMessageID(), msg.createMessageID());
    }

    // ---------- message sent (send / disregard / store) ----------
    @Test
    public void testMessageSentSend() {
        Message msg = new Message(RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully sent.", msg.SentMessage(new Scanner("1\n")));
    }

    @Test
    public void testMessageSentDisregard() {
        Message msg = new Message(RECIPIENT_2, TEXT_2);
        assertEquals("Press 0 to delete the message.", msg.SentMessage(new Scanner("2\n")));
    }

    @Test
    public void testMessageSentStore() {
        Message msg = new Message(RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully stored.", msg.SentMessage(new Scanner("3\n")));
    }

    // ---------- total number sent ----------
    @Test
    public void testReturnTotalMessages() {
        Message first = new Message(RECIPIENT_1, TEXT_1);
        first.SentMessage(new Scanner("1\n"));      // Send

        Message second = new Message(RECIPIENT_2, TEXT_2);
        second.SentMessage(new Scanner("2\n"));     // Discard

        // only the sent message counts
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessagesContainsSentMessage() {
        Message msg = new Message(RECIPIENT_1, TEXT_1);
        msg.SentMessage(new Scanner("1\n"));
        assertTrue(Message.printMessages().contains(TEXT_1));
    }
}