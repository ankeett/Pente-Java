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
        Button buttonHelp = findViewById(R.id.button);
        Button buttonQuit = findViewById(R.id.button2);



        // Set an OnClickListener for the "Start a Game" button
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the StartGame activity
                Intent intent = new Intent(MainActivity.this, RoundPlay.class);

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

//

    public void LoadGame(View view) {

        AlertDialog.Builder LoadFileDialog = new AlertDialog.Builder(MainActivity.this);
        LoadFileDialog.setTitle("Select a saved game");

        //saveTestFile();

        String serializeDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Download";
//      "/storage/emulated/0/Download"
        final File directory = new File(serializeDirectory);
        File[] filesList = directory.listFiles();

        Vector<String> filesNames = new Vector<>();


//        //getting the names of the file and add them to the list of files_name if the file's extension is .txt
        for (File file : filesList) {
            String fileName = file.getName();
            if (fileName.endsWith(".txt")){
                filesNames.add(fileName);
            }
        }

        String[] FileNames = new String[filesNames.size()];
        FileNames = filesNames.toArray(FileNames);

        // displays the list of files and starts another activity when a file is chosen.
        LoadFileDialog.setItems(FileNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int these) {
                ListView select = ((AlertDialog) dialog).getListView();
                String fileName = (String) select.getAdapter().getItem(these);
                Intent intent = new Intent(MainActivity.this, RoundPlay.class);
                intent.putExtra("filepath", directory + "/" + fileName);
                intent.putExtra("gameType", "Load");
                //intent.putExtra("gameObject", mGameObject);
                startActivity(intent);
            }
        });
        LoadFileDialog.show();
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
