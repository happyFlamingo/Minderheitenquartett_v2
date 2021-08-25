package com.example.minderheitenquartett;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Übersicht zu den Runden, Spieler, Karten usw.

public class MainActivity extends AppCompatActivity {

    // Deklaration aller TextViews aus dem Layout
    TextView naechsterSpielzug;
    TextView gegnerName;
    TextView gegnerGewonneneRunden;
    TextView gegnerAnzKarten;
    TextView spielerName;
    TextView spielerGewonneneRunden;
    TextView spielerAnzKarten;

    Game game;
    Player player;
    Dealer dealer; // Dealer ist hier unser Gegner bzw. Computer
    Playable turn;

    // in der Override weisen wir allen Deklarationen IDs zu und geben Anweisungen zum Ablauf
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = Game.getInstance();
        dealer = game.getDealer();
        player = game.getPlayer1();
        turn = game.getPlayerTurn();

        // Festlegen des nächsten Spielzugs
        naechsterSpielzug = (TextView) findViewById(R.id.anz_Runden);
        String roundStr = "" + turn.getName();
        naechsterSpielzug.setText(roundStr);

        // Spieler Zuweisungen:

        // Spielername
        spielerName = (TextView) findViewById(R.id.username);
        spielerName.setText(player.getName());

        // AnzKarten im Spielerstapel
        spielerAnzKarten = (TextView) findViewById(R.id.spielerstapel);
        // add column to DB table?
        Integer userNumCards = Integer.valueOf(player.getNumCards());
        String userNumCardsStr = "Karten im Spielerstapel:  " + userNumCards.toString();
        spielerAnzKarten.setText(userNumCardsStr);

        // gewonnene Runden:
        spielerGewonneneRunden = (TextView) findViewById(R.id.gewonneneRuden);
        String userWinsStr = "Gewonnene Runden:  " + player.getNumWins().toString();
        spielerGewonneneRunden.setText(userWinsStr);

        // Gegner Zuweisungen:

        // Gegner Name
        gegnerName = (TextView) findViewById(R.id.username_gegner);
        gegnerName.setText(dealer.getName());

        // Anzkarten im Stapel des Gegners
        gegnerAnzKarten = (TextView) findViewById(R.id.spielerstapel_gegner);
        Integer cpuNumCards = Integer.valueOf(dealer.getNumCards());
        String cpuNumCardsStr = "Karten im Spielerstapel:  " + cpuNumCards.toString();
        gegnerAnzKarten.setText(cpuNumCardsStr);

        // gewonnene Runden Gegner
        gegnerGewonneneRunden = (TextView) findViewById(R.id.gewonneneRuden_gegner);
        String cpuWins = "Gewonnene Runden:  " + dealer.getNumWins().toString();
        gegnerGewonneneRunden.setText(cpuWins);


    }

    // Im Design Code wurde der Button mit der Methode verknüpft: android:onClick="playRoundButtonOnClick"
    public void playRoundButtonOnClick(View button){
        // Case für die Anzeige der TopCard
        if ((turn).equals(player)){
            zeigeTopKarte();
            // Case für die Anzeige des Ergebnisses
        } else {
            zeigeErgebnis();
        }
    }

    // springt hier in die TopCard Klasse und das entsprechende Layout
    public void zeigeTopKarte(){
        Intent topCardActivityintent = new Intent(MainActivity.this, TopCard.class);
        startActivity(topCardActivityintent);
    }

    // springt hier in die Ergebnis Klasse und das entsprechende Layout
    public void zeigeErgebnis(){
        Intent resultActivityIntent = new Intent(MainActivity.this, Ergebnis.class);
        startActivity(resultActivityIntent);
    }
}