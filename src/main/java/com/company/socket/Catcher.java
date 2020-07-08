package main.java.com.company.socket;

import main.java.com.company.messages.Message;
import main.java.com.company.messages.MessageParser;
import main.java.com.company.utils.LoggerClass;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Socket server class for handling incoming messages.
 *
 * @author Ivana Salmanić
 */
public class Catcher implements ICatcher {
    private ServerSocket serverSocket;
    private int numOfThreadsUsed;

    /**
     * @param bind socket server IP
     * @param port socket server port number
     */
    public Catcher(String bind, int port) throws IOException {
        serverSocket = new ServerSocket(port, 10, InetAddress.getByName(bind));
    }

    /**
     * @param numOfThreadsUsed number of threads for handling incoming messages
     */
    public void setNumOfThreadsUsed(int numOfThreadsUsed) {
        this.numOfThreadsUsed = numOfThreadsUsed;
    }

    public void start() {
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreadsUsed);

        while (true) {
            try {
                executor.execute((new MessageHandler(serverSocket.accept())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class MessageHandler extends SocketDataStream implements Runnable {

        MessageHandler(Socket socket) {
            super.clientSocket = socket;
        }

        public void run() {
            try {
                this.startConnection();

                if(in.readChar() != 'b') throw new RuntimeException("The Catcher can only read byte type messages!");

                int length = in.readInt();
                byte[] messageBytes = new byte[length];
                in.readFully(messageBytes);
                long receivedOnB = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();

                Message message = MessageParser.createMessageFromByteArray(messageBytes);
                message.setReceivedOnB(receivedOnB);
                message.setSendToA(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
                out.writeChar('b');
                out.writeInt(length);
                out.write(MessageParser.createByteArrayFromMessage(message, length));

                this.stopConnection();
            } catch (Exception e) {
                e.printStackTrace();
                LoggerClass.log.warning("Exception occurred while handling incoming message!");
            }
        }
    }
}
