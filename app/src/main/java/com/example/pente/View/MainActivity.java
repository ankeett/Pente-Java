package com.example.pente.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;

import com.example.pente.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. Responsible for initializing the activity,
     * setting up the user interface, and defining event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down, this Bundle contains the data it most recently supplied
     *                           in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference the buttons using their IDs
        Button buttonStartGame = findViewById(R.id.button3);
        Button buttonLoadGame = findViewById(R.id.button4);

        // Set an OnClickListener for the "Start a Game" button
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the StartGame activity
                Intent intent = new Intent(MainActivity.this, CoinTossActivity.class);
                intent.putExtra("gameType", "Start");
                // Start the StartGame activity
                startActivity(intent);
            }
        });

        // Set an OnClickListener for the "Load a Game" button

        buttonLoadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a dialog to open a file picker
                LoadGame(view);
            }
        });

    }

    /**
     * Displays an AlertDialog for selecting a saved game. Lists available saved game files
     * in the specified directory and allows the user to choose one. Displays the content of
     * the selected file and provides options to either load the file and start a new activity
     * or cancel the operation.
     *
     * @param view The View that triggered the method, typically a button or other UI element.
     */
    public void LoadGame(View view) {
        AlertDialog.Builder loadFileDialog = new AlertDialog.Builder(MainActivity.this);
        loadFileDialog.setTitle("Select a saved game");

        String serializeDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        final File directory = new File(serializeDirectory);
        File[] filesList = directory.listFiles();

        Vector<String> filesNames = new Vector<>();

        for (File file : filesList) {
            String fileName = file.getName();
            if (fileName.endsWith(".txt")) {
                filesNames.add(fileName);
            }
        }

        String[] fileNamesArray = new String[filesNames.size()];
        fileNamesArray = filesNames.toArray(fileNamesArray);

        loadFileDialog.setItems(fileNamesArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView select = ((AlertDialog) dialog).getListView();
                String fileName = (String) select.getAdapter().getItem(which);

                // Read and display the content of the selected file
                String filePath = directory + "/" + fileName;
                String fileContent = readFileContent(filePath);

                // Show a dialog with the file content
                AlertDialog.Builder contentDialog = new AlertDialog.Builder(MainActivity.this);
                contentDialog.setTitle("File Content");
                contentDialog.setMessage(fileContent);
                contentDialog.setPositiveButton("Load", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Load the file and start a new activity
                        Intent intent = new Intent(MainActivity.this, RoundPlay.class);
                        intent.putExtra("filepath", filePath);
                        intent.putExtra("gameType", "Load");
                        startActivity(intent);
                    }
                });
                contentDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, just dismiss the dialog
                    }
                });
                contentDialog.show();
            }
        });

        loadFileDialog.show();
    }

    /**
     * Reads the content of a file specified by the provided file path.
     *
     * @param filePath The path to the file whose content needs to be read.
     * @return The content of the file as a string. If an error occurs during reading, returns
     * "Error reading file content."
     */
    private String readFileContent(String filePath) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file content";
        }
        return content.toString();
    }



}
