package de.philinko.bundesliga.manager.mappings;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class Bewertung implements Serializable {

    private static final int FAKTOR_NOTE = 1;
    private static final int FAKTOR_VORLAGE = 5;
    private static final int FAKTOR_TOR = 5;
    private static final int FAKTOR_GT = -2;
    private static final int FAKTOR_GELB = -1;
    private static final int FAKTOR_ROT = -3;
    private static final int FAKTOR_ET = -3;
    private static BigDecimal NULL_PUNKTE_NOTE = new BigDecimal(3.5).setScale(1);
    private static BigDecimal ZWEI = new BigDecimal("2");
    @Id
    private int spieltag;
    @Id
    @JoinColumn
    @ManyToOne
    private Spieler spieler;
    @Column(scale=1)
    private BigDecimal note = new BigDecimal("4.0");
    private int tore = 0;
    private int gegentore = 0;
    private int vorlagen = 0;
    private int eigentore = 0;
    private int gelb = 0;
    private int rot = 0;
    private boolean unbenotet = false;
    private int punkte = -1;

    public Bewertung() {
    }

    public Bewertung(int spieltag, Spieler spieler) {
        this.spieltag = spieltag;
        this.spieler = spieler;
    }

    private void aktualisierePunkte() {
        int notenPunkte = NULL_PUNKTE_NOTE.subtract(this.note).multiply(ZWEI).intValueExact();
        this.punkte = this.tore * FAKTOR_TOR + this.vorlagen * FAKTOR_VORLAGE
                + this.gegentore * FAKTOR_GT + this.gelb * FAKTOR_GELB
                + this.rot * FAKTOR_ROT + this.eigentore * FAKTOR_ET
                + notenPunkte * FAKTOR_NOTE;
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
        return gelb != 0;
    }

    public boolean isRot() {
        return rot != 0;
    }

    public boolean isUnbenotet() {
        if (note.compareTo(new BigDecimal("4.0")) != 0) {
            unbenotet = false;
        }
        return unbenotet;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setNote(BigDecimal note) {
        this.note = note;
        aktualisierePunkte();
    }

    public void setTore(int tore) {
        this.tore = tore;
        aktualisierePunkte();
    }

    public void setGegentore(int gegentore) {
        this.gegentore = gegentore;
        aktualisierePunkte();
    }

    public void setVorlagen(int vorlagen) {
        this.vorlagen = vorlagen;
        aktualisierePunkte();
    }

    public void setEigentore(int eigentore) {
        this.eigentore = eigentore;
        aktualisierePunkte();
    }

    public void setGelb(boolean gelb) {
        this.gelb = (gelb ? 1 : 0);
        aktualisierePunkte();
    }

    public void setRot(boolean rot) {
        this.rot = (rot ? 1 : 0);
        aktualisierePunkte();
    }

    public void setUnbenotet(boolean unbenotet) {
        this.unbenotet = unbenotet;
        this.note = new BigDecimal("4.0");
        aktualisierePunkte();
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
