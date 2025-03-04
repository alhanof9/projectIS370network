import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    //private static Set<ClientHandler> clientHandlers = new HashSet<>();
    public static Scanner input=new Scanner(System.in);
    public static void main(String[] args) {
        int PORT = 1254;
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            //System.out.println("Server started on port " + PORT + "...");
            System.out.println("Establishin connection. please wait ...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected");
               // System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                Client clientHandler = new ClientHandler(clientSocket);
                //clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
            
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
    
    // Method to broadcast messages to all clients
    static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(message);
            }
        }
    }

    // Method to remove client from active list

    static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }
}


// ClientHandler class to handle multiple clients
/* 
class ClientHandler implements Runnable {

    private String clientName;
    private Socket socket;
    private DataInputStream inS;
    private DataOutputStream outS;

    public ClientHandler(Socket s) {
        socket = s;
        try {
            inS = new DataInputStream(socket.getInputStream());
            outS = new DataOutputStream(socket.getOutputStream());
            output.writeUTF("What is your name?");
            //System.out.println("What is your name?");
            this.clientName = inS.readUTF();
            System.out.println("Hi "+clientName+" you can start chatting with friends ... type bye to exit");
            Server.broadcast(":"+clientName + ": has joined the chat", this);

        } catch (IOException e) {
            System.out.println("Error initializing client: " + e.getMessage());
        }
    }

    @Override

    public void run() {
        try {
            String message;
            while (true) {
                message = inS.readUTF();
                if (message.equalsIgnoreCase("bye")) {
                    break;
                }
                System.out.println(clientName + ": " + message);
                Server.broadcast(clientName + ": " + message, this);
            }
            
        } catch (IOException e) {
            System.out.println(clientName + " has disconnected.");
        } finally {
            try {
                Server.broadcast(clientName + " has left the chat.", this);
                Server.removeClient(this);
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing connection for " + clientName);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            outS.writeUTF(message);
            
        } catch (IOException e) {
            System.out.println("Error sending message to " + clientName);
        }
    } */

