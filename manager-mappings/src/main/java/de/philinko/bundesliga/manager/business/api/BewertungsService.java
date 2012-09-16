package de.philinko.bundesliga.manager.business.api;

import de.philinko.bundesliga.manager.mappings.Bewertung;
import de.philinko.bundesliga.manager.mappings.Spieler;
import de.philinko.bundesliga.manager.mappings.Verein;
import java.util.List;
import java.util.Set;

/**
 *
 * @author philippe
 */
public interface BewertungsService {

    public void bewertungEintragen(List<Bewertung> bewertungen);

    public List<Verein> ladeVereine();

    public Set<Bewertung> bewertungenLaden(int aktuellerSpieltag, Verein aktuellerVerein);

    public List<Spieler> spielerVonVereinLaden(int aktuellerSpieltag, Verein aktuellerVerein);
    
}
