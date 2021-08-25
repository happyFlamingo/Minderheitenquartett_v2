package com.example.minderheitenquartett;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayerModus extends AppCompatActivity {
    Button single;
    Button multi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playermodus);

        single = (Button) findViewById(R.id.button_singlePlayer);
        multi = (Button) findViewById(R.id.button_multiPlayer);

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRules();
            }
        });

        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBluetooth();
            }
        });
    }

    public void openRules() {
        Intent intent = new Intent(PlayerModus.this, Rules.class);
        startActivity(intent);
    }

    public void openBluetooth() {
        Intent intent = new Intent(PlayerModus.this, Bluetooth.class);
        startActivity(intent);
    }
}