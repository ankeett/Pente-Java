package com.example.pente.Model;

public class Player {

    /**
     * Data members
     */

    boolean quitGame  = false;
    protected static int position;
    private char symbol;
    private boolean hasCaptured = false;


    /**
    setters and getters
     */
    public void setPosition(int position){
        Player.position = position;
    }


    public void setHasCaptured(boolean result){
        hasCaptured = result;
    }

    public boolean getHasCaptured(){
        return hasCaptured;
    }


    public int getPosition(){
        return position;
    }


    public Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char newSymbol) {
        symbol = newSymbol;
    }

    public void makeMove(Board board, int moveCount) {
        // The makeMove method implementation is specific to the HumanPlayer and ComputerPlayer classes.
    }

    /**
     * Checks if the specified move (row, col) on the game board is valid.
     * @param board The current game board.
     * @param row The row index of the move.
     * @param col The column index of the move.
     * @return True if the move is valid; otherwise, false.
     */
    public boolean isValidMove(Board board, int row, int col) {
        return row >= 1 && row <= 19 && col >= 0 && col < 19 && board.isEmptyCell(row, col);
    }

    public void hasQuit(boolean quit) {
        quitGame = quit;
    }
    public boolean getQuit() {
        return quitGame;
    }

    /**
     * Checks if the next position is at least three intersections away from the initial position.
     * @param initialPos The initial position in standard board notation (e.g., "J10").
     * @param nextPos The next position in standard board notation.
     * @return True if the next position is at least three intersections away; otherwise, false.
     */
    public boolean isThreePointsAway(String initialPos, String nextPos) {
        // Convert the numeric parts of the positions to integers.
        int initialPosRow = Integer.parseInt(initialPos.substring(1));
        int initialPosCol = initialPos.charAt(0) - 'A';

        int nextPosRow = Integer.parseInt(nextPos.substring(1));
        int nextPosCol = nextPos.charAt(0) - 'A';

        // Calculate the absolute differences in rows and columns.
        int rowDifference = Math.abs(initialPosRow - nextPosRow);
        int colDifference = Math.abs(initialPosCol - nextPosCol);

        // Check if the move is at least 3 intersections away in any direction.
        return (rowDifference >= 3 || colDifference >= 3 || (rowDifference >= 3 && colDifference >= 3));
    }

}


