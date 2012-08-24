package de.philinko.bundesliga.manager.business.api;

import de.philinko.bundesliga.manager.mappings.Aufstellung;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;
import java.math.BigDecimal;

public interface AufstellungService {
    public void aufstellungenSpeichern(Aufstellung[] aufstellungen);
//    public void aufstellungEintragen(int spieltag, Spieler[] spieler, Kontrahent mitspieler);
//    public void spielerAendern(int spieltag, Spieler spieler, int posInAufstellung);
//    public Spieler[] aufstellungAbfragen(int spieltag, Kontrahent mitspieler);
//    public void noteEintragen(int spieltag, Spieler spieler, BigDecimal note);
//    public void gegentoreEintragen(int spieltag, Spieler spieler, int gegentore);
//    public void toreEintragen(int spieltag, Spieler spieler, int tore);
//    public void vorlagenEintragen(int spieltag, Spieler spieler, int vorlagen);
//    public Kontrahent aktuellerBesitzer(int spieltag, Spieler spieler);

    public Kontrahent aktuellerBesitzer(int spieltag, Spieler spieler);

    public Aufstellung[] aufstellungAbfragen(int spieltag, Kontrahent mitspieler);

    public void aufstellungAktualisieren(int spieltag, Spieler spieler, Kontrahent mitspieler, int posInAufstellung);
}
