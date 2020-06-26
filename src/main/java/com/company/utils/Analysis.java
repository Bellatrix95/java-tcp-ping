package main.java.com.company.utils;

import main.java.com.company.socket.Message;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;


public class Analysis {
    private long startTime = ZonedDateTime.now().getSecond();
    private int messagesSend = 0;
    private int messagesReceived = 0;
    private int sumAB;
    private int sumBA;
    private long maxABA;

    public void newMessageSent() {
        this.messagesSend++;
    }

    public void newMessageReceived(Message message) {
        //if one second time frame passes, don't add massage because new analysis has been started
        if(ZonedDateTime.now().getSecond() - startTime > 1) return;

        this.messagesReceived++;
        this.sumAB += (message.getReceivedOnB() - message.getSendToB());
        this.sumBA += (message.getReceivedOnA() - message.getSendToA());
        long sumABA = (message.getReceivedOnB() - message.getSendToB()) + (message.getReceivedOnA() - message.getSendToA());
        if (sumABA > this.maxABA) this.maxABA = sumABA;
    }

    public Map<String, Object> getNetworkStats() {
        Map<String, Object> networkStats = new LinkedHashMap<String, Object>();
        networkStats.put("messagesSend", this.messagesSend);
        networkStats.put("messagesLost", this.messagesSend - this.messagesReceived);
        networkStats.put("maxTimeABA", this.maxABA);
        networkStats.put("averageTimeAB", ((float) sumAB / messagesReceived));
        networkStats.put("averageTimeBA", ((float) sumBA / messagesReceived));
        networkStats.put("averageTimeABA", ((float) (sumAB + sumBA) / messagesReceived));

        return networkStats;
    }

}
