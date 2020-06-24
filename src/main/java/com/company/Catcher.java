package main.java.com.company;

import java.net.*;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Catcher {

    private ServerSocket serverSocket;

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
                long receivedOnB = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();

                char dataType = in.readChar();
                int length = in.readInt();

                if(dataType == 'b') {
                    byte[] messageBytes = new byte[length];
                    in.readFully(messageBytes);

                    Message message = new Message(messageBytes);
                    message.setReceivedOnB(receivedOnB);
                    message.setSendToA(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
                    out.writeChar('b');
                    out.writeInt(length);
                    out.write(message.createByteArrayFromMessage(length));
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
