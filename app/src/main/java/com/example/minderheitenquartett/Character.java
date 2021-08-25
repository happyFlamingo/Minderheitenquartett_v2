package com.example.minderheitenquartett;

public enum Character {

    /* Enum: vordefinierte Konstanten für meine Variablen
    -> sinnvoll, wenn eine Variable nur eine kleine Anzahl von Werten bestitzt (Spielekarten, Wochentage oä.) */

    // R.drawable: der Pfad links unter "project", der alle Bilder abspeichert -> und dann jeweils das gewünschte pgn

    STUDENTEN("Studenten", 20, 40, 10, 30, 40,15, R.drawable.studenten),
    NAZIS("Nazis", 10, 10, 20, 45, 10,10, R.drawable.nazis),
    HARTZ4EMPFAENGER("Hartz 4 Empfaenger", 35, 0, 15, 40, 0,50, R.drawable.hart4),
    MUSLIME("Muslime", 40, 15, 20, 45, 20,0, R.drawable.muslime),
    NEGER("Neger", 20, 25, 10, 20, 30,10, R.drawable.neger),
    SCHWULE("Schwule", 25, 40, 45, 40, 20,15, R.drawable.schwule),
    JUDEN("Juden", 20, 40, 50, 40, 50,15, R.drawable.juden),
    PAEDOPHILE("Pädophile", 5, 30, 25, 15, 0,50, R.drawable.paedophile),
    FEMINISTEN("Feministen", 25, 35, 30, 50, 25,35, R.drawable.feministen),
    ROLLSTUHLFAHRER("Rollstuhlfahrer", 5, 25, 25, 25, 40,30, R.drawable.rollstuhlfahrer),
    KINDER("Kinder", 30, 0, 5, 45, 20,0, R.drawable.kinder),
    MIGRANTEN("Migranten", 40, 0, 10, 50, 0,30, R.drawable.migranten);

    // Deklaration aller benötigten Variablen für den Charakter
    String name;
    int bevoelkerungsanteil;
    int bildungsniveau;
    int wohlstand;
    int homogenitaet;
    int gesellschaftlicheAkzeptanz;
    int schamgefuehl;
    int imageSource;

    // Konstruktor zum Charakter
    Character(String name, int bevoelkerungsanteil, int bildungsniveau, int wohlstand, int homogenitaet, int gesellschaftlicheAkzeptanz,  int schamgefuehl, int imageSource) {
        this.name = name;
        this.bevoelkerungsanteil = bevoelkerungsanteil;
        this.bildungsniveau = bildungsniveau;
        this.wohlstand = wohlstand;
        this.homogenitaet = homogenitaet;
        this.gesellschaftlicheAkzeptanz = gesellschaftlicheAkzeptanz;
        this.schamgefuehl = schamgefuehl;
        this.imageSource = imageSource;
    }

    // getter Methoden zum Charakter
    public String getName() {
        return name;
    }

    public int getBevoelkerungsanteil() {
        return bevoelkerungsanteil;
    }

    public int getBildungsniveau() {
        return bildungsniveau;
    }

    public int getWohlstand() {
        return wohlstand;
    }

    public int getHomogenitaet() {
        return homogenitaet;
    }

    public int getGesellschaftlicheAkzeptanz() {
        return gesellschaftlicheAkzeptanz;
    }

    public int getSchamgefuehl() {
        return schamgefuehl;
    }

    public int getImageSource() {
        return imageSource;
    }

}
