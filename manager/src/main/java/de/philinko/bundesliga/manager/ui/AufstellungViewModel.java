package de.philinko.bundesliga.manager.ui;

import de.philinko.bundesliga.dto.AufstellungDTO;
import de.philinko.bundesliga.manager.business.AufstellungController;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

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
        Kontrahent k = new Kontrahent("Philippe");
        mitspielerListe = new ArrayList<Kontrahent>();
        mitspielerListe.add(k);
        mitspielerListe.add(new Kontrahent("Pierre"));
        aktuellerMitspieler = k;
        aktuellerSpieltag = 5;
        spieltage = initializeIntList(34);
        verfuegbareSpieler = new ArrayList<AufstellungDTO>();
        AufstellungDTO spieler = new AufstellungDTO(aktuellerSpieltag, "Zidane", Position.MITTELFELD, "Real Madrid", true);
        spieler.setName("Ronaldo");
        spieler.setPosition(Position.ANGRIFF);
        verfuegbareSpieler.add(spieler);
        spieler = new AufstellungDTO(aktuellerSpieltag, "Ronaldo", Position.ANGRIFF, "Real Madrid", true);
        spieler.setName("Zidane, Zinedine");
        spieler.setPosition(Position.MITTELFELD);
        verfuegbareSpieler.add(spieler);
        aufgestellteSpieler = new HashSet<AufstellungDTO>();
        aufgestellteSpieler.add(spieler);
        controller = new AufstellungController();
    }

    @Command
    @NotifyChange("verfuegbareSpieler,aufgestellteSpieler")
    public void aufstellungLaden() {
        verfuegbareSpieler = controller.spielerListeLaden(aktuellerSpieltag, aktuellerMitspieler);
        aufgestellteSpieler.clear();
        for (AufstellungDTO spieler : verfuegbareSpieler) {
            if (spieler.isEingesetzt()) {
                aufgestellteSpieler.add(spieler);
            }
        }
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
        System.err.println("Called aufgestellte Spieler. Size: " + aufgestellteSpieler.size());
        return aufgestellteSpieler;
    }

    public void setAufgestellteSpieler(Set<AufstellungDTO> aufgestellteSpieler) {
        System.err.println("Called set aufgestellte Spieler");
        this.aufgestellteSpieler = aufgestellteSpieler;
    }

    private List<Integer> initializeIntList(int size) {
        List<Integer> result = new ArrayList<Integer>(34);
        for (int i = 0; i < size; ++i) {
            result.add(i + 1);
        }
        return Collections.unmodifiableList(result);
    }
}
