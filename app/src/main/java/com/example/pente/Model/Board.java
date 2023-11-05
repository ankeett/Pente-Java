package com.example.pente.Model;
import android.util.Log;

public class Board {
    private int[][] board = new int[19][19];
    private boolean gameOver = false;
    private int humanCaptures = 0;
    private int computerCaptures = 0;
    private int moveCount = 1;
    private int winner = 0;

    public Board deepCopy() {
        Board copy = new Board();
        for (int row = 0; row < 19; row++) {
            for (int col = 0; col < 19; col++) {
                copy.board[row][col] = this.board[row][col];
            }
        }
        // Copy other fields if necessary
        copy.gameOver = this.gameOver;
        copy.humanCaptures = this.humanCaptures;
        copy.computerCaptures = this.computerCaptures;
        copy.moveCount = this.moveCount;
        copy.winner = this.winner;

        return copy;
    }


//    public void printBoard(char symbol) {
//        // Print column labels (A - S)
//        System.out.print("   ");
//        for (char col = 'A'; col <= 'S'; col++) {
//            System.out.print(col + "  ");
//        }
//        System.out.println();
//
//        // Print the board with row labels (1 - 19)
//        // Rows go from 1 to 19, so we start from 18 down to 0
//        int row = 0;
//        for (int rowName = 18; rowName >= 0; rowName--) {
//            // Set the width to 2 for row labels
//            System.out.print(String.format("%2d ", rowName + 1));
//            for (int col = 0; col < 19; col++) {
//                if (board[row][col] == 0) {
//                    System.out.print(".  ");
//                } else if (board[row][col] == 1) {
//                    System.out.print(symbol + "  ");
//                } else {
//                    if (symbol == 'W') {
//                        System.out.print("B  ");
//                    } else {
//                        System.out.print("W  ");
//                    }
//                }
//            }
//            row++;
//            System.out.println();
//        }
//    }
public String printBoard(char player) {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < board.length; row++) {
        for (int col = 0; col < board[row].length; col++) {
            sb.append(board[row][col]);
            sb.append(' ');
        }
        sb.append('\n');
    }
    return sb.toString();
}


    public void placeStone(String move, char symbol) {
        Log.d("MyTag", "Entering myFunction");
        char colChar = Character.toUpperCase(move.charAt(0));
        int row = Integer.parseInt(move.substring(1));
        int col = colChar - 'A';
        row = 20 - row;

        if (symbol == 'H') {
            board[row - 1][col] = 1;
        } else {
            board[row - 1][col] = 2;
        }
    }

    public boolean checkFive(int row, int col, int symbol) {
        int consecutiveSum = 0;

        consecutiveSum += checkDirection(row, col, symbol, 0, -1, 5);
        consecutiveSum += checkDirection(row, col, symbol, 0, 1, 5);

        if (consecutiveSum >= 4) {
            setWinner(symbol);
            return true;
        }

        consecutiveSum = 0;
        consecutiveSum += checkDirection(row, col, symbol, -1, 0, 5);
        consecutiveSum += checkDirection(row, col, symbol, 1, 0, 5);

        if (consecutiveSum >= 4) {
            setWinner(symbol);
            return true;
        }

        consecutiveSum = 0;
        consecutiveSum += checkDirection(row, col, symbol, -1, -1, 5);
        consecutiveSum += checkDirection(row, col, symbol, 1, 1, 5);

        if (consecutiveSum >= 4) {
            setWinner(symbol);
            return true;
        }

        consecutiveSum = 0;
        consecutiveSum += checkDirection(row, col, symbol, -1, 1, 5);
        consecutiveSum += checkDirection(row, col, symbol, 1, -1, 5);

        if (consecutiveSum >= 4) {
            setWinner(symbol);
            return true;
        }

        return false;
    }

    public boolean checkFour(int row, int col, int symbol) {
        int consecutiveSum = 0;

        // Check for five consecutive stones in the vertical direction (up and down)
        consecutiveSum += checkDirection(row, col, symbol, 0, -1, 4); // Check up
        consecutiveSum += checkDirection(row, col, symbol, 0, 1, 4);  // Check down

        if (consecutiveSum >= 3) {
            //setWinner(symbol);
            return true;
        }

        // Check for five consecutive stones in the horizontal direction (left and right)
        consecutiveSum = 0;
        consecutiveSum += checkDirection(row, col, symbol, -1, 0, 4); // Check left
        consecutiveSum += checkDirection(row, col, symbol, 1, 0, 4);  // Check right

        if (consecutiveSum >= 3) {
            //setWinner(symbol);
            return true;
        }

        // Check for five consecutive stones in the diagonal direction (left-up and right-down)
        consecutiveSum = 0;
        consecutiveSum += checkDirection(row, col, symbol, -1, -1, 4); // Check left-up
        consecutiveSum += checkDirection(row, col, symbol, 1, 1, 4);   // Check right-down

        if (consecutiveSum >= 3) {
            //setWinner(symbol);
            return true;
        }

        // Check for five consecutive stones in the diagonal direction (left-down and right-up)
        consecutiveSum = 0;
        consecutiveSum += checkDirection(row, col, symbol, -1, 1, 4);  // Check left-down
        consecutiveSum += checkDirection(row, col, symbol, 1, -1, 4);  // Check right-up

        if (consecutiveSum >= 3) {
            //setWinner(symbol);
            return true;
        }

        return false; // No four consecutive stones found
    }


    public int checkDirection(int row, int col, int symbol, int deltaRow, int deltaCol, int count) {
        int consecutiveStones = 0;
        int r = row - 1;
        int c = col;
        count = count - 1;

        while (consecutiveStones < count) {
            r += deltaRow;
            c += deltaCol;

            if (r < 0 || r >= 19 || c < 0 || c >= 19 || board[r][c] != symbol) {
                break;
            }

            consecutiveStones++;
        }

        return consecutiveStones;
    }

    public boolean checkCapture(int row, int col, int symbol) {
        // Check for capture in different directions
        if (checkCaptureDirection(row, col, symbol, -1, 0) ||
                checkCaptureDirection(row, col, symbol, 1, 0) ||
                checkCaptureDirection(row, col, symbol, 0, -1) ||
                checkCaptureDirection(row, col, symbol, 0, 1) ||
                checkCaptureDirection(row, col, symbol, -1, -1) ||
                checkCaptureDirection(row, col, symbol, -1, 1) ||
                checkCaptureDirection(row, col, symbol, 1, -1) ||
                checkCaptureDirection(row, col, symbol, 1, 1)) {

            // Save the captured count for the human and computer player
            if (symbol == 1) {
                setHumanCaptures(getHumanCaptures() + 1);
            } else {
                setComputerCaptures(getComputerCaptures() + 1);
            }

            // Capture found
            return true;
        }
        // No capture found
        return false;
    }

    public boolean checkCaptureDirection(int row, int col, int symbol, int deltaRow, int deltaCol) {
        // Determine the opponent's symbol
        int opponentSymbol = (symbol == 1) ? 2 : 1;

        int consecutiveOpponentStones = 0;

        row -= 1;

        while (consecutiveOpponentStones < 2) {
            row += deltaRow;
            col += deltaCol;

            if (row < 0 || row >= 19 || col < 0 || col >= 19 || board[row][col] != opponentSymbol) {
                // Stone not of the opponent's symbol or out of bounds
                break;
            }

            consecutiveOpponentStones++;
        }

        if (consecutiveOpponentStones == 2) {
            // Two consecutive opponent stones found, now check for player's stone
            row += deltaRow;
            col += deltaCol;

            if (row >= 0 && row < 19 && col >= 0 && col < 19 && board[row][col] == symbol) {
                // Three stones in a row: opponent-opponent-player
                // Reset the board to empty cells for the two opponent stones
                row -= deltaRow * 2;
                col -= deltaCol * 2;

                for (int i = 0; i < 2; i++) {
                    board[row][col] = 0;
                    row += deltaRow;
                    col += deltaCol;
                }
                // Capture found
                return true;
            }
        }
        // No capture found
        return false;
    }


    public int getWinner() {
        return winner;
    }

    public void setWinner(int symbol) {
        winner = symbol;
    }

    public int getHumanCaptures() {
        return humanCaptures;
    }

    public void setHumanCaptures(int captures) {
        humanCaptures = captures;
    }

    public int getComputerCaptures() {
        return computerCaptures;
    }

    public void setComputerCaptures(int captures) {
        computerCaptures = captures;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int count) {
        moveCount = count;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean over) {
        gameOver = over;
    }

    public boolean isOccupied(String move) {
        char colChar = Character.toUpperCase(move.charAt(0));
        int row = Integer.parseInt(move.substring(1));
        int col = colChar - 'A';
        row = 20 - row;

        return board[row - 1][col] != 0;
    }

    public void setBoard(int row, int col, int symbol) {
        board[row-1][col] = symbol;
    }

    public int getBoard(int row, int col) {
        return board[row-1][col];
    }
    public boolean isEmptyCell(int row, int col) {
        return board[row-1][col] == 0;
    }

    public int calculateConsecutiveCount(int row, int col, int playerSymbol) {
        int consecutiveCount = 0;

        // Define directions for checking consecutive stones (horizontal, vertical, diagonal)
        int[][] directions = { {1, 0}, {0, 1}, {1, 1}, {1, -1} };

        // Iterate through the four directions
        for (int i = 0; i < 4; i++) {
            int dRow = directions[i][0];
            int dCol = directions[i][1];

            // Check in both forward and backward directions
            for (int sign = -1; sign <= 1; sign += 2) {
                int count = 0;

                // Iterate in the current direction
                for (int step = 1; step <= 4; step++) { // Check up to 4 stones in a row
                    int newRow = row + dRow * sign * step;
                    int newCol = col + dCol * sign * step;

                    // Check if the new position is within the board boundaries
                    if (newRow >= 0 && newRow < 19 && newCol >= 0 && newCol < 19) {
                        if (board[newRow][newCol] == playerSymbol) {
                            count++;
                        } else {
                            // Stop checking in this direction
                            break;
                        }
                    } else {
                        // Stop checking in this direction (out of bounds)
                        break;
                    }
                }

                // Update the consecutive count
                if (count > consecutiveCount) {
                    consecutiveCount = count;
                }
            }
        }

        return consecutiveCount;
    }

    public int checkEmptyBoard() {
        int count = 0;
        for (int i = 1; i <= 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i-1][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countFour(int symbol) {
        int[][] directions = { {0, 1}, {1, 0}, {1, 1}, {1, -1} };
        int totalCount = 0;

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                for (int d = 0; d < 4; d++) {
                    int dx = directions[d][0];
                    int dy = directions[d][1];
                    int count = 0;
                    int x = i, y = j;

                    for (int step = 0; step < 5; step++) {
                        if (x < 0 || x >= 19 || y < 0 || y >= 19) {
                            break;
                        }
                        if (board[x][y] == symbol) {
                            count++;
                            x += dx;
                            y += dy;
                        } else {
                            break;
                        }
                    }

                    if (count == 4) {
                        totalCount++;
                    }
                }
            }
        }

        return totalCount;
    }



}

