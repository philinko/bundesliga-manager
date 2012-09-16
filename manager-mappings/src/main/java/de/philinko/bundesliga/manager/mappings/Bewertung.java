package de.philinko.bundesliga.manager.mappings;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author philippe
 */
@Entity
public class Bewertung implements Serializable {
    @Id
    private int spieltag;
    @Id
    @JoinColumn
    @ManyToOne
    private Spieler spieler;
    private BigDecimal note = new BigDecimal("4.0");
    private int tore = 0;
    private int gegentore = 0;
    private int vorlagen = 0;
    private int eigentore = 0;
    private boolean gelb = false;
    private boolean rot = false;
    private boolean unbenotet = false;

    public Bewertung() {
    }

    public Bewertung(int spieltag, Spieler spieler) {
        this.spieltag = spieltag;
        this.spieler = spieler;
    }

    public Spieler getSpieler() {
        return spieler;
    }

    public int getSpieltag() {
        return spieltag;
    }

    public BigDecimal getNote() {
        return note;
    }

    public int getTore() {
        return tore;
    }

    public int getGegentore() {
        return gegentore;
    }

    public int getVorlagen() {
        return vorlagen;
    }

    public int getEigentore() {
        return eigentore;
    }

    public boolean isGelb() {
        return gelb;
    }

    public boolean isRot() {
        return rot;
    }

    public boolean isUnbenotet() {
        if (note.compareTo(new BigDecimal("4.0")) != 0) {
            unbenotet = false;
        }
        return unbenotet;
    }
    
    public void setNote(BigDecimal note) {
        this.note = note;
    }

    public void setTore(int tore) {
        this.tore = tore;
    }

    public void setGegentore(int gegentore) {
        this.gegentore = gegentore;
    }

    public void setVorlagen(int vorlagen) {
        this.vorlagen = vorlagen;
    }

    public void setEigentore(int eigentore) {
        this.eigentore = eigentore;
    }

    public void setGelb(boolean gelb) {
        this.gelb = gelb;
    }

    public void setRot(boolean rot) {
        this.rot = rot;
    }

    public void setUnbenotet(boolean unbenotet) {
        this.unbenotet = unbenotet;
        this.note = new BigDecimal("4.0");
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.spieltag;
        hash = 67 * hash + (this.spieler != null ? this.spieler.hashCode() : 0);
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
        final Bewertung other = (Bewertung) obj;
        if (this.spieltag != other.spieltag) {
            return false;
        }
        if (this.spieler != other.spieler && (this.spieler == null || !this.spieler.equals(other.spieler))) {
            return false;
        }
        return true;
    }
    
}
