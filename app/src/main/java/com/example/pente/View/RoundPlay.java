package com.example.pente.View;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.pente.Model.Tournament;
import com.example.pente.R;

public class RoundPlay extends AppCompatActivity {
    private Board board;
    //private Round round = new Round();

    private Tournament tournament;

    private Round round;
    private GridView boardGridView;
    private ArrayAdapter<String> boardAdapter;
    private String[] boardData;
    //private boolean isBlackPlayer = true;

    private boolean menuVisible = true;

    private Player[] players;

    private HumanPlayer humanPlayer;
    private ComputerPlayer computerPlayer;

    private TextView headerText;
    private TextView infoText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_play);

        boardGridView = findViewById(R.id.boardGridView);
        boardAdapter = new ArrayAdapter<>(this, R.layout.board_item, R.id.itemTextView);
        boardGridView.setAdapter(boardAdapter);

        board = new Board();
        boardData = new String[361];
        computerPlayer = new ComputerPlayer('W', this);
        humanPlayer = new HumanPlayer('B', this);
        round = new Round(humanPlayer, computerPlayer);

        tournament = new Tournament();

        initializeGridView();




//        // In the RoundView class, assuming you have a reference to the TextView
         headerText = findViewById(R.id.headerTextView);
//        headerText.setText("Human's Turn");
//
//        // In the RoundView class, assuming you have a reference to the TextView
         infoText = findViewById(R.id.infoTextView);
//        infoTextView.setText("Human Move");
        setupGridViewClickListener();
        updateView();

        TextView tossCoinText = findViewById(R.id.tossCoinText);
        Button headsButton = findViewById(R.id.headsButton);
        Button tailsButton = findViewById(R.id.tailsButton);
        Button helpMode = findViewById(R.id.helpButton);

        helpMode.setOnClickListener(view -> {
            String bestMove = humanPlayer.helpMode(board, 5); // Assuming helpMode returns the best move as a string

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


            // Enable the board (make it clickable)
            boardGridView.setEnabled(true);

            // Update the menu state
            menuVisible = false;
        });

        tailsButton.setOnClickListener(view -> {
            // Handle the selection of "Tails"
            // You can add your logic here

            // Hide the "Heads" button
            headsButton.setVisibility(View.GONE);
            tailsButton.setVisibility(View.GONE);
            // Hide the "Toss a Coin" text
            tossCoinText.setVisibility(View.GONE);

            players = tournament.startGame("Tails");
            round = new Round(players[0], players[1]);
            round.setHumanColor(tournament.getHumanColor());

            // Enable the board (make it clickable)
            boardGridView.setEnabled(true);

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

//    private void setupGridViewClickListener() {
//        boardGridView.setOnItemClickListener((parent, view, position, id) -> {
//            if (!board.isGameOver()) {
//                System.out.println("position " + position);
//                if (boardData[position].isEmpty()) {
//                    round.getCurrentPlayer().setPosition(position);
//                    headerText.setText(round.getCurrentPlayer().getSymbol()+ "");
//                    round.getCurrentPlayer().makeMove(board, 5);
//                    updateView();
//                    boardAdapter.notifyDataSetChanged();
//                    headerText.setText(round.getCurrentPlayer().getSymbol()+ "");
//
//                    round.getCurrentPlayer().makeMove(board, 5);
//
//                    updateView();
//                    boardAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(boardGridView.getContext(), "Cell is already populated.", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//            else
//            {
//                // In your activity
//                round.calculateScores(board);
//                round.printScores();
//                tournament.calculateTournamentScore(round);
//                DialogHelper.showGameOverDialog(this, tournament, round,board); // Pass the 'round' object
//
//
//            }
//        });
//    }

    private void setupGridViewClickListener() {
        boardGridView.setOnItemClickListener((parent, view, position, id) -> {
            if (!board.isGameOver()) {
                System.out.println("position " + position);
                if (boardData[position].isEmpty()) {
                    Player currentPlayer = round.getCurrentPlayer();
                    currentPlayer.setPosition(position);
                    currentPlayer.makeMove(board, 5);
                    updateView();
                    boardAdapter.notifyDataSetChanged();


                    // Update the header based on the current player
                    if (round.getHumanColor() == currentPlayer.getSymbol()) {
                        headerText.setText("Human's Turn");
                    } else {
                        headerText.setText("Computer's Turn");
                        infoText.setText(computerPlayer.returnMove(board, 5));
                    }

                    round.switchTurn(); // Switch to the next player's turn
                    Player anotherPlayer = round.getCurrentPlayer();
                    anotherPlayer.setPosition(position);
                    anotherPlayer.makeMove(board, 5);
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
                DialogHelper.showGameOverDialog(this, tournament, round, board, this::updateView); // Pass the 'round' object
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

        boardAdapter.notifyDataSetChanged();
    }

}