package main.java.com.company.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Socket data stream abstraction
 *
 * @author Ivana Salmanić
 */
public abstract class SocketDataStream {
    public Socket clientSocket;
    public DataOutputStream out;
    public DataInputStream in;

    public void startConnection() throws IOException {
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
