package com.example.pente.Model;

public class Player {
    private char symbol;
    boolean quitGame  = false;

    protected static int position;

    public void setPosition(int position){
        System.out.println("position is changed to "+ position);
        Player.position = position;
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

    public boolean isValidMove(Board board, int row, int col) {
        return row >= 1 && row <= 19 && col >= 0 && col < 19 && board.isEmptyCell(row, col);
    }

    public void hasQuit(boolean quit) {
        quitGame = quit;
    }
    public boolean getQuit() {
        return quitGame;
    }

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


