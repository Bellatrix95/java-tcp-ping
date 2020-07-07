package main.java.com.company.analysis;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class SentMessageAnalytics implements ISentMessageAnalytics{

    protected final ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC"));
    protected ArrayList<Integer> messagesOrderNums = new ArrayList<>();

    /**
     * @param messageOrderNum the message identification number
     */
    public synchronized void newMessageSent(int messageOrderNum) {
        messagesOrderNums.add(messageOrderNum);
    }

    public synchronized int getSentMessagesCount() {
        return messagesOrderNums.size();
    }

    public synchronized ArrayList<Integer> getMessagesOrderNums() { return messagesOrderNums; }

    public ZonedDateTime getStartTime() { return startTime; }
}
