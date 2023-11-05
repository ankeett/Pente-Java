package com.example.pente.View;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.GridLayout;

import android.util.Log;

import com.example.pente.Model.Player;
import com.example.pente.R;

import com.example.pente.Model.Board;
import com.example.pente.Model.ComputerPlayer;

public class test2 extends AppCompatActivity {

    private Board board = new Board();

    private GridLayout boardLayout;
    private GridView boardGridView;
    private ArrayAdapter<String> boardAdapter;
    private String[] boardData;
    private boolean isBlackPlayer = true; // Track the current player

    private ComputerPlayer computerPlayer = new ComputerPlayer('W', test2.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        // Initialize the board grid view
        boardLayout = findViewById(R.id.boardLayout);
        boardGridView = findViewById(R.id.boardGridView);

        // Add column labels (A to S) with a space
        for (char colLabel = 'A'; colLabel <= 'S'; colLabel++) {
            addLabel(String.valueOf(colLabel), Gravity.CENTER, 14);
        }


        initBoard();

        boardGridView.setOnItemClickListener((parent, view, position, id) -> {
            // Check if the cell is already populated
            if (boardData[position].isEmpty()) {
                // Place a stone or populate the cell
                if (isBlackPlayer) {
                    boardData[position] = "⚫";
                } else {
                    boardData[position] = "⚪";
                }
                int boardSize = 19; // The size of the board

                int row = position / boardSize; // Calculate the row
                int col = position % boardSize; // Calculate the column


                char colChar = (char) ('A' + col);
                String move = colChar + Integer.toString(19 - row);
                //Toast.makeText(test2.this, "Position:" + move, Toast.LENGTH_SHORT).show();
                if (isBlackPlayer) {
                    board.placeStone(move, 'H');
                }
                else{
                    board.placeStone(move, 'C');
                }

                // Implement your game logic here:
                // - Check for win conditions
                // - Check for captures
                // - Switch players
                if(isBlackPlayer){
                    if(board.checkFive(row,col,1)){
                        Toast.makeText(test2.this, "Five in a row", Toast.LENGTH_SHORT).show();

                    }
                    while (board.checkCapture(row, col, 1)) {
                        //System.out.println("You captured a stone!");
                        Toast.makeText(test2.this, "You captured a stone!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if(board.checkFive(row,col,2)){
                        Toast.makeText(test2.this, "Five in a row", Toast.LENGTH_SHORT).show();
                    }

                    while (board.checkCapture(row, col, 2)) {
                        //System.out.println("You captured a stone!");
                        Toast.makeText(test2.this, "You captured a stone!", Toast.LENGTH_SHORT).show();
                    }
                }

                // Update the data in the adapter
                boardAdapter.notifyDataSetChanged();

                updateView();

                //computer making a move
                computerPlayer.makeMove(board,5);

                Log.d("myTag", board.printBoard('W'));


                // Update the data in the adapter
                boardAdapter.notifyDataSetChanged();

                updateView();

                //isBlackPlayer = !isBlackPlayer; // Switch players

            } else {
                // Cell is already populated, handle this case if needed
                Toast.makeText(test2.this, "Cell is already populated.", Toast.LENGTH_SHORT).show();
            }

        });


    }

    // Method to add labels
    private void addLabel(String label, int gravity, int spacingInPixels) {
        TextView labelView = new TextView(this);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.setMargins(spacingInPixels, 0, spacingInPixels, 0); // Left and right margin for spacing
        labelView.setLayoutParams(layoutParams);
        labelView.setText(label);
        labelView.setGravity(gravity);
        boardLayout.addView(labelView);
    }


    private void initBoard() {
        boardData = new String[361]; // 19x19 grid
        for (int i = 0; i < 361; i++) {
            boardData[i] = "";
        }

        // Create an adapter and set it to the grid view
        boardAdapter = new ArrayAdapter<>(this, R.layout.board_item, R.id.itemTextView, boardData);
        boardGridView.setAdapter(boardAdapter);
    }

    // Update the view based on the model data
    private void updateView() {
        // Create a mapping for your integer values to their corresponding string representations
        String[] mapping = {"", "⚫", "⚪"};

        // Iterate through the 2D board array and update the boardData array
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                int cellValue = board.getBoard(i+1,j);
                boardData[i * 19 + j] = mapping[cellValue];
            }
        }

        // Notify the adapter to update the GridView
        boardAdapter.notifyDataSetChanged();
    }


}
