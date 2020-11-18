package javache;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import java.net.*;
import java.util.concurrent.FutureTask;

import static javache.constants.GlobalMessages.LISTENING;
import static javache.constants.GlobalMessages.TIMEOUT_DETECTED;
import static javache.constants.WebConstants.SOCKET_TIMEOUT_MILLISECONDS;

public class Server {

    private int timeouts;
    private ServerSocket server;
    private int port;

    public Server(int port) {
        this.timeouts = 0;
        this.port = port;
    }

    public void run() throws IOException {

        this.server = new ServerSocket(this.port);
        System.out.println(LISTENING + this.port);
        this.server.setSoTimeout(SOCKET_TIMEOUT_MILLISECONDS);

        while (true) {
            try(Socket clientSocket = this.server.accept()) {
                clientSocket.setSoTimeout(SOCKET_TIMEOUT_MILLISECONDS);

                ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket, new RequestHandler());

                FutureTask<?> task = new FutureTask<>(connectionHandler, null);
                task.run();

            } catch (SocketTimeoutException e) {
                System.out.println(TIMEOUT_DETECTED);
                ++this.timeouts;
            }
        }
    }
}
