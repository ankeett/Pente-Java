package com.example.pente.View;

import android.os.Bundle;
import com.example.pente.Model.ComputerPlayer;
import com.example.pente.Model.HumanPlayer;
import com.example.pente.Model.Round;

import com.example.pente.R;
import com.example.pente.View.test2;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RoundPlay extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
    }


    private Round round = new Round(new ComputerPlayer('W',context), new HumanPlayer('B',context));

}
