package com.example.pente;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference the buttons using their IDs
        Button buttonStartGame = findViewById(R.id.button3);
        Button buttonLoadGame = findViewById(R.id.button4);
        Button buttonHelp = findViewById(R.id.button);
        Button buttonQuit = findViewById(R.id.button2);

        // Set an OnClickListener for the "Start a Game" button
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the StartGame activity
                Intent intent = new Intent(MainActivity.this, test2.class);

                // Start the StartGame activity
                startActivity(intent);
            }
        });

        buttonLoadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the action to be taken when the "Load a Game" button is clicked.
                // For example, you can load a saved game or navigate to a game loading screen.
                // Replace the following line with your desired action.
                // Example: loadSavedGame();
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the action to be taken when the "Help" button is clicked.
                // For example, you can show a help dialog or navigate to a help screen.
                // Replace the following line with your desired action.
                // Example: showHelpDialog();
            }
        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the action to be taken when the "Quit" button is clicked.
                // For example, you can close the app or show a confirmation dialog.
                // Replace the following line with your desired action.
                // Example: finish();
            }
        });
    }
}
