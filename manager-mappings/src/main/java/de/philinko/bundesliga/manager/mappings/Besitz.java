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
    @JoinColumn(nullable=false)
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
}
