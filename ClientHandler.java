import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{ // runnable allow each client connection to run independently in a separate thread.  {
    public static ArrayList<ClientHandler> clientsList = new ArrayList<>(); //keep track of all clients so any massage come we can loop over it to send it
    private Socket socketObject;//passed from server class
    private BufferedReader buffReader; //read massages from clients
    private BufferedWriter buffWriter;//write massages to clients
    private String userName;

    public ClientHandler(Socket socket) {
        try {
            socketObject = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = buffReader.readLine(); // Read username
            clientsList.add(this);
            broadcastMessage("SERVER:"+ userName +"has enterd the chat" );
                   } catch (IOException e) {
            closeEverything(socket, buffReader, buffWriter);
        }
    }

    @Override
    public void run(){ 
        String messageFromClient;
        while (socketObject.isConnected()) {
            try {
                messageFromClient = buffReader.readLine();//becouse it is a blockng massage it must be in sperate thread
                if (messageFromClient == null) {
                    break;
                }
                if (messageFromClient.equalsIgnoreCase("Bye")) {
                clientsList.remove(this);
                    broadcastMessage("Server: " + userName + " has left the chat.");
                    break;
                }
                broadcastMessage(userName + ": " + messageFromClient);
            } catch (IOException e) {
             closeEverything(socketObject, buffReader, buffWriter);

                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
            for (ClientHandler client : clientsList) {
                 try {
                if (!client.userName.equals(userName)) { // Avoid sending the message back to the sender
                    client.buffWriter.write(messageToSend);
                    client.buffWriter.newLine();
                    client.buffWriter.flush();//BUFFER WILL NOT SEND UNTIL IT IS FULL , SO WE DO THAT TO FORCE IT TO SEND
                }
            } catch (IOException e) {
                closeEverything(client.socketObject, client.buffReader, client.buffWriter);
            }
        }
    }
    
    
    
    public void closeEverything(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
       
        clientsList.remove(this);
        broadcastMessage("Server: " + userName + " has left the chat.");

        try {
            if (buffReader != null) buffReader.close();
            if (buffWriter != null) buffWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
