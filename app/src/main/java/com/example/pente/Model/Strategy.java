package com.example.pente.Model;

import java.util.Random;
import android.util.Pair;

import android.widget.Toast;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;


import com.example.pente.Model.Board;
public class Strategy {

    /**
     * private data members
     */
    private Board board;
    private int playerSymbol;
    private Context context;



    /**
     * Initializes a new instance of the Strategy class.
     * @param originalBoard The original game board to create a deep copy from.
     * @param playerSymbol The symbol representing the player (1 for Human, 2 for Computer).
     * @param context The context of the game.
     */
    public Strategy(Board originalBoard, int playerSymbol, Context context) {
        this.board = originalBoard.deepCopy(); // Create a deep copy of the original board
        this.playerSymbol = playerSymbol;
        this.context = context;

    }

    /**
     * Finds a winning move for the current player on the current game board.
     * @return A Pair object representing the row and column of a winning move, or (-1, -1) if no winning move is found.
     */
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

    /**
     * Defends against a potential winning move by the opponent on the current game board.
     * @return A Pair object representing the row and column to defend, or (-1, -1) if no defending move is needed.
     */
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


    /**
     * Finds a position to capture the maximum number of opponent stones on the current game board.
     * @return A Pair object representing the row and column to place a stone for capturing, or (-1, -1) if no capturing move is found.
     */
    public Pair<Integer, Integer> captureOpponent() {

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
                        // If winning move is found, return the position
                        board.setBoard(row, col, 0); // Undo the simulation

                        // Increment the number of opponent stones that can be captured from this position
                        Pair<Integer, Integer> position = new Pair<>(row, col);
                        captureMap.put(position, captureMap.getOrDefault(position, 0) + 1);

                        // Return new Pair<>(row, col);
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
            if (entry.getValue() > max) {
                max = entry.getValue();
                bestPosition = entry.getKey();
            }
        }

        return bestPosition;
    }




    /**
     * Finds a defensive move to prevent the opponent from capturing stones on the current game board.
     * @return A Pair object representing the row and column to place a defensive stone, or (-1, -1) if no defensive move is found.
     */
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



    /**
     * Provides reasoning for the next move based on different game situations.
     * @return A string indicating the recommended move reasoning.
     */
    public String evaluateAllCasesReasoning(){
        Pair<Integer, Integer> winningMove = findWinningMove();
        if (!winningMove.equals(new Pair<>(-1, -1))) {
            //showToast(winningMove, "winning move");
            return "make a winning move.";
        }

        Pair<Integer, Integer> defendingWin = defendWinningMove();
        if (!defendingWin.equals(new Pair<>(-1, -1))) {
            //showToast(defendingWin, "defending win");
            return "defending the winning move.";
        }

        Pair<Integer, Integer> defendingFour = defendFour();
        if (!defendingFour.equals(new Pair<>(-1, -1))) {
            //showToast(defendingFour, "defending four");
            return "defending making four.";
        }

        Pair<Integer, Integer> capturingOpponent = captureOpponent();
        if (!capturingOpponent.equals(new Pair<>(-1, -1))) {
            //showToast(capturingOpponent, "capturing opponent");
            return "capturing the opponent.";
        }

        Pair<Integer, Integer> defendingCapture = defendCapture();
        if (!defendingCapture.equals(new Pair<>(-1, -1))) {
            //showToast(defendingCapture, "defending capture");
            return "defending capture.";
        }

        Pair<Integer, Integer> maxConsecutivePos = maxConsecutive();
        if (!maxConsecutivePos.equals(new Pair<>(-1, -1))) {
            //showToast(maxConsecutivePos, "max consecutive");
            return "make max consecutive.";
        }

        Pair<Integer, Integer> centerPos = controlCenter();
        if (!centerPos.equals(new Pair<>(-1, -1))) {
            //showToast(centerPos, "center");
            return "control the center of the board.";
        }

        //showToast(new Pair<>(-1, -1), "random move");
        return "Random Move";

    }

    /**
     * Evaluates different game situations and recommends the next move.
     * Prioritizes winning moves, defensive strategies, capturing opportunities,
     * and controlling the center of the board.
     *
     * @return A pair of integers representing the recommended row and column for the next move.
     */
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



    /**
     * Generates a random legal move on the game board.
     *
     * @return A pair of integers representing the randomly selected row and column for the next move.
     */
    public Pair<Integer, Integer> randomMove() {
        int row, col;
        Random random = new Random();
        do {
            row = random.nextInt(19) + 1;
            col = random.nextInt(19);
        } while (!board.isEmptyCell(row, col));
        return new Pair<>(row, col);
    }

    /**
     * Defends against an opponent's potential four-in-a-row by finding a defensive move.
     *
     * @return A pair of integers representing the row and column of a defensive move, or (-1, -1) if no defensive move is needed.
     */
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

    /**
     * Finds the position that maximizes the consecutive stone count for the current player.
     *
     * @return A pair of integers representing the row and column of the position with the maximum consecutive stone count,
     *         or (-1, -1) if no such position is found.
     */
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

    /**
     * Finds the position around the center of the board for the current player's move.
     *
     * The function first checks the exact center (9, 9) and then expands in concentric circles
     * until an empty cell is found.
     *
     * @return A pair of integers representing the row and column of the found position,
     *         or (-1, -1) if no such position is found.
     */
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

    /**
     * Evaluates the best move for the second turn of the game.
     *
     * The function searches for an empty cell that is exactly 3 intersections away from the center
     * (J10) but closer to the center. If no suitable position is found, a random move is returned.
     *
     * @return A pair of integers representing the row and column of the evaluated move,
     *         or (-1, -1) if no suitable position is found.
     */
    public Pair<Integer, Integer> evaluateSecondMove() {
        // Define the coordinates of the center of the board
        int centerRow = 9;
        int centerCol = 9;

        // Search for an empty cell that is exactly 3 steps away from the center (J10)
        for (int dr = -2; dr <= 2; dr++) {
            for (int dc = -2; dc <= 2; dc++) {
                int newRow = centerRow + dr;
                int newCol = centerCol + dc;

                // Check if the new position is within bounds, exactly 3 intersections away, and empty
                if (newRow >= 0 && newRow < 19 && newCol >= 0 && newCol < 19 &&
                        (dr * dr + dc * dc == 4) && board.isEmptyCell(newRow, newCol)) {
                    System.out.println("Reason: 3 intersections away but closer to the center of the board");
                    return new Pair<>(newRow, newCol);
                }
            }
        }

        // If no suitable position is found, return {-1, -1} to indicate no move is available
        return randomMove();
    }
}
