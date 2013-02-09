package de.philinko.bundesliga.manager.ui;

import de.philinko.bundesliga.dto.AufstellungDTO;
import de.philinko.bundesliga.manager.business.AufstellungController;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;
import de.philinko.bundesliga.utility.CommonFunctions;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author philippe
 */
public class AufstellungViewModel {

    private int aktuellerSpieltag;
    private List<Integer> spieltage;
    private Kontrahent aktuellerMitspieler;
    private List<Kontrahent> mitspielerListe;
    private List<AufstellungDTO> verfuegbareSpieler;
    private Set<AufstellungDTO> aufgestellteSpieler;
    private AufstellungController controller;

    @Init
    public void initialize() {
        spieltage = CommonFunctions.initializeIntList(34);
        controller = new AufstellungController();
        aktuellerSpieltag = controller.aktuellerSpieltag();
        mitspielerListe = controller.getKontrahenten();
        aktuellerMitspieler = mitspielerListe.get(1);
        aufgestellteSpieler = new HashSet<AufstellungDTO>();
        aufstellungLaden();
    }

    @Command
    @NotifyChange({"verfuegbareSpieler","aufgestellteSpieler"})
    public void aufstellungLaden() {
        verfuegbareSpieler = controller.spielerListeLaden(aktuellerSpieltag, aktuellerMitspieler);
        aufgestellteSpieler.clear();
        for (AufstellungDTO spieler : verfuegbareSpieler) {
            if (spieler.isEingesetzt()) {
                aufgestellteSpieler.add(spieler);
            }
        }
    }

    @Command
    @NotifyChange({"verfuegbareSpieler","aufgestellteSpieler"})
    public void aufstellungLadenVorwoche() {
        verfuegbareSpieler = controller.spielerListeLaden(aktuellerSpieltag-1, aktuellerMitspieler);
        aufgestellteSpieler.clear();
        for (AufstellungDTO spieler : verfuegbareSpieler) {
            if (spieler.isEingesetzt()) {
                aufgestellteSpieler.add(spieler);
            }
        }
        if (aufgestellteSpieler.isEmpty()) {
        	Messagebox.show("Keine Aufstellung für vorherigen Spieltag vorhanden");
        } else {        
        	Messagebox.show("Aufstellung des vorherigen Spieltags geladen, aber noch nicht gespeichert!");
        }
    }

    @Command
    public void aufstellungSpeichern() {
        if (aufgestellteSpieler.size() != 11) {
            throw new RuntimeException("Es sind nicht 11 Spieler aufgestellt sondern: " + aufgestellteSpieler.size());
        }
        Spieler[] spielerListe = new Spieler[11];
        int i = 0;
        for (AufstellungDTO aufstellungsDTO : aufgestellteSpieler) {
            spielerListe[i++] = aufstellungsDTO.getSpieler();
        }
        controller.aufstellungEintragen(aktuellerSpieltag, spielerListe, aktuellerMitspieler);
        Messagebox.show("Aufstellung gespeichert");
    }
    
    public int getAktuellerSpieltag() {
        return aktuellerSpieltag;
    }

    public void setAktuellerSpieltag(int aktuellerSpieltag) {
        this.aktuellerSpieltag = aktuellerSpieltag;
    }

    public Kontrahent getAktuellerMitspieler() {
        return aktuellerMitspieler;
    }

    public void setAktuellerMitspieler(Kontrahent aktuellerMitspieler) {
        this.aktuellerMitspieler = aktuellerMitspieler;
    }

    public List<Kontrahent> getMitspielerListe() {
        return mitspielerListe;
    }

    public void setMitspielerListe(List<Kontrahent> mitspielerListe) {
        this.mitspielerListe = mitspielerListe;
    }

    public List<AufstellungDTO> getVerfuegbareSpieler() {
        return verfuegbareSpieler;
    }

    public void setVerfuegbareSpieler(List<AufstellungDTO> verfuegbareSpieler) {
        this.verfuegbareSpieler = verfuegbareSpieler;
    }

    public List<Integer> getSpieltage() {
        return spieltage;
    }

    public Set<AufstellungDTO> getAufgestellteSpieler() {
        return aufgestellteSpieler;
    }

    public void setAufgestellteSpieler(Set<AufstellungDTO> aufgestellteSpieler) {
        this.aufgestellteSpieler = aufgestellteSpieler;
    }

}
