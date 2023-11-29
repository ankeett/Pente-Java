package com.example.pente.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pente.R;

import androidx.appcompat.app.AppCompatActivity;

public class CoinTossActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Otherwise,
     *                           it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_toss_activity);

        Button headButton = findViewById(R.id.headButton);
        Button tailsButton = findViewById(R.id.tailsButton);

        headButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start RoundClass with a "Head" flag
                startRoundClassActivity("Heads");
            }
        });

        tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start RoundClass with a "Tails" flag
                startRoundClassActivity("Tails");
            }
        });
    }

    /**
     * Starts the RoundPlay activity with the given coin toss result and game type.
     *
     * @param result   The result of the coin toss ("Heads" or "Tails").
     */
    private void startRoundClassActivity(String result) {
        Intent intent = new Intent(CoinTossActivity.this, RoundPlay.class);
        intent.putExtra("coin_result", result);
        intent.putExtra("gameType", "Start");
        startActivity(intent);
    }
}