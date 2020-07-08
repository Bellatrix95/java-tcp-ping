package main.java.com.company.client;

import java.io.IOException;

public abstract class DefineClients {
    public enum Type {
        SOCKET
    }

    abstract void defineCatcher() throws IOException;

    abstract void definePitcher() throws IOException;
}
