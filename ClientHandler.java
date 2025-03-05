import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientsList = new ArrayList<>();
    private Socket socketObject;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String userName;

    public ClientHandler(Socket socket) {
        try {
            this.socketObject = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            this.userName = buffReader.readLine(); // Read username
            if (this.userName == null || this.userName.trim().isEmpty()) {
                buffWriter.write("A name must be entered.\n");
                buffWriter.flush();
                socket.close();
                return;
            }

            clientsList.add(this);
            broadcastMessage("Server: " + userName + " has entered the chat.");

        } catch (IOException e) {
            closeEverything(socket, buffReader, buffWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socketObject.isConnected()) {
            try {
                messageFromClient = buffReader.readLine();
                if (messageFromClient == null) {
                    break;
                }
                if (messageFromClient.equalsIgnoreCase("Bye")) {
                    broadcastMessage("Server: " + userName + " has left the chat.");
                    break;
                }
                broadcastMessage(userName + ": " + messageFromClient);
            } catch (IOException e) {
                break;
            }
        }
        closeEverything(socketObject, buffReader, buffWriter);
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler client : clientsList) {
            try {
                if (!client.userName.equals(userName)) { // Avoid sending the message back to the sender
                    client.buffWriter.write(messageToSend);
                    client.buffWriter.newLine();
                    client.buffWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(client.socketObject, client.buffReader, client.buffWriter);
            }
        }
    }

    public void removeClient() {
        clientsList.remove(this);
        broadcastMessage("Server: " + userName + " has left the chat.");
    }

    public void closeEverything(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
        removeClient();
        try {
            if (buffReader != null) buffReader.close();
            if (buffWriter != null) buffWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
