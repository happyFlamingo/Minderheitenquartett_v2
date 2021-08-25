package com.example.minderheitenquartett;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import static com.example.minderheitenquartett.R.id.game_result_text;
import androidx.appcompat.app.AppCompatActivity;


public class Gewinner extends AppCompatActivity {
    TextView gameResultText;
    Game game;
    Playable gameWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gewinner);

        game = Game.getInstance();

        gameResultText = (TextView) findViewById(game_result_text);
        gameWinner = game.getWinner();

        String gameWinnerStr = gameWinner.getName() + " gewinnt!";
        gameResultText.setText(gameWinnerStr);
    }

    public void playAgainOnClick(View button){
        // restart game
        game = Game.getInstance();
        game.playAgain();

        initializeMainActivity();
    }

    public void initializeMainActivity(){
        Intent MainActivityIntent = new Intent(Gewinner.this, MainActivity.class);
        startActivity(MainActivityIntent);
    }

}