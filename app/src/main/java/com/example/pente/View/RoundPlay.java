package com.example.pente.View;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;



import com.example.pente.Model.Board;
import com.example.pente.Model.ComputerPlayer;
import com.example.pente.Model.HumanPlayer;
import com.example.pente.Model.Player;
import com.example.pente.Model.Round;
import com.example.pente.Model.Serialization;
import com.example.pente.Model.Tournament;
import com.example.pente.R;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundPlay extends AppCompatActivity {

    private Board board = new Board();

    private Tournament tournament;

    private Round round;

    private GridView boardGridView;
    private ArrayAdapter<String> boardAdapter;
    private String[] boardData;
    //private boolean isBlackPlayer = true;

    private AtomicInteger moveCount = new AtomicInteger(1);

    private boolean menuVisible = true;

    private Player[] players;

    private HumanPlayer humanPlayer;
    private ComputerPlayer computerPlayer;

    private TextView headerText;
    private TextView infoText;

    private boolean isHumanMove;

    private String gameType, filePath;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_play);

        boardGridView = findViewById(R.id.boardGridView);
        boardAdapter = new ArrayAdapter<>(this, R.layout.board_item, R.id.itemTextView);
        boardGridView.setAdapter(boardAdapter);

        boardData = new String[361];
        computerPlayer = new ComputerPlayer('W', this);
        humanPlayer = new HumanPlayer('B', this);

        //check the tournament scores and declare round, if equal call toss dialog which determines the round
        tournament = new Tournament();
        round = new Round(humanPlayer, computerPlayer);
        round.setBoard(board);

        initializeGridView();

//        // In the RoundView class, assuming you have a reference to the TextView
         headerText = findViewById(R.id.headerTextView);
//        headerText.setText("Human's Turn");
//
//        // In the RoundView class, assuming you have a reference to the TextView
         infoText = findViewById(R.id.infoTextView);

        // Set the initial visibility of headerText and infoText
        headerText.setVisibility(View.GONE);
        infoText.setVisibility(View.GONE);

        //infoText.setText("Human Move");
        setupGridViewClickListener();
        updateView();


        gameType = getIntent().getStringExtra("gameType");

        System.out.println(gameType);

        if(gameType.equals("Load")){
            Log.d("Load", "I'm here");
            filePath = getIntent().getStringExtra("filepath");
            loadGame();
        }


        TextView tossCoinText = findViewById(R.id.tossCoinText);
        Button headsButton = findViewById(R.id.headsButton);
        Button tailsButton = findViewById(R.id.tailsButton);
        Button helpMode = findViewById(R.id.helpButton);
        Button saveGame = findViewById(R.id.saveButton);

        helpMode.setOnClickListener(view -> {
            String bestMove = humanPlayer.helpMode(board, moveCount.get()); // Assuming helpMode returns the best move as a string

            // Create an AlertDialog to display the best move
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Best Move");
            alertDialogBuilder.setMessage("The best move is: " + bestMove);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // You can add any code here that should run when the user clicks "OK"
                    dialog.dismiss(); // Close the dialog
                }
            });

            // Show the AlertDialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        saveGame.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter the filename for the game.");

            final EditText userInput = new EditText(this);

            userInput.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(userInput);

            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Serialization serialization = new Serialization(board);
                    //serialization.writeIntoFile(, userInput.getText().toString());
                    round.setBoard(board);
                    System.out.println(board.printBoard('W'));
                    System.out.println(round.getBoard().printBoard('W'));
                    tournament.WriteToFile(round, userInput.getText().toString());
                    builder.setMessage("File saved successfully");
                    startActivity(new Intent(RoundPlay.this, MainActivity.class));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        });


        GridView boardGridView = findViewById(R.id.boardGridView);
        // Initialize boardGridView and other components

        // Initially, the menu is visible, so disable the board
        boardGridView.setEnabled(false);

        // Set up the click listeners for "Heads" and "Tails" buttons
        headsButton.setOnClickListener(view -> {
            // Hide the "Tails" button
            headsButton.setVisibility(View.GONE);
            tailsButton.setVisibility(View.GONE);
            // Hide the "Toss a Coin" text
            tossCoinText.setVisibility(View.GONE);

            players = tournament.startGame("Heads");
            round = new Round(players[0], players[1]);
            round.setHumanColor(tournament.getHumanColor());

            // Check if the human player plays as 'W' and display a dialog if true
            if (tournament.getHumanColor() == 'W') {
                tossDialog("You win the toss, you play White");
            }
            else{
                tossDialog("You lost the toss, you play Black!");
            }

            // Enable the board (make it clickable)
            boardGridView.setEnabled(true);

            updateView();

            // Set the initial visibility of headerText and infoText (assuming they are initially visible)
            headerText.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.VISIBLE);

            // Update the menu state
            menuVisible = false;
        });


        tailsButton.setOnClickListener(view -> {
            // Hide the "Heads" button
            headsButton.setVisibility(View.GONE);
            tailsButton.setVisibility(View.GONE);
            // Hide the "Toss a Coin" text
            tossCoinText.setVisibility(View.GONE);

            players = tournament.startGame("Tails");

            round = new Round(players[0], players[1]);
            round.setHumanColor(tournament.getHumanColor());

            //open a dialog
            // Check if the human player plays as 'W' and display a dialog if true
            if (tournament.getHumanColor() == 'W') {
                tossDialog("You win the toss, you play White");
            }
            else{
                tossDialog("You lost the toss, you play Black!");
            }

            // Enable the board (make it clickable)
            boardGridView.setEnabled(true);

            updateView();
            // Set the initial visibility of headerText and infoText (assuming they are initially visible)
            headerText.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.VISIBLE);

            // Update the menu state
            menuVisible = false;
        });


    }

    private void initializeGridView() {
        for (int i = 0; i < 361; i++) {
            boardData[i] = "";
        }

        boardAdapter = new ArrayAdapter<>(boardGridView.getContext(), R.layout.board_item, R.id.itemTextView, boardData);
        boardGridView.setAdapter(boardAdapter);
    }


    private void setupGridViewClickListener() {
        boardGridView.setOnItemClickListener((parent, view, position, id) -> {
            if (!board.isGameOver()) {
                System.out.println("position " + position);
                if (boardData[position].isEmpty()) {
                    Player currentPlayer = round.getCurrentPlayer();
                    currentPlayer.setPosition(position);
                    currentPlayer.makeMove(board, moveCount.getAndIncrement());

                    updateView();
                    boardAdapter.notifyDataSetChanged();

                    round.switchTurn(); // Switch to the next player's turn
                    // Update the header based on the current player

                    Player anotherPlayer = round.getCurrentPlayer();
                    anotherPlayer.setPosition(position);
                    anotherPlayer.makeMove(board, moveCount.getAndIncrement());
                    updateView();
                    boardAdapter.notifyDataSetChanged();
                    round.switchTurn();


                } else {
                    Toast.makeText(boardGridView.getContext(), "Cell is already populated.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // In your activity
                round.calculateScores(board);
                round.printScores();
                tournament.calculateTournamentScore(round);
                DialogHelper.showGameOverDialog(this, tournament, round, board,moveCount, this::updateView); // Pass the 'round' object
            }
        });
    }


    public void updateView() {
        String[] mapping = {"", round.getHumanColor() == 'B' ? "⚫" : "⚪", round.getHumanColor() == 'B' ? "⚪" : "⚫"};

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                int cellValue = board.getBoard(i + 1, j);
                boardData[i * 19 + j] = mapping[cellValue];
            }
        }


        String message = "Human Color: " + (round.getHumanColor() == 'B' ? "⚫" : "⚪") +
                "\n Computer Color: " + (round.getHumanColor() == 'B' ? "⚪" : "⚫") + "\n";

        headerText.setText(message);

        String infoMessage = "\nHuman Captures: " + board.getHumanCaptures() + "\n" +
                "Computer Captures: " + board.getComputerCaptures() + "\n";

        infoText.setText(infoMessage);

        boardAdapter.notifyDataSetChanged();
    }

    public void tossDialog(String message){
        // Check if the human player plays as 'W' and display a dialog if true
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Replace 'context' with your activity's context
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the OK button click
                        dialog.dismiss();
                    }
                });
        builder.create().show();

    }

    public void loadGame(){
        Serialization serialization = new Serialization(board );

        serialization.readBoard(board,filePath);

        Log.d("hererere", "load gmae");
        System.out.println(board.printBoard('W'));
        round.setBoard(board);
        moveCount.set(board.checkEmptyBoard());
        moveCount.set(moveCount.get() +1);

        updateView();

        //set the human color
        round.setHumanColor(serialization.getHumanColor());

        if (serialization.getHumanColor() == 'W') {
            //player 1 is human
            //if next player is human, then set index

            if (serialization.getNextPlayer() == "Human") {
                round.setCurrentPlayerIndex(0);
            }
            else {
                round.setCurrentPlayerIndex(1);
            }
        }
        else {
            //player 2 is human
            //swap the players
            //if next player is human, then set index
            round.swapPlayers();

            if (serialization.getNextPlayer() == "Human") {
                round.setCurrentPlayerIndex(1);
            }
            else {
                round.setCurrentPlayerIndex(0);
            }

        }



        //set the scores and captures
        board.setHumanCaptures(serialization.getHumanCaptures());
        board.setComputerCaptures(serialization.getComputerCaptures());

        round.setTournamentHumanScore(serialization.getHumanScore());
        round.setTournamentComputerScore(serialization.getComputerScore());






    }

}