package com.example.minderheitenquartett;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class HomeScreen extends AppCompatActivity {

    /* Deklaration des Buttons auf der Home Seite */
 Button home;
 Button about;

 /* Hier wird das Layout zum Home Screen verknüpft */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        /* Hier wird zuerst dem Button die ID / der Name des Buttons im Layout zugeordnet */
        /* Methode setOnClickListener -> openRules(); ist eine ausgelagerte Methode...*/
        home = (Button) findViewById(R.id.button_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerModus();
            }
        });

        about = (Button) findViewById(R.id.button_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAbout();
            }
        });


    }
    /* ...die hier den nächsten Screen / die nächste Klasse für die Displayanzeige öffnet */
    public void openPlayerModus() {
        Intent intent = new Intent(HomeScreen.this, PlayerModus.class);
        startActivity(intent);
    }

    public void openAbout() {
        Intent intent = new Intent(HomeScreen.this, About.class);
        startActivity(intent);
    }

}