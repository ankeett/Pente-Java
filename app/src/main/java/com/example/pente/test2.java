package com.example.pente;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.GridLayout;

public class test2 extends AppCompatActivity {

    private GridLayout boardLayout;
    private GridView boardGridView;
    private ArrayAdapter<String> boardAdapter;
    private String[] boardData;
    private boolean isBlackPlayer = true; // Track the current player

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

                // Update the data in the adapter
                boardAdapter.notifyDataSetChanged();

                // Implement your game logic here:
                // - Check for win conditions
                // - Check for captures
                // - Switch players

                isBlackPlayer = !isBlackPlayer; // Switch players
                Toast.makeText(test2.this, "Position:"+ position, Toast.LENGTH_SHORT).show();
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
}
