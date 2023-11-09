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


    //private int position;

//    public void setPosition(int position){
//        this.position = position;
//    }




    public HumanPlayer(char symbol,Context context) {
        super(symbol);
        this.context = context;
    }

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
    @Override
    public void makeMove(Board board, int moveCount) {
        //Pattern movePattern = Pattern.compile("[A-S]([1-9]|1[0-9])");
            String move;
            if(moveCount == 1){
                move = "J10";

            }
            else{
                System.out.println(position);
                move = returnMove(position);
            }

//            System.out.println(position);
//            move = returnMove(position);

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
                }
                //break;

                System.out.println("Human Captures: "+ board.getHumanCaptures());
                System.out.println("Computer Captures: "+ board.getComputerCaptures());
            } else {
                System.out.println(move);
                System.out.println("Invalid move. Please enter a valid position.");
            }
       // } while (true);
    }

    public String helpMode(Board board, int moveCount){
        Strategy strategy = new Strategy(board, 2, context);

        Pair<Integer, Integer> bestMove = strategy.evaluateAllCases();

        //Log.d("strategy", bestMove.first + " " + bestMove.second );

        int row = 20 - bestMove.first;  // Get the first element
        int col = bestMove.second;      // Get the second element

        char colChar = (char) ('A' + col);
        String move = colChar + Integer.toString(row);

        return move;


    }
}

