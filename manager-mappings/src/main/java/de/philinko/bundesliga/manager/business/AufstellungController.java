package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.manager.business.api.AufstellungService;
import de.philinko.bundesliga.manager.mappings.Aufstellung;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;
import java.math.BigDecimal;

/**
 *
 * @author philippe
 */
public class AufstellungController {

    private AufstellungService service;

    public void aufstellungEintragen(int spieltag, Spieler[] spielerListe, Kontrahent mitspieler) {
        checkSpieltag(spieltag);
        if (spielerListe.length != 11) {
            throw new IllegalArgumentException("Eine Aufstellung muss 11 Spieler enthalten");
        }
        byte torwartZaehler = 0;
        byte abwehrZaehler = 0;
        byte mittelfeldZaehler = 0;
        byte angriffZaehler = 0;
        Aufstellung[] aufstellungen = new Aufstellung[spielerListe.length];
        for (int i = 0; i < spielerListe.length; ++i) {
            Spieler spieler = spielerListe[i];
            aufstellungen[i] = new Aufstellung(spieltag, spieler, mitspieler, i);
            switch (spieler.getPosition()) {
                case ABWEHR:
                    abwehrZaehler++;
                    break;
                case MITTELFELD:
                    mittelfeldZaehler++;
                    break;
                case ANGRIFF:
                    angriffZaehler++;
                    break;
                case TOR:
                    torwartZaehler++;
                    break;
            }
            Kontrahent aktuellerBesitzer = service.aktuellerBesitzer(spieltag, spieler);
            if (aktuellerBesitzer != mitspieler) {
                throw new IllegalArgumentException("Spieler " + spieler + " gehört " + aktuellerBesitzer + " sollte aber " + mitspieler + " gehören");
            }
        }
        if (torwartZaehler != 1) {
            throw new IllegalArgumentException("Es muss genau ein Torwart in der Aufstellung sein, es sind aber: " + torwartZaehler);
        }
        if (abwehrZaehler != 4) {
            throw new IllegalArgumentException("Es müssen genau 4 Abwehrspieler in der Aufstellung sein, es sind aber: " + abwehrZaehler);
        }
        if (Math.abs(mittelfeldZaehler - 4) > 1) {
            throw new IllegalArgumentException("Es müssen 3-5 Mittelfeldspieler in der Aufstellung sein, es sind aber: " + mittelfeldZaehler);
        }
        if (Math.abs(angriffZaehler - 2) > 1) {
            throw new IllegalArgumentException("Es müssen 1-3 Stürmer in der Aufstellung sein, es sind aber: " + angriffZaehler);
        }
        service.aufstellungenSpeichern(aufstellungen);

    }

    public void spielerAendern(int spieltag, Spieler spieler, int posInAufstellung, Kontrahent mitspieler) {
        checkSpieltag(spieltag);
        Kontrahent aktuellerBesitzer = service.aktuellerBesitzer(spieltag, spieler);
        if (aktuellerBesitzer != mitspieler) {
            throw new IllegalArgumentException("Spieler " + spieler + " gehört " + aktuellerBesitzer + " sollte aber " + mitspieler + " gehören");
        }
        Aufstellung[] bisherigeAufstellung = service.aufstellungAbfragen(spieltag, mitspieler);
        for (Aufstellung aufstellung : bisherigeAufstellung) {
            if (aufstellung.getSpieler().equals(spieler) && aufstellung.getNum() != posInAufstellung) {
                throw new IllegalArgumentException("Spieler " + spieler + " bereits eingesetzt");
            }
        }
        service.aufstellungAktualisieren(spieltag, spieler, mitspieler, posInAufstellung);
    }

    private void checkSpieltag(int spieltag) throws IllegalArgumentException {
        if (spieltag < 1 || spieltag > 34) {
            throw new IllegalArgumentException("Spieltag muss zwischen 1 und 34 liegen");
        }
    }
}
