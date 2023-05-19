import java.util.Scanner;

public class OseroMethods {

    int board_size = 8; //８×８の盤面
    int[][] board;

    OseroMethods(int[][] board){
        this.board = board;
    }

    public int checkStone(int player){//playerが置ける場所が存在するかチェックし、あれば１、なければ０(bool型にしても良き)
        int playerStone = 0, enemyStone = 0;
        int count = 0;
        if(player == 1){
            playerStone = 1;
            enemyStone = 2;
        } else if(player == 2){
            playerStone = 2;
            enemyStone = 1;
        }
        for(int i = 0; i < board_size; i++){
            for(int j = 0; j < board_size; j++){
                if(board[i][j] == enemyStone){
                    continue;
                } else if(board[i][j] == playerStone){
                    for(int k = i-1; k > 0; k--){//上方向
                        if(board[k][j] == enemyStone){
                            continue;
                        } else if(board[k][j] == 0){
                            count++;
                            break;
                        }
                        if(count>0){return 1;}
                        break;
                    }
                    for(int k = i-1, l = j+1; k > 0 && l < board_size; k--, l++){//右上方向
                        if(board[k][l] == enemyStone){
                            continue;
                        } else if(board[k][l] == 0){
                            count++;
                            break;
                        }
                        if(count>0){return 1;}
                        break;
                    }
                    for(int k = j+1; k < board_size; k++){//右方向
                        if(board[i][k] == enemyStone){
                            continue;
                        } else if(board[i][k] == 0){
                            count++;
                            break;
                        }
                        if(count>0){return 1;}
                        break;
                    }
                    for(int k = i+1, l = j+1; k < board_size && l < board_size; k++, l++){//右下方向
                        if(board[k][l] == enemyStone){
                            continue;
                        } else if(board[k][l] == 0){
                            count++;
                            break;
                        }
                        if(count>0){return 1;}
                        break;
                    }
                    for(int k = i+1; k < board_size; k++){//下方向
                        if(board[k][j] == enemyStone){
                            continue;
                        } else if(board[k][j] == 0){
                            count++;
                            break;
                        }
                        if(count>0){return 1;}
                        break;
                    }
                    for(int k = i+1, l = j-1; k < board_size && l > 0; k++, l--){//左下方向
                        if(board[k][l] == enemyStone){
                            continue;
                        } else if(board[k][l] == 0){
                            count++;
                            break;
                        }
                        if(count>0){return 1;}
                        break;
                    }
                    for(int k = j-1; k > 0; k--){//左方向
                        if(board[i][k] == enemyStone){
                            continue;
                        } else if(board[i][k] == 0){
                            count++;
                            break;
                        }
                        if(count>0){return 1;}
                        break;
                    }
                    for(int k = i-1, l = j-1; k > 0 && l > 0; k--, l--){//左上方向
                        if(board[k][l] == enemyStone){
                            continue;
                        } else if(board[k][l] == 0){
                            count++;
                            break;
                        }
                        if(count>0){return 1;}
                        break;
                    }
                }
            }
        }
        return 0;//おける場所がない
    }
    public int scoreStone(){//駒の数を数える
        int black = 1, white = 2;
        for(int i = 0; i < board_size; i++){
            for(int j = 0; j < board_size; j++){
                if(board[i][j] == 1){
                    black++;
                } else if(board[i][j] == 2){
                    white++;
                }
            }
        }
        System.out.println("黒 = " + black + " 白 = " + white);//デバッグ用
        if(black > white){
            return 1;//黒の勝ち
        } else if(black < white){
            return 2;//白の勝ち
        } else{
            return 0;//引き分け
        }
    }

    public int setStone(int player, int x, int y){ //駒を置く
        // if((board[x][y].equals(black))||(board[x][y].equals(white))){//すでにコマがある場合
        //     return -1;//置けなかったことを表す
        // }
        if(player == 1){
            board[x][y] = 1; //黒
        } else if(player == 2){
            board[x][y] = 2; //白
        }
        // } else{
        //     System.out.println("プレイヤーの指定が間違っています");
        //     return -1;
        // }
        int count = reverseStone(player, x, y);
        if(count < 1){
            board[x][y] = 0;
            return -1;
        }
        return 0;
    }
    public int reverseStone(int player, int x, int y){ //駒をひっくり返す
        int count = 0;
        int playerStone = 0, enemyStone = 0;

        if(player == 1){
            playerStone = 1;
            enemyStone = 2;
        } else if(player == 2){
            playerStone = 2;
            enemyStone = 1;
        }
        for(int i = x-1; i > 0; i--){//上方向の駒をひっくり返す
            if(board[i][y] == enemyStone){
                continue;
            }else if(board[i][y] ==playerStone){
                for(i+=1; i<x; i++){
                    board[i][y] = playerStone;
                    count++;
                }
            }
            break;
        }
        for(int i = x-1, j = y+1; i > 0 && j < board_size; i--, j++){//右上方向の駒をひっくり返す
            if(board[i][j] == enemyStone){
                continue;
            }else if(board[i][j] == playerStone){
                for(i+=1, j-=1; i<x && j>y; i++, j--){
                    board[i][j] = playerStone;
                    count++;
                }
            }
            break;
        }
        for(int i = y+1; i < board_size; i++){//右方向の駒をひっくり返す
            if(board[x][i] == enemyStone){
                continue;
            }else if(board[x][i] == playerStone){
                for(i-=1; i>y; i--){
                    board[x][i] = playerStone;
                    count++;
                }
            }
            break;
        }
        for(int i = x+1, j = y+1; i < board_size && j < board_size; i++, j++){//右下方向の駒をひっくり返す
            if(board[i][j] == enemyStone){
                continue;
            }else if(board[i][j] == playerStone){
                for(i-=1, j-=1; i>x && j>y; i--, j--){
                    board[i][j] = playerStone;
                    count++;
                }
            }
            break;
        }
        for(int i = x+1; i < board_size; i++){//下方向の駒をひっくり返す
            if(board[i][y] == enemyStone){
                continue;
            }else if(board[i][y] == playerStone){
                for(i-=1; i>x; i--){
                    board[i][y] = playerStone;
                    count++;
                }
            }
            break;
        }
        for(int i = x+1, j = y-1; i < board_size && j > 0; i++, j--){//左下方向の駒をひっくり返す

            if(board[i][j] == enemyStone){
                continue;
            }else if(board[i][j] == playerStone){
                for(i-=1, j+=1; i>x && j<y; i--, j++){
                    board[i][j] = playerStone;
                    count++;
                }
            }
            break;
        }
        for(int i = y-1; i > 0; i--){//左方向の駒をひっくり返す
            if(board[x][i] == enemyStone){
                continue;
            }else if(board[x][i] == playerStone){
                for(i+=1; i<y; i++){
                    board[x][i] = playerStone;
                    count++;
                }
            }
            break;
        }
        for(int i = x-1, j = y-1; i > 0 && j > 0; i--, j--){//左上方向の駒をひっくり返す

            if(board[i][j] == enemyStone){
                continue;
            }else if(board[i][j] == playerStone){
                for(i+=1, j+=1; i<x && j<y; i++, j++){
                    board[i][j] = playerStone;
                    count++;
                }
            }
            break;
        }
        return count;
    }

    public int PlayOsero(int player, int x, int y){
        // while(true){
        //     osero.setStone(1, x, y);
        //     osero.setStone(2, x, y);
        // }
        int check = setStone(player, x, y);
        System.out.println(checkStone(player%2+1));//デバッグ用
        System.out.println(scoreStone());//デバッグ用
        return check;
    }
     public int[][] get_Board(){
        return board;
     }
}
