package de.philinko.bundesliga.manager.business.api;

import de.philinko.bundesliga.manager.mappings.Bewertung;
import de.philinko.bundesliga.manager.mappings.Spieler;
import de.philinko.bundesliga.manager.mappings.Verein;
import java.util.List;
import java.util.Map;

/**
 *
 * @author philippe
 */
public interface BewertungsService {

    public void bewertungEintragen(List<Bewertung> bewertungen);

    public List<Verein> ladeVereine();

    public Map<Spieler, Bewertung> bewertungenLaden(int spieltag, Verein verein);

    public List<Spieler> spielerVonVereinLaden(int aktuellerSpieltag, Verein aktuellerVerein);
    
}
