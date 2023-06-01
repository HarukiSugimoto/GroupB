import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String username = "";
    String[] record;
    int win;
    int lose;
    int draw;
    boolean flag = true; //スレッドのrun関数用
    static OseroGame room;

    ServerThread(Socket socket) throws IOException{
        System.out.println("スレッドが立ち上がりました(ID:"+this.getId()+")");
        this.socket = socket;

    }

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
			            draw = Integer.parseInt(record[2]); //*引き分けた回数
                        this.out.println(win);
                        this.out.println(lose);
                        this.out.println(draw);
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
        } catch (IOException e) {
            System.out.println("入出力エラーが発生しました");
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
                    fw.println(String.format("%s,%s,0:0:0", name, pass)); //*
                    fw.close();
                    username = name;
                    this.out.println("SUCCESS");
                    this.out.println(win);
                    this.out.println(lose);
                    this.out.println(draw);
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
            } else if (command.equals("gamestart")){ 
                synchronized(this){
                    if(wait_player == 0){
                        wait_player = 1;
                        room = get_or_set_room(new OseroGame());
                        System.out.println(username + "がルームを作成(ObjectID: "+room.hashCode()+")");
                        room.FirstAttack(this);
                    }else{
                        wait_player = 0;
                        room = get_or_set_room(null);
                        System.out.println(username + "がルームに参加(ObjectID: "+room.hashCode()+")");
                        room.SecondAttack(this);
                    }
                    
                }
                if(!(room == null)){
                    System.out.println("ルームを削除(ObjectID: "+room.hashCode()+")");
                    room = null;
                }
	        } else if (command.equals("finishrecord")){ 
		        gamerecord(username); 
            } else if (command.equals("finish")){
                socket.close();
                flag = false;
                System.out.println("スレッドが終了しました(ID:"+this.getId()+")");
            }
        }catch (IOException e){
            System.out.println("ERROR in received");
        }
    }
    public void run(){
        try{
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            while(flag){
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
    
    public void gamerecord(String name){ //*戦績
        try{
            BufferedReader usersdata = new BufferedReader(new InputStreamReader(new FileInputStream("./userdata.csv")));
            String userdata;
            StringBuilder updatedData = new StringBuilder();
            
            while((userdata = usersdata.readLine()) != null) {
                String[] user = userdata.split(",", 0);
                
                if (user[0].equals(name)){ //usernameが一致する行を探す
                    String pass = user[1]; 
                    record = user[2].split(":");       
                    String result = this.in.readLine();

                    if(result.equals("winrecord")){
                        win++;
                        record[0] = String.valueOf(win);
                    } else if(result.equals("loserecord")){
                        lose++;
                        record[1] = String.valueOf(lose);
                    } else if(result.equals("drawrecord")){
                        draw++;
                        record[2] = String.valueOf(draw);
                    } else {
                        this.out.println("error in recording results.");
                    }
                    userdata = userdata.replace(userdata, String.format("%s,%s,%s:%s:%s", name, pass, record[0], record[1], record[2]));
                }
                updatedData.append(userdata).append("\n");
            }
            usersdata.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("./userdata.csv"));
            writer.write(updatedData.toString());
            writer.close();  
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
			
		      
