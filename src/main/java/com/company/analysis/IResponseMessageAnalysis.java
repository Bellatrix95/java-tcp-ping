package main.java.com.company.analysis;

import main.java.com.company.messages.IMessage;

public interface IResponseMessageAnalysis {
    void newMessageReceived(IMessage message);

    int getMessagesReceived();

    long getMaxTimeABA();

    float getAverageTimeAB();

    float getAverageTimeBA();

    float getAverageTimeABA();

}
