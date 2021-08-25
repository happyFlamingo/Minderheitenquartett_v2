package com.example.minderheitenquartett;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    // Deklaration einer neuen Liste
    private ArrayList<Card> cards;

    // neue Liste für die Spielkarten erstellen:
    public Deck() {
        this.cards = new ArrayList<>();
        generateCards(); // ruft hier die generateCards Methode auf
    }

    // for Schleife durchläuft alle unsere Enums und  fügt sie der Liste dieser Klasse hinzu
    public void generateCards(){
        for (Character character : Character.values()){ // Hier wird auf die Enums (unsere Charaktere) zugegriffen
            String name = character.getName();
            Integer bevoelkerungsanteil = character.getBevoelkerungsanteil();
            Integer bildungsniveau = character.getBildungsniveau();
            Integer wohlstand = character.getWohlstand();
            Integer homogenitaet = character.getHomogenitaet();
            Integer gesellschaftlicheAkzeptanz = character.getGesellschaftlicheAkzeptanz();
            Integer schamgefuehl = character.getSchamgefuehl();
            Integer imageSource = character.getImageSource();

            cards.add(new Card(name, bevoelkerungsanteil, bildungsniveau, wohlstand , homogenitaet, gesellschaftlicheAkzeptanz, schamgefuehl, imageSource));
        }
    }

    // getter Methoden

    // gibt die Anzahl der Karten in der Liste dieser Klasse aus
    public int getAnzKarten() {
        return cards.size();
    }

    // shuffle ist eine Methode für Listen, die den Inhalt random neu zusammen stellt
    public void mischen(){
        Collections.shuffle(cards);
    }

    // add ist eine Methode um weitere Variablen in unsere Liste einzufügen
    public void demDeckHinzufuegen(Card karte){
        cards.add(karte);
    }

    public Card obersteKarteEntfernen(){
        // wenn Anzahl an Karten in der Liste > 0
        if (getAnzKarten() > 0){
            // dann index = 0
            return cards.remove(0); // Karte an der erstel Stelle in der Liste hat den Index 0 -> wird hier entfernt mit remove(karte bei Stelle Index 0)
        }
        return null;
    }

}
