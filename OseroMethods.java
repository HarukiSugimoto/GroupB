public class OseroMethods {

    int board_size = 8; //８×８の盤面
    String[][] board = new String[board_size][board_size];

    public OseroMethods(){
        init();
    }

    public void init(){ //初期化,盤面の作成

        for(int i = 0; i < board_size; i++){
            for(int j = 0; j < board_size; j++){
                board[i][j] = "　";
            }
        }
        board[3][3] = "⚫︎";
        board[4][4] = "︎⚫︎";
        board[3][4] = "⚪︎";
        board[4][3] = "⚪︎";
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
            board[x][y] = "⚫︎";
        } else if(player == 2){
            board[x][y] = "⚪︎";
        } else{
            System.out.println("プレイヤーの指定が間違っています");
        }
        reverseStone(player, x, y);
    }
    public void reverseStone(int player, int x, int y){ //駒をひっくり返す
        String playerStone = "";
        String enemyStone = "";
        if(player == 1){
            playerStone = "⚫︎";
            enemyStone = "⚪︎";
        } else if(player == 2){
            playerStone = "⚪︎";
            enemyStone = "⚫︎";
        }
        reverseTop(playerStone, enemyStone, x, y);//左側のコマをひっくり返す
    }
    public void reverseTop(String playerStone, String enemyStone, int x, int y){ //左側のコマをひっくり返す
        for(int i = x-1; i > 0; i--){
            if(board[i][y]==enemyStone){
                continue;
            }else if(board[i][y]==playerStone){
                for(i+=1; i<x; i++){
                    board[i][y] = playerStone;
                }
                break;
            }
        }
    }

    public static void main(String[] args) {
        OseroMethods osero = new OseroMethods();
        osero.seeBoard();
        osero.setStone(1, 4, 2);
        osero.seeBoard();
        osero.setStone(2, 3, 2);
        osero.seeBoard();
        osero.setStone(1, 5, 3);
        osero.seeBoard();
        osero.setStone(2, 5, 4);
        osero.seeBoard();
    }

}
