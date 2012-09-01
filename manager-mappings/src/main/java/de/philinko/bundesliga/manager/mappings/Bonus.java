package de.philinko.bundesliga.manager.mappings;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author philippe
 */
@Entity
public class Bonus {
    @Id
    private int spieltag;
    @Id
    @ManyToOne
    private Kontrahent kontrahent;
    private int torBonus;
    private int vorlagenBonus;
    private int gegentorBonus;
    private int notenBonus;

    public int getSpieltag() {
        return spieltag;
    }

    public void setSpieltag(int spieltag) {
        this.spieltag = spieltag;
    }

    public Kontrahent getKontrahent() {
        return kontrahent;
    }

    public void setKontrahent(Kontrahent kontrahent) {
        this.kontrahent = kontrahent;
    }

    public int getTorBonus() {
        return torBonus;
    }

    public void setTorBonus(int torBonus) {
        this.torBonus = torBonus;
    }

    public int getVorlagenBonus() {
        return vorlagenBonus;
    }

    public void setVorlagenBonus(int vorlagenBonus) {
        this.vorlagenBonus = vorlagenBonus;
    }

    public int getGegentorBonus() {
        return gegentorBonus;
    }

    public void setGegentorBonus(int gegentorBonus) {
        this.gegentorBonus = gegentorBonus;
    }

    public int getNotenBonus() {
        return notenBonus;
    }

    public void setNotenBonus(int notenBonus) {
        this.notenBonus = notenBonus;
    }    
}
