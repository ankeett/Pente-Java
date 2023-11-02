////package com.example.pente;
////
////import android.content.Context;
////import android.graphics.Canvas;
////import android.graphics.Color;
////import android.graphics.Paint;
////import android.view.View;
////import android.util.AttributeSet;
//
//
////public class BoardView extends View {
////    private int numRows;
////    private int numColumns;
////    private int cellSize;
////    private Paint paint;
////
////    public BoardView(Context context, AttributeSet attrs) {
////        super(context, attrs);
////        // Initialize any attributes here if needed
////        init();
////    }
////
////    protected void init() {
////        // Initialization code
////        this.numRows = 19;
////        this.numColumns = 19;
////        this.cellSize = 50;
////        this.paint = new Paint();
////        paint.setColor(Color.BLACK);
////        paint.setStyle(Paint.Style.STROKE);
////    }
////
////    @Override
////    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        int width = (numColumns +2) * cellSize;
////        int height = (numRows+2) * cellSize;
////        setMeasuredDimension(width, height);
////    }
////
////    @Override
////    protected void onDraw(Canvas canvas) {
////        super.onDraw(canvas);
////
////        // Set the background color to yellow
////        canvas.drawColor(Color.YELLOW);
////
////        // Calculate the starting position to center the board in the canvas
////        int startX = (getWidth() - (numColumns * cellSize)) / 2;
////        int startY = (getHeight() - (numRows * cellSize)) / 2;
////
////        // Set up paint for labels
////        Paint labelPaint = new Paint();
////        labelPaint.setColor(Color.BLACK);
////        labelPaint.setTextSize(24);
////
////        for (int j = 0; j <= numColumns; j++) {
////            int x = startX + j * cellSize;
////
////            // Add column label
////
////            if(j != 19) {
////                String colLabel = String.valueOf((char)('A' + j));
////                canvas.drawText(colLabel, x + 10, startY - 20, labelPaint);
////            }
////
////            canvas.drawLine(x, startY, x, startY + numRows * cellSize, paint);
////        }
////
////        for (int i = 0; i <= numRows; i++) {
////            int y = startY + i * cellSize;
////
////            // Add row label
////            int rowLabel = 19 - i;
////            canvas.drawText(String.valueOf(rowLabel), startX - 30, y + 20, labelPaint);
////
////            canvas.drawLine(startX, y, startX + numColumns * cellSize, y, paint);
////        }
////
////        // Add your custom drawing logic for the game pieces or other content here
////    }
////
////
////
////
////}
//


package com.example.pente;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import android.util.AttributeSet;


public class BoardView extends GridLayout {

    public BoardView(Context context) {
        super(context);
        setRowCount(19);
        setColumnCount(19);
        createBoardButtons(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRowCount(19);
        setColumnCount(19);
        createBoardButtons(context);
    }

    private void createBoardButtons(final Context context) {
        // Inside your createBoardButtons method
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                Button button = new Button(context);
                button.setLayoutParams(new GridLayout.LayoutParams());

                // Set the background color for each individual button
                button.setBackgroundColor(Color.RED); // Or any other color

                // Set other button properties as needed
                button.setWidth(8);
                button.setHeight(8);
                button.setTextSize(4);

                final int row = i;
                final int col = j;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle button click
                        Toast.makeText(context, "Button clicked at row " + row + " and column " + col, Toast.LENGTH_SHORT).show();
                    }
                });

                this.addView(button);
            }
        }

    }

}



