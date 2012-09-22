package de.philinko.bundesliga.manager.ui;

import de.philinko.bundesliga.dto.GesamtDTO;
import de.philinko.bundesliga.manager.business.AuswertungsController;
import de.philinko.bundesliga.manager.mappings.Auswertung;
import java.util.List;
import org.zkoss.bind.annotation.Init;

/**
 *
 * @author philippe
 */
public class AuswertungViewModel {

    private List<Auswertung> auswertungen;
    private AuswertungsController controller;
    private List<GesamtDTO> gesamtAuswertungen;

    @Init
    public void initialize() {
        controller = new AuswertungsController();
        auswertungen = controller.getAuswertungen();
        //gesamtAuswertungen = controller.getGesamt();
    }

    public List<Auswertung> getAuswertungen() {
        return auswertungen;
    }

    public void setAuswertungen(List<Auswertung> auswertungen) {
        this.auswertungen = auswertungen;
    }

    public List<GesamtDTO> getGesamtAuswertungen() {
        return gesamtAuswertungen;
    }

    public void setGesamtAuswertungen(List<GesamtDTO> gesamtAuswertungen) {
        this.gesamtAuswertungen = gesamtAuswertungen;
    }
    
}
