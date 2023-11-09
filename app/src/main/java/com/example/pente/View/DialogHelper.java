package com.example.pente.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.pente.Model.Board;
import com.example.pente.Model.Round;
import com.example.pente.Model.Tournament;

import java.util.concurrent.atomic.AtomicInteger;

public class DialogHelper {
    public static void showGameOverDialog(Context context, Tournament tournament, Round round, Board board, AtomicInteger moveCount, Runnable updateView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Game Over");

        // Get the round details
        int humanScores = round.getHumanScore(); // Replace with your actual method
        int computerScores = round.getComputerScore();
        int humanCaptures = board.getHumanCaptures();
        int computerCapture = board.getComputerCaptures();
        int winner = round.getWinner();

        String winnerString = winner == 1 ? "Human" : "Computer";

        // Construct the message with all round details
        String message = "Results:\n" +
                "Human Scores: " + humanScores + "\n" +
                "Computer Scores: " + computerScores + "\n" +
                "Human Captures: " + humanCaptures + "\n" +
                "Computer Captures: " + computerCapture + "\n" +
                "Winner: " + winnerString;

        builder.setMessage(message);

        // Add a button to start a new round
        builder.setPositiveButton("New Round", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //create a new round
                round.reset();

                board.reset();
                moveCount.set(1);
                updateView.run();

                dialog.dismiss();
                // Add your code to start a new round here
            }
        });

        // Add a button to quit the game
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showQuitConfirmationDialog(context, tournament);

            }
        });

        // Create and show the "Game Over" AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void showQuitConfirmationDialog(Context context,Tournament tournament) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tournament Scores:");
        int humanScores = tournament.getHumanScores(); // Replace with your actual method
        int computerScores = tournament.getComputerScores();

        // Construct the message with all round details
        String message = "Results:\n" +
                "Human Scores: " + humanScores + "\n" +
                "Computer Scores: " + computerScores + "\n";

        builder.setMessage(message);


        // Add a button to confirm quitting
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                System.exit(0);


                // Add your code to handle quitting the game here
            }
        });

        // Create and show the "Quit Confirmation" AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
