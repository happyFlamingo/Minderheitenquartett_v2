package com.example.minderheitenquartett;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game {

    /* Instanz erstellen */
    private static Game INSTANCE = new Game();

    // Deklarationen Listen -> Listen werden mit Iteratoren durchsucht
    private ArrayList<Playable> spieler;
    private ArrayList<String> gegnerBestCategory;
    private ArrayList<Card> drawPile;

    // Deklarationen HashMaps -> HasMaps werden mit der Methode KeySet() durchsucht, haben keinen Interator (wie in Listen)
    private HashMap<Card, Playable> pile;
    private HashMap<Card, Playable> cardsToBeat;

    /* vom Typ der Klasse / des Objekts Dealer */
    private Dealer dealer;

    /* vom Typ der Klasse / des Objekts Deck */
    private Deck deck;

    /* vom Typ der Klasse / des Objekts Player */
    private Player spieler1;

    /* vom Typ der Klasse / des Objekts Card */
    private Card besteKarte;
    private Card gewinnerKarteRunde;

    /* allgemeiner Deklarationsteil */
    private String kategorie;
    private int anzKarten;
    private int anzUngenutzteKarten;
    private Integer zuSchlagenderScore;
    private Integer scoreGewinnerRunde;

    /* Deklaration booleans */
    private boolean roundDraw; // draw = unentschieden
    private boolean spielGewonnen;

    /* vom Typ des Interface */
    private Playable playerTurn;
    private Playable gewinnerRunde;
    private Playable gewinner;


    private Game() {
        // Listen erstellen
        this.spieler = new ArrayList<>();
        this.gegnerBestCategory = new ArrayList<>();
        this.drawPile = new ArrayList<>();
        // Hash Maps erstellen
        this.pile = new HashMap<>();
        this.cardsToBeat = new HashMap<>();

        // Spieleinstellungen zum Beginn der ersten Runde
        this.anzKarten = 0; // Die Anzahl der Karten beträgt zum Spielstart 0
        this.zuSchlagenderScore = 0; // soweit noch kein Score, daher vorbelegen mit 0
        this.scoreGewinnerRunde = 0;

        // booleans vorerst auf false setzen
        this.roundDraw = false;
        this.spielGewonnen = false;
    }

    // getter Methoden

    // return: Anzahl der Spieler
    public int getNumPlayers(){
        return spieler.size();
    }

    // der Spieler, der gerade dran ist
    public Playable getPlayerTurn() {
        return playerTurn;
    }

    // zweiter Stapel (selektionen der obersten Karten vom Stapel)
    public boolean isRoundDraw() {
        return roundDraw;
    }

    // return: Gewinner der einzelnen Runde
    public Playable getRoundWinner() {
        return gewinnerRunde;
    }

    // return: Karte des Gewinners
    public Card getRoundWinningCard() {
        return gewinnerKarteRunde;
    }

    // return: Score vom jeweiligen Gewinner der Runde
    public Integer getRoundWinningScore() {
        return scoreGewinnerRunde;
    }

    // Anzahl der Karten in DrawPile (= Stapel, indem die Karten, die gegeneinander "antreten" aufgeführt sind)
    public int getDrawPileSize() {
        return drawPile.size();
    }

    // return: Spiel wurde gewonnen ( = true )
    public boolean isGameWon() {
        return spielGewonnen;
    }

    // return: dealer
    public Dealer getDealer() {
        return dealer;
    }

    // return: Spieler 1
    public Player getPlayer1() {
        return spieler1;
    }

    // return: Gewinner des gesamten Spiels
    public Playable getWinner() {
        return gewinner;
    }


    // setter Methoden

    // der Spieler, der an der Reihe ist, sucht sich seine gewünschte Kategorie aus
    public String playerSetCategory(String kategorie){
        this.kategorie = kategorie;
        return this.kategorie;
    }


    // setter Methoden

    public static Game getInstance() {
        return INSTANCE;
    }

    public void start(String playerName){
        // wenn ein Spiel gestartet wird, wird die Spieler Liste geleert
        spieler.clear();

        // setup Computer dealer / Gegner
        String[] dealerNames = new String[]{ "Susanne", "Sophia", "Thomas", "Dominik" };
        this.dealer = new Dealer("CPU " + dealerNames[generateRandom(dealerNames.length)]); // sucht hier random einen der Name aus dem String Array heraus und hängt es an CPU
        spieler.add(dealer);

        // Setup und Anzahl aller Karten
        this.deck = dealer.getDeck();
        anzKarten = deck.getAnzKarten();

        // Neue Spieler Instanz wird erstellt und hinzugefügt
        spieler1 = new Player(playerName, 0);
        spieler.add(spieler1);

        // der Spieler links vom Dealer aus beginnt
        this.playerTurn = spieler.get(1);

        // Das Deck mischen
        dealer.mischen();

        // Karten auf die Spieler verteilen (min. 1 pro Nase) -> Deck Größe % Anzahl der Spieler
        // verteilt so lange, bis nicht gleich verteilt werden kann, dann hört der Dealer auf
        anzUngenutzteKarten = dealer.getDeckSize() % getNumPlayers();
        while (dealer.getDeckSize() > anzUngenutzteKarten ){
            for (Playable player : spieler){
                player.addToHand(dealer.obersteKarteEntfernen());
            }
        }
    }


    public void playRound(String kategorie){
        // die cardsToBeat HashMap wird geleert
        cardsToBeat.clear();

        // nimmt die Karte vom Spieler, der an der Reihe ist und setzt diese als zu schlagende Karte
        besteKarte = playerTurn.getTopCard();
        cardsToBeat.put(besteKarte, playerTurn);
        this.kategorie = kategorie;

        // zu schlagender Score wird festgelegt
        zuSchlagenderScore = getCardCategoryValue(besteKarte, this.kategorie);

        // von jedem Spieler wird die oberste Karte in den Stapel hinzugefügt
        addToPile();

        // vergleicht die Scores der Karten
        compareValues();

        // der Gewinner der Runde bekommt die Karten, die miteinander verglichen wurden
        if (checkRoundWin()){
            winnerTakePileCards();
            winnerTakeDrawPileCards();
        } else {
            moveToDrawPile();
        }

        // vorgegebene Methode: return Wert ist 1. wenn es einen Gewinner gibt
        checkWin();

        // Spielerwechsel
        if (spieler.size() > 1) {
            changeTurn();
        }

    }

    public void playAgain(){
        roundDraw = false;
        spielGewonnen = false;

        dealer.removeWins();
        spieler1.removeWins();

        returnCardsToDealer();

        dealer.mischen();

        // Karten auf die Spieler verteilen (min. 1 pro Nase) -> Deck Größe % Anzahl der Spieler
        // verteilt so lange, bis nicht gleich verteilt werden kann, dann hört der Dealer auf
        anzUngenutzteKarten = dealer.getDeckSize() % getNumPlayers();
        while (dealer.getDeckSize() > anzUngenutzteKarten ){
            for (Playable player : spieler){
                player.addToHand(dealer.obersteKarteEntfernen());
            }
        }

        // der Spieler links vom Dealer aus beginnt
        this.playerTurn = spieler.get(1);
    }

    // Nebenfunktionen

    // Random ist eine vorgegebene Klasse in Android Studio
    // gibt eine zufällige Zahl für limit zurück
    public int generateRandom(int limit){
        Random rand = new Random();
        return rand.nextInt(limit);
    }

    // holt sich von der obersten Karte die ausgewählte Kategorie
    public int getCardCategoryValue(Card topCard, String category){
        switch (category) {
            case "Bevoelkerungsanteil":
                return topCard.getBevoelkerungsanteil();
            case "Bildungsniveau":
                return topCard.getBildungsniveau();
            case "Wohlstand":
                return topCard.getWohlstand();
            case "Homogenitaet":
                return topCard.getHomogenitaet();
            case "Gesellschaftliche Akzeptanz":
                return topCard.getGesellschaftlicheAkzeptanz();
            case "Schamgefuehl":
                return topCard.getSchamgefuehl();
            default:
                return 0;
        }
    }

    public String getCPUBestCategory(){
        /*die topCard des dealers wird in Liste "categoryValues" eingelesen, dann wird die Liste nach der Kategorie eingelesen
        * idr ist der value der topCard größer als -1, daher wird der neue cpuCategoryValue 20 und in die Liste gegnerBestCategory aufgenommen
        * wenn beide Karten den gleichen Wert haben, dann wird der Wert direkt in die Liste gegnerBestCategory aufgenommen
        * return: übergibt die Größe der Liste und generiert daraus eine beliebig große Zahl, die zurückgegeben wird */

        Card card = dealer.getTopCard(); // change this to if statement to check if player is a bot
        gegnerBestCategory.clear();
        Integer cpuCategoryValue = -1; // alle Werte der Karten werden > 0 sein, daher -1 als erster Richtwert

        // HashMap wird erstellt: Kategorie ist der Key, der Wert der Integer
        HashMap<String, Integer> categoryValues = new HashMap<>();
        categoryValues.put("Bevoelkerungsanteil", card.getBevoelkerungsanteil());
        categoryValues.put("Bildungsniveau", card.getBildungsniveau());
        categoryValues.put("Wohlstand", card.getWohlstand());
        categoryValues.put("Homogenitaet", card.getHomogenitaet());
        categoryValues.put("Gesellschaftliche Akzeptanz", card.getGesellschaftlicheAkzeptanz());
        categoryValues.put("Schamgefuehl", card.getSchamgefuehl());

        for (String category : categoryValues.keySet()){
            Integer value = categoryValues.get(category); // value wird aus der HashMap categoryValues geholt

            if (value > cpuCategoryValue) {

                gegnerBestCategory.clear();
                cpuCategoryValue = value; //
                gegnerBestCategory.add(category);
            } else if (value.equals(cpuCategoryValue)){
                gegnerBestCategory.add(category);
            }
        } return gegnerBestCategory.get(generateRandom(gegnerBestCategory.size()));
    }

    // nimmt die Karten in den Stapel "Pile" auf
    public void addToPile(){
        for (Playable player : spieler){
            Card playerCard = player.removeFromHand(player.getTopCard());
            pile.put(playerCard, player);
        }
    }

    // nimmt die Karte aus dem "Pile" und legt sie auf den anderen Stapel "drawPile"
    public void moveToDrawPile(){
        ArrayList<Card> toMove = new ArrayList<>();

        for (Card card : pile.keySet()){
            toMove.add(card);
        }

        for (Card card : toMove){
            drawPile.add(card);
            pile.remove(card);
        }
        toMove.clear();
        pile.clear();
    }


    // vergleicht den Score der Karten und legt die beste Karte fest
    public void compareValues(){
        for (Card card : pile.keySet()){ // Die Methode keySet liefert die Menge der Schlüssel in der HashMap
            if (card != besteKarte){
                Integer nextCardValue = getCardCategoryValue(card, kategorie); // holt sich den Wert der Karte

                if (nextCardValue > zuSchlagenderScore){
                    cardsToBeat.remove(besteKarte); // besteKarte wird entfernt, weil sie schlechter ist
                    cardsToBeat.put(card, pile.get(card)); // wird der HashMap cardsToBeat geaddet
                    besteKarte = card; // die neue Karte ist nun die beste Karte
                    zuSchlagenderScore = nextCardValue; // der neue Score ist der der neuen besten Karte
                } else if (nextCardValue.equals(zuSchlagenderScore)){
                   // bei einem unentschieden -> karte wird auf cardsToBeat gelegt
                    cardsToBeat.put(card, pile.get(card));
                }
            }  // else: geht zur nächsten Karte weiter
        }
    }

    public boolean checkRoundWin(){
        // cardsToBeat ist größer als 1, wenn unentschieden (braucht min. 2 Karten von 2 Spielern)
        if (cardsToBeat.size() == 1){
            roundDraw = false;
            for (Card card : cardsToBeat.keySet()){
                gewinnerRunde = cardsToBeat.get(card); // holt sich die aktuelle Karte vom Stapel (was immer die größere ist)
                gewinnerKarteRunde = card; // Karte wird nochmal explizit als gewinnende Karte ausgewiesen / gespeichert
                scoreGewinnerRunde = getCardCategoryValue(card, kategorie); // switch case zu den Kategorien (oben im Code)
                gewinnerRunde.addWin();
                return true; // return true: Runde wurde gewonnen
            }
        } // hier für den Fall, dass es ein unentschieden gibt
        roundDraw = true; // runde unentschieden true
        moveToDrawPile(); // ruft Methode auf, die die Karte auf den Stapel "Draw Pile" legt
        return false; // return false: Spiel wurde nicht gewonnen
    }

    // der Gewinner bekommt die Karten aus dem Pile Stapel auf seine "Hand"
    public void winnerTakePileCards(){
        for (Card card : pile.keySet()){
            gewinnerRunde.addToHand(card);
        }
        pile.clear();
    }

    // der Gewinner der nächsten Runde, nach dem unentschieden, erhält auch zusätzlich zum pile den drawPile
    public void winnerTakeDrawPileCards(){
        for (Card card : drawPile){
            gewinnerRunde.addToHand(card);
        }
        drawPile.clear();
    }

    // prüft, ob es schon einen Gewinner des gesamten Spiels gibt
    public void checkWin(){
        // anzahl aller karten - ungenutzte Karten = Anzahl an Karten, die bespielt wurden
        int numCardsInPlay = anzKarten - anzUngenutzteKarten;

        for (Playable player : spieler){
            if (player.getNumCards() + getDrawPileSize() == numCardsInPlay){ // wenn ein Spieler so viele Karten im Spielstapel hat, plus Drawpile, wie es generell Karten gibt
                spielGewonnen = true; // dann ist das Spiel gewonnen und der boolean true
                gewinner = player; // der gewinner ist der Spieler, der das if-Statement erfüllt hat
                player.addWin(); // addWin() gibt die Meldung zum Gewinner aus
            }
        }
    }

    // legt fest, wer als nächstes im Spielzug dran ist
    public void changeTurn(){
        playerTurn = gewinnerRunde;
    }  // der Gewinner der Runde, darf auch in der nächsten Runde die Kategorie raussuchen

    // holt sich die Karten aus der Liste vom Gewinner (toMove = Zwischenspeicher Liste) und fügt sie beim Dealer ein
    public void returnCardsToDealer(){
        // return cards to dealer's deck
        // need additional toMove ArrayList to prevent iterating over and modifying winnerHand
        ArrayList <Card> toMove = new ArrayList<>();
        ArrayList<Card> winnerHand = gewinner.getHand();

        for (Card card : winnerHand){
            toMove.add(card);
        }

        for (Card card : toMove){
            dealer.demDeckHinzufuegen(card); // fügt die Karten aus dem toMove in die Liste des Dealers ein
            gewinner.removeFromHand(card); // Karten werden dem Gewinner entnommen
        }
        toMove.clear(); // Zwischenspeicher Liste wird wieder geleert
    }

}

