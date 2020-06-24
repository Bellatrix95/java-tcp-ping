package main.java.com.company;

import main.java.com.company.custom.exceptions.MessagesLengthDoesNotMatch;

import java.net.*;
import java.io.*;

public class Pitcher {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public void startConnection(String hostname, int port) throws IOException {
        clientSocket = new Socket(hostname, port);
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
    }

    public byte[] sendMessage(byte[] codedMessage) throws IOException,MessagesLengthDoesNotMatch {

        //Sending data in TLV format
        out.writeChar('b'); // b for byte
        out.writeInt(codedMessage.length);
        out.write(codedMessage);

        in.readChar();
        if(codedMessage.length != in.readInt()) throw new MessagesLengthDoesNotMatch("Length of messages does not match!");
        byte[] response = new byte[codedMessage.length];
        in.readFully(response);

        return response;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
