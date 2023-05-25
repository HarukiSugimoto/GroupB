import java.io.*;
import java.net.*;
import java.util.jar.Attributes.Name;

public class ServerThread extends Thread {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String username = "";
    String[] record;
    int win;
    int lose;

    static int num_thread = 0; //全スレッド共通の値にしたいのでstatic
    static OseroGame room;

    ServerThread(Socket socket) throws IOException{
        System.out.println("スレッドが立ち上がりました");
        this.socket = socket;

    }
    public void get_inout() throws IOException{
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        num_thread++;
    }

    // public synchronized void login(String name, String pass) throws FileNotFoundException, IOException{
    //     int flag = 1;
    //     try (BufferedReader usersdata = new BufferedReader(new InputStreamReader(new FileInputStream("./userdata.csv")))) {
    //         String userdata;
    //         while((userdata = usersdata.readLine()) != null){
    //             String[] user = userdata.split(",", 0);
    //             if(user[0].equals(name)){
    //                 if(user[1].equals(pass)){
    //                     flag = 0;
    //                     this.out.println("SUCCESS");
    //                     record = user[2].split(":");
    //                     win = Integer.parseInt(record[0]); //勝った回数
    //                     lose = Integer.parseInt(record[1]); //負けた回数
    //                     username = name;
    //                     usersdata.close();
    //                 }else{
    //                     System.out.println("pass違う");
    //                     this.out.println("ERROR in login pass");
    //                     usersdata.close();
    //                 }
                    
    //             }
    //         }
    //         if (flag == 1){
    //             System.out.println("登録無し");
    //             this.out.println("ERROR in login name");
    //             usersdata.close();
    //         }
    //     }
    // } 
    public synchronized void login(String name, String pass) {
        int flag = 1;
        try (BufferedReader usersdata = new BufferedReader(new InputStreamReader(new FileInputStream("./userdata.csv")))) {
            String userdata;
            while ((userdata = usersdata.readLine()) != null) {
                String[] user = userdata.split(",", 0);
                if (user[0].equals(name)) {
                    if (user[1].equals(pass)) {
                        flag = 0;
                        this.out.println("SUCCESS");
                        record = user[2].split(":");
                        win = Integer.parseInt(record[0]); //勝った回数
                        lose = Integer.parseInt(record[1]); //負けた回数
                        username = name;
                        break; // ユーザーが見つかったのでループを終了
                    } else {
                        System.out.println("パスワードが一致しません");
                        this.out.println("ERROR in login pass");
                        usersdata.close();
                        return; // パスワードが一致しないため、メソッドを終了
                    }
                }
            }
            if (flag == 1) {
                System.out.println("ユーザーが見つかりません");
                this.out.println("ERROR in login name");
            }
            usersdata.close();
        } catch (FileNotFoundException e) {
            System.out.println("userdata.csv ファイルが見つかりません");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("入出力エラーが発生しました");
            e.printStackTrace();
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
                    this.out.println("ERROR in signup");
                    usersdata.close();
                }
            }
        }catch(IOException e){
            System.out.println("Error in signup");
        }
    }
    static int wait_player = 0; //0:待機あり，1：待機なし
    public void received(){ //ユーザーからの受け取り
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
                synchronized(this){
                    if(wait_player == 0){
                        wait_player = 1;
                        room = get_or_set_room(new OseroGame());
                        room.FirstAttack(this);
                    }else{
                        wait_player = 0;
                        room = get_or_set_room(null);
                        room.SecondAttack(this);
                    }

                }
            }
        }catch (IOException e){
            System.out.println("ERROR in recived");
            e.printStackTrace();
        }
    }
    public void run(){
        try{
            get_inout();
            while(true){
                received();
            }
        }catch(IOException e){
            System.out.println("Error in run");
        }
    }

    public synchronized OseroGame get_or_set_room(OseroGame r){
        if(r == null){
            return room;
        }else{
            room = r;
            return room;
        }
    }
}
