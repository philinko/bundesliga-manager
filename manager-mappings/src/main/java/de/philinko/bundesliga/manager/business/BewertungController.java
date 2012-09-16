package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.manager.business.api.BewertungsService;
import de.philinko.bundesliga.manager.mappings.Bewertung;
import de.philinko.bundesliga.manager.mappings.Spieler;
import de.philinko.bundesliga.manager.mappings.Verein;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author philippe
 */
public class BewertungController {

    private BewertungsService service;

    public BewertungController() {
        service = new BewertungsServiceImpl();
    }
    public List<Verein> getVereine() {
        return service.ladeVereine();
    }

    public int aktuellerSpieltag() {
        return 1;
    }

    public void saveBewertungen(List<Bewertung> bewertungListe) {
        service.bewertungEintragen(bewertungListe);
    }

    public List<Bewertung> loadBewertungen(int spieltag, Verein verein) {
        Set<Bewertung> bewertungen = service.bewertungenLaden(spieltag, verein);
        List<Spieler> spielerListe = service.spielerVonVereinLaden(spieltag, verein);
        for (Spieler spieler : spielerListe) {
            Bewertung toInsert = new Bewertung(spieltag, spieler);
            if (!bewertungen.contains(toInsert)) {
                bewertungen.add(toInsert);
            }
        }
        List<Bewertung> result = new ArrayList<Bewertung>(bewertungen.size());
        result.addAll(bewertungen);
        return result;
    }
}
