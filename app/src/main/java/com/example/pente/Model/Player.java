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
}


