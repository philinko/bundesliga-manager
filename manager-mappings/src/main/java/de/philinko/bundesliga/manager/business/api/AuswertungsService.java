package de.philinko.bundesliga.manager.business.api;

/**
 *
 * @author philippe
 */
public interface AuswertungsService {
    
    public int berechneSpieltagsPunkte(int spieltag, String mitspieler);
    
    public int berechneGesamtPunkte(String mitspieler);
    
    public void berechneBonus(int spieltag);
}
