import java.io.*;
import java.net.*;

public class Server {
    public static final int PORT = 8080;
    public static void main(String args[]) throws IOException{
        System.out.println("サーバー起動");
        ServerSocket s = new ServerSocket(PORT);
        try{
            while(true){
                Socket socket = s.accept();
                new ServerThread(socket).start();
            }
        } finally{
            s.close();
        }

    }
    
}
