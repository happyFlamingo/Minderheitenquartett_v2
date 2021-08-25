package com.example.minderheitenquartett;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


public class Rules extends AppCompatActivity {
    // Button und EditText aus dem Layout:
    private Button start;
    EditText spieler_name;
    // String name wird den Edittext später übernehmen
    String name;
    // Objekt der Klasse Game wird erstellt -> für den Aufruf der Methoden in der Klasse Game
    Game game;

    /* onCreate baut das Layout zu den Spielregeln auf*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        // Hier werden die IDs aus dem Layout den obigen Deklarationen zugeordnet
        spieler_name = (EditText) findViewById(R.id.spieler_name);
        start = (Button) findViewById(R.id.button_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGameButtonOnClick(); //Springt hier in die playGameButtonOnClick() Methode
            }
        });
    }

    public void playGameButtonOnClick() {

        game = Game.getInstance();
        // Leeres Spiel
        // die tearDown() Methode befindet sich am Ende dieser Klasse -> Instanz wird neu geladen + reset
        try {
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // das EditText Feld wird hier dem String "name" zugewiesen und außerdem zu einem String umgewandelt
        name = spieler_name.getText().toString();

        if (!name.equals("")){
            initializeIntent();
        } else {
            Toast.makeText(this, "Bitte geben Sie ihren Namen ein.", Toast.LENGTH_SHORT).show();
        }
    }

    /* Wir holen uns hier die Instanz, geben die Anweisung das Spiel zu starten und wechseln unser Layout / die Klasse zur MainActivity */
    public void initializeIntent(){
        game = Game.getInstance();
        game.start(name);
        Intent intent = new Intent(Rules.this, MainActivity.class);
        startActivity(intent);
    }

    /* Die Klasse tearDown() gehört zu den TestCases:
        public abstract class TestCase
        extends Assert implements Test */
    public void tearDown() throws Exception {
        // Die Methode erstellt zuerst eine Klasse c -> Repräsentiert unsere Minderheitenquartett Datei
        // Danach werden alle Konstruktoren unserer Datei geladen
        // Game Instanz erfährt einen Reset für ein neues Spiel

        try {
            Class c = Class.forName("com.example.minderheitenquartett");
            Constructor[] constructors = c.getDeclaredConstructors();
            //Fehler bei der Zugriffsprüfung werden hier unterdrückt
            constructors[0].setAccessible(true);

            Field field = Game.class.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            field.set(game, constructors[0].newInstance());
            field.setAccessible(false);
        } catch (ClassNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}



