package com.example.pente.Model;

import java.util.Scanner;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Pair;

import com.example.pente.Model.Board;
import com.example.pente.Model.Strategy;

import android.content.Context;

public class HumanPlayer extends Player {

    private Context context;

    public HumanPlayer(char symbol,Context context) {
        super(symbol);
        this.context = context;
    }

    @Override
    public void makeMove(Board board, int moveCount) {
        Pattern movePattern = Pattern.compile("[A-S]([1-9]|1[0-9])");
        Scanner scanner = new Scanner(System.in);

        do {
            String move;
            if (moveCount == 1) {
                System.out.println("The first move is always in the center of the board.");
                move = "J10";
            } else {
                System.out.println("Enter your move (e.g., K10):");
                System.out.println("Enter HELP for a hint or QUIT for quitting the game.");
                move = scanner.nextLine().toUpperCase();

                if (move.equals("HELP")) {
                    Strategy strategy = new Strategy(board, 1,context);
                    // Set the scores to determine the strategy

                    // Determine the best move based on the game situation
                    // ...

                    Pair<Integer, Integer> bestMove = strategy.evaluateAllCases();
                    int row = 20 - bestMove.first; // Get the first element
                    int col = bestMove.second;    // Get the second element

                    char colChar = (char) ('A' + col);
                    move = colChar + Integer.toString(row);


                    System.out.println("The best move is " + move); // Replace with the actual best move
                    System.out.println("Enter your move (e.g., K10):");
                    move = scanner.nextLine().toUpperCase();
                }

                if (move.equals("QUIT")) {
                    System.out.println("Quitting the game");
                    hasQuit(true);
                    return;
                }

                if (!movePattern.matcher(move).matches()) {
                    System.out.println("Invalid input. Please enter a valid position (e.g., K10).");
                    continue;
                }
            }

            char colChar = move.charAt(0);
            int row = 20 - Integer.parseInt(move.substring(1));
            int col = colChar - 'A';

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
                break;
            } else {
                System.out.println("Invalid move. Please enter a valid position.");
            }
        } while (true);
    }
}

