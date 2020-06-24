package main.java.com.company;

import com.google.common.util.concurrent.RateLimiter;


import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;


public class Main {

    static RateLimiter rateLimiter;
    public static void main(String[] args) {
        try {
            // write your code here
            System.out.println("Starting app");

            boolean isPitcher = true;
            int port = 6000;
            String hostname = "127.0.0.1";
            int mps = 2;
            int messageSize = 50;
            String bind = "127.0.0.1";
            int numOfConnections = 10;

            rateLimiter = RateLimiter.create(mps); // rate is "mps permits per second"

            if (isPitcher) {
                int orderNum = 0;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
                while (true) {
                    orderNum++;

                    ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC"));
                    long startTimeSeconds = startTime.getSecond();

                    System.out.println(startTime.format(formatter));

                    Pitcher client = new Pitcher();
                    client.startConnection(hostname, port);

                    //rateLimiter.acquire();
                    rateLimiter.tryAcquire(mps, 1, TimeUnit.SECONDS);
                    Message message =  new Message(orderNum, ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
                    byte[] response = client.sendMessage(message.createByteArrayFromMessage(messageSize));
                    message = new Message(response);
                    message.setReceivedOnA(ZonedDateTime.now().toInstant().toEpochMilli());

                    client.stopConnection();
                    System.out.println("Message responce ---->>>>> " + message);
                    System.out.println("Message order num ---->>>>> " + message.getOrderNumber());
                    System.out.println("Message getSendToB ---->>>>> " + message.getSendToB());
                    System.out.println("Message getReceivedOnB ---->>>>> " + message.getReceivedOnB());
                    System.out.println("Message getSendToA ---->>>>> " + message.getSendToA());
                    System.out.println("Message getReceivedOnA ---->>>>> " + message.getReceivedOnA());

                    long elapsedTimeSeconds = ZonedDateTime.now(ZoneId.of("UTC")).getSecond() - startTimeSeconds;

                    System.out.println("start time - " + startTime.format(formatter) + " -----  " + elapsedTimeSeconds);
                    break;
                }
            } else {
                Catcher server = new Catcher();
                server.start(bind, numOfConnections, port);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
