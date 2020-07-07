package main.java.com.company.analysis;

import main.java.com.company.messages.IMessage;

import java.time.ZonedDateTime;

/**
 * Analytics class for tracking network statistics from response messages info
 *
 * @author Ivana Salmanić
 */
public class ResponseMessageAnalytics implements IResponseMessageAnalysis {
    private ISentMessageAnalytics sentMessageAnalytics;
    private volatile int responseMessagesCounter;
    private long sumAB;
    private long sumBA;
    private long maxABA;

    public ResponseMessageAnalytics(ISentMessageAnalytics sentMessageAnalytics) {
        this.sentMessageAnalytics = sentMessageAnalytics;
    }

    /**
     * @param message the response message from socket server
     */
    public synchronized void newMessageReceived(IMessage message) {
        //if one second time frame passes or message order num isn't in sent messages, don't add message because new time frame has been started
        if(ZonedDateTime.now().getSecond() - sentMessageAnalytics.getStartTime().getSecond() > 1 || !sentMessageAnalytics.getMessagesOrderNums().contains(message.getOrderNum())) return;

        responseMessagesCounter++;
        sumAB += (message.getReceivedOnB() - message.getSendToB());
        sumBA += (message.getReceivedOnA() - message.getSendToA());
        long sumABA = (message.getReceivedOnB() - message.getSendToB()) + (message.getReceivedOnA() - message.getSendToA());
        if (sumABA > maxABA) maxABA = sumABA;
    }

    /**
     * @return  number of received messages
     */
    public int getMessagesReceived() {
        return this.responseMessagesCounter;
    }

    /**
     * @return max time in milliseconds from Pitcher to Catcher and back
     */
    public long getMaxTimeABA() {
        return this.maxABA;
    }

    /**
     * @return average time in millisecond for Pitcher to Catcher
     */
    public float getAverageTimeAB() {
        return (float) this.sumAB / this.responseMessagesCounter;
    }

    /**
     * @return average time in millisecond for Catcher to Pitcher
     */
    public float getAverageTimeBA() {
        return (float) this.sumBA / this.responseMessagesCounter;
    }

    /**
     * @return average time in millisecond for Pitcher to Catcher and back
     */
    public float getAverageTimeABA() {
        return (float) (this.sumAB + this.sumBA) / this.responseMessagesCounter;
    }
}
