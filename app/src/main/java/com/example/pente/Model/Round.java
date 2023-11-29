package com.example.pente.Model;

import java.util.NoSuchElementException;
import java.util.Scanner;
import android.content.Context;
public class Round {

    /**
     * private data members
     */
    private Board B = new Board();
    private Player[] playerList = new Player[2];

    private int humanScore = 0;
    private int computerScore = 0;
    private int tournamentHumanScore = 0;
    private int tournamentComputerScore = 0;

    private int winner = 0;
    private char humanColor = 'W';
    private boolean quit = false;
    private int currentPlayerIndex = 0;

    private Context context;

    /**
     * Resets the scores and winner status in the game.
     */
    public void reset() {
        humanScore = 0;
        computerScore = 0;
        winner = 0;
    }

    /**
     * Switches the player's turn to the human player's round.
     */
    public void humanRound(){
        if(getHumanColor() == 'W'){
            //do nothing
        }
        else{
            swapPlayers();
            setHumanColor('W');
        }
    }
    /**
     * Switches the player's turn to the computer player's round.
     */

    public void computerRound(){
        if(getHumanColor() == 'W'){
            swapPlayers();
            setHumanColor('B');
        }
        else{
           //do nothing
        }
    }




    /**
     * Initializes a round with two players.
     * @param player1 The first player.
     * @param player2 The second player.
     */
    public Round(Player player1, Player player2) {
        playerList[0] = player1;
        playerList[1] = player2;
    }


    /**
     * setters and getters
     */
    public int getHumanScore() {
        return humanScore;
    }

    public int getComputerScore() {
        return computerScore;
    }

    public void setHumanScore(int score) {
        humanScore = score;
    }

    public void setComputerScore(int score) {
        computerScore = score;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }

    public boolean quitTournament() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public void setHumanColor(char color) {
        humanColor = color;
    }

    public char getHumanColor() {
        return humanColor;
    }

    public void setCurrentPlayerIndex(int index) {
        currentPlayerIndex = index;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Board getBoard() {
        return B;
    }

    public void setBoard(Board board){
        B = board;
    }

    public int getHumanCapture() {
        return B.getHumanCaptures();
    }

    public int getComputerCapture() {
        return B.getComputerCaptures();
    }

    public int getTournamentHumanScore() {
        return tournamentHumanScore;
    }

    public int getTournamentComputerScore() {
        return tournamentComputerScore;
    }

    public void setTournamentHumanScore(int score) {
        tournamentHumanScore = score;
    }

    public void setTournamentComputerScore(int score) {
        tournamentComputerScore = score;
    }

    public Player getCurrentPlayer(){
        return playerList[currentPlayerIndex];
    }

    public void switchTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerList.length;
    }

    /**
     * Swaps the positions of the two players in the player list and updates their symbols.
     */
    public void swapPlayers(){
        Player temp = playerList[0];
        playerList[0] = playerList[1];
        playerList[1] = temp;

        playerList[0].setSymbol('W');
        playerList[1].setSymbol('B');
    }









    /**
     * Calculates and updates the scores based on the current state of the game board.
     * @param B The current game board.
     */
    public void calculateScores(Board B) {
        int humanFour = B.countFour(1);
        int computerFour = B.countFour(2);

        setHumanScore(B.getHumanCaptures());
        setComputerScore(B.getComputerCaptures());

        if (B.getWinner() != 0) {
            if (B.getWinner() == 1) {
                setWinner(1);
                setHumanScore(getHumanScore() + 5);
                humanFour--;
            } else {
                setWinner(2);
                setComputerScore(getComputerScore() + 5);
                computerFour--;
            }
        }

        setHumanScore(getHumanScore() + humanFour);
        setComputerScore(getComputerScore() + computerFour);
    }


    /**
     * Generates a string representing the current state of the game.
     * @param round The current round of the game.
     * @return A string containing the board state, scores, and next player information.
     */
    public String GameState(Round round) {

        StringBuilder result = new StringBuilder();

        result.append("Board:\n");

        for (int row = 1; row <= 19; row++) {
            for (int col = 0; col < 19; col++) {
                int value = round.getBoard().getBoard(row, col);
                char symbol;

                if (value == 1) {
                    symbol = (round.getHumanColor() == 'W') ? 'W' : 'B';
                } else if (value == 2) {
                    symbol = (round.getHumanColor() == 'B') ? 'W' : 'B';
                } else {
                    symbol = 'O';
                }

                result.append(symbol);
            }

            result.append("\n");
        }

        result.append("Human:\n");
        result.append("Captured pairs: ").append(round.getBoard().getHumanCaptures()).append("\n");
        result.append("Score: ").append(round.getTournamentHumanScore()).append("\n\n");

        result.append("Computer:\n");
        result.append("Captured pairs: ").append(round.getBoard().getComputerCaptures()).append("\n");
        result.append("Score: ").append(round.getTournamentComputerScore()).append("\n\n");

        String color = round.getHumanColor() == 'W' ? "White" : "Black";
        result.append("Next Player: ").append("Human").append(" - ").append(color).append("\n");

        return result.toString();
    }

}

