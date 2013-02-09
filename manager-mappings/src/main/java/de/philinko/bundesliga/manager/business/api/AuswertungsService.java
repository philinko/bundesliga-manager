package de.philinko.bundesliga.manager.business.api;

import de.philinko.bundesliga.dto.AuswertungDTO;
import de.philinko.bundesliga.dto.GesamtDTO;
import de.philinko.bundesliga.manager.mappings.Auswertung;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;
import java.util.List;

/**
 *
 * @author philippe
 */
public interface AuswertungsService {
    
    public void berechneBonus(int spieltag);

    public void berechneAuswertungen(int letzterSpieltag);

    public List<Auswertung> getAuswertungen();

    public List<GesamtDTO> getGesamt();

    public int gesamtPunkteSpieler(Spieler spieler);

	public int letzte3PunkteSpieler(Spieler spieler);
	
	public List<AuswertungDTO> getFussballerAuswertungen(int spieltag, Kontrahent mitspieler);
}
