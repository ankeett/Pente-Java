package com.example.pente.Model;

import java.util.NoSuchElementException;
import java.util.Scanner;
import android.content.Context;
public class Round {
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

    public void reset() {
        checkPlayers();
        humanScore = 0;
        computerScore = 0;
        winner = 0;


    }

    public Round(Player player1, Player player2) {
        playerList[0] = player1;
        playerList[1] = player2;
    }

    public void startMenu() {
        System.out.println("----Round begins----");

        // Print the board with the column and row labels
        B.printBoard('W');
        startGame(B);

        // Set the move number
        B.setMoveCount(1);
    }

    public void continueMenu() {
        System.out.println("----Continue the Game----");
        //continueGame(B);
    }

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

    public void swapPlayers(){
        Player temp = playerList[0];
        playerList[0] = playerList[1];
        playerList[1] = temp;

        playerList[0].setSymbol('W');
        playerList[1].setSymbol('B');
    }

    public void checkPlayers(){
        System.out.println("I am here" + getWinner());

        if(getHumanColor() == 'W'){
            if(getWinner() == 1){

                //do nothing
            }
            else{
                System.out.println("before swapping");
                swapPlayers();
                setHumanColor('B');
            }
        }
        //human is black
        else{
            if(getWinner() == 1){
                System.out.println("before swapping");
                swapPlayers();
                setHumanColor('W');
            }
            else{
                //do nothing
            }

        }
    }



    public void startGame(Board B) {
        //Scanner scanner = new Scanner(System.in);

        Player currentPlayer = playerList[currentPlayerIndex];

        while (!B.isGameOver()) {
            currentPlayer.makeMove(B, B.getMoveCount());

            if (currentPlayer.getQuit()) {
                setQuit(true);
                B.setGameOver(true);
                break;  // Use break to exit the loop
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % 2;
            currentPlayer = playerList[currentPlayerIndex];

            B.setMoveCount(B.getMoveCount() + 1);

            System.out.println("Human Captures: " + B.getHumanCaptures());
            System.out.println("Computer Captures: " + B.getComputerCaptures());

            if (B.getHumanCaptures() >= 5 || B.getComputerCaptures() >= 5) {
                System.out.println("Game Over");
                if (B.getHumanCaptures() == 5) {
                    setWinner(1);
                    System.out.println("You win!");
                } else {
                    setWinner(2);
                    System.out.println("Computer wins");
                }
                B.setGameOver(true);
                break;  // Use break to exit the loop
            }

//            if (currentPlayer.getSymbol() != getHumanColor()) {
//                System.out.println("Before asking to quit");
//                System.out.println("Do you want to quit? (y/n)");
//
//                try {
//                    String response = scanner.nextLine().toUpperCase();
//                    // ... your code
//                    System.out.println("After asking to quit");
//                    if (response.equals("Y")) {
//                        System.out.println("Quitting the game");
//                        setQuit(true);
//                        B.setGameOver(true);
//                        break;  // Use break to exit the loop
//                    }
//                } catch (NoSuchElementException e) {
//                    e.printStackTrace();
//                }
//
//            }
        }

        // Don't close the scanner here to keep System.in open for further input
        calculateScores(B);
        printScores();
        //scanner.close();  // Close the scanner when you're done with input
    }



//    public void continueGame(Board B) {
//        Serialization s = new Serialization(B);
//        s.readBoard(B);
//
//        int moveCount = B.checkEmptyBoard();
//        B.setMoveCount(moveCount + 1);
//
//        setHumanColor(s.getHumanColor());
//
//        if (s.getHumanColor() == 'W') {
//            if (s.getNextPlayer().equals("Human")) {
//                setCurrentPlayerIndex(0);
//            } else {
//                setCurrentPlayerIndex(1);
//            }
//        } else {
//            Player temp = playerList[0];
//            playerList[0] = playerList[1];
//            playerList[1] = temp;
//
//            if (s.getNextPlayer().equals("Human")) {
//                setCurrentPlayerIndex(1);
//            } else {
//                setCurrentPlayerIndex(0);
//            }
//        }
//
//        playerList[0].setSymbol('W');
//        playerList[1].setSymbol('B');
//
//        B.setHumanCaptures(s.getHumanCaptures());
//        B.setComputerCaptures(s.getComputerCaptures());
//
//        setTournamentHumanScore(s.getHumanScore());
//        setTournamentComputerScore(s.getComputerScore());
//
//        startGame(B);
//    }

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

        System.out.println("Human 4 in a row: " + humanFour);
        System.out.println("Computer 4 in a row: " + computerFour);
        setHumanScore(getHumanScore() + humanFour);
        setComputerScore(getComputerScore() + computerFour);
    }

    public void printScores() {
        System.out.println("----Round Scores----");
        System.out.println("Human Score: " + getHumanScore());
        System.out.println("Computer Score: " + getComputerScore());
    }

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

