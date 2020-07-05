package main.java.com.company.messages;

import main.java.com.company.utils.ByteArray;

/**
 * Message object parser class, parses message from/to byte array
 *
 * @author Ivana Salmanić
 */
public abstract class MessageParser {

    /**
     * @param message Message object
     * @return message object parsed to byte array + random bytes
     */
    public static byte[] createByteArrayFromMessage(Message message, int messageSize) {
        ByteArray byteArray = new ByteArray(messageSize);

        byteArray.writeInt(message.getOrderNum());
        byteArray.writeLong(message.getSendToB());
        byteArray.writeLong(message.getReceivedOnB());
        byteArray.writeLong(message.getSendToA());
        byteArray.writeLong(message.getReceivedOnA());

        int size = byteArray.getLength();

        byteArray.writeBytes(ByteArray.generateRandomBytesArray(size - byteArray.getPos()), byteArray.getPos(), size - byteArray.getPos());

        return byteArray.getData();
    }

    /**
     * @param byteArray containing parsed information for message object
     * @return parsed message object
     */
    public static Message createMessageFromByteArray(byte[] byteArray) {
        int orderNum = ByteArray.readInt(byteArray,0);
        long sendToB = ByteArray.readLong(byteArray,4);

        Message message = new Message(sendToB, orderNum);
        message.setReceivedOnB(ByteArray.readLong(byteArray,12));
        message.setSendToA(ByteArray.readLong(byteArray,20));
        message.setReceivedOnA(ByteArray.readLong(byteArray,28));
        //the rest of the bytes in the byteArray are randomly generated

        return message;
    }
}
