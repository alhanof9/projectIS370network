import java.net.*;
import java.io.*;

public class server {
public static void main(String args[]) throws IOException {

    // Register service on port 1254
ServerSocket server = new ServerSocket(1254);
System.out.println("Waiting at port 1254...");

    // Wait and accept a connection
Socket client=server.accept();

    // Print the client IP address
InetAddress address = client.getLocalAddress();
System.out.println("IP Addrsss is: "+ address.getHostAddress());

    // Get a communication stream associated with the socket
DataOutputStream out = new DataOutputStream (client.getOutputStream());
    
    // Send a string!
out.writeUTF("Message from the server: Hi there");

    // Close the connection, but not the server socket
out.close();}
}
