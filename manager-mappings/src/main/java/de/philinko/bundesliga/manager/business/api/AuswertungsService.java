package de.philinko.bundesliga.manager.business.api;

import de.philinko.bundesliga.manager.mappings.Auswertung;
import java.util.List;

/**
 *
 * @author philippe
 */
public interface AuswertungsService {
    
    public void berechneBonus(int spieltag);

    public void berechneAuswertungen(int letzterSpieltag);

    public List<Auswertung> getAuswertungen();
}
