//package com.example.pente.View;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.GridView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.view.Gravity;
//
//import com.example.pente.Model.Board;
//import com.example.pente.Model.ComputerPlayer;
//import com.example.pente.Model.HumanPlayer;
//import com.example.pente.Model.Round;
//
//import com.example.pente.R;
//
//public class BoardView {
//    private Board board;
//    private Round round;
//    private GridView boardGridView;
//    private ArrayAdapter<String> boardAdapter;
//    private String[] boardData;
//    private boolean isBlackPlayer = true;
//
//
//
//    private HumanPlayer humanPlayer;
//    private ComputerPlayer computerPlayer;
//
//    public BoardView(Context context, GridView gridView, ArrayAdapter<String> adapter) {
//        board = new Board();
//        boardGridView = gridView;
//        boardAdapter = adapter;
//        boardData = new String[361];
//        computerPlayer = new ComputerPlayer('W', context);
//        humanPlayer = new HumanPlayer('B', context);
//
//        initializeGridView();
//    }
//    // Add the setRound method
//    public void setRound(Round round) {
//        this.round = round;
//    }
//
//    private void initializeGridView() {
//        for (int i = 0; i < 361; i++) {
//            boardData[i] = "";
//        }
//
//        boardAdapter = new ArrayAdapter<>(boardGridView.getContext(), R.layout.board_item, R.id.itemTextView, boardData);
//        boardGridView.setAdapter(boardAdapter);
//    }
//
//
//    public void setupGridViewClickListener() {
//        boardGridView.setOnItemClickListener((parent, view, position, id) -> {
//            if (boardData[position].isEmpty()) {
//                if (isBlackPlayer) {
//                    boardData[position] = "⚫";
//                } else {
//                    boardData[position] = "⚪";
//                }
//                int boardSize = 19;
//                int row = position / boardSize;
//                int col = position % boardSize;
//
//                char colChar = (char) ('A' + col);
//                String move = colChar + Integer.toString(19 - row);
//                if (isBlackPlayer) {
//                    Log.d("here", "I'm here");
//                    board.placeStone(move, 'H');
//                } else {
//                    board.placeStone(move, 'C');
//                }
//
//                if (isBlackPlayer) {
//                    if (board.checkFive(row, col, 1)) {
//                        Toast.makeText(boardGridView.getContext(), "Five in a row", Toast.LENGTH_SHORT).show();
//                    }
//                    while (board.checkCapture(row, col, 1)) {
//                        Toast.makeText(boardGridView.getContext(), "You captured a stone!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                boardAdapter.notifyDataSetChanged();
//                updateView();
//
//                computerPlayer.makeMove(board, 5);
//                updateView();
//            } else {
//                Toast.makeText(boardGridView.getContext(), "Cell is already populated.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateView() {
//        String[] mapping = {"", "⚫", "⚪"};
//
//        for (int i = 0; i < 19; i++) {
//            for (int j = 0; j < 19; j++) {
//                int cellValue = board.getBoard(i + 1, j);
//                boardData[i * 19 + j] = mapping[cellValue];
//            }
//        }
//
//        boardAdapter.notifyDataSetChanged();
//    }
//}