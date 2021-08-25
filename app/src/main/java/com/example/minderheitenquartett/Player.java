package com.example.minderheitenquartett;

import java.util.ArrayList;

public class Player implements Playable {

    // Spieler bekommt eine ID
    private int id;
    int counter;

    private String name; // für den Spielernamen
    private ArrayList<Card> hand;
    private Integer gewonneneRunden; // Speicher für die Anzahl der gewonnen Runden


    // Konstruktor 1
    public Player(String name, Integer gewonneneRunden) {
        this.name = name;
        this.gewonneneRunden = gewonneneRunden;
        this.hand = new ArrayList<>();
        this.id = counter + 1;
        counter++;
    }

    // getter Methoden:

    // gibt die ID des Spielers zurück
    public int getId() {
        return id;
    }

    // gibt es Spielernamen zurück
    @Override
    public String getName() {
        return name;
    }

    // gibt die Liste dieser Klasse zurück (die Karten, die der Spieler auf der Hand "hält")
    @Override
    public ArrayList<Card> getHand() {
        return hand;
    }

    // gibt die Anzahl der Spielekarten im Deck des Spielers zurück
    public int getNumCards(){
        return hand.size();
    }

    // gibt hier das jeweils erste Element in der Liste (Index dieser Karte ist 0) zurück
    public Card getTopCard(){
        if (hand.size() > 0){
            return hand.get(0);
        }
        return null;
    }

    // gibt die Anzahl der gewonnenen Runden des Spielers zurück
    public Integer getNumWins() {
        return gewonneneRunden;
    }

    // setter Methoden:

    // Zählt die Runde um 1 nach oben, wenn der Spieler den besseren Score hat
    public void addWin() {
        this.gewonneneRunden++;
    }

    public void removeWins() { this.gewonneneRunden = 0; }

    // fügt dem Stapel an Karten eine weitere hinzu
    @Override
    public Card addToHand(Card card) {
        hand.add(card);
        return card;
    }

    // wenn der Stapel eine Karte enthält, dann soll diese von der Liste "hand" entfernt werden
    @Override
    public Card removeFromHand(Card card) {
        if (hand.contains(card)){
            hand.remove(card);
            return card;
        }
        return null;
    }

}
