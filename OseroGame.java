import java.io.*;
import java.net.*;

public class OseroGame{
    String interchange = ""; //この値を交互で変更していくことでデータのやり取りをする
    public synchronized void FirstAttack(ServerThread thread){ //先攻
        System.out.printf("%s(先攻)者参加", thread.username);
        System.out.println("");
        try{
            wait();
        } catch(InterruptedException e){
            System.out.println("ERROR in FirstAttack");
        }
        notify();
        while(true){
            send(thread.username);
            try{
                wait();
            } catch(InterruptedException e){
                System.out.println("ERROR in FirstAttack");
            }
            String partner_name = get();
            thread.out.println(partner_name);
            System.out.println("first");
            notify();
            try{
                Thread.sleep(5000);
            } catch(InterruptedException e){
                System.out.println("ERROR in FirstAttack Sleep");
            }
        }
    }

    public synchronized void SecondAttack(ServerThread thread){ //後攻
        System.out.printf("%s(後攻)者参加", thread.username);
        System.out.println("");
        notify();
        try{
            wait();
        } catch(InterruptedException e){
            System.out.println("ERROR in SecondAttack");
        }
        while(true){
            String partner_name = get();
            thread.out.println(partner_name);
            send(thread.username);
            System.out.println("second");
            notify();
            try{
                Thread.sleep(5000);
            } catch(InterruptedException e){
                System.out.println("ERROR in SeconfAttack Sleep");
            }
            try{
                wait();
            } catch(InterruptedException e){
                System.out.println("ERROR in SecondAttack");
            }
        }
    }

    public synchronized void send(String text){
        interchange = text;
    }

    public synchronized String get(){
        return interchange;
    }
}