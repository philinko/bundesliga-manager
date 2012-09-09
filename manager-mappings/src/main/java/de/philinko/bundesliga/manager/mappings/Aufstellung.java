package de.philinko.bundesliga.manager.mappings;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author philippe
 */
@Entity
public class Aufstellung implements Serializable {
    @Id
    private int num;
    @Id
    private int spieltag;
    @JoinColumn
    @ManyToOne
    private Spieler spieler;
    @ManyToOne
    private Kontrahent mitspieler;

    public Aufstellung(int spieltag, Spieler spieler, Kontrahent mitspieler, int num) {
        this.num = num;
        this.spieltag = spieltag;
        this.spieler = spieler;
        this.mitspieler = mitspieler;
    }
    
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSpieltag() {
        return spieltag;
    }

    public void setSpieltag(int spieltag) {
        this.spieltag = spieltag;
    }

    public Spieler getSpieler() {
        return spieler;
    }

    public void setSpieler(Spieler spieler) {
        this.spieler = spieler;
    }

    public Kontrahent getMitspieler() {
        return mitspieler;
    }

    public void setMitspieler(Kontrahent mitspieler) {
        this.mitspieler = mitspieler;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.spieltag;
        hash = 71 * hash + (this.spieler != null ? this.spieler.hashCode() : 0);
        hash = 71 * hash + (this.mitspieler != null ? this.mitspieler.hashCode() : 0);
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
        final Aufstellung other = (Aufstellung) obj;
        if (this.spieltag != other.spieltag) {
            return false;
        }
        if (this.spieler != other.spieler && (this.spieler == null || !this.spieler.equals(other.spieler))) {
            return false;
        }
        if (this.mitspieler != other.mitspieler && (this.mitspieler == null || !this.mitspieler.equals(other.mitspieler))) {
            return false;
        }
        return true;
    }
    
}
