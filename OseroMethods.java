import java.util.Scanner;

public class OseroMethods {

    int board_size = 8; //８×８の盤面
    String[][] board = new String[board_size][board_size];
    String black ="⚫︎";
    String white = "⚪︎"; 

    public OseroMethods(){
        init();
    }

    public void init(){ //初期化,盤面の作成

        for(int i = 0; i < board_size; i++){
            for(int j = 0; j < board_size; j++){
                board[i][j] = "　";
            }
        }
        board[3][3] = black;
        board[4][4] = black;
        board[3][4] = white;
        board[4][3] = white;
    }

    public void seeBoard(){ //ターミナル上で盤面を確認するため
        System.out.print("  ");
        for(int i = 0; i < board_size; i++){
            System.out.print(i + "  ");
        }
        System.out.println();
        for(int i = 0; i < board_size; i++){
            System.out.print(i);
            for(int j = 0; j < board_size; j++){
                System.out.print("|" + board[i][j]);
            }
        System.out.println("|");
        }
    }
    public void setStone(int player, int x, int y){ //駒を置く
        if(player == 1){
            board[x][y] = black;
        } else if(player == 2){
            board[x][y] = white;
        } else{
            System.out.println("プレイヤーの指定が間違っています");
        }
        reverseStone(player, x, y);
    }
    public void reverseStone(int player, int x, int y){ //駒をひっくり返す
        String playerStone = "", enemyStone= "";

        if(player == 1){
            playerStone = black;
            enemyStone = white;
        } else if(player == 2){
            playerStone = white;
            enemyStone = black;
        }
        
        for(int i = x+1; i < board_size; i++){//上方向の駒をひっくり返す
            if(board[i][y].equals(enemyStone)){
                continue;
            }else if(board[i][y].equals(playerStone)){
                for(i-=1; i>x; i--){
                    board[i][y] = playerStone;
                }
            }
            break;
        }

        for(int i = y+1; i < board_size; i++){//右方向の駒をひっくり返す
            if(board[x][i].equals(enemyStone)){
                continue;
            }else if(board[x][i].equals(playerStone)){
                for(i-=1; i>y; i--){
                    board[x][i] = playerStone;
                }
            }
            break;
        }

        for(int i = x-1; i > 0; i--){//下方向の駒をひっくり返す
            if(board[i][y].equals(enemyStone)){
                continue;
            }else if(board[i][y].equals(playerStone)){
                for(i+=1; i<x; i++){
                    board[i][y] = playerStone;
                }
            }
            break;
        }

        for(int i = y-1; i > 0; i--){//左方向の駒をひっくり返す
            if(board[x][i].equals(enemyStone)){
                continue;
            }else if(board[x][i].equals(playerStone)){
                for(i+=1; i<y; i++){
                    board[x][i] = playerStone;
                }
            }
            break;
        }
    }

    public void debMode(OseroMethods osero){
        osero.seeBoard();
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.print("駒をおくx座標を入力してください:");
            int x = s.nextInt();
            System.out.print("駒をおくy座標を入力してください:");
            int y = s.nextInt();
            osero.setStone(1, x, y);
            osero.seeBoard();

            System.out.print("駒をおくx座標を入力してください:");
            x = s.nextInt();
            System.out.print("駒をおくy座標を入力してください:");
            y = s.nextInt();
            osero.setStone(2, x, y);
            osero.seeBoard();
        }
    }


    public static void main(String[] args) {
        OseroMethods osero = new OseroMethods();
        
        osero.debMode(osero);
    }

}
