package com.example.pente.Model;

import java.util.Random;
import android.util.Pair;

import android.widget.Toast;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;


import com.example.pente.Model.Board;
public class Strategy {
    private Board board;
    private int playerSymbol;
    private Context context;


    public Strategy(Board originalBoard, int playerSymbol, Context context) {
        this.board = originalBoard.deepCopy(); // Create a deep copy of the original board
        this.playerSymbol = playerSymbol;
        this.context = context;

    }

    public Pair<Integer, Integer> findWinningMove() {
        for (int row = 1; row <= 19; row++) {
            for (int col = 0; col < 19; col++) {
                if (board.isEmptyCell(row, col)) {
                    board.setBoard(row, col, playerSymbol);
                    if (board.checkFive(row, col, playerSymbol)) {
                        board.setBoard(row, col, 0);
                        return new Pair<>(row, col);
                    }
                    board.setBoard(row, col, 0);
                }
            }
        }
        return new Pair<>(-1, -1);
    }

    public Pair<Integer, Integer> defendWinningMove() {
        int opponentSymbol = (playerSymbol == 1) ? 2 : 1;
        for (int row = 1; row <= 19; row++) {
            for (int col = 0; col < 19; col++) {
                if (board.isEmptyCell(row, col)) {
                    board.setBoard(row, col, opponentSymbol);
                    if (board.checkFive(row, col, opponentSymbol)) {
                        board.setBoard(row, col, 0);
                        return new Pair<>(row, col);
                    }
                    board.setBoard(row, col, 0);
                }
            }
        }
        return new Pair<>(-1, -1);
    }



        public Pair<Integer, Integer> captureOpponent() {
            int opponentSymbol = (playerSymbol == 1) ? 2 : 1;

            // Keep track of the number of opponent stones that can be captured from a position
            // Return the position that can capture the most number of opponent stones
            // Keep the pair and the number of opponent stones that can be captured in a map

            Map<Pair<Integer, Integer>, Integer> captureMap = new HashMap<>();

            // Iterate through the entire game board
            for (int row = 1; row <= 19; row++) {
                for (int col = 0; col < 19; col++) {
                    // Check if the current cell is empty
                    if (board.isEmptyCell(row, col)) {
                        // Simulate placing the player's stone in the current empty cell
                        board.setBoard(row, col, playerSymbol);

                        // Check for five-in-a-row with the simulated stone
                        while (board.checkCapture(row, col, playerSymbol)) {
                            // If a winning move is found, return the position
                            board.setBoard(row, col, 0); // Undo the simulation

                            // Increment the number of opponent stones that can be captured from this position
                            Pair<Integer, Integer> key = new Pair<>(row, col);
                            Integer value = captureMap.get(key);
                            if (value != null) {
                                // Key exists in the map, increment the value
                                captureMap.put(key, value + 1);
                            } else {
                                // Key doesn't exist, so create it with a value of 1
                                captureMap.put(key, 1);
                            }
                        }

                        // Undo the simulation by resetting the cell to empty
                        board.setBoard(row, col, 0);
                    } else {
                        continue;
                    }
                }
            }

            // Return the key with the highest value
            int max = -1;
            Pair<Integer, Integer> bestPosition = new Pair<>(-1, -1);
            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : captureMap.entrySet()) {
                Pair<Integer, Integer> key = entry.getKey();
                Integer value = entry.getValue();
                if (value > max) {
                    max = value;
                    bestPosition = key;
                }
            }

            return bestPosition;
        }



    public Pair<Integer, Integer> defendCapture() {
        int opponentSymbol = (playerSymbol == 1) ? 2 : 1;
        for (int row = 1; row <= 19; row++) {
            for (int col = 0; col < 19; col++) {
                if (board.isEmptyCell(row, col)) {
                    board.setBoard(row, col, opponentSymbol);
                    if (board.checkCapture(row, col, opponentSymbol)) {
                        board.setBoard(row, col, 0);
                        return new Pair<>(row, col);
                    }
                    board.setBoard(row, col, 0);
                }
            }
        }
        return new Pair<>(-1, -1);
    }



// ...

    public Pair<Integer, Integer> evaluateAllCases() {
        Pair<Integer, Integer> winningMove = findWinningMove();
        if (!winningMove.equals(new Pair<>(-1, -1))) {
            showToast(winningMove, "winning move");
            return winningMove;
        }

        Pair<Integer, Integer> defendingWin = defendWinningMove();
        if (!defendingWin.equals(new Pair<>(-1, -1))) {
            showToast(defendingWin, "defending win");
            return defendingWin;
        }

        Pair<Integer, Integer> defendingFour = defendFour();
        if (!defendingFour.equals(new Pair<>(-1, -1))) {
            showToast(defendingFour, "defending four");
            return defendingFour;
        }

        Pair<Integer, Integer> capturingOpponent = captureOpponent();
        if (!capturingOpponent.equals(new Pair<>(-1, -1))) {
            showToast(capturingOpponent, "capturing opponent");
            return capturingOpponent;
        }

        Pair<Integer, Integer> defendingCapture = defendCapture();
        if (!defendingCapture.equals(new Pair<>(-1, -1))) {
            showToast(defendingCapture, "defending capture");
            return defendingCapture;
        }

        Pair<Integer, Integer> maxConsecutivePos = maxConsecutive();
        if (!maxConsecutivePos.equals(new Pair<>(-1, -1))) {
            showToast(maxConsecutivePos, "max consecutive");
            return maxConsecutivePos;
        }

        Pair<Integer, Integer> centerPos = controlCenter();
        if (!centerPos.equals(new Pair<>(-1, -1))) {
            showToast(centerPos, "center");
            return centerPos;
        }

        showToast(new Pair<>(-1, -1), "random move");
        return randomMove();
    }

    // Updated helper method to display a toast with the Pair and message
    private void showToast(Pair<Integer, Integer> pair, String message) {
        String toastMessage = "Pair: (" + pair.first + ", " + pair.second + ") - " + message;
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }



    public Pair<Integer, Integer> randomMove() {
        int row, col;
        Random random = new Random();
        do {
            row = random.nextInt(19) + 1;
            col = random.nextInt(19);
        } while (!board.isEmptyCell(row, col));
        return new Pair<>(row, col);
    }

    public Pair<Integer, Integer> defendFour() {
        int opponentSymbol = (playerSymbol == 1) ? 2 : 1;
        for (int row = 1; row <= 19; row++) {
            for (int col = 0; col < 19; col++) {
                if (board.isEmptyCell(row, col)) {
                    board.setBoard(row, col, opponentSymbol);
                    if (board.checkFour(row, col, opponentSymbol)) {
                        board.setBoard(row, col, 0);
                        return new Pair<>(row, col);
                    }
                    board.setBoard(row, col, 0);
                }
            }
        }
        return new Pair<>(-1, -1);
    }

    public Pair<Integer, Integer> maxConsecutive() {
        int maxConsecutiveCount = -1;
        Pair<Integer, Integer> bestPosition = new Pair<>(-1, -1);

        for (int row = 1; row < 19; row++) {
            for (int col = 0; col < 19; col++) {
                if (board.isEmptyCell(row, col)) {
                    int consecutiveCount = board.calculateConsecutiveCount(row - 1, col, playerSymbol);
                    if (consecutiveCount > maxConsecutiveCount) {
                        maxConsecutiveCount = consecutiveCount;
                        bestPosition = new Pair<>(row, col);
                    }
                }
            }
        }

        if (maxConsecutiveCount <= 0) {
            return new Pair<>(-1, -1);
        }

        return bestPosition;
    }

    public Pair<Integer, Integer> controlCenter() {
        int centerRow = 9;
        int centerCol = 9;

        if (board.isEmptyCell(centerRow, centerCol)) {
            return new Pair<>(centerRow, centerCol);
        }

        for (int radius = 1; radius < 5; radius++) {
            for (int dr = -radius; dr <= radius; dr++) {
                for (int dc = -radius; dc <= radius; dc++) {
                    int newRow = centerRow + dr;
                    int newCol = centerCol + dc;

                    if (newRow >= 0 && newRow < 19 && newCol >= 0 && newCol < 19 &&
                            board.isEmptyCell(newRow, newCol)) {
                        return new Pair<>(newRow, newCol);
                    }
                }
            }
        }

        return new Pair<>(-1, -1);
    }
}
