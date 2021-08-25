package com.example.minderheitenquartett;

public class Dealer extends Player implements Playable {

    // Deklaration Kartendeck
    private Deck deck;

    // Dealer als Ableitung von Player (Dealer ist dem Spieler in der Hierarchie untergeordnet)
    public Dealer(String name) {
        super(name, 0);
        this.deck = new Deck();
    }

    // getter Methoden:

    // return: Anzahl der Karten im Deck
    public int getDeckSize(){
        return deck.getAnzKarten();
    }

    // return: gibt das Deck als Variable zurück
    public Deck getDeck(){
        return deck;
    }

    // Boolean methode: Deck wird gemischt und dann auf "true" gesetzt
    public boolean mischen(){
        deck.mischen();
        return true;
    }

    // eine Karte dem Deck hinzufügen
    public void demDeckHinzufuegen(Card karte){
        deck.demDeckHinzufuegen(karte);
    }

    // die oberste Karte vom Stapel entfernen
    public Card obersteKarteEntfernen(){
        return deck.obersteKarteEntfernen();
    }

}
