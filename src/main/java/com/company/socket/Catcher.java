package main.java.com.company.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

/**
 * Socket server class for handling incoming massages.
 *
 * @author Ivana Salmanić
 */

public class Catcher {

    private ServerSocket serverSocket;
    private static final Logger log = Logger.getLogger("Catcher");

    /**
     * @param bind socket server IP
     * @param numOfConnections number of connections
     * @param port socket server port number
     */

    public void start(String bind, int numOfConnections, int port) throws IOException {
        serverSocket = new ServerSocket(port, numOfConnections,InetAddress.getByName(bind));

        while (true)
            new Thread(new ClientHandler(serverSocket.accept())).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataOutputStream out;
        private DataInputStream in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                this.startConnection();

                if(in.readChar() != 'b') throw new RuntimeException("Catcher can only read byte type messages!");

                byte[] messageBytes = new byte[in.readInt()];
                in.readFully(messageBytes);
                long receivedOnB = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();

                Message message = new Message(messageBytes);
                message.setReceivedOnB(receivedOnB);
                message.setSendToA(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
                out.writeChar('b');
                out.writeInt(messageBytes.length);
                out.write(message.createByteArrayFromMessage(messageBytes.length));

                this.stopConnection();
            } catch (Exception e) {
                e.printStackTrace();
                log.warning("Exception occurred while handling incoming message!");
            }
        }

        private void startConnection() throws IOException {
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
        }

        private void stopConnection() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
        }
    }
}
