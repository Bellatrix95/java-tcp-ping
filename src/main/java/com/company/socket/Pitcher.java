package main.java.com.company.socket;

import com.google.common.util.concurrent.RateLimiter;
import main.java.com.company.utils.Analysis;

import java.net.*;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Socket client class for sending byte messages to socket server.
 *
 * @author Ivana Salmanić
 */
public class Pitcher {
    private static final Logger log = Logger.getLogger("Pitcher");

    /**
     * @param hostname socket server hostname
     * @param port socket server port number
     * @param messageSize message size in bytes
     */
    public Pitcher(String hostname, int port, int messageSize){

        MessageHandler.setStaticParams(hostname, port, messageSize);
    }

    /**
     * @param messagesPerSecond number of messages per second
     */
    public void startProducing(int messagesPerSecond) {
        // rate is "messagesPerSecond permits per second";
        final RateLimiter rateLimiter = RateLimiter.create(messagesPerSecond);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
        int lastTimeFrameOrderNum = 0;

        while(true) {
            if(!rateLimiter.tryAcquire()) continue;
            int logTimeFrameStats = Message.getGeneralOrderNum();
            if(logTimeFrameStats != 0 && logTimeFrameStats % messagesPerSecond == 0 && logTimeFrameStats != lastTimeFrameOrderNum) {

                StringBuilder formatForLog = new StringBuilder(ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(1).format(formatter)).append(" : ");
                log.info(parseStatsObject(formatForLog));
                MessageHandler.resetAnalysisParameter();
                lastTimeFrameOrderNum = logTimeFrameStats;
            }
            new Thread(new MessageHandler()).start();
        }
    }

    /**
     * @return String value containing statistics for past time-frame
     */
    private String parseStatsObject(StringBuilder formatForLog) {
        Map<String, Object> statsForLastTimeFrame = MessageHandler.getAnalysisParameter().getNetworkStats();
        Iterator<String> iterate = statsForLastTimeFrame.keySet().iterator();

        while(iterate.hasNext()) {
            String key = iterate.next();
            formatForLog.append(key).append("=").append(statsForLastTimeFrame.get(key)).append("  ");
        }
        formatForLog.append("\n");
        return formatForLog.toString();
    }

    private static class MessageHandler implements Runnable {
        private Socket clientSocket;
        private DataOutputStream out;
        private DataInputStream in;
        private static Analysis analyzePastTimeFrame;
        private static String hostname;
        private static int port;
        private static int messageSize;

        /**
         * @param hostname socket server hostname
         * @param port socket server port number
         * @param messageSize message size in bytes
         */
        public static void setStaticParams(String hostname, int port, int messageSize) {
            MessageHandler.hostname = hostname;
            MessageHandler.port = port;
            MessageHandler.messageSize = messageSize;
            MessageHandler.analyzePastTimeFrame = new Analysis();
        }

        public void run() {
            try {
                this.startConnection();
                Message message =  new Message(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
                analyzePastTimeFrame.newMessageSent(message.getOrderNum());
                byte[] response = sendSingleMessage(message.createByteArrayFromMessage(messageSize));
                message = new Message(response);
                message.setReceivedOnA(ZonedDateTime.now().toInstant().toEpochMilli());
                analyzePastTimeFrame.newMessageReceived(message);

                this.stopConnection();
            } catch (IOException e) {
                e.getStackTrace();
                log.warning("Exception occurred while sending a message!");
            }
        }

        /**
         * Sends message in TLV format (Type Length Value) to socket server and reads response
         * @param codedMessage message object parsed to byte array
         * @return byte array response value from input stream
         */
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

        /**
         * @return Analysis statistics for past time-frame
         */
        public static Analysis getAnalysisParameter() {
            return analyzePastTimeFrame;
        }

    }
}
