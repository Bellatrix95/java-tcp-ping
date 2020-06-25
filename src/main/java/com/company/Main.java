package main.java.com.company;

import java.util.logging.Logger;

public class Main {

    private static final Logger log = Logger.getLogger("Main");

    public static void main(String[] args) {

        log.info("Starting application `java-tcp-ping` !");

        boolean isPitcher = true;
        int port = 6000;

        if (isPitcher) {
            String hostname = "127.0.0.1";
            int messageSize = 50;
            int messagesPerSecond = 2;
            try {
                Pitcher client = new Pitcher(hostname, port);
                client.startProducing(messageSize, messagesPerSecond);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            int numOfConnections = 10;
            String bind = "127.0.0.1";
            try {
                Catcher server = new Catcher();
                server.start(bind, numOfConnections, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("Exiting application `java-tcp-ping` !");
    }

}
