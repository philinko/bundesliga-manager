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
public class Besitz implements Serializable {
    @Id
    private int id;
    @JoinColumn
    @ManyToOne
    private Kontrahent besitzer;
    @JoinColumn(nullable=false)
    @ManyToOne
    private Spieler spieler;
    private int beginn = 1;
    private int ende = 34;

    public Kontrahent getBesitzer() {
        return besitzer;
    }

    public void setBesitzer(Kontrahent besitzer) {
        this.besitzer = besitzer;
    }

    public Spieler getSpieler() {
        return spieler;
    }

    public void setSpieler(Spieler spieler) {
        this.spieler = spieler;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.besitzer != null ? this.besitzer.hashCode() : 0);
        hash = 79 * hash + (this.spieler != null ? this.spieler.hashCode() : 0);
        hash = 79 * hash + this.beginn;
        hash = 79 * hash + this.ende;
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
        final Besitz other = (Besitz) obj;
        if (this.besitzer != other.besitzer && (this.besitzer == null || !this.besitzer.equals(other.besitzer))) {
            return false;
        }
        if (this.spieler != other.spieler && (this.spieler == null || !this.spieler.equals(other.spieler))) {
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
