
import java.io.*;
import java.net.*;
import java.util.*;

public class myServer {
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
 static String name;
    public static void main(String[] args) {
        final int PORT = 8081;
        System.out.println("Establishin connection. please wait ...");
                
        try (ServerSocket serverSocket = new ServerSocket(PORT))
            {
            System.out.println("connected");

            Scanner scanner = new Scanner(System.in);        
            System.out.println("What is your name ?");
                 name = scanner.nextLine().trim();
                while (name.isEmpty()) {
                    System.out.print("Name cannot be empty, Enter your name: ");
                    name = scanner.nextLine().trim();
                }
                System.out.println("Hi " + name + " you can start chatting with friends ... Type bye to exit");
                System.out.println(name+" has joined the chat.");

            
            Thread serverSender = new Thread(() -> {
                while (true) {
                    String serverMessage = scanner.nextLine();
                    if (serverMessage.equalsIgnoreCase("bye")) {
                        System.out.println("good bye");///**
                        System.exit(0);
                    }
                    broadcast(name+": " + serverMessage);
                }
            });
            serverSender.start();
            
            while (true) {
                Socket clientSocket = serverSocket.accept();                 
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static synchronized void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                try {
                    client.toClient.write(message);
                    client.toClient.newLine();
                    client.toClient.flush();
                } catch (IOException e) {
                    System.out.println("Broadcast error: " + e.getMessage());
                }
            }
              System.out.println(message);
 
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader fromClient;
        private BufferedWriter toClient;
        private String clientName;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.toClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }

        public void run() {
            try {
                clientName = fromClient.readLine();
                broadcast(clientName + " has joined the chat.");

                String message;
                while ((message = fromClient.readLine()) != null) {
                    if (message.equalsIgnoreCase("bye")) {
                        break;
                    }
                    broadcast(clientName + " : " + message);
                }
            } catch (IOException e) {
                System.out.println("Error with client " + clientName + ": " + e.getMessage());
            } finally {
                try {
                    clients.remove(this);
                    clientSocket.close();
                    broadcast(clientName + " has left the chat.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}