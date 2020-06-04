package backend;

import java.util.HashMap;


public class Core {
    private static int row = 15, col = 15;
    private HashMap<String, Integer> heuristic = new HashMap<String, Integer>();
    private int[] isMoved = new int[2]; // isMoved[0]: #black moves; isMoved[1]: #white moves
    private int[][][] latestMoves = new int[2][2][2];  // latestMoves[0][0]: latest black move; latestMoves[0][1]: second latest black move;
    private int[][] value = new int[row][col];
    public int[][] chessboard = new int[row][col];


    
    public static void main(String[] args) {
        
    }

    public boolean add(int x, int y, int player){   // player: black: 1, white: 2
        if (chessboard[x][y] == 0 ){
            chessboard[x][y] = player;
            latestMoves[player-1][1][0] = latestMoves[player-1][0][0];  // change latest to second latest
            latestMoves[player-1][1][1] = latestMoves[player-1][0][1];
            latestMoves[player-1][0][0] = x;
            latestMoves[player-1][0][1] = y;
            isMoved[player-1] = isMoved[player-1] + 1;
            if (player == 1) System.out.println("Black move at (" + x + ", " + y + ")");
            else System.out.println("White move at (" + x + ", " + y + ")");
            return true;
        }
        else {
            System.out.println("Invalid move.");
            return false;
        }
    }

    public boolean undo(int player, boolean singlePlayer){
        if (isMoved[player-1] > 0){
            int x = latestMoves[player-1][0][0], y = latestMoves[player-1][0][1];
            chessboard[x][y] = 0;
            isMoved[player-1] = isMoved[player-1] - 1;
            latestMoves[player-1][0][0] = latestMoves[player-1][1][0];  // change second latest to latest
            latestMoves[player-1][0][1] = latestMoves[player-1][1][1];
            latestMoves[player-1][1][0] = -1;
            latestMoves[player-1][1][1] = -1;
            if (player == 1) System.out.println("Black latest move at " + x + ", " + y + ") is undone.");
            else System.out.println("White latest move at " + x + ", " + y + ") is undone.");

            if (singlePlayer == true) {
                int computerPlayer = player == 1 ? 2 : 1;
                int computerX = latestMoves[computerPlayer-1][0][0], computerY = latestMoves[computerPlayer-1][0][1];
                chessboard[computerX][computerY] = 0;
                latestMoves[computerPlayer-1][0][0] = latestMoves[computerPlayer-1][1][0];  // change second latest to latest
                latestMoves[computerPlayer-1][0][1] = latestMoves[computerPlayer-1][1][1];
                latestMoves[computerPlayer-1][1][0] = -1;
                latestMoves[computerPlayer-1][1][1] = -1;
            }
            return true;
        }
        else {
            if (player == 1) System.out.println("Black has not moved yet.");
            else System.out.println("White has not moved yet.");
            return false;
        }
    }

    public boolean check(int x, int y, int player){  // check after player moves at (x,y)
        int count = 1;
        // horizontal
        for (int i = x - 1; i >= 0; i--) {
            if (chessboard[i][y] == player){
                count = count + 1; 
                if (count >= 5) return true;
            } 
            else break;
        }
        for (int i = x + 1; i < 15; i++) {
            if (chessboard[i][y] == player) {
                count = count + 1;
                if (count >= 5) return true;
            }
            else break;
        }

        // vertical
        count = 1;
        for (int i = y - 1; i >= 0; i--) {
            if (chessboard[x][i] == player){
                count = count + 1; 
                if (count >= 5) return true;
            } 
            else break;
        }
        for (int i = y + 1; i < 15; i++) {
            if (chessboard[x][i] == player) {
                count = count + 1;
                if (count >= 5) return true;
            }
            else break;
        }

        // "\"
        count = 1;
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            if (chessboard[i][j] == player){
                count = count + 1; 
                if (count >= 5) return true;
            } 
            else break;
        }
        for (int i = x + 1, j = y + 1; i < 15 && j < 15; i++, j++) {
            if (chessboard[i][j] == player) {
                count = count + 1;
                if (count >= 5) return true;
            }
            else break;
        }

        // "/"
        count = 1;
        for (int i = x - 1, j = y + 1; i >= 0 && j < 15; i--, j++) {
            if (chessboard[i][j] == player){
                count = count + 1; 
                if (count >= 5) return true;
            } 
            else break;
        }
        for (int i = x + 1, j = y - 1; i < 15 && j >= 0; i++, j--) {
            if (chessboard[i][j] == player) {
                count = count + 1;
                if (count >= 5) return true;
            }
            else break;
        }

        return false;
    }

    public void newGame(){
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                chessboard[i][j] = 0;
            }
        }
        for (int i = 0; i < 2; i++) isMoved[i] = 0;
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 2; j++){
                for (int k = 0; k < 2; k++){
                    latestMoves[i][j][k] = -1;
                }
            }
        }
    }

    public int[] computer(int computerColor){
        this.setHeuristic();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (chessboard[i][j] == 0) {    // check unoccupied spots
                    String pattern = "";
                    int color = 0;

                    // downwards
                    for (int k = i + 1; k < 15; k++) {   
                        if (chessboard[k][j] == 0) break;
                        else {
                            if (chessboard[k][j] == computerColor) {
                                pattern += "1";
                            }
                            else pattern += "2";
                        }
                    }
                    if (heuristic.get(pattern) != null) value[i][j] += heuristic.get(pattern);

                    // upwards
                    pattern = "";
                    for (int k = i - 1; k >= 0; k--) {   
                        if (chessboard[k][j] == 0) break;
                        else {
                            if (chessboard[k][j] == computerColor) {
                                pattern += "1";
                            }
                            else pattern += "2";
                        }
                    }
                    if (heuristic.get(pattern) != null) value[i][j] += heuristic.get(pattern);

                    // rightwards
                    pattern = "";
                    for (int k = j + 1; k < 15; k++) {   
                        if (chessboard[i][k] == 0) break;
                        else {
                            if (chessboard[i][k] == computerColor) {
                                pattern += "1";
                            }
                            else pattern += "2";
                        }
                    }
                    if (heuristic.get(pattern) != null) value[i][j] += heuristic.get(pattern);

                    // leftwards
                    pattern = "";
                    for (int k = j - 1; k >= 0; k--) {   
                        if (chessboard[i][k] == 0) break;
                        else {
                            if (chessboard[i][k] == computerColor) {
                                pattern += "1";
                            }
                            else pattern += "2";
                        }
                    }
                    if (heuristic.get(pattern) != null) value[i][j] += heuristic.get(pattern);

                    // lowerright
                    pattern = "";
                    for (int k = i + 1, l = j + 1; k < 15 && l < 15; k++, l++) {   
                        if (chessboard[k][l] == 0) break;
                        else {
                            if (chessboard[k][l] == computerColor) {
                                pattern += "1";
                            }
                            else pattern += "2";
                        }
                    }
                    if (heuristic.get(pattern) != null) value[i][j] += heuristic.get(pattern);

                    // upperleft
                    pattern = "";
                    for (int k = i - 1, l = j - 1; k >= 0 && l >= 0; k--, l--) {   
                        if (chessboard[k][l] == 0) break;
                        else {
                            if (chessboard[k][l] == computerColor) {
                                pattern += "1";
                            }
                            else pattern += "2";
                        }
                    }
                    if (heuristic.get(pattern) != null) value[i][j] += heuristic.get(pattern);

                    // upperright
                    pattern = "";
                    for (int k = i - 1, l = j + 1; k >= 0 && l < 15; k--, l++) {   
                        if (chessboard[k][l] == 0) break;
                        else {
                            if (chessboard[k][l] == computerColor) {
                                pattern += "1";
                            }
                            else pattern += "2";
                        }
                    }
                    if (heuristic.get(pattern) != null) value[i][j] += heuristic.get(pattern);

                    // lowerleft
                    pattern = "";
                    for (int k = i + 1, l = j - 1; k < 15 && l >= 0; k++, l--) {   
                        if (chessboard[k][l] == 0) break;
                        else {
                            if (chessboard[k][l] == computerColor) {
                                pattern += "1";
                            }
                            else pattern += "2";
                        }
                    }
                    if (heuristic.get(pattern) != null) value[i][j] += heuristic.get(pattern);
                }
                else value[i][j] = 0;
            }
        }

        int maxValue = -1;
        int[] finalMove = new int[2];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (maxValue < value[i][j]) {
                    maxValue = value[i][j];
                    finalMove[0] = i;
                    finalMove[1] = j;
                }
            }
        }

        return finalMove;
    }

    public void setHeuristic(){
        heuristic.put("1", 20);
        heuristic.put("11", 200);
        heuristic.put("111", 2000);
        heuristic.put("1111", 3000);
        heuristic.put("12", 10);
        heuristic.put("112", 100);
        heuristic.put("1112", 1000);
        heuristic.put("11112", 2000);
    }

    public void resetValue(){
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                value[i][j] = 0;
            }
        }

    }
}