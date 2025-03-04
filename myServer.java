import java.io.*;
import java.net.*;
import java.util.*;

public class myServer {
    public static void main(String[] args)throws IOException {
        Scanner inFromServer = new Scanner(System.in);
            System.out.println("What is your name ?");
                String name = inFromServer.nextLine().trim();
                while (name.isEmpty()) {
                    System.out.print("Name cannot be empty, Enter your name: ");
                    name = inFromServer.nextLine().trim();
                }
                System.out.println("Hi " + name + " you can start chatting with friends ... Type bye to exit");

        int PORT = 8081;
        System.out.println("Server started on port " + PORT + "...");
            System.out.println("Establishin connection. please wait ...");
        
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket socket =  serverSocket.accept();

            Scanner fromClient=new Scanner(socket.getInputStream());
            BufferedWriter fromServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //Scanner inFromServer = new Scanner(System.in);
            
    
            //System.out.println("Server started on port " + PORT + "...");
            //System.out.println("Establishin connection. please wait ...");

            String input, output;
            while (true) {
                input = fromClient.nextLine();
                System.out.println(input);
                fromServer.write(input);
                fromServer.newLine(); 
                fromServer.flush();
                
                if(input.equalsIgnoreCase("bye")){
                    break;
                }
                
        
                output = inFromServer.nextLine();
                System.out.println(name+" : "+output);
                fromServer.write(name+" : "+output);
                fromServer.newLine(); 
                fromServer.flush();
                
            }
            serverSocket.close();
        }
    }
 