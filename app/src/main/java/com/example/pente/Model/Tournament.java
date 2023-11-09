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
    private Player[] playerList;
    private ArrayList<Round> games;
    private int humanScores;
    private int computerScores;
    private int lastWinner;

    private Context context;

    private char humanColor = 'W';


    public char getHumanColor(){
        return humanColor;
    }

    public Tournament() {
        playerList = new Player[2];
        playerList[0] = new HumanPlayer('W',context);
        playerList[1] = new ComputerPlayer('B',context);
        games = new ArrayList<>();
    }


    public int tossCoin() {
        Random random = new Random();
        int toss = random.nextInt(2) + 1;
        return toss;
    }

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


    public void run() {
        System.out.println("Welcome to Pente");

        int option = 0;

//        do {
//            System.out.println("Choose an option:");
//            System.out.println(" 1. Start the Game");
//            System.out.println(" 2. Continue the Game");
//
//            Scanner scanner = new Scanner(System.in);
//
//            if (scanner.hasNextInt()) {
//                option = scanner.nextInt();
//                if (option == 1) {
//                    startGame();
//                } else if (option == 2) {
//                    continueGame();
//                } else {
//                    System.out.println("Invalid option. Please choose 1 or 2.");
//                }
//            } else {
//                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
//                // Ignore any remaining input
//                scanner.nextLine();
//            }
//        } while (option != 1 && option != 2);
    }



    public void continueGame() {
        Scanner scanner = new Scanner(System.in);
        boolean isFirstGame = true;

        while (true) {
            Round game = new Round(playerList[0], playerList[1]);

            if (isFirstGame) {
                game.continueMenu();
                setHumanScores(game.getTournamentHumanScore());
                setComputerScores(game.getTournamentComputerScore());
                isFirstGame = false;
                setLastWinner(game.getHumanColor() == 'W' ? 1 : 2);
            } else {
                game.setHumanColor(getLastWinner() == 1 ? 'W' : 'B');
                game.startMenu();
            }

            if (game.quitTournament()) {
                System.out.println("Do you want to serialize the game? (y/n)");
                //Scanner scanner = new Scanner(System.in);
                String answer = scanner.next().toLowerCase();

                if (answer.equals("y")) {
                    //serializeGame(game);
                }

                break;
            }

            setHumanScores(getHumanScores() + game.getHumanScore());
            setComputerScores(getComputerScores() + game.getComputerScore());
            games.add(game);

            System.out.println("----Tournament Scores----");
            System.out.println("Your score: " + getHumanScores());
            System.out.println("Computer score: " + getComputerScores());

            System.out.println("Do you want to continue? (y/n)");
            //Scanner scanner = new Scanner(System.in);
            String choice = scanner.next().toLowerCase();

            if (!choice.equals("y")) {
                break;
            }

            if (getLastWinner() != game.getWinner()) {
                Player temp = playerList[0];
                playerList[0] = playerList[1];
                playerList[1] = temp;
            }

            playerList[0].setSymbol('W');
            playerList[1].setSymbol('B');
        }

        announceWinner();
    }

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

    public Player[] startGame(String toss) {
        //int toss = 0;
        System.out.println("Toss. Choose 1 for Head and 2 for Tails: ");
        Scanner scanner = new Scanner(System.in);

//        do {
//            if (scanner.hasNextInt()) {
//                toss = scanner.nextInt();
//                if (toss != 1 && toss != 2) {
//                    System.out.println("Invalid option. Please choose 1 for Head or 2 for Tails.");
//                }
//            } else {
//                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
//                scanner.next();
//            }
//        } while (toss != 1 && toss != 2);

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




//        while (true) {
//            Round game = new Round(playerList[0], playerList[1]);
//            game.setHumanColor(getLastWinner() == 1 ? 'W' : 'B');
//            game.startMenu();
//
//            if (game.quitTournament()) {
//                System.out.println("Do you want to serialize the game? (y/n)");
//                String answer = scanner.next().toLowerCase();
//
//                if (answer.equals("y")) {
//                    serializeGame(game);
//                }
//
//                break;
//            }
//
//            setHumanScores(getHumanScores() + game.getHumanScore());
//            setComputerScores(getComputerScores() + game.getComputerScore());
//            games.add(game);
//
//            System.out.println("----Tournament Scores----");
//            System.out.println("Your score: " + getHumanScores());
//            System.out.println("Computer score: " + getComputerScores());
//
//            System.out.println("Do you want to continue? (y/n)");
//            String choice = scanner.next().toLowerCase();
//
//            if (!choice.equals("y")) {
//                break;
//            }
//
//            if (getLastWinner() != game.getWinner()) {
//                Player temp = playerList[0];
//                playerList[0] = playerList[1];
//                playerList[1] = temp;
//            }
//
//            playerList[0].setSymbol('W');
//            playerList[1].setSymbol('B');
//
//            if (getHumanScores() > getComputerScores()) {
//                setLastWinner(1);
//            } else if (getHumanScores() < getComputerScores()) {
//                setLastWinner(2);
//            } else {
//                System.out.println("Toss. Choose 0 for Head and 1 for Tails: ");
////                toss = scanner.nextInt();
////
////                if (toss == tossCoin()) {
////                    setLastWinner(1);
////                } else {
////                    setLastWinner(2);
////                }
//            }
//        }
//
//        announceWinner();
//        scanner.close();
    }

    public void calculateTournamentScore(Round round){
        setHumanScores(getHumanScores() + round.getHumanScore());
        setComputerScores(getComputerScores() + round.getComputerScore());

        System.out.println("----Tournament Scores----");
        System.out.println("Your score: " + getHumanScores());
        System.out.println("Computer score: " + getComputerScores());


    }


    public void announceWinner() {
        System.out.println("-----------------------------------");
        System.out.println("Tournament Results");
        if (getHumanScores() > getComputerScores()) {
            System.out.println("Congratulations! You won the tournament!");
        } else if (getHumanScores() < getComputerScores()) {
            System.out.println("Sorry! You lost the tournament!");
        } else {
            System.out.println("It's a tie!");
        }
    }


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


