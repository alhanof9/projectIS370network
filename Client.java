import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socketObject;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String userName;

    public Client(Socket socket, String userName) {
        try {
            socketObject = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        } catch (IOException e) {
            closeEverything(socket, buffReader, buffWriter);
        }
    }

    public void sendMessage() {//send massages to client handler
       
       try {

       buffWriter.write(userName);
       buffWriter.newLine();
       buffWriter.flush();
        
        Scanner scanner = new Scanner(System.in);
        
        while (socketObject.isConnected()) {
            String messageToSend = scanner.nextLine();
               buffWriter.write(userName + ": " + messageToSendS);
                buffWriter.newLine();
                buffWriter.flush();
            }
                } catch (IOException e) {
                closeEverything(socketObject, buffReader, buffWriter);
                
            }
        }


    public void listenToMessages() {
        new Thread(new Runnable(){
        
        public void run(){
        String messageFromGroup;
        
            while (socketObject.isConnected()) {
                try {
                    messageFromGroup = buffReader.readLine();
                        System.out.println(messageFromGroup);
        
                } catch (IOException e) {
                    closeEverything(socketObject, buffReader, buffWriter);
       }
       }
       }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
        try {
            if (buffReader != null) buffReader.close();
            if (buffWriter != null) buffWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your beautiful name: ");
        String userName = scanner.nextLine();
      
        Socket socket = new Socket("localhost", 8888);


            Client client = new Client(socket, userName);
            client.listenToMessages();
            client.sendMessage();
       }
       
       }
