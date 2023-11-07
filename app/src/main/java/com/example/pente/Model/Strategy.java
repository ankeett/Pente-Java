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
        for (int row = 1; row <= 19; row++) {
            for (int col = 0; col < 19; col++) {
                if (board.isEmptyCell(row, col)) {
                    board.setBoard(row, col, playerSymbol);
                    if (board.checkCapture(row, col, playerSymbol)) {
                        board.setBoard(row, col, 0);
                        return new Pair<>(row, col);
                    }
                    board.setBoard(row, col, 0);
                }
            }
        }
        return new Pair<>(-1, -1);
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
            //showToast(winningMove, "winning move");
            return winningMove;
        }

        Pair<Integer, Integer> defendingWin = defendWinningMove();
        if (!defendingWin.equals(new Pair<>(-1, -1))) {
            //showToast(defendingWin, "defending win");
            return defendingWin;
        }

        Pair<Integer, Integer> defendingFour = defendFour();
        if (!defendingFour.equals(new Pair<>(-1, -1))) {
            //showToast(defendingFour, "defending four");
            return defendingFour;
        }

        Pair<Integer, Integer> capturingOpponent = captureOpponent();
        if (!capturingOpponent.equals(new Pair<>(-1, -1))) {
            //showToast(capturingOpponent, "capturing opponent");
            return capturingOpponent;
        }

        Pair<Integer, Integer> defendingCapture = defendCapture();
        if (!defendingCapture.equals(new Pair<>(-1, -1))) {
            //showToast(defendingCapture, "defending capture");
            return defendingCapture;
        }

        Pair<Integer, Integer> maxConsecutivePos = maxConsecutive();
        if (!maxConsecutivePos.equals(new Pair<>(-1, -1))) {
            //showToast(maxConsecutivePos, "max consecutive");
            return maxConsecutivePos;
        }

        Pair<Integer, Integer> centerPos = controlCenter();
        if (!centerPos.equals(new Pair<>(-1, -1))) {
            //showToast(centerPos, "center");
            return centerPos;
        }

        //showToast(new Pair<>(-1, -1), "random move");
        return randomMove();
    }

    // Updated helper method to display a toast with the Pair and message
//    private void showToast(Pair<Integer, Integer> pair, String message) {
//        String toastMessage = "Pair: (" + pair.first + ", " + pair.second + ") - " + message;
//        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
//    }



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
