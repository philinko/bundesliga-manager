package de.philinko.bundesliga.manager.ui;

import de.philinko.bundesliga.manager.business.BewertungController;
import de.philinko.bundesliga.manager.mappings.Bewertung;
import de.philinko.bundesliga.manager.mappings.Position;
import de.philinko.bundesliga.manager.mappings.Verein;
import de.philinko.bundesliga.utility.CommonFunctions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author philippe
 */
public class BewertungViewModel {

    private List<Integer> spieltage;
    private int aktuellerSpieltag;
    private List<Verein> vereine;
    private Verein aktuellerVerein;
    private List<Bewertung> bewertungListe;
    private BewertungController controller;
    private List<BigDecimal> noten;
    private int gegentore = 0;

    @Init
    public void initialize() {
        controller = new BewertungController();
        spieltage = CommonFunctions.initializeIntList(34);
        vereine = controller.getVereine();
        aktuellerSpieltag = controller.aktuellerSpieltag();
        aktuellerVerein = vereine.get(0);
        initializeNoten();
        bewertungenLaden();
    }

    @Command
    @NotifyChange({"bewertungListe","gegentore"})
    public void bewertungenSpeichernUndLaden() {
        bewertungenSpeichern(false);
        bewertungenLaden();
    }

    @Command
    @NotifyChange({"bewertungListe","gegentore"})
    public void bewertungenLaden() {
        gegentore = 0;
        bewertungListe = controller.loadBewertungen(aktuellerSpieltag, aktuellerVerein);
    }

    @Command
    public void bewertungenSpeichern() {
        bewertungenSpeichern(true);
    }

    public void bewertungenSpeichern(boolean showMessage) {
        controller.saveBewertungen(bewertungListe);
        if (showMessage) {
            Messagebox.show("Auswertung gespeichert");
        }
    }

    public void initializeNoten() {
        BigDecimal toInsert = BigDecimal.ONE;
        noten = new ArrayList<BigDecimal>(11);
        for (int i = 0; i < 11; ++i) {
            noten.add(toInsert);
            toInsert = toInsert.add(new BigDecimal("0.5"));
        }
    }

    @Command
    @NotifyChange("bewertungListe")
    public void gegentoreEintragen() {
        for (Bewertung bewertung : bewertungListe) {
            Position position = bewertung.getSpieler().getPosition();
            if (position.equals(Position.TOR) || position.equals(Position.ABWEHR)) {
                bewertung.setGegentore(gegentore);
            }
        }
    }

    public List<Integer> getSpieltage() {
        return spieltage;
    }

    public int getAktuellerSpieltag() {
        return aktuellerSpieltag;
    }

    public void setAktuellerSpieltag(int aktuellerSpieltag) {
        this.aktuellerSpieltag = aktuellerSpieltag;
    }

    public List<Verein> getVereine() {
        return vereine;
    }

    public Verein getAktuellerVerein() {
        return aktuellerVerein;
    }

    public void setAktuellerVerein(Verein aktuellerVerein) {
        this.aktuellerVerein = aktuellerVerein;
    }

    public List<Bewertung> getBewertungListe() {
        return bewertungListe;
    }

    public List<BigDecimal> getNoten() {
        return noten;
    }

    public int getGegentore() {
        return gegentore;
    }

    public void setGegentore(int gegentore) {
        this.gegentore = gegentore;
    }
}
