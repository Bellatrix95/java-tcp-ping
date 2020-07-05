package main.java.com.company.messages;

import java.io.Serializable;

/**
 * Class describing messages sent over sockets.
 *
 * @author Ivana Salmanić
 */
public class Message implements Serializable {
    private int orderNum;
    private long sendToB;
    private long receivedOnB;
    private long sendToA;
    private long receivedOnA;

    /**
     * @param sendToB timestamp when the message was sent to Catcher
     * @param messageCounter message order number
     */
    public Message (long sendToB, int messageCounter) {
        this.sendToB = sendToB;
        this.orderNum = messageCounter;
    }
    
    public int getOrderNum() {
        return this.orderNum;
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
}
