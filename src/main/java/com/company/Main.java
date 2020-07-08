package main.java.com.company;

import main.java.com.company.client.DefineSocketClients;
import main.java.com.company.client.DefineClients.Type;
import main.java.com.company.utils.LoggerClass;

/**
 * Main class for handling the creation of Pitcher and/or Catcher using DefineSocketClients.
 *
 * @author Ivana Salmanić
 */

public class Main {

    public static void main(String[] args) {
        LoggerClass.log.info("Starting application `java-tcp-ping` !");

        try {
            new DefineSocketClients(Type.SOCKET, args);

        } catch (Exception e) {
            e.printStackTrace();
            LoggerClass.log.info("Exiting application `java-tcp-ping` !");
        }
    }
}
