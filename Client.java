import java.io.*;
import java.net.*;
public class Client {
    InetAddress addr;
    int PORT;
    Socket socket;
    BufferedReader in;
    PrintWriter out;

    Client() throws IOException{
        this.addr = InetAddress.getByName("localhost");
        this.PORT = 8080;
        this.socket = new Socket(addr, PORT);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    public static void main(String args[]) throws IOException{
        // Client client = new Client();
        // new OseroGui(client);
        new OseroGui(new Client());
    }
}