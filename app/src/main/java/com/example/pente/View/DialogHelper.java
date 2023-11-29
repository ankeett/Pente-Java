package com.example.pente.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.pente.Model.Board;
import com.example.pente.Model.ComputerPlayer;
import com.example.pente.Model.HumanPlayer;
import com.example.pente.Model.Round;
import com.example.pente.Model.Tournament;

import java.util.concurrent.atomic.AtomicInteger;

public class DialogHelper {

    /**
     * Displays a "Game Over" dialog with round and tournament details.
     *
     * @param context     The context from which the method is called.
     * @param tournament  The tournament object containing scores and rounds.
     * @param round       The current round object with scores and winner information.
     * @param board       The game board object.
     * @param moveCount   The atomic integer representing the move count.
     * @param updateView  A runnable to update the game view.
     */
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
                "Winner: " + winnerString + "\n" +
                "Human tournament score: " + tournament.getHumanScores() + "\n"+
                "Computer tournament score: " + tournament.getComputerScores() + "\n";



        builder.setMessage(message);

        // Add a button to start a new round
        builder.setPositiveButton("New Round", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //create a new round
                tournament.setRound(round);
                round.reset();

                board.reset();
                moveCount.set(1);
                updateView.run();

                System.out.println(tournament.getHumanScores());
                System.out.println(tournament.getComputerScores());

                if (tournament.getHumanScores() > tournament.getComputerScores()) {
                    round.humanRound();
                    round.setCurrentPlayerIndex(0);

                    System.out.println(round.getCurrentPlayer() instanceof HumanPlayer);
                } else if (tournament.getHumanScores() < tournament.getComputerScores()) {
                    round.computerRound();
                }
                else{
                    //toss the coin
                    Intent intent = new Intent(context, CoinTossActivity.class);
                    intent.putExtra("gameType", "Start");
                    // Start the StartGame activity
                    context.startActivity(intent);
                }

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

    /**
     * Displays a "Quit Confirmation" dialog with tournament scores and winner information.
     *
     * @param context    The context from which the method is called.
     * @param tournament The tournament object containing human and computer scores.
     */
    private static void showQuitConfirmationDialog(Context context,Tournament tournament) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tournament Scores:");
        int humanScores = tournament.getHumanScores(); // Replace with your actual method
        int computerScores = tournament.getComputerScores();

        String winner;

        if(humanScores>computerScores){
            winner = "You win the game!";
        }
        else if(humanScores<computerScores){
            winner= "Computer wins the game!";
        }
        else{
            winner = "It's a tie!";
        }

        // Construct the message with all round details
        String message = "Results:\n" +
                "Human Scores: " + humanScores + "\n" +
                "Computer Scores: " + computerScores + "\n" +
                "Result: " + winner;

        builder.setMessage(message);


        // Add a button to confirm quitting
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                System.exit(0);
            }
        });

        // Create and show the "Quit Confirmation" AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
