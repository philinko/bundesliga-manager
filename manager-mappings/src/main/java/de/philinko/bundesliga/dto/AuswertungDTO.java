package de.philinko.bundesliga.dto;

import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;

import java.math.BigDecimal;

/**
 *
 * @author philippe
 */
public class AuswertungDTO {
    private int spieltag;
    private Spieler spieler;
    private int tore;
    private int vorlagen;
    private int gegentore;
    private BigDecimal notenschnitt;
    private int gesamtpunkte;
    
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
	public BigDecimal getNotenschnitt() {
		return notenschnitt;
	}
	public void setNotenschnitt(BigDecimal notenschnitt) {
		this.notenschnitt = notenschnitt;
	}
	public int getGesamtpunkte() {
		return gesamtpunkte;
	}
	public void setGesamtpunkte(int gesamtpunkte) {
		this.gesamtpunkte = gesamtpunkte;
	}
    
}
