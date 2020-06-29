package main.java.com.company.utils;

import main.java.com.company.socket.Message;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class containing network statistics information for the one-second time frame
 *
 * @author Ivana Salmanić
 */
public class Analysis {
    private long startTime = ZonedDateTime.now().getSecond();
    private int messagesSent = 0;
    private int messagesReceived = 0;
    private int sumAB;
    private int sumBA;
    private long maxABA;

    public void newMessageSent() {
        this.messagesSent++;
    }

    /**
     * @param message the response message from socket server
     */
    public void newMessageReceived(Message message) {
        //if one second time frame passes, don't add message because new time frame has been started
        if(ZonedDateTime.now().getSecond() - startTime > 1) return;

        this.messagesReceived++;
        this.sumAB += (message.getReceivedOnB() - message.getSendToB());
        this.sumBA += (message.getReceivedOnA() - message.getSendToA());
        long sumABA = (message.getReceivedOnB() - message.getSendToB()) + (message.getReceivedOnA() - message.getSendToA());
        if (sumABA > this.maxABA) this.maxABA = sumABA;
    }

    /**
     * @return network statistics for the passed time frame
     */
    public Map<String, Object> getNetworkStats() {
        Map<String, Object> networkStats = new LinkedHashMap<>();
        networkStats.put("messagesSent", this.messagesSent);
        networkStats.put("messagesLost", this.messagesSent - this.messagesReceived);
        networkStats.put("maxTimeABA", this.maxABA);
        networkStats.put("averageTimeAB", ((float) sumAB / messagesReceived));
        networkStats.put("averageTimeBA", ((float) sumBA / messagesReceived));
        networkStats.put("averageTimeABA", ((float) (sumAB + sumBA) / messagesReceived));

        return networkStats;
    }

}
