package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.dto.AuswertungDTO;
import de.philinko.bundesliga.dto.GesamtDTO;
import de.philinko.bundesliga.manager.business.api.AuswertungsService;
import de.philinko.bundesliga.manager.mappings.Auswertung;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

/**
 *
 * @author philippe
 */
public class AuswertungsController {
    private Map<String, Integer> gesamtPunkte;
    private List<AuswertungDTO> spieltagAuswertungen;
    private AuswertungsService service;

    public AuswertungsController() {
        gesamtPunkte = new HashMap<String, Integer>();
        spieltagAuswertungen = new ArrayList<AuswertungDTO>();
        service = new AuswertungsServiceImpl();
    }

    public void recalculate() {
        gesamtPunkte = new HashMap<String, Integer>();
        spieltagAuswertungen = new ArrayList<AuswertungDTO>();

    }

    public List<Auswertung> getAuswertungen() {
        return service.getAuswertungen();
    }

    public List<GesamtDTO> getGesamt() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
