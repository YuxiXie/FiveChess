package backend;

public class Core {
    private static int row = 15, col = 15;
    private int[] isMoved = new int[2]; // isMoved[0]: #black moves; isMoved[1]: #white moves
    private int[][][] latestMoves = new int[2][2][2];  // latestMoves[0][0]: latest black move; latestMoves[0][1]: second latest black move;
    private int[][] value = new int[row][col];
    public int[][] chessboard = new int[row][col];


    
    public static void main(String[] args) {
        Core testcore = new Core();
        testcore.newGame();
        testcore.resetValue();
        testcore.chessboard[7][7] = 1;
        int[] xy = testcore.computer(2);
        System.out.println(xy[0]);
        System.out.println(xy[1]);
        
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
        this.resetValue();
    }

    public int[] computer(int computerColor){
        this.resetValue();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (chessboard[i][j] == 0) {    // check unoccupied spots
                    String pattern = "";
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
                    value[i][j] += getHeuristic(pattern);

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
                    value[i][j] += getHeuristic(pattern);

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
                    value[i][j] += getHeuristic(pattern);

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
                    value[i][j] += getHeuristic(pattern);

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
                    value[i][j] += getHeuristic(pattern);

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
                    value[i][j] += getHeuristic(pattern);

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
                    value[i][j] += getHeuristic(pattern);

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
                    value[i][j] += getHeuristic(pattern);
                }
                else value[i][j] = 0;
            }
        }

        int maxValue = -1;
        int[] finalMove = new int[2];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (maxValue < value[i][j]) {
                    finalMove[0] = i;
                    finalMove[1] = j;
                    maxValue = value[i][j];
                }
            }
        }

        return finalMove;
    }


    public int getHeuristic(String pattern){
        if (pattern.startsWith("1111")) return 5000;
        else if (pattern.startsWith("2222")) return 4000;
        else if (pattern.startsWith("1112")) return 1000;
        else if (pattern.startsWith("111")) return 3000;
        else if (pattern.startsWith("2221")) return 800;
        else if (pattern.startsWith("222")) return 3500;
        else if (pattern.startsWith("112")) return 600;
        else if (pattern.startsWith("11")) return 2000;
        else if (pattern.startsWith("221")) return 400;
        else if (pattern.startsWith("22")) return 800;
        else if (pattern.startsWith("1")) return 200;
        else if (pattern.startsWith("12")) return 100;
        else if (pattern.startsWith("2")) return 300;
        else if (pattern.startsWith("21")) return 150;
        else return 0;
    }

    public void resetValue(){
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                value[i][j] = 0;
            }
        }

    }
}
