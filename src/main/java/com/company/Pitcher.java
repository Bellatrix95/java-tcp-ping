package main.java.com.company;

import com.google.common.util.concurrent.RateLimiter;
import main.java.com.company.utils.Analysis;

import java.net.*;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Pitcher {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private String hostname;
    private int port;
    private static final Logger log = Logger.getLogger("Pitcher");

    public Pitcher(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }


    public void startConnection() throws IOException {
        clientSocket = new Socket(hostname, port);
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
    }

    public void startProducing(int messageSize, int messagesPerSecond) {
        int orderNum = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
        Analysis analyzePastTimeFrame = new Analysis();
        RateLimiter rateLimiter = RateLimiter.create(messagesPerSecond); // rate is "mps permits per second";
        ExecutorService service = Executors.newCachedThreadPool();

        while (true) {
            ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC"));
            log.info("START TIME: " + startTime.format(formatter));
            if (orderNum != 0) log.info("Analyze network traffic for previous second: " +  analyzePastTimeFrame.getNetworkStats());
            analyzePastTimeFrame = new Analysis();
            orderNum++;

            try {
                this.startConnection();

                //has a bug related to reteLimiter
                rateLimiter.tryAcquire(messagesPerSecond, 1, TimeUnit.SECONDS);
                Message message =  new Message(orderNum, ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
                analyzePastTimeFrame.newMessageSent();
                byte[] response = sendSingleMessage(message.createByteArrayFromMessage(messageSize));
                message = new Message(response);
                message.setReceivedOnA(ZonedDateTime.now().toInstant().toEpochMilli());
                analyzePastTimeFrame.newMessageReceived(message);

                this.stopConnection();
            } catch (IOException e) {
                log.warning("Iteration step failed!");
            }
            long elapsedTimeSeconds = ZonedDateTime.now(ZoneId.of("UTC")).getSecond() - startTime.getSecond();

            System.out.println("start time - " + startTime.format(formatter) + " -----  " + elapsedTimeSeconds);
        }
    }

    private byte[] sendSingleMessage(byte[] codedMessage) throws IOException {

        //Sending data in TLV format
        out.writeChar('b'); // b for byte
        out.writeInt(codedMessage.length);
        out.write(codedMessage);

        in.readChar();
        if(codedMessage.length != in.readInt()) log.warning("Length of messages does not match!");
        byte[] response = new byte[codedMessage.length];
        in.readFully(response);

        return response;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
