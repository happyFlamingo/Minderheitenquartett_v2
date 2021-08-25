package com.example.minderheitenquartett;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Ergebnis extends AppCompatActivity {

    TextView kategorie;
    TextView cpuName;
    TextView spielerName;
    TextView gewinnerName;
    ImageView karteCPU;
    ImageView karteSpieler;

    Game game;
    Card roundWinningCard;
    String category;
    String roundWinnerName;
    Integer roundWinnerScore;
    boolean isRoundDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnis);

        // setup game
        game = Game.getInstance();

        //get extras
        handleExtras();

        // setup views
        getLayoutViews();
        setLayoutViews();

        // play round and get winner details
        game.playRound(category);
        isRoundDraw = game.isRoundDraw();
        roundWinnerName = game.getRoundWinner().getName();
        roundWinningCard = game.getRoundWinningCard();
        roundWinnerScore = game.getRoundWinningScore();

        // check if draw then set colours accordingly
        if (isRoundDraw){

            String unentschieden = "Unentschieden!";
            gewinnerName.setText(unentschieden);

        } else {
            gewinnerName.setText(roundWinnerName);
        }
    }

    public void handleExtras(){
        Intent resultActivityIntent = getIntent();

        if (resultActivityIntent.hasExtra("category")){
            Bundle extras = resultActivityIntent.getExtras();
            String roundCategory = extras.getString("category");
            category = game.playerSetCategory(roundCategory);
        } else {
            category = game.getCPUBestCategory();
        }
    }

    public void getLayoutViews(){
        // Kategorie der Runde
        kategorie = (TextView) findViewById(R.id.kategorie);

        // Outpout Gewinner oder Draw
        gewinnerName = (TextView) findViewById(R.id.gewinner_name);

        // Computer Setup
        cpuName = (TextView) findViewById(R.id.cpu_name);

        Card cpuCard = game.getDealer().getTopCard();

        karteCPU = (ImageView) findViewById(R.id.character_cpu);
        karteCPU.setImageResource(cpuCard.getImageSource());


        // Spieler Setup
        spielerName = (TextView) findViewById(R.id.spieler_name);

        Card playerCard = game.getPlayer1().getTopCard();

        karteSpieler = (ImageView) findViewById(R.id.character_spieler);
        karteSpieler.setImageResource(playerCard .getImageSource());
    }

    public void setLayoutViews(){
        // set round category
        String roundCategoryTextStr = "Kategorie:  " + category;
        kategorie.setText(roundCategoryTextStr);

        cpuName.setText(game.getDealer().getName());

        spielerName.setText(game.getPlayer1().getName());
    }

    public void nextRoundButtonOnClick(View button){
        if (!game.isGameWon()){
            initializeMainActivityIntent();
        } else {
            initializeGameWonIntent();
        }
    }

    public void initializeMainActivityIntent(){
        Intent mainActivityIntent = new Intent(Ergebnis.this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    public void initializeGameWonIntent(){
        Intent gameWonIntent = new Intent(Ergebnis.this, Gewinner.class);
        startActivity(gameWonIntent);
    }

}