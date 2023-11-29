package com.example.pente.Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Tournament {

    /**
     * private data members
     */
    private Player[] playerList;

    private Round round;
    private ArrayList<Round> games;
    private int humanScores;
    private int computerScores;
    private int lastWinner;

    private Context context;

    private char humanColor = 'W';

    public void setRound(Round round){
        this.round = round;
    }
    public Round getRound(){
        return round;
    }

    public char getHumanColor(){
        return humanColor;
    }

    /**
     * Constructs a new tournament with a human player and a computer player.
     */
    public Tournament() {
        playerList = new Player[2];
        playerList[0] = new HumanPlayer('W',context);
        playerList[1] = new ComputerPlayer('B',context);
        games = new ArrayList<>();
    }

    /**
     * setters and getters
     * @return
     */
    public int getHumanScores() {
        return humanScores;
    }

    public int getComputerScores() {
        return computerScores;
    }

    public void setHumanScores(int score) {
        humanScores = score;
    }

    public void setComputerScores(int score) {
        computerScores = score;
    }

    public int getLastWinner() {
        return lastWinner;
    }

    public void setLastWinner(int winner) {
        lastWinner = winner;
    }


    /**
     * Simulates a coin toss and checks if the user's choice matches the result.
     *
     * @param value The user's choice ("Heads" or "Tails").
     * @return True if the user's choice matches the toss result, false otherwise.
     */
    public boolean tossCoin(String value) {
        Random random = new Random();
        int toss = random.nextInt(2) + 1; // Generates a random number between 1 and 2

        // Compare the random toss result with the user's choice
        if ("Heads".equals(value) && toss == 1) {
            // User chose "Heads" and the toss result is "Heads," so the user wins
            return true;
        } else if ("Tails".equals(value) && toss == 2) {
            // User chose "Tails" and the toss result is "Tails," so the user wins
            return true;
        }

        // If none of the conditions above are met, the user loses
        return false;
    }

    /**
     * Starts the game by simulating a coin toss and determining the player order.
     *
     * @param toss The user's choice for the coin toss ("Heads" or "Tails").
     * @return An array containing the players in the determined order.
     */
    public Player[] startGame(String toss) {
        //int toss = 0;
        System.out.println("Toss. Choose 1 for Head and 2 for Tails: ");
        Scanner scanner = new Scanner(System.in);


        if (tossCoin(toss)) {
            System.out.println("You won the toss");
            System.out.println("You play White");
            humanColor = 'W';
            setLastWinner(1);
        } else {
            System.out.println("Computer won the toss");
            System.out.println("You play Black");
            humanColor = 'B';

            Player temp = playerList[0];
            playerList[0] = playerList[1];
            playerList[1] = temp;
            setLastWinner(2);
        }

        playerList[0].setSymbol('W');
        playerList[1].setSymbol('B');

        return playerList;




    }


    /**
     * Calculates and updates the cumulative tournament scores based on the scores of a round.
     *
     * @param round The round for which the scores are considered.
     */
    public void calculateTournamentScore(Round round){
        setHumanScores(getHumanScores() + round.getHumanScore());
        setComputerScores(getComputerScores() + round.getComputerScore());

    }


    /**
     * Writes the current game state to a file.
     *
     * @param round     The current round containing the game state.
     * @param aFileName The name of the file to be created or overwritten.
     */
    public void WriteToFile(Round round, String aFileName) {
        String gameState = round.GameState(round);

        String serializeDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        final File path = new File(serializeDirectory);

        if (!path.exists()) {
            if (!path.mkdirs()) {
                Log.e("Error", "Failed to create directory");
                return;
            }
        }

        final File file = new File(path, aFileName);

        if (file.exists()) {
            Log.w("Warning", "File already exists. Decide what to do.");
            return;
        }

        try {
            if (file.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fileOutputStream);
                myOutWriter.append(gameState);
                myOutWriter.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                Log.i("Success", "File write successful");
            } else {
                Log.e("Error", "Failed to create the file");
            }
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}







