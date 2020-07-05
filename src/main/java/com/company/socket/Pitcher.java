package main.java.com.company.socket;

import com.google.common.util.concurrent.RateLimiter;
import main.java.com.company.analysis.Analysis;
import main.java.com.company.analysis.AtomicMessageCounter;
import main.java.com.company.messages.Message;
import main.java.com.company.messages.MessageParser;
import main.java.com.company.utils.LoggerClass;

import java.io.IOException;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Socket client class for sending byte messages to socket server.
 *
 * @author Ivana Salmanić
 */
public class Pitcher implements IPitcher{
    private String hostname;
    private int port;

    /**
     * @param hostname socket server hostname
     * @param port socket server port number
     */
    public Pitcher(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * @param messagesPerSecond number of messages per second
     * @param messageSize message size in bytes
     */
    public void start(int messagesPerSecond, int messageSize) {
        // rate is "messagesPerSecond permits per second";
        final RateLimiter rateLimiter = RateLimiter.create(messagesPerSecond);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        Analysis analyzePastTimeFrame = new Analysis();
        MessageSender.setMessageSize(messageSize);

        while(true) {
            rateLimiter.acquire();
            if(AtomicMessageCounter.getMessageCounter() % messagesPerSecond == 0 ) {
                LoggerClass.log.info(LoggerClass.parseStatisticsObject(analyzePastTimeFrame.getNetworkStats()));
                analyzePastTimeFrame = new Analysis();
            }
            try {
                executor.execute(new MessageSender(new Socket(hostname, port), AtomicMessageCounter.incrementMessageCounterAndReturn(), analyzePastTimeFrame));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class MessageSender extends SocketDataStream implements Runnable {
        private Analysis analyzePastTimeFrame;
        private int messageOrderNum;
        private static int messageSize;

        /**
         * @param socket socket server
         * @param messageOrderNum message identification number
         * @param analyzePastTimeFrame analysis object for current time frame
         */
        MessageSender(Socket socket, int messageOrderNum, Analysis analyzePastTimeFrame) {
            super.clientSocket = socket;
            this.messageOrderNum = messageOrderNum;
            this.analyzePastTimeFrame = analyzePastTimeFrame;
        }

        static void setMessageSize(int passedMessageSize) {
            messageSize = passedMessageSize;
        }

        public void run() {
            try {
                this.startConnection();

                Message message =  new Message(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli(), messageOrderNum);
                analyzePastTimeFrame.newMessageSent(messageOrderNum);
                byte[] response = sendSingleMessage(MessageParser.createByteArrayFromMessage(message, messageSize));

                Message responseMessage = MessageParser.createMessageFromByteArray(response);
                responseMessage.setReceivedOnA(ZonedDateTime.now().toInstant().toEpochMilli());
                analyzePastTimeFrame.newMessageReceived(responseMessage);

                this.stopConnection();
            } catch (Exception e) {
                e.getStackTrace();
                LoggerClass.log.warning("Exception occurred while sending a message!");
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
            if(codedMessage.length != in.readInt()) LoggerClass.log.warning("Length of messages does not match!");
            byte[] response = new byte[codedMessage.length];
            in.readFully(response);
            return response;
        }
    }
}
