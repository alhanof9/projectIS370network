import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serverleen {
    private ServerSocket serverSocket;

    public Serverleen(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        System.out.println("Server is running...");

        while (!serverSocket.isClosed()) {
            try {
                System.out.println("Waiting for clients to connect...");
                Socket socketObject = serverSocket.accept();
                System.out.println("Yay! A client has connected.");

                ClientHandler clientHandler = new ClientHandler(socketObject);
                Thread thread = new Thread(clientHandler);
                thread.start();

            } catch (IOException e) {
                closeServer();
            }
        }
    }

    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            Serverleen server = new Serverleen(serverSocket);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
