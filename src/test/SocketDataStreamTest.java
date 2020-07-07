package test;

import main.java.com.company.socket.SocketDataStream;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * SocketDataStream abstract class - testing. Simple Socket client and server classes that extend SocketDataStream are created for exchanging 'char' via sockets
 *
 * @author Ivana Salmanić
 */

public class SocketDataStreamTest {

    private GreetClient client;
    private static int port;

    public static class GreetClient extends SocketDataStream {

        GreetClient(Socket socket) {
            super.clientSocket = socket;
        }

        char sendMessage(char someRandomChar) throws IOException {
            this.startConnection();

            out.writeChar(someRandomChar);
            char resp = in.readChar();

            this.stopConnection();
            return resp;
        }
    }

    public static class GreetServer extends SocketDataStream {

        GreetServer(Socket socket) {
            super.clientSocket = socket;
        }

        void run() {
            try {
                this.startConnection();

                char greeting = in.readChar();
                if ('c' == greeting) {
                    out.writeChar('s');
                } else {
                    out.writeChar('w');
                }

                this.stopConnection();
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    @BeforeClass
    public static void start() throws InterruptedException, IOException {

        // Take an available port
        ServerSocket s = new ServerSocket(0);
        port = s.getLocalPort();
        s.close();

        Executors.newSingleThreadExecutor()
                .submit(() -> {
                    try {
                        new GreetServer(new ServerSocket(port, 1, InetAddress.getByName("localhost")).accept()).run();
                    } catch (IOException e) {
                        e.printStackTrace();
                        fail();
                    }
                });
        Thread.sleep(500);
    }

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        try {
            client = new GreetClient(new Socket("localhost", port));

            char response = client.sendMessage('c');
            assertEquals('s', response);

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @After
    public void finish() {
        try {
            client.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
