package de.philinko.bundesliga.manager.mappings;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

/**
 *
 * @author philippe
 */
@Entity
public class Vereinszuordnung implements Serializable {
    @Id
    private int id;
    @JoinColumn(nullable=false)
    private Spieler spieler;
    @JoinColumn(nullable=false)
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
}
