




import java.io.*;
import java.net.*;
import java.util.*;

public class myServer {
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
 static String name;
    public static void main(String[] args) {
        final int PORT = 8081;
        System.out.println("Establishin connection. please wait ...");
        System.out.println("connected");////**


        Scanner inFromServer = new Scanner(System.in);
        
            System.out.println("What is your name ?");
                 name = inFromServer.nextLine().trim();
                while (name.isEmpty()) {
                    System.out.print("Name cannot be empty, Enter your name: ");
                    name = inFromServer.nextLine().trim();
                }
                System.out.println("Hi " + name + " you can start chatting with friends ... Type bye to exit");
                System.out.println(name+" has joined the chat.");
                
        
        try (ServerSocket serverSocket = new ServerSocket(PORT);
        
             Scanner scanner = new Scanner(System.in)) {
            
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
                //System.out.println("connected");
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
                    System.out.println(message);
                    client.toClient.newLine();
                    client.toClient.flush();
                } catch (IOException e) {
                    System.out.println("Broadcast error: " + e.getMessage());
                }
            }
            
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


























// import java.io.*;
// import java.net.*;
// import java.util.*;

// public class myServer {
//     public static void main(String[] args)throws IOException {
//         Scanner inFromServer = new Scanner(System.in);
//             System.out.println("What is your name ?");
//                 String name = inFromServer.nextLine().trim();
//                 while (name.isEmpty()) {
//                     System.out.print("Name cannot be empty, Enter your name: ");
//                     name = inFromServer.nextLine().trim();
//                 }
//                 System.out.println("Hi " + name + " you can start chatting with friends ... Type bye to exit");

//         int PORT = 8081;
//         System.out.println("Server started on port " + PORT + "...");
//             System.out.println("Establishin connection. please wait ...");
        
//             ServerSocket serverSocket = new ServerSocket(PORT);
//             Socket socket =  serverSocket.accept();

//             Scanner fromClient=new Scanner(socket.getInputStream());// thread
//             BufferedWriter fromServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//             //Scanner inFromServer = new Scanner(System.in);
            
    
//             //System.out.println("Server started on port " + PORT + "...");
//             //System.out.println("Establishin connection. please wait ...");

//             String input, output;
//             while (true) {
//                 input = fromClient.nextLine();
//                 System.out.println(input);
//                 fromServer.write(input);
//                 fromServer.newLine(); 
//                 fromServer.flush();
                
//                 if(input.equalsIgnoreCase("bye")){
//                     break;
//                 }
                
        
//                 output = inFromServer.nextLine();
//                 System.out.println(name+" : "+output);
//                 fromServer.write(name+" : "+output);
//                 fromServer.newLine(); 
//                 fromServer.flush();
                
//             }
//             serverSocket.close();
//         }
//     }
 