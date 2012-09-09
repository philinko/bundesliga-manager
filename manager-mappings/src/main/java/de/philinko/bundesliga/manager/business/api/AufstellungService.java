package de.philinko.bundesliga.manager.business.api;

import de.philinko.bundesliga.manager.mappings.Aufstellung;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;

public interface AufstellungService {
    public void aufstellungenSpeichern(Aufstellung[] aufstellungen);

    public Kontrahent aktuellerBesitzer(int spieltag, Spieler spieler);

    public Aufstellung[] aufstellungAbfragen(int spieltag, Kontrahent mitspieler);

    public void aufstellungAktualisieren(int spieltag, Spieler spieler, Kontrahent mitspieler, int posInAufstellung);
    public Spieler[] aktuelleSpieler(int spieltag, Kontrahent mitspieler);
}
