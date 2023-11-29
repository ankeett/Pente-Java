package com.example.pente.Model;

import android.util.Pair;
import android.content.Context;
import android.util.Log;
import android.content.Context;

import com.example.pente.Model.Strategy;
public class ComputerPlayer extends Player {

    private Context context;


    /**
     * Constructs a ComputerPlayer with the specified symbol and associated context.
     * @param symbol The character representing the player's symbol ('W' for white, 'B' for black).
     * @param context The context associated with the computer player, typically used for game-related operations.
     * Help from : https://developer.android.com/reference/android/content/Context
     */
    public ComputerPlayer(char symbol, Context context) {
        super(symbol);
        this.context = context;
    }


    /**
     * Provides reasoning for the computer player's move based on the current game state and move count.
     * @param board The current game board.
     * @param moveCount The current move count.
     * @return A string describing the reasoning behind the computer player's move.
     */
     public String reasoningMove(Board board, int moveCount){
            if(moveCount ==1){
                return "accommodate the first move rule.";
            }
            else if(moveCount == 3){
                return "accommodate three intersections away from the center";
            }
            else{
                Strategy strategy = new Strategy(board,2,context);

                return strategy.evaluateAllCasesReasoning();

            }

     }

    /**
     * Generates a string representation of the computer player's move based on the current game state and move count.
     * @param board The current game board.
     * @param moveCount The current move count.
     * @return A string representing the computer player's move in standard board notation.
     */
     public String moveString(Board board, int moveCount){
            if(moveCount == 1){
                return "J10";
            }
            else {
                Strategy strategy = new Strategy(board, 2, context);
                Pair<Integer, Integer> bestMove;

                if (moveCount == 3) {
                    bestMove = strategy.evaluateSecondMove();

                }
                else {
                    bestMove = strategy.evaluateAllCases();
                }

                int row = 20 - bestMove.first;  // Get the first element
                int col = bestMove.second;      // Get the second element

                char colChar = (char) ('A' + col);

                return colChar + Integer.toString(row);

            }
     }

    /**
     * Virtual function that makes a move for the computer player based on the current game state and move count.
     * @param board The current game board.
     * @param moveCount The current move count.
     */
    @Override
    public void makeMove(Board board, int moveCount) {
        do {
            setHasCaptured(false);
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
                    setHasCaptured(true);
                }
                break;
            } else {
                System.out.println("Invalid move. Please enter a valid position.");
            }
        } while (true);
    }

    /**
     * Returns the computer player's move as a string based on the current game state and move count.
     * @param board The current game board.
     * @param moveCount The current move count.
     * @return A string representing the computer player's move in standard board notation.
     */
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
