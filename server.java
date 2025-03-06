import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        System.out.println("Server is running...");

        while (!serverSocket.isClosed()) {
            try {
                System.out.println("Waiting for clients to connect...");
                Socket socketObject = serverSocket.accept(); //wait client connect . socket object is returned to help communicate
                System.out.println("Yay! a client has connected.");

                ClientHandler clientHandler = new ClientHandler(socketObject);// will handel the new client
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
                System.out.println("Good Bye");
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            Server server = new Server(serverSocket);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
