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

    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 123;

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
                Intent intent = new Intent(MainActivity.this, RoundPlay.class);
                intent.putExtra("gameType", "Start");
                // Start the StartGame activity
                startActivity(intent);
            }
        });

        buttonLoadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open a file picker
                LoadGame(view);
            }
        });

    }

//

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



    public void saveTestFile() {
        String serializeDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        File directory = new File(serializeDirectory);

        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        String fileName = "test_saved_game.txt"; // The name of your test file
        String fileContent = "This is a test saved game content."; // Content of the test file

        File file = new File(directory, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
