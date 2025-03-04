import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
    public static void main(String args[]) throws IOException {

        // Open your connection to a server, at port 1254
        try{
        Socket client = new Socket("localhost",1254);
        BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter fromClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        Scanner scanner = new Scanner(System.in);

        System.out.println("What is your name ?");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) { // if the user didn"t enter his name
            System.out.print("Name cannot be empty, Enter your name: ");
            name = scanner.nextLine().trim();
        }

        fromClient.write(name);
        fromClient.newLine();
        fromClient.flush(); 
        
        System.out.println("Hi"+ name + "you can start chating with friends ... Type bye to exit");
        System.out.println(":"+name+": Has joined the chat");

        String input, output;
        while (true) { 
            input = scanner.nextLine();
            fromClient.write(input);

            if(input.equalsIgnoreCase("bye")){
            System.out.println(name+" has left the chat");
            break;
            }
            output= fromServer.readLine();
            System.out.println(output);

        }

  
        fromServer.close();
        fromClient.close();
        client.close();
        System.out.println("Good Bye.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
