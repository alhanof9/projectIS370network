import java.io.*;
import java.net.*;
import java.util.*;

public class myServer {
    public static void main(String[] args)throws IOException {
        int PORT = 8081;
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket socket =  serverSocket.accept();

            Scanner fromClient=new Scanner(socket.getInputStream());
            BufferedWriter fromServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner inFromServer = new Scanner(System.in);

            System.out.println("Server started on port " + PORT + "...");
            System.out.println("Establishin connection. please wait ...");

            String input, output;
            while (true) {
                input = fromClient.nextLine();
                System.out.println(input);
                
                if(input.equalsIgnoreCase("bye")){
                    break;
                }
                
        
                output = inFromServer.nextLine();
    
                fromServer.write(output);
                fromServer.newLine(); 
                fromServer.flush();
                
            }
            serverSocket.close();
        }
    }
 