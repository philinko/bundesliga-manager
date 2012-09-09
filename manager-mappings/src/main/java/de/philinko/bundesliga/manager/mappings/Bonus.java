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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.spieltag;
        hash = 53 * hash + (this.kontrahent != null ? this.kontrahent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bonus other = (Bonus) obj;
        if (this.spieltag != other.spieltag) {
            return false;
        }
        if (this.kontrahent != other.kontrahent && (this.kontrahent == null || !this.kontrahent.equals(other.kontrahent))) {
            return false;
        }
        return true;
    }
}
