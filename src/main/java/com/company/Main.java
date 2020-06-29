package main.java.com.company;

import main.java.com.company.socket.Catcher;
import main.java.com.company.socket.Pitcher;
import org.apache.commons.cli.*;

import java.util.logging.Logger;

import static main.java.com.company.utils.ArgumentParser.checkCommandLineParams;


public class Main {
    private final static Logger log;
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s %5$s%6$s%n");
        log = Logger.getLogger("Main");
    }

    public static void main(String[] args) {
        log.info("Starting application `java-tcp-ping` !");

        try {
            CommandLine commandLine = checkCommandLineParams(args);
            if(commandLine.hasOption("p")) {
                log.info("The application will be started as Pitcher");

                int messageSize = commandLine.hasOption("size") ? Integer.parseInt(commandLine.getOptionValue("size")) : 300;
                int messagesPerSecond = commandLine.hasOption("mps") ? Integer.parseInt(commandLine.getOptionValue("mps")) : 1;

                Pitcher client = new Pitcher(commandLine.getOptionValue("h"), Integer.parseInt(commandLine.getOptionValue("port")), messageSize);
                client.startProducing(messagesPerSecond);

            } else if(commandLine.hasOption("c")) {
                log.info("The application will be started as Catcher");

                Catcher server = new Catcher();
                server.start(commandLine.getOptionValue("b"), 10, Integer.parseInt(commandLine.getOptionValue("port")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exiting application `java-tcp-ping` !");
        }
    }
}
