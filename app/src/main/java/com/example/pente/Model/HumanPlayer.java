package com.example.pente.Model;

import java.util.Scanner;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.pente.Model.Board;
import com.example.pente.Model.Strategy;

import android.content.Context;

public class HumanPlayer extends Player {

    private Context context;



    /**
     * Constructs a HumanPlayer with the specified symbol and associated context.
     * @param symbol The character representing the player's symbol ('W' for white, 'B' for black).
     * @param context The context associated with the computer player, typically used for game-related operations.
     * Help from : https://developer.android.com/reference/android/content/Context
     */
    public HumanPlayer(char symbol,Context context) {
        super(symbol);
        this.context = context;
    }

    /**
     * Checks if a specified position on the game board is valid for the computer player's move.
     * @param board The current game board.
     * @param position The position to check for validity.
     * @return True if the position is valid; otherwise, false.
     */
    public boolean checkPosition(Board board,int position){

        String move = returnMove(position);
        char colChar = move.charAt(0);
        int row = 20 - Integer.parseInt(move.substring(1));
        int col = colChar - 'A';

        //check Validity
        if(isValidMove(board, row, col)){
            return true;
        }
        else{
            return false;
        }


    }



    /**
     * Converts a linear position index to a string representation of a move on the game board.
     * @param position The linear position index to convert.
     * @return A string representing the move in standard board notation.
     */
    public String returnMove(int position){
        if (position >= 0 && position < 19 * 19) {
            int boardSize = 19;
            int row = position / boardSize;
            int col = position % boardSize;

            char colChar = (char) ('A' + col);
            String move = colChar + Integer.toString(19 - row);
            return move;
        } else {
            // Handle the case where position is out of bounds (e.g., return an error or throw an exception)
            return "Invalid position";
        }
    }

    /**
     * Makes a move for the human player based on the current game state and move count.
     * @param board The current game board.
     * @param moveCount The current move count.
     */
    @Override
    public void makeMove(Board board, int moveCount) {
            setHasCaptured(false);
            String move;
            if(moveCount == 1){
                move = "J10";

            }
            else{
                move = returnMove(position);
            }


            if(moveCount == 3){
                if(!isThreePointsAway("J10", move)){
                    System.out.println("Invalid move. Please enter a valid position.");
                    System.out.println("Hint: The stone should be at least 3 intersections away from the center of the board i.e. J10");
                    return;
                }

            }


            char colChar = move.charAt(0);
            int row = 20 - Integer.parseInt(move.substring(1));
            int col = colChar - 'A';

            System.out.println(row+ " " +col);

            if (isValidMove(board, row, col)) {
                board.placeStone(move, 'H');
                board.printBoard(HumanPlayer.super.getSymbol());
                if (board.checkFive(row, col, 1)) {
                    System.out.println("You win!");
                    board.setGameOver(true);
                    return;
                }

                while (board.checkCapture(row, col, 1)) {
                    board.printBoard(HumanPlayer.super.getSymbol());
                    System.out.println("You captured a stone!");
                    setHasCaptured(true);
                }

                System.out.println("Human Captures: "+ board.getHumanCaptures());
                System.out.println("Computer Captures: "+ board.getComputerCaptures());
            } else {
                System.out.println(move);
                System.out.println("Invalid move. Please enter a valid position.");
            }
    }

    /**
     * Generates a move suggestion for the human player in help mode based on the current game state and move count.
     * @param board The current game board.
     * @param moveCount The current move count.
     * @return A string representing the suggested move in standard board notation.
     */
    public String helpMode(Board board, int moveCount){
        Strategy strategy = new Strategy(board, 1, context);
        String move;
        Pair<Integer, Integer> bestMove;

        if(moveCount == 1){
            return "J10";
        }
        else if(moveCount == 3){
            bestMove = strategy.evaluateSecondMove();
        }
        else {
            bestMove = strategy.evaluateAllCases();
        }

        //Log.d("strategy", bestMove.first + " " + bestMove.second );

        int row = 20 - bestMove.first;  // Get the first element
        int col = bestMove.second;      // Get the second element

        char colChar = (char) ('A' + col);
        move = colChar + Integer.toString(row);

        return move;


    }

    /**
     * Provides reasoning for the suggested move in help mode based on the current game state and move count.
     * @param board The current game board.
     * @param moveCount The current move count.
     * @return A string describing the reasoning behind the suggested move.
     */
    public String helpModeReasoning(Board board, int moveCount){
        Strategy strategy = new Strategy(board, 1, context);
        String reason;

        if(moveCount == 1){
            return "First move at the center fo the center of the board";
        }
        else if(moveCount == 3){
            return "Three intersection away from the center";
        }
        else{
            return strategy.evaluateAllCasesReasoning();

        }


    }
}

