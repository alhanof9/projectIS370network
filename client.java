import java.io.*;
import java.net.*;
import java.util.*;

public class client {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 8081);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter fromClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("What is your name ?");
            String name = scanner.nextLine().trim();
            while (name.isEmpty()) {
                System.out.print("Name cannot be empty, Enter your name: ");
                name = scanner.nextLine().trim();
            }
            
            System.out.println("Hi " + name + " you can start chatting with friends ... Type bye to exit");
           fromClient.write(":"+name);

            fromClient.newLine();
            fromClient.flush(); 
            
            new Thread(() -> {
                try {
                    String message;
                    while ((message = fromServer.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println("Good Bye.");
                }
            }).start();
            
            String input;
            while (true) {
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("bye")) {
                    fromClient.write(name+" has left the chat");
                    fromClient.newLine();
                    fromClient.flush();                
                    break;
                }
                fromClient.write(name+" :"+input);
                fromClient.newLine();
                fromClient.flush();
                
                
            }
            
            fromServer.close();
            fromClient.close();
            client.close();
           // System.out.println("Good Bye.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
