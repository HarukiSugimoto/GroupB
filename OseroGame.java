import java.io.*;
import java.net.*;
import java.util.Arrays;

public class OseroGame{
    ServerThread interchange;//この値を交互で変更していくことでデータのやり取りをする
    // int board[][] = {
    //     {0,0,0,0,0,0,0,0},
    //     {0,0,0,0,0,0,0,0},
    //     {0,0,0,0,0,0,0,0},
    //     {0,0,0,1,2,0,0,0},
    //     {0,0,0,2,1,0,0,0},
    //     {0,0,0,0,0,0,0,0},
    //     {0,0,0,0,0,0,0,0},
    //     {0,0,0,0,0,0,0,0},
    // };
    int board[][] = {
        {0,2,1,2,1,2,1,1},
        {2,1,1,2,2,2,2,2},
        {2,2,1,2,2,1,2,1},
        {2,2,1,1,2,1,2,1},
        {2,2,1,2,1,1,2,1},
        {2,1,1,2,2,1,2,2},
        {2,2,2,2,2,1,0,0},
        {2,2,2,2,2,1,0,0},
    };
    OseroMethods osero = new OseroMethods(board);
    int flag = 0;

    public synchronized void FirstAttack(ServerThread thread){ //先攻
        int player = 1;
        send(thread);
        try{
            wait();
        } catch(InterruptedException e){
            System.out.println("ERROR in FirstAttack");
        }
        ServerThread partner_thread = get();
        thread.out.println(partner_thread.username); //対戦相手の名前を送信
        //対戦相手の戦績を送信
        thread.out.println(partner_thread.win); 
        thread.out.println(partner_thread.lose); 

        thread.out.println("先攻");
        while(true){
            int check = check(player);
            if(check == -1){
                change_turn();
            } 
            if(check == 1){
                notify();
                break;
            }
            if(check == 0){
                thread.out.println("START");
                send_board(player, thread);
                try{
                    int col = Integer.parseInt(thread.in.readLine());
                    int row = Integer.parseInt(thread.in.readLine());
                    // board[row][col] = 1;//黒
                    flag = osero.PlayOsero(player, row, col);
                    board = osero.get_Board();
                }catch(IOException e){
                    
                }
                send_board(player, thread);
                if(flag == -1) continue;
                change_turn();
            }
        }
        finish_game(player, thread);
    }

    public synchronized void SecondAttack(ServerThread thread) throws IOException{ //後攻
        int player = 2;
        ServerThread partner_thread = get();
        thread.out.println(partner_thread.username); //対戦相手の名前を送信
        //対戦相手の戦績を送信
        thread.out.println(partner_thread.win); 
        thread.out.println(partner_thread.lose);

        thread.out.println("後攻");
        send(thread);
        change_turn();
        while(true){
            int check = check(player);
            if(check == -1) {
                change_turn();
            }
            if(check == 1){
                notify();
                break;
            }
            if(check == 0){
                thread.out.println("START");
                send_board(player, thread);
                try{
                    int col = Integer.parseInt(thread.in.readLine());
                    int row = Integer.parseInt(thread.in.readLine());
                    flag = osero.PlayOsero(player, Math.abs(7-row), Math.abs(7-col)); //押した場所にコマを置けるかどうか
                    board = osero.get_Board();
                }catch(IOException e){
                    
                }
                send_board(player, thread);
                if(flag == -1) continue;
                change_turn();
            }
        }
        finish_game(player, thread);

    }

    public synchronized void send(ServerThread server){
        interchange = server;
    }
    public synchronized ServerThread get(){
        return interchange;
    }
    public void change_turn(){
        notify();
        try{
            wait();
        } catch(InterruptedException e){
            System.out.println("ERROR in ChangeTurn");
        }
    }
    public void send_board(int player, ServerThread thread){
        if(player == 1){
            for (int i = 0; i < board.length; i++) {
                for(int k = 0; k < board[0].length; k++){
                    thread.out.println(board[i][k]);
                }
            }
        }else{
            for (int i = board.length-1; i >= 0; i--) {
                for(int k = board.length-1; k >= 0; k--){
                    thread.out.println(board[i][k]);
                }
            }
        }
    }

    int pass_flag = 0;
    public int check(int player){
        // System.out.printf("Player %d 's checkStone = %d\n", player, osero.checkStone(player));
        if(pass_flag == 2) return 1;
        if(osero.checkStone(player)==0){
            pass_flag++;
            if(pass_flag == 2) return 1;
            //おけない
            return -1;
        }else{
            pass_flag = 0;
            //おける
            return 0;
        }

    }
    public void finish_game(int player, ServerThread thread){
        int score = osero.scoreStone();
        thread.out.println("FINISH");
        send_board(player, thread);
        if(score == 0){
            //引き分け
            thread.out.println("DRAW");
        }else if(score == player){
            thread.out.println("WIN");
        }else{
            thread.out.println("LOSE");
        }
    }
}