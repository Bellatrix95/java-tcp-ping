package main.java.com.company.client;

import main.java.com.company.arguments.ArgumentChecker;
import main.java.com.company.arguments.IArgumentChecker;
import main.java.com.company.socket.Catcher;
import main.java.com.company.socket.ICatcher;
import main.java.com.company.socket.IPitcher;
import main.java.com.company.socket.Pitcher;
import main.java.com.company.utils.LoggerClass;
import org.apache.commons.cli.ParseException;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.util.Map;


/**
 * Currently, this class creates Socket Pitcher and Catcher but if there is a need for expansion, a new class and interface can be added to handle the creation of different Pitcher and Catcher clients like HTTP clients over REST or like UDP over Sockets.
 *
 * @author Ivana Salmanić
 */
public class DefineSocketClients extends DefineClients {
    private Map<String, String> properties;

    public DefineSocketClients(DefineClients.Type type, String[] args) throws ConfigurationException, ParseException, IOException {
        if(!type.equals(DefineClients.Type.SOCKET)) throw new ConfigurationException("Type " + type + "not supported!");

        IArgumentChecker argumentChecker = new ArgumentChecker();
        properties = argumentChecker.checkArgs(args);

        if(properties.containsKey("c")) defineCatcher();
        if(properties.containsKey("p")) definePitcher();
    }

    public void defineCatcher() throws IOException {
        int port = Integer.parseInt(properties.get("port"));
        int numOfThreadsUsed = properties.containsKey("numThreads") ? Integer.parseInt(properties.get("numThreads")) : 2;

        LoggerClass.log.info("The application will be started as Catcher");

        String bindAddress = properties.get("b");

        ICatcher server = new Catcher(bindAddress, port);
        server.setNumOfThreadsUsed(numOfThreadsUsed);
        server.start();

    }

    public void definePitcher() {
        int port = Integer.parseInt(properties.get("port"));
        int numOfThreadsUsed = properties.containsKey("numThreads") ? Integer.parseInt(properties.get("numThreads")) : 2;

        LoggerClass.log.info("The application will be started as Pitcher");

        String hostname = properties.get("h");
        int messageSize = properties.containsKey("size") ? Integer.parseInt(properties.get("size")) : 300;
        int messagesPerSecond = properties.containsKey("mps") ? Integer.parseInt(properties.get("mps")) : 1;

        IPitcher client = new Pitcher(hostname, port);
        client.setNumOfThreadsUsed(numOfThreadsUsed);
        client.start(messagesPerSecond, messageSize);
    }
}
