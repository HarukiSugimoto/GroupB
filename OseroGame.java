import java.io.*;
import java.net.*;
import java.util.Arrays;

public class OseroGame{
    ServerThread interchange;//この値を交互で変更していくことでデータのやり取りをする
    int board[][] = {
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,1,2,0,0,0},
        {0,0,0,2,1,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
    };

    public synchronized void FirstAttack(ServerThread thread){ //先攻
        System.out.printf("%s(先攻)者参加", thread.username);
        System.out.println("");
        send(thread);
        try{
            wait();
        } catch(InterruptedException e){
            System.out.println("ERROR in FirstAttack");
        }
        ServerThread partner_thread = get();
        String partner_name = partner_thread.username;
        thread.out.println(partner_name);
        thread.out.println("先攻");
        while(true){
            thread.out.println("STOP");
            for (int i = 0; i < board.length; i++) {
                for(int k = 0; k < board[0].length; k++){
                    thread.out.println(board[i][k]);
                }
            }
            try{
                int col = Integer.parseInt(thread.in.readLine());
                int row = Integer.parseInt(thread.in.readLine());
                board[row][col] = 1;//黒
            }catch(IOException e){
                
            }
            for (int i = 0; i < board.length; i++) {
                for(int k = 0; k < board[0].length; k++){
                    thread.out.println(board[i][k]);
                }
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notify();
            try{
                wait();
            } catch(InterruptedException e){
                System.out.println("ERROR in FirstAttack");
            }
        }
    }

    public synchronized void SecondAttack(ServerThread thread) throws IOException{ //後攻
        System.out.printf("%s(後攻)者参加", thread.username);
        System.out.println("");
        ServerThread partner_thread = get();
        String partner_name = partner_thread.username;
        thread.out.println(partner_name);
        thread.out.println("後攻");
        send(thread);
        notify();
        while(true){
            try{
                wait();
            } catch(InterruptedException e){
                System.out.println("ERROR in SecondAttack");
            }
            thread.out.println("STOP");
            for (int i = board.length-1; i >= 0; i--) {
                for(int k = board.length-1; k >= 0; k--){
                    thread.out.println(board[i][k]);
                }
            }
            try{
                int col = Integer.parseInt(thread.in.readLine());
                int row = Integer.parseInt(thread.in.readLine());
                board[Math.abs(7-row)][Math.abs(7-col)] = 2;//しろ
            }catch(IOException e){
                
            }
            for (int i = board.length-1; i >= 0; i--) {
                for(int k = board.length-1; k >= 0; k--){
                    thread.out.println(board[i][k]);
                }
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notify();
        }
    }

    public synchronized void send(ServerThread server){
        interchange = server;
    }

    public synchronized ServerThread get(){
        return interchange;
    }

}