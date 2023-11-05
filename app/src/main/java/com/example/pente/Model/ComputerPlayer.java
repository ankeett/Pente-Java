package com.example.pente.Model;

import android.util.Pair;
import android.content.Context;
import android.util.Log;
import android.content.Context;

import com.example.pente.Model.Strategy;
public class ComputerPlayer extends Player {

    private  Context context;
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
                Strategy strategy = new Strategy(board, 2, context);
                // Set the scores to determine the strategy


                // Determine the best move based on the game situation
                // if (moveCount == 3) {
                //     // Evaluate second move strategy
                //     // The best move is calculated here
                //     // ...

                // } else {
                //     // Evaluate all cases strategy
                //     // The best move is calculated here
                //     // ...

                // }
                Pair<Integer, Integer> bestMove = strategy.evaluateAllCases();

                Log.d("strategy", bestMove.first + " " + bestMove.second );

                 row = 20 - bestMove.first;  // Get the first element
                 col = bestMove.second;      // Get the second element

                char colChar = (char) ('A' + col);
                move = colChar + Integer.toString(row);

                if (moveCount == 3) {
                    // Ensure that the stone is at least 3 intersections away from the center of the board
                    // if (!isThreePointsAway("J10", move)) {
                    //     continue;
                    // }
                }
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
                break;
            }
            else{
                System.out.println("Invalid move. Please enter a valid position.");
            }
        } while (true);
    }
}

