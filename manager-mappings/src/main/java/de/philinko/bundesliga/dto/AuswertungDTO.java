package de.philinko.bundesliga.dto;

import de.philinko.bundesliga.manager.mappings.Kontrahent;
import java.math.BigDecimal;

/**
 *
 * @author philippe
 */
public class AuswertungDTO {
    private int spieltag;
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
    
}
