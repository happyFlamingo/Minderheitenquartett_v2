package com.example.minderheitenquartett;

public class Card {

    private String name;
    private Integer bevoelkerungsanteil;
    private Integer wohlstand ;
    private Integer bildungsniveau;
    private Integer homogenitaet;
    private Integer gesellschaftlicheAkzeptanz;
    private Integer schamgefuehl;
    private Integer imageSource;

        // Konstruktor zur Spielkarte
    Card(String name, Integer bevoelkerungsanteil, Integer bildungsniveau, Integer wohlstand, Integer homogenitaet, Integer gesellschaftlicheAkzeptanz, Integer schamgefuehl, Integer imageSource) {
        this.name = name;
        this.bevoelkerungsanteil = bevoelkerungsanteil;
        this.bildungsniveau = bildungsniveau;
        this.wohlstand = wohlstand;
        this.homogenitaet = homogenitaet;
        this.gesellschaftlicheAkzeptanz = gesellschaftlicheAkzeptanz;
        this.schamgefuehl = schamgefuehl;
        this.imageSource = imageSource;
    }

        // getter Methoden
        public String getName() {
            return name;
        }

        public Integer getBevoelkerungsanteil() {
            return bevoelkerungsanteil;
        }

        public Integer getBildungsniveau() {
            return bildungsniveau;
        }

        public Integer getWohlstand() {
            return wohlstand;
        }

        public Integer getHomogenitaet() {
            return homogenitaet;
        }

        public Integer getGesellschaftlicheAkzeptanz() {
            return gesellschaftlicheAkzeptanz;
        }

        public Integer getSchamgefuehl() {
            return schamgefuehl;
        }

        public Integer getImageSource() {
            return imageSource;
        }

}

