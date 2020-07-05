package main.java.com.company.analysis;

import main.java.com.company.messages.Message;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class containing network statistics for sent messages objects
 *
 * @author Ivana Salmanić
 */
public class Analysis {
    private ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC"));
    private int messagesReceived;
    private int sumAB;
    private int sumBA;
    private long maxABA;
    private ArrayList<Integer> messagesOrderNums = new ArrayList<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    /**
     * @param orderNum the message identification number
     */
    public void newMessageSent(int orderNum) {
        writeLock.lock();
        try {
            messagesOrderNums.add(orderNum);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * @param message the response message from socket server
     */
    public void newMessageReceived(Message message) {
        //if one second time frame passes, don't add message because new time frame has been started
        if(ZonedDateTime.now().getSecond() - startTime.getSecond() > 1 || !messagesOrderNums.contains(message.getOrderNum())) return;

        writeLock.lock();
        try {
            this.messagesReceived++;
            this.sumAB += (message.getReceivedOnB() - message.getSendToB());
            this.sumBA += (message.getReceivedOnA() - message.getSendToA());
            long sumABA = (message.getReceivedOnB() - message.getSendToB()) + (message.getReceivedOnA() - message.getSendToA());
            if (sumABA > this.maxABA) this.maxABA = sumABA;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * @return network statistics for the passed time frame
     */
    public Map<String, Object> getNetworkStats() {
        Map<String, Object> networkStats = new LinkedHashMap<>();
        networkStats.put("startTime", this.startTime);

        readLock.lock();
        try {
            networkStats.put("messagesSent", this.messagesOrderNums.size());
            networkStats.put("messagesLost", this.messagesOrderNums.size() - this.messagesReceived);
            networkStats.put("maxTimeABA", this.maxABA);
            networkStats.put("averageTimeAB", ((float) sumAB / messagesReceived));
            networkStats.put("averageTimeBA", ((float) sumBA / messagesReceived));
            networkStats.put("averageTimeABA", ((float) (sumAB + sumBA) / messagesReceived));
        } finally {
            readLock.unlock();
        }
        return networkStats;
    }

//    public ZonedDateTime getStartTime() {
//        return this.startTime;
//    }
//
//    public int getSentMessageCount() {
//        return this.messagesOrderNums.size();
//    }
//
//    public int getLostMessagesCount() {
//        return this.messagesOrderNums.size() - this.messagesReceived;
//    }
//
//    public float getMaxTimeABA() {
//        return this.maxABA;
//    }
//
//    public float getAverageTimeAB() {
//        return (float) sumAB / messagesReceived;
//    }
//
//    public float getAverageTimeBA() {
//        return (float) sumBA / messagesReceived;
//    }
//
//    public float getAverageTimeABA() {
//        return (float) (sumAB + sumBA) / messagesReceived;
//    }
}
