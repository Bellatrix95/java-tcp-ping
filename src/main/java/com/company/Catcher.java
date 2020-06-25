package main.java.com.company;

import javax.imageio.IIOException;
import java.net.*;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

public class Catcher {

    private ServerSocket serverSocket;
    private static final Logger log = Logger.getLogger("Catcher");

    public void start(String bind, int numOfConnections, int port) throws IOException {
        serverSocket = new ServerSocket(port, numOfConnections,InetAddress.getByName(bind));

        while (true)
            new ClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private DataOutputStream out;
        private DataInputStream in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new DataOutputStream(clientSocket.getOutputStream());
                in = new DataInputStream(clientSocket.getInputStream());

                if(in.readChar() != 'b') {
                    log.warning("Catcher can only read byte type messages!");
                    return;
                }

                byte[] messageBytes = new byte[in.readInt()];
                in.readFully(messageBytes);
                long receivedOnB = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();

                Message message = new Message(messageBytes);
                message.setReceivedOnB(receivedOnB);
                message.setSendToA(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
                out.writeChar('b');
                out.writeInt(messageBytes.length);
                out.write(message.createByteArrayFromMessage(messageBytes.length));

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                log.warning("Exception occurred while handling incoming message!");
            }
        }
    }
}
