package com.example.minderheitenquartett;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TopCard extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    TextView characterNameText;
    Game game;
    String category;
    ImageView characterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_card);

        game = Game.getInstance();

        Card userCard = game.getPlayer1().getTopCard();

        characterNameText = (TextView) findViewById(R.id.character);
        characterNameText.setText(userCard.getName());

        characterImageView = (ImageView) findViewById(R.id.character_image);
        characterImageView.setImageResource(userCard.getImageSource());

        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.text_view_selected);

    }

    public void checkButton(View button){

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Deine Auswahl: " + radioButton.getText(), Toast.LENGTH_SHORT).show();

        // set the category for the round
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.bevoelkerungsanteil:
                category = "Bevoelkerungsanteil";
                break;
            case R.id.bildungsniveau:
                category = "Bildungsniveau";
                break;
            case R.id.wohlstand:
                category = "Wohlstand";
                break;
            case R.id.homogenitaet:
                category = "Homogenitaet";
                break;
            case R.id.gesellschaftlicheAkzeptanz:
                category = "Gesellschaftliche Akzeptanz";
                break;
            case R.id.schamgefuehl:
                category = "Schamgefuehl";
                break;
            default:
                break;
        }
        initializeIntent();
    }

    public void initializeIntent(){
        Intent resultActivityIntent = new Intent(TopCard.this, Ergebnis.class);

        resultActivityIntent.putExtra("category", category);

        startActivity(resultActivityIntent);
    }

}
