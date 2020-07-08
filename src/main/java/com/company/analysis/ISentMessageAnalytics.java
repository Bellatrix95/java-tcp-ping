package main.java.com.company.analysis;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public interface ISentMessageAnalytics {

    void newMessageSent(int messageOrderNum);

    ArrayList<Integer> getMessagesOrderNums();

    ZonedDateTime getStartTime();

    int getSentMessagesCount();
}
