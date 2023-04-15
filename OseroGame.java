import java.io.*;
import java.net.*;

public class OseroGame{
    String interchange = ""; //この値を交互で変更していくことでデータのやり取りをする
    public synchronized void FirstAttack(String name){ //先攻
        System.out.printf("%s(先攻)者参加", name);
        System.out.println("");
        try{
            wait();
        } catch(InterruptedException e){
            System.out.println("ERROR");
        }
        System.out.println("先攻ゲーム開始");
        send(name);
        System.out.println("send関数実行");
        notify();
        try{
            wait();
        } catch(InterruptedException e){
            System.out.println("ERROR");
        }
    }

    public synchronized void SecondAttack(String name){ //後攻
        System.out.printf("%s(後攻)者参加", name);
        System.out.println("");
        notify();
        System.out.println("後攻ゲーム開始");
        try{
            wait();
        } catch(InterruptedException e){
            System.out.println("ERROR");
        }
        String na = get();
        System.out.printf("get関数によりna = %s", na);
        System.out.println("");
    }

    public void send(String text){
        interchange = text;
    }

    public String get(){
        return interchange;
    }
}