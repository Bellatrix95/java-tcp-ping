package main.java.com.company.socket;

import com.google.common.util.concurrent.RateLimiter;
import main.java.com.company.utils.Analysis;

import java.net.*;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class Pitcher {
    private String hostname;
    private int port;
    private static final Logger log = Logger.getLogger("Pitcher");

    public Pitcher(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void startProducing(int messagesPerSecond, int messageSize) {
        final RateLimiter rateLimiter = RateLimiter.create(messagesPerSecond); // rate is "mps permits per second";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
        MessageHandler.resetAnalysisParameter();

        while(true) {
            rateLimiter.acquire();
            if(Message.getGeneralOrderNum() % messagesPerSecond == 0) {
                ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC"));
                if (Message.getGeneralOrderNum() != 0) log.info("Network traffic analysis for previous second: " +  MessageHandler.getAnalysisParameter().getNetworkStats());
                log.info("START TIME: " + startTime.format(formatter));
                MessageHandler.resetAnalysisParameter();
            }
            new MessageHandler(hostname, port, messageSize).start();
        }
    }

    private static class MessageHandler extends Thread {
        private Socket clientSocket;
        private DataOutputStream out;
        private DataInputStream in;
        private static Analysis analyzePastTimeFrame;
        private static String hostname;
        private static int port;
        int messageSize;


        public MessageHandler(String hostname, int port, int messageSize){
            this.hostname = hostname;
            this.port = port;
            this.messageSize = messageSize;
        }

        public void run() {
            try {
                this.startConnection();
                Message message =  new Message(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
                analyzePastTimeFrame.newMessageSent();
                byte[] response = sendSingleMessage(message.createByteArrayFromMessage(messageSize));
                message = new Message(response);
                message.setReceivedOnA(ZonedDateTime.now().toInstant().toEpochMilli());
                analyzePastTimeFrame.newMessageReceived(message);

                this.stopConnection();
            } catch (IOException e) {
                log.warning("Exception occurred while sending a message!");
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

        public void startConnection() throws IOException {
            clientSocket = new Socket(hostname, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
        }

        public void stopConnection() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
        }

        public static void resetAnalysisParameter() {
            analyzePastTimeFrame = new Analysis();
        }

        public static Analysis getAnalysisParameter() {
            return analyzePastTimeFrame;
        }

    }
}
