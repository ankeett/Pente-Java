package com.example.pente.Model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Serialization {

    /**
     * private data members
     */
    private Board B;
    private char humanColor = 'W';
    private char computerColor = 'B';
    private int humanCaptures = 0;
    private int computerCaptures = 0;
    private int humanScore = 0;
    private int computerScore = 0;
    private String nextPlayer = "Human";

    /**
     * constructor for Serialization class
     * @param board
     */

    public Serialization(Board board) {
        B = board;
    }


    /**
     * setters and getters
     */
    public void setHumanColor(char stone) {
        humanColor = stone;
    }

    // Getter for humanColor
    public char getHumanColor() {
        return humanColor;
    }

    // Setter for computerColor
    public void setComputerColor(char stone) {
        computerColor = stone;
    }

    // Getter for computerColor
    public char getComputerColor() {
        return computerColor;
    }

    // Setter for humanCaptures
    public void setHumanCaptures(int captures) {
        humanCaptures = captures;
    }

    // Getter for humanCaptures
    public int getHumanCaptures() {
        return humanCaptures;
    }

    // Setter for computerCaptures
    public void setComputerCaptures(int captures) {
        computerCaptures = captures;
    }

    // Getter for computerCaptures
    public int getComputerCaptures() {
        return computerCaptures;
    }

    // Setter for humanScore
    public void setHumanScore(int score) {
        humanScore = score;
    }

    // Getter for humanScore
    public int getHumanScore() {
        return humanScore;
    }

    // Setter for computerScore
    public void setComputerScore(int score) {
        computerScore = score;
    }

    // Getter for computerScore
    public int getComputerScore() {
        return computerScore;
    }

    // Setter for nextPlayer
    public void setNextPlayer(String player) {
        nextPlayer = player;
    }

    // Getter for nextPlayer
    public String getNextPlayer() {
        return nextPlayer;
    }



    /**
     * Reads the board state and player information from a file and updates the game board and player scores.
     * @param B The game board to be updated.
     * @param fileName The name of the file containing the board state and player information.
     */
    public void readBoard(Board B,String fileName) {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)) ){
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                findColor(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int row = 1;
            boolean isBoard = false;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Board:")) {
                    isBoard = true;
                    continue;
                }

                if (isBoard) {
                    if (row < 19) {
                        if (line.length() != 19) {
                            System.out.println("Invalid board state");
                            System.exit(1);
                        }
                    }

                    for (int col = 0; col < 19; col++) {
                        char symbol = line.charAt(col);
                        int intValue;

                        if (symbol == 'W') {
                            if (getHumanColor() == 'W') {
                                intValue = 1;
                            } else {
                                intValue = 2;
                            }
                        } else if (symbol == 'B') {
                            if (getHumanColor() == 'B') {
                                intValue = 1;
                            } else{
                                intValue = 2;
                            }
                        }else {
                            intValue = 0;
                        }


                        B.setBoard(row, col, intValue);
                    }
                    row++;

                    if (row >= 20) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            Log.e("Error", "Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }

        int humanCaptures = 0;
        int humanScore = 0;
        int computerCaptures = 0;
        int computerScore = 0;
        boolean readingPlayerInfo = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Human:")) {
                    readingPlayerInfo = true;
                } else if (line.contains("Computer:")) {
                    readingPlayerInfo = false;
                } else if (readingPlayerInfo) {
                    if (line.contains("Captured pairs:")) {
                        humanCaptures = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    } else if (line.contains("Score:")) {
                        humanScore = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    }
                } else {
                    if (line.contains("Captured pairs:")) {
                        computerCaptures = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    } else if (line.contains("Score:")) {
                        computerScore = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    }
                }
            }
        } catch (IOException e) {
            Log.d("Next","herererer");
            Log.e("Error", "Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }

        setHumanCaptures(humanCaptures);
        setComputerCaptures(computerCaptures);
        setHumanScore(humanScore);
        setComputerScore(computerScore);

        B.printBoard(getHumanColor());
    }

    /**
     * Parses the player information from a line and sets the next player and player colors.
     * @param line The line containing player information.
     */
    public void findColor(String line) {
        int pos = line.indexOf("Next Player:");
        String playerPair = "1-1";

        if (pos != -1) {
            String remaining = line.substring(pos + 12); // 12 is the length of "Next Player:"
            int hyphenPos = remaining.indexOf('-');

            if (hyphenPos != -1) {
                String player1 = remaining.substring(0, hyphenPos).trim();
                String player2 = remaining.substring(hyphenPos + 1).trim();

                playerPair = player1 + "-" + player2;
            }
        }

        if (!playerPair.equals("1-1")) {
            setNextPlayer(playerPair.split("-")[0].trim());

            if (playerPair.split("-")[0].trim().equals("Human")) {
                if (playerPair.split("-")[1].trim().equals("White")) {
                    setHumanColor('W');
                    setComputerColor('B');
                } else {
                    setHumanColor('B');
                    setComputerColor('W');
                }
            } else {
                if (playerPair.split("-")[1].trim().equals("White")) {
                    setComputerColor('W');
                    setHumanColor('B');
                } else {
                    setComputerColor('B');
                    setHumanColor('W');
                }
            }
        }
    }


}



