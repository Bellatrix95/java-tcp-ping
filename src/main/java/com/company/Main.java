package main.java.com.company;

import main.java.com.company.socket.Catcher;
import main.java.com.company.socket.Pitcher;

import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s %5$s%6$s%n");
        final Logger log = Logger.getLogger("Main");
        log.info("Starting application `java-tcp-ping` !");

        boolean isPitcher = true;
        int port = 6000;

        if (isPitcher) {
            String hostname = "127.0.0.1";
            int messageSize = 50;
            int messagesPerSecond = 4;
            try {
                Pitcher client = new Pitcher(hostname, port);
                client.startProducing(messagesPerSecond, messageSize);
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
