package main.java.com.company;

import main.java.com.company.utils.ByteArray;

import java.io.Serializable;

public class Message implements Serializable {
    private int orderNumber;
    private long sendToB;
    private long receivedOnB;
    private long sendToA;
    private long receivedOnA;

    public Message (int orderNumber, long sendToB) {
        this.orderNumber = orderNumber;
        this.sendToB = sendToB;
    }

    public Message (byte[] byteArray) {
        this.orderNumber = ByteArray.readInt(byteArray,0);
        this.sendToB = ByteArray.readLong(byteArray,4);
        this.receivedOnB = ByteArray.readLong(byteArray,12);
        this.sendToA = ByteArray.readLong(byteArray,20);
        this.receivedOnA = ByteArray.readLong(byteArray,28);
        //the rest of the bytes in the byteArray are randomly generated

    }

    public int getOrderNumber() {
        return this.orderNumber;
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

    public byte[] createByteArrayFromMessage(int messageSize) {
        ByteArray byteArray = new ByteArray(messageSize);

        byteArray.writeInt(this.getOrderNumber());
        byteArray.writeLong(this.getSendToB());
        byteArray.writeLong(this.getReceivedOnB());
        byteArray.writeLong(this.getSendToA());
        byteArray.writeLong(this.getReceivedOnA());


        int size = byteArray.getLength();

        byteArray.writeBytes(ByteArray.generateRandomBytesArray(size - byteArray.getPos()), byteArray.getPos(), size - byteArray.getPos());

        return byteArray.getData();
    }
}
