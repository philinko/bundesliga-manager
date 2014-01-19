package de.philinko.bundesliga.manager.ui;

import de.philinko.bundesliga.manager.business.AufstellungController;
import de.philinko.bundesliga.manager.business.AuswertungsController;
import de.philinko.bundesliga.manager.mappings.Bewertung;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.utility.CommonFunctions;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

/**
 *
 * @author philippe
 */
public class BerichtViewModel {

    private List<Integer> spieltage;

    public List<Integer> getSpieltage() {
        return spieltage;
    }

    public void setSpieltage(List<Integer> spieltage) {
        this.spieltage = spieltage;
    }
    private int ausgewaehlterSpieltag;
    private List<Bewertung> spielerBewertungen;

    public List<Bewertung> getSpielerBewertungen() {
        return spielerBewertungen;
    }

    public void setSpielerBewertungen(List<Bewertung> spielerBewertungen) {
        this.spielerBewertungen = spielerBewertungen;
    }
    private AuswertungsController controller;
    private AufstellungController aufstellungController;
    private Kontrahent ausgewaehlterMitspieler;
    private List<Kontrahent> mitspielerListe;

    @Init
    public void initialize() {
        spieltage = CommonFunctions.initializeIntList(34);
        controller = new AuswertungsController();
        aufstellungController = new AufstellungController();
        mitspielerListe = aufstellungController.getKontrahenten();
        ausgewaehlterMitspieler = mitspielerListe.get(1);
        ausgewaehlterSpieltag = aufstellungController.aktuellerSpieltag();
        bewertungenLaden();
    }

    private void bewertungenLaden() {
        spielerBewertungen = controller.getSpielerAuswertungenSpieltag(ausgewaehlterSpieltag, ausgewaehlterMitspieler);
    }

   @Command
   @NotifyChange({"spielerBewertungen"})
   public void bewertungenAktualisieren() {
       bewertungenLaden();
   } 

    public int getAusgewaehlterSpieltag() {
        return ausgewaehlterSpieltag;
    }

    public void setAusgewaehlterSpieltag(int ausgewaehlterSpieltag) {
        this.ausgewaehlterSpieltag = ausgewaehlterSpieltag;
    }

    public Kontrahent getAusgewaehlterMitspieler() {
        return ausgewaehlterMitspieler;
    }

    public void setAusgewaehlterMitspieler(Kontrahent ausgewaehlterMitspieler) {
        this.ausgewaehlterMitspieler = ausgewaehlterMitspieler;
    }

    public List<Kontrahent> getMitspielerListe() {
        return mitspielerListe;
    }

    public void setMitspielerListe(List<Kontrahent> mitspielerListe) {
        this.mitspielerListe = mitspielerListe;
    }

}
