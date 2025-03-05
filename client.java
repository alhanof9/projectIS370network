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
<<<<<<< HEAD
=======

>>>>>>> 97258a0dc883a4050ec1413856fb4d98c6340fa7
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
                input="";
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("bye")) {
                    fromClient.write("");
                    fromClient.newLine();
                    fromClient.flush();                
                    break;
                }
                fromClient.write(input);
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
