import java.net.*;
import java.io.*;

public class client {
public static void main(String args[]) throws IOException {

    // Open your connection to a server, at port 1254
Socket client = new Socket("localhost",1254);

    // Get an input file handle from the socket and read the input

DataInputStream is = new DataInputStream(client.getInputStream());
String st = new String (is.readUTF());
System.out.println(st);
// When done, just close the connection and exit
is.close();
client.close();
}
}