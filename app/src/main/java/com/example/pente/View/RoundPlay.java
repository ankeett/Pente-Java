package com.example.pente.View;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.pente.Model.Logger;
import com.example.pente.Model.Player;
import com.example.pente.Model.Round;
import com.example.pente.Model.Serialization;
import com.example.pente.Model.Tournament;
import com.example.pente.R;
import com.example.pente.Model.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundPlay extends AppCompatActivity {
    private Board board = new Board();
    private Tournament tournament;
    private Round round;
    private GridView boardGridView;
    private ArrayAdapter<String> boardAdapter;
    private String[] boardData;
    private AtomicInteger moveCount = new AtomicInteger(1);
    private boolean menuVisible = true;
    private Player[] players;
    private HumanPlayer humanPlayer;
    private ComputerPlayer computerPlayer;
    private TextView headerText;
    private TextView infoText;
    private boolean isHumanMove;
    private String gameType, filePath;
    private TextView computerMoveView;
    private Logger logger = Logger.getInstance();

    /**
     * Called when the activity is first created. Responsible for initializing the activity,
     * setting up UI components, handling intent extras, and managing game state.
     *
     * @param savedInstanceState A Bundle containing the data most recently supplied
     *                           in onSaveInstanceState(Bundle).
     */
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

        // Find the TextView by its ID
        TextView alphabeticalLabelsTextView = findViewById(R.id.alphabeticalLabels);

        // Generate the alphabetical labels as a string
        StringBuilder labels = new StringBuilder();

        for (char c = 'A'; c <= 'S'; c++) {
            labels.append("  ").append(c).append(" ");
        }

        // Set the generated labels as the text for the TextView
        alphabeticalLabelsTextView.setText(labels.toString());

        // Find the TextView by its ID
        TextView numericalLabelsTextView = findViewById(R.id.numericalLabels);

        // Generate the numerical labels as a string
        StringBuilder numLabels = new StringBuilder();
        for (int i = 19; i >= 1; i--) {
            numLabels.append(i).append("\n");
        }

        // Set the generated labels as the text for the TextView
        numericalLabelsTextView.setText(numLabels.toString());

        // Adjust line spacing for the TextView
        numericalLabelsTextView.setLineSpacing(8f, 1.15f); // Adjust the spacing as needed

        headerText = findViewById(R.id.headerTextView);

        infoText = findViewById(R.id.infoTextView);

        // Set the initial visibility of headerText and infoText
        headerText.setVisibility(View.GONE);
        infoText.setVisibility(View.GONE);

        // Get a reference to the TextView
        computerMoveView = findViewById(R.id.computerMoveView);
        computerMoveView.setVisibility(View.VISIBLE);

        setupGridViewClickListener();
        updateView();



        gameType = getIntent().getStringExtra("gameType");

        System.out.println(gameType);

        if(gameType.equals("Load")){
            Log.d("Load", "I'm here");
            filePath = getIntent().getStringExtra("filepath");
            loadGame();
        }
        else if(gameType.equals("Start")){
            logger.log("Round started");
            String tossResult = getIntent().getStringExtra("coin_result");
            players = tournament.startGame(tossResult);
            round = new Round(players[0], players[1]);
            round.setHumanColor(tournament.getHumanColor());


            if (tournament.getHumanColor() == 'W') {
                tossDialog("You win the toss, you play White");
                logger.log("Human won the toss");
                tournament.setLastWinner(1);
            }
            else{
                tossDialog("You lost the toss, you play Black!");
                logger.log("Human lost the toss");
                tournament.setLastWinner(2);
            }
        }
        updateView();

        // Set the initial visibility of headerText and infoText
        headerText.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.VISIBLE);





        Button helpMode = findViewById(R.id.helpButton);
        Button saveGame = findViewById(R.id.saveButton);
        Button logButton = findViewById(R.id.logButton);

        logButton.setOnClickListener(view -> {
            showLogDialog();
        });

        helpMode.setOnClickListener(view -> {
            logger.log("Help Asked");
            String bestMove = humanPlayer.helpMode(board, moveCount.get()); // Assuming helpMode returns the best move as a string

            String reasoning = humanPlayer.helpModeReasoning(board,moveCount.get());
            // Create an AlertDialog to display the best move
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Best Move");
            alertDialogBuilder.setMessage("The best move is: " + bestMove + "\nReason: " + reasoning);

            logger.log("Computer suggested move as "+ bestMove);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
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
                    logger.log("Saving game");
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
                    logger.log("Cancelling the event");
                    dialog.cancel();
                }
            });
            builder.show();
        });


        GridView boardGridView = findViewById(R.id.boardGridView);
        // Initialize boardGridView and other components

        // Initially, the menu is visible, so disable the board
        boardGridView.setEnabled(true);


    }

    /**
     * Initializes the GridView with empty data.
     * Help from: https://developer.android.com/reference/android/widget/ArrayAdapter
     */
    private void initializeGridView() {
        for (int i = 0; i < 361; i++) {
            boardData[i] = "";
        }

        boardAdapter = new ArrayAdapter<>(boardGridView.getContext(), R.layout.board_item, R.id.itemTextView, boardData);
        boardGridView.setAdapter(boardAdapter);
    }


    /**
     * Sets up the click listener for the GridView based on the current player's turn.
     * If it's the human player's turn, it calls {@link #handleHumanPlayerTurn()},
     * otherwise, it calls {@link #handleComputerPlayerTurn()}.
     */
    private void setupGridViewClickListener() {
        if (round.getCurrentPlayer() instanceof HumanPlayer) {
            handleHumanPlayerTurn();
        } else {
            handleComputerPlayerTurn();
        }
    }

    /**
     * Handles the human player's turn by setting up a click listener for the GridView.
     * If the selected position is valid, it calls {@link #handleValidHumanMove(Player)}.
     * If the human player captures at least 5 stones, it declares the human player as the winner.
     * If the game is over, it calls {@link #handleGameEnd()}.
     */
    private void handleHumanPlayerTurn() {
        if (!board.isGameOver()) {
            boardGridView.setOnItemClickListener((parent, view, position, id) -> {
                if (!boardData[position].isEmpty()) {
                    showInvalidMoveDialog("Invalid move. Spot already occupied.");
                    return;
                }

                Player currentPlayer = round.getCurrentPlayer();
                currentPlayer.setPosition(position);

                if (humanPlayer.checkPosition(board, position)) {
                    handleValidHumanMove(currentPlayer);

                    if (board.getHumanCaptures() >= 5) {
                        round.setWinner(1);
                        board.setGameOver(true);
                    }

                } else {
                    setupGridViewClickListener();
                }
            });
        } else {
            handleGameEnd();
        }
    }

    /**
     * Handles a valid move made by the human player. If it's the first move, logs the stone placement
     * at J10. If it's the third move, checks if it's three intersections away from J10. Updates the
     * move count, sets the computer move view, logs the human move, makes the move on the board, and
     * updates the view. If the human player captures a stone, logs the capture. Finally, switches
     * the turn and sets up the grid view click listener.
     *
     * @param currentPlayer The current human player making the move.
     */
    private void handleValidHumanMove(Player currentPlayer) {
        if (moveCount.get() == 1) {
            logger.log("Human tried to place a stone at " + humanPlayer.returnMove(currentPlayer.getPosition()));
            logger.log("It got placed at J10");
        } else if(moveCount.get() == 3){
            if(!currentPlayer.isThreePointsAway("J10", humanPlayer.returnMove(currentPlayer.getPosition()))){
                showInvalidMoveDialog("Invalid move. The second move should be three intersections away.");
                return;
            }
        }
        computerMoveView.setText("Your turn");


        logger.log("Human placed a stone at " + humanPlayer.returnMove(currentPlayer.getPosition()));

        currentPlayer.makeMove(board, moveCount.getAndIncrement());
        if(currentPlayer.getHasCaptured()){
            logger.log("Human captured a stone");
        }

        updateView();
        boardAdapter.notifyDataSetChanged();

        round.switchTurn();
        setupGridViewClickListener();
    }

    /**
     * Handles the turn for the computer player. If the game is not over, retrieves the current player,
     * obtains reasoning for the move, sets the computer move view with the move and reasoning, logs the
     * computer move, makes the move on the board, and checks for captures. If the computer player
     * captures five stones, sets the winner and ends the game. Updates the view, switches the turn,
     * and sets up the grid view click listener. If the game is over, handles the end of the game.
     */
    private void handleComputerPlayerTurn() {
        if (!board.isGameOver()) {
            Player currentPlayer = round.getCurrentPlayer();
            String reasoning = computerPlayer.reasoningMove(board, moveCount.get());


            String newText = computerPlayer.moveString(board, moveCount.get());
            String htmlText = "Computer made a move at <font color='red'>" + newText + "</font>" + " to " + reasoning;

            computerMoveView.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY));
            logger.log("Computer placed a stone at " + newText);
            currentPlayer.makeMove(board, moveCount.getAndIncrement());

            if(currentPlayer.getHasCaptured()){
                logger.log("Computer captured a stone");
            }

            if (board.getComputerCaptures() >= 5) {
                round.setWinner(2);
                System.out.println("Computer wins!");
                board.setGameOver(true);
            }

            //computerMoveView.setVisibility(View.VISIBLE);

            updateView();
            boardAdapter.notifyDataSetChanged();

            round.switchTurn();
            setupGridViewClickListener();
        } else {
            handleGameEnd();
        }
    }

    /**
     * Handles the end of the game by calculating scores, updating the tournament score, and displaying
     * the game over dialog with the relevant information. The dialog includes round details such as
     * human and computer scores, captures, winner, and the overall tournament scores. After handling
     * the game end, the view is updated.
     */
    private void handleGameEnd() {
        round.calculateScores(board);
        tournament.calculateTournamentScore(round);
        DialogHelper.showGameOverDialog(this, tournament, round, board, moveCount, this::updateView);
    }



    /**
     * Updates the view by mapping the current state of the game board to visual representations,
     * including player stones and captures. Also updates the header and info text with relevant
     * information such as human and computer colors, and the number of captures for each player.
     */
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

    /**
     * Displays a dialog with the specified message for the coin toss result.
     *
     * @param message The message to be displayed in the dialog.
     */
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

    /**
     * Loads a saved game state from the specified file path, updates the board, round, and tournament.
     */
    public void loadGame(){

        logger.log("Loading Game");

        Serialization serialization = new Serialization(board );

        serialization.readBoard(board,filePath);

        round.setBoard(board);
        moveCount.set(board.checkEmptyBoard() + 1);

        updateView();

        //set the human color
        round.setHumanColor(serialization.getHumanColor());

        System.out.println(serialization.getNextPlayer());

        if (serialization.getHumanColor() == 'W') {

            tournament.setLastWinner(1);

            if (serialization.getNextPlayer().equals("Human")) {
                System.out.println(serialization.getNextPlayer());
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
            tournament.setLastWinner(2);

            if (serialization.getNextPlayer().equals("Human")) {
                round.setCurrentPlayerIndex(1);
            }
            else {
                round.setCurrentPlayerIndex(0);
            }

        }

        //set the scores and captures
        board.setHumanCaptures(serialization.getHumanCaptures());
        board.setComputerCaptures(serialization.getComputerCaptures());

        tournament.setHumanScores(serialization.getHumanScore());
        tournament.setComputerScores(serialization.getComputerScore());

        showNextPlayerDialog(serialization.getNextPlayer());

    }

    /**
     * Displays a dialog containing the log contents.
     */
    private void showLogDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.log_dialog, null);
        builder.setView(dialogView);
        builder.setTitle("Log Contents");

        TextView logTextView = dialogView.findViewById(R.id.logTextView);
        logTextView.setText(Logger.getInstance().getLog());

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays an AlertDialog indicating that the move is invalid with the specified message.
     *
     * @param message The message to be displayed in the dialog.
     */
    private void showInvalidMoveDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Invalid Move")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the click on the OK button if needed
                        dialog.dismiss(); // Dismiss the dialog
                    }
                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays an AlertDialog indicating the player who is making the next move.
     *
     * @param nextPlayer The player who is making the next move ("Human" or "Computer").
     */
    private void showNextPlayerDialog(String nextPlayer){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = nextPlayer.equals("Human") ? "You are making the next move!" : "Computer is making the next move! \n Click anywhere to continue.";
        builder.setMessage(message)
                .setTitle("Next Move")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the click on the OK button if needed
                        dialog.dismiss(); // Dismiss the dialog
                    }
                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}