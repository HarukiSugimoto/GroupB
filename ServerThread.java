import java.io.*;
import java.net.*;
import java.util.jar.Attributes.Name;

public class ServerThread extends Thread {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String username = "";
    static OseroGame room = new OseroGame();

    static int num_thread = 0; //全スレッド共通の値にしたいのでstatic

    ServerThread(Socket socket) throws IOException{
        System.out.println("スレッドが立ち上がりました");
        this.socket = socket;

    }
    public void get_inout() throws IOException{
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        num_thread++;
        System.out.printf("aaa:%d", num_thread);
        System.out.println("");
    }

    public synchronized void login(String name, String pass){
        int flag = 1;
        try{
            try (BufferedReader usersdata = new BufferedReader(new InputStreamReader(new FileInputStream("./userdata.csv")))) {
                String userdata;
                while((userdata = usersdata.readLine()) != null){
                    String[] user = userdata.split(",", 0);
                    if(user[0].equals(name)){
                        if(user[1].equals(pass)){
                            flag = 0;
                            this.out.println("SUCCESS");
                            username = name;
                            usersdata.close();
                        }else{
                            System.out.println("pass違う");
                            this.out.println("ERROR");
                            usersdata.close();
                        }
                        
                    }
                }
                if (flag == 1){
                    System.out.println("登録無し");
                    this.out.println("ERROR");
                    usersdata.close();
                }
            }
        }catch(IOException e){
            System.out.println("Error");
        }
    } 

    public synchronized void signup(String name, String pass){
        int flag = 0; //nameが存在すれば1，しなければ0
        try{
            try (BufferedReader usersdata = new BufferedReader(new InputStreamReader(new FileInputStream("./userdata.csv")))) {
                String userdata;
                while((userdata = usersdata.readLine()) != null){
                    String[] user = userdata.split(",", 0);
                    if(user[0].equals(name)){
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0){
                    PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter("./userdata.csv", true)));
                    fw.println(String.format("%s,%s,0:0", name, pass));
                    fw.close();
                    username = name;
                    this.out.println("SUCCESS");
                } else {
                    this.out.println("ERROR");
                    usersdata.close();
                }
            }
        }catch(IOException e){
            System.out.println("Error");
        }
    }
    static int wait_player = 0; //0:待機あり，1：待機なし
    public void recived(){ //ユーザーからの受け取り
        try{
            String command = this.in.readLine();
            if (command.equals("login")){
                String name = this.in.readLine();
                String pass = this.in.readLine();
                login(name, pass);
            } else if (command.equals("signup")){
                String name = this.in.readLine();
                String pass = this.in.readLine();
                signup(name, pass);
            } else if (command.equals("gamestart")){ //このブロックシンクロナイズ考えないとダメそう
                if(wait_player == 0){
                    wait_player = 1;
                    room.FirstAttack(username);
                }else{
                    wait_player = 0;
                    room.SecondAttack(username);
                }
            }
        }catch (IOException e){
            System.out.println("ERROR");
        }
    }
    public void run(){
        try{
            get_inout();
            while(true){
                recived();
            }
        }catch(IOException e){
            System.out.println("Error");
        }
    }
}
