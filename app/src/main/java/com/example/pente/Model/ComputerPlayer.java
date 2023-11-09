package com.example.pente.Model;

import android.util.Pair;
import android.content.Context;
import android.util.Log;
import android.content.Context;

import com.example.pente.Model.Strategy;
public class ComputerPlayer extends Player {

    private Context context;

    //private int position;

    //    public void setPosition(int position){
//        this.position = position;
//    }
    public ComputerPlayer(char symbol, Context context) {
        super(symbol);
        this.context = context;
    }


    @Override
    public void makeMove(Board board, int moveCount) {
        do {
            String move;
            int row, col;

            if (moveCount == 1) {
                System.out.println("The first move is always in the center of the board.");
                move = "J10";
                row = 11;
                col = 10;
            } else {

                // Set the scores to determine the strategy
                Strategy strategy = new Strategy(board, 2, context);
                Pair<Integer, Integer> bestMove;

                // Determine the best move based on the game situation
                 if (moveCount == 3) {
                     bestMove = strategy.evaluateSecondMove();

                 }
                 else {
                     bestMove = strategy.evaluateAllCases();
                 }

                Log.d("strategy", bestMove.first + " " + bestMove.second);

                row = 20 - bestMove.first;  // Get the first element
                col = bestMove.second;      // Get the second element

                char colChar = (char) ('A' + col);
                move = colChar + Integer.toString(row);
            }

            row = 20 - row;

            if (isValidMove(board, row, col)) {
                System.out.println("Computer's move: " + move);
                board.placeStone(move, 'C');

                if (ComputerPlayer.super.getSymbol() == 'W') {
                    board.printBoard('B');
                } else {
                    board.printBoard('W');
                }

                if (board.checkFive(row, col, 2)) {
                    System.out.println("Computer wins!");
                    board.setGameOver(true);
                    return;
                }

                while (board.checkCapture(row, col, 2)) {
                    if (ComputerPlayer.super.getSymbol() == 'W') {
                        board.printBoard('B');
                    } else {
                        board.printBoard('W');
                    }
                    System.out.println("Computer captured a stone!");
                }

                System.out.println("Human Captures: " + board.getHumanCaptures());
                System.out.println("Computer Captures: " + board.getComputerCaptures());
                break;
            } else {
                System.out.println("Invalid move. Please enter a valid position.");
            }
        } while (true);
    }


    public String returnMove(Board board, int moveCount) {
        Strategy strategy = new Strategy(board, 2, context);
        Pair<Integer, Integer> bestMove = strategy.evaluateAllCases();

        Log.d("strategy", bestMove.first + " " + bestMove.second);

        int row = 20 - bestMove.first;  // Get the first element
        int col = bestMove.second;      // Get the second element

        char colChar = (char) ('A' + col);
        String move = colChar + Integer.toString(row);

        return move;
    }
}
