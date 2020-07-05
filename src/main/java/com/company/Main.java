package main.java.com.company;

import main.java.com.company.socket.Catcher;
import main.java.com.company.socket.ICatcher;
import main.java.com.company.socket.IPitcher;
import main.java.com.company.socket.Pitcher;
import main.java.com.company.arguments.ArgumentChecker;
import main.java.com.company.utils.LoggerClass;
import org.apache.commons.cli.CommandLine;


public class Main {

    public static void main(String[] args) {
        LoggerClass.log.info("Starting application `java-tcp-ping` !");

        try {
            CommandLine commandLine = ArgumentChecker.checkCommandLineParams(args);

            int port = Integer.parseInt(commandLine.getOptionValue("port"));

            if(commandLine.hasOption("p")) {
                LoggerClass.log.info("The application will be started as Pitcher");

                String hostname = commandLine.getOptionValue("h");
                int messageSize = commandLine.hasOption("size") ? Integer.parseInt(commandLine.getOptionValue("size")) : 300;
                int messagesPerSecond = commandLine.hasOption("mps") ? Integer.parseInt(commandLine.getOptionValue("mps")) : 1;

                IPitcher client = new Pitcher(hostname, port);
                client.start(messagesPerSecond, messageSize);
            }

            if(commandLine.hasOption("c")) {
                LoggerClass.log.info("The application will be started as Catcher");

                String bindAddress = commandLine.getOptionValue("b");
                ICatcher server = new Catcher(bindAddress, port);
                server.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
            LoggerClass.log.info("Exiting application `java-tcp-ping` !");
        }
    }
}
