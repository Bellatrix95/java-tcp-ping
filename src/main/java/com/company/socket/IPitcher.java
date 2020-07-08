package main.java.com.company.socket;

public interface IPitcher {

    void start(int messagesPerSecond, int messageSize);

    void setNumOfThreadsUsed(int numOfThreadsUsed);

}
