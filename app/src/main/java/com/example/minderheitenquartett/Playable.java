package com.example.minderheitenquartett;

import java.util.ArrayList;

public interface Playable {
        // Voraussetzungen, die es grundsätzlich für den Spielmechanismus braucht
        // weitere Eigenschaften wie ID oä. werden dann in den Klassen Player oder Dealer definiert

        ArrayList<Card> getHand();

        String getName();
        int getNumCards();
        void addWin();
        Integer getNumWins();

        Card getTopCard();
        Card addToHand(Card card);
        Card removeFromHand(Card card);

    }

