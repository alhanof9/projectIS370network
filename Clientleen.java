import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Clientleen {
    private Socket socketObject;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String userName;

    public Clientleen(Socket socket, String userName) {
        try {
            this.socketObject = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;

            buffWriter.write(userName);
            buffWriter.newLine();
            buffWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, buffReader, buffWriter);
        }
    }

    public void sendMessage() {
        Scanner scanner = new Scanner(System.in);
        while (socketObject.isConnected()) {
            String messageToSend = scanner.nextLine();
            try {
                buffWriter.write(userName + ": " + messageToSend);
                buffWriter.newLine();
                buffWriter.flush();

                if (messageToSend.equalsIgnoreCase("Bye")) {
                    System.out.println("Good Bye");
                    closeEverything(socketObject, buffReader, buffWriter);
                    break;
                }
            } catch (IOException e) {
                closeEverything(socketObject, buffReader, buffWriter);
                break;
            }
        }
        scanner.close();
    }

    public void listenToMessages() {
        new Thread(() -> {
            String messageFromGroup;
            while (socketObject.isConnected()) {
                try {
                    messageFromGroup = buffReader.readLine();
                    if (messageFromGroup != null) {
                        System.out.println(messageFromGroup);
                    }
                } catch (IOException e) {
                    closeEverything(socketObject, buffReader, buffWriter);
                    break;
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your beautiful username: ");
        String userName = scanner.nextLine();

        try {
            Socket socket = new Socket("localhost", 8888);


            Clientleen client = new Clientleen(socket, userName);
            client.listenToMessages();
            client.sendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
