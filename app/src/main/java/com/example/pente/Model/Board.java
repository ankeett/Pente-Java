package com.example.pente.Model;
import android.util.Log;

public class Board {
    /*** Private member variables ***/
    private int[][] board = new int[19][19];
    private boolean gameOver = false;
    private int humanCaptures = 0;
    private int computerCaptures = 0;
    private int moveCount = 1;
    private int winner = 0;

    /**
     * Resets the game state, clearing the 2D array representing the board and resetting other game-related variables.
     */
    public void reset() {
        // Example: Clear a 2D array representing the board
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                board[i][j] = 0; // Set to your initial state constant
            }
        }

        humanCaptures = 0;
        computerCaptures = 0;
        winner = 0;
        gameOver = false;
    }

    /**
     * Creates a deep copy of the current game board, including all game-related variables.
     * @return A new Board object that is an independent copy of the current board state.
     */
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


    /**
     * Generates a string representation of the current game board, replacing numeric values with a specified player character.
     * @param player The character representation of a player to use in the printed board.
     * @return A string representation of the current game board with player characters for debugging purposes.
     */
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

    /**
     * Places a stone on the game board at the specified position.
     * @param move A string representation of the move, where the first character is the column (A to T) and the remaining characters are the row number.
     * @param symbol The symbol representing the player ('H' for human, 'C' for computer).
     */
    public void placeStone(String move, char symbol) {
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

    /**
     * Checks if there are five consecutive stones of the specified symbol in any direction starting from the given position.
     * @param row The row index of the starting position.
     * @param col The column index of the starting position.
     * @param symbol The numeric symbol representing the player's stone (1 for human, 2 for computer).
     * @return True if there are five consecutive stones in any direction; otherwise, false.
     */
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

    /**
     * Checks if there are five consecutive stones of the specified symbol in any direction starting from the given position.
     * @param row The row index of the starting position.
     * @param col The column index of the starting position.
     * @param symbol The numeric symbol representing the player's stone (1 for human, 2 for computer).
     * @return True if there are four consecutive stones in any direction; otherwise, false.
     */
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

    /**
     * Checks the number of consecutive stones in a specified direction starting from a given position.
     * @param row The row index of the starting position.
     * @param col The column index of the starting position.
     * @param symbol The numeric symbol representing the player's stone (1 for human, 2 for computer).
     * @param deltaRow The change in row index for each step in the specified direction.
     * @param deltaCol The change in column index for each step in the specified direction.
     * @param count The maximum number of consecutive stones to check.
     * @return The number of consecutive stones in the specified direction.
     */
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

    /**
     * Checks for a capture in the specified direction starting from a given position.
     * @param row The row index of the starting position.
     * @param col The column index of the starting position.
     * @param symbol The numeric symbol representing the player's stone (1 for human, 2 for computer).
     * @param deltaRow The change in row index for each step in the specified direction.
     * @param deltaCol The change in column index for each step in the specified direction.
     * @return True if a capture is found in the specified direction; otherwise, false.
     */
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

    /** setters and getters
     */
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

    public void setBoard(int row, int col, int symbol) {
        board[row-1][col] = symbol;
    }

    public int getBoard(int row, int col) {
        return board[row-1][col];
    }
    public boolean isEmptyCell(int row, int col) {
        return board[row-1][col] == 0;
    }

    /**
     * Calculates the maximum number of consecutive stones in any direction starting from a given position for a specified player.
     * @param row The row index of the starting position.
     * @param col The column index of the starting position.
     * @param playerSymbol The numeric symbol representing the player's stone (1 for human, 2 for computer).
     * @return The maximum number of consecutive stones in any direction.
     */
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

    /**
     * Checks the number of non-empty cells on the game board.
     * @return The count of non-empty cells on the board.
     */
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

    /**
     * Counts the number of occurrences where there are four consecutive stones of the specified symbol in any direction on the board.
     * @param symbol The numeric symbol representing the player's stone (1 for human, 2 for computer).
     * @return The count of occurrences where there are four consecutive stones in any direction.
     */
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

