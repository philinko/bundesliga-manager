package de.philinko.bundesliga.dto;

import de.philinko.bundesliga.manager.mappings.Kontrahent;
import java.math.BigDecimal;

/**
 *
 * @author philippe
 */
public class GesamtDTO {
    private Kontrahent mitspieler;
    private int tore;
    private int vorlagen;
    private int gegentore;
    private int gelbeKarten;
    private int roteKarten;
    private int eigentore;
    private BigDecimal notenschnitt;
    private int bonuspunkte;
    private int gesamtpunkte;

    public Kontrahent getMitspieler() {
        return mitspieler;
    }

    public void setMitspieler(Kontrahent mitspieler) {
        this.mitspieler = mitspieler;
    }

    public int getTore() {
        return tore;
    }

    public void setTore(int tore) {
        this.tore = tore;
    }

    public int getVorlagen() {
        return vorlagen;
    }

    public void setVorlagen(int vorlagen) {
        this.vorlagen = vorlagen;
    }

    public int getGegentore() {
        return gegentore;
    }

    public void setGegentore(int gegentore) {
        this.gegentore = gegentore;
    }

    public int getGelbeKarten() {
        return gelbeKarten;
    }

    public void setGelbeKarten(int gelbeKarten) {
        this.gelbeKarten = gelbeKarten;
    }

    public int getRoteKarten() {
        return roteKarten;
    }

    public void setRoteKarten(int roteKarten) {
        this.roteKarten = roteKarten;
    }

    public int getEigentore() {
        return eigentore;
    }

    public void setEigentore(int eigentore) {
        this.eigentore = eigentore;
    }

    public BigDecimal getNotenschnitt() {
        return notenschnitt;
    }

    public void setNotenschnitt(BigDecimal notenschnitt) {
        this.notenschnitt = notenschnitt;
    }

    public int getBonuspunkte() {
        return bonuspunkte;
    }

    public void setBonuspunkte(int bonuspunkte) {
        this.bonuspunkte = bonuspunkte;
    }

    public int getGesamtpunkte() {
        return gesamtpunkte;
    }

    public void setGesamtpunkte(int gesamtpunkte) {
        this.gesamtpunkte = gesamtpunkte;
    }
    
}
