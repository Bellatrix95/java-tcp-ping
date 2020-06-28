package main.java.com.company.socket;

import main.java.com.company.utils.ByteArray;

import java.io.Serializable;

/**
 * Class describing messages sent over sockets.
 *
 * @author Ivana Salmanić
 */
public class Message implements Serializable {
    private static int generalOrderNum = 0;
    private long sendToB;
    private long receivedOnB;
    private long sendToA;
    private long receivedOnA;

    /**
     * @param sendToB timestamp when message was sent to Catcher
     */
    public Message (long sendToB) {
        generalOrderNum++;
        this.sendToB = sendToB;
    }

    /**
     * @param byteArray containing parsed message object
     */
    public Message (byte[] byteArray) {
        //int orderNumber = ByteArray.readInt(byteArray,0);
        this.sendToB = ByteArray.readLong(byteArray,4);
        this.receivedOnB = ByteArray.readLong(byteArray,12);
        this.sendToA = ByteArray.readLong(byteArray,20);
        this.receivedOnA = ByteArray.readLong(byteArray,28);
        //the rest of the bytes in the byteArray are randomly generated
    }

    public static int getGeneralOrderNum() {
        return generalOrderNum;
    }

    public long getSendToB() {
        return this.sendToB;
    }

    public long getSendToA() {
        return this.sendToA;
    }

    public long getReceivedOnB() {
        return this.receivedOnB;
    }

    public long getReceivedOnA() {
        return this.receivedOnA;
    }

    public void setSendToA(long sendToA) {
         this.sendToA = sendToA;
    }

    public void setReceivedOnB(long receivedOnB) {
         this.receivedOnB = receivedOnB;
    }

    public void setReceivedOnA(long receivedOnA) {
         this.receivedOnA = receivedOnA;
    }

    /**
     * @param messageSize final byte array size
     * @return message object parsed to byte array + random bytes
     */
    public byte[] createByteArrayFromMessage(int messageSize) {
        ByteArray byteArray = new ByteArray(messageSize);

        byteArray.writeInt(generalOrderNum);
        byteArray.writeLong(this.getSendToB());
        byteArray.writeLong(this.getReceivedOnB());
        byteArray.writeLong(this.getSendToA());
        byteArray.writeLong(this.getReceivedOnA());

        int size = byteArray.getLength();

        byteArray.writeBytes(ByteArray.generateRandomBytesArray(size - byteArray.getPos()), byteArray.getPos(), size - byteArray.getPos());

        return byteArray.getData();
    }
}
