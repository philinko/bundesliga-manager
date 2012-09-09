package de.philinko.bundesliga.manager.mappings;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author philippe
 */
@Entity
public class Vereinszuordnung implements Serializable {
    @Id
    private int id;
    @ManyToOne
    @JoinColumn(nullable=false)
    private Spieler spieler;
    private Verein verein;
    @Column(nullable=false)
    private int beginn = 1;
    @Column(nullable=false)
    private int ende = 34;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Spieler getSpieler() {
        return spieler;
    }

    public void setSpieler(Spieler spieler) {
        this.spieler = spieler;
    }

    public Verein getVerein() {
        return verein;
    }

    public void setVerein(Verein verein) {
        this.verein = verein;
    }

    public int getBeginn() {
        return beginn;
    }

    public void setBeginn(int beginn) {
        this.beginn = beginn;
    }

    public int getEnde() {
        return ende;
    }

    public void setEnde(int ende) {
        this.ende = ende;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.spieler != null ? this.spieler.hashCode() : 0);
        hash = 89 * hash + (this.verein != null ? this.verein.hashCode() : 0);
        hash = 89 * hash + this.beginn;
        hash = 89 * hash + this.ende;
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
        final Vereinszuordnung other = (Vereinszuordnung) obj;
        if (this.spieler != other.spieler && (this.spieler == null || !this.spieler.equals(other.spieler))) {
            return false;
        }
        if (this.verein != other.verein && (this.verein == null || !this.verein.equals(other.verein))) {
            return false;
        }
        if (this.beginn != other.beginn) {
            return false;
        }
        if (this.ende != other.ende) {
            return false;
        }
        return true;
    }
}
