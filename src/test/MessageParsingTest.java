package test;

import main.java.com.company.messages.Message;
import main.java.com.company.messages.MessageParser;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

/**
 * MessageParser class - testing. Message with fixed values is created and parsed to bytes array. Later on, given bytes array is parsed back to message object.
 *
 * @author Ivana Salmanić
 */
public class MessageParsingTest {

    @Test
    public void givenMessage_whenParsed_shouldReturnSameMessageInfo() {
        int messageSize = 50;

        Message message =  createMessageObject();

        byte[] byteArray = MessageParser.createByteArrayFromMessage(message, messageSize);

        Message messageParsedFromBytes = MessageParser.createMessageFromByteArray(byteArray);

        assertEquals(messageParsedFromBytes.getOrderNum(), message.getOrderNum());
        assertEquals(messageParsedFromBytes.getSendToA(), message.getSendToA());
        assertEquals(messageParsedFromBytes.getSendToB(), message.getSendToB());
        assertEquals(messageParsedFromBytes.getReceivedOnA(), message.getReceivedOnA());
        assertEquals(messageParsedFromBytes.getReceivedOnB(), message.getReceivedOnB());
    }

    private Message createMessageObject() {
        ZonedDateTime startTime = ZonedDateTime.parse("2020-06-29T11:24:11.252+05:30[UTC]");

        Message message =  new Message(startTime.toInstant().toEpochMilli(), 1);
        message.setReceivedOnB(startTime.plusNanos(10000000).toInstant().toEpochMilli());
        message.setSendToA(startTime.plusNanos(11000000).toInstant().toEpochMilli());
        message.setReceivedOnA(startTime.plusNanos(12000000).toInstant().toEpochMilli());
        return message;
    }
}
