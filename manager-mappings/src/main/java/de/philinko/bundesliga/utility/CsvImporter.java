package de.philinko.bundesliga.utility;

import de.philinko.bundesliga.manager.mappings.Besitz;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Position;
import de.philinko.bundesliga.manager.mappings.Spieler;
import de.philinko.bundesliga.manager.mappings.Verein;
import de.philinko.bundesliga.manager.mappings.Vereinszuordnung;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author philippe
 */
public class CsvImporter {

    private EntityManager em;

    public static void main(String... args) throws IOException {
        File csvFile;
        if (args.length > 0) {
            csvFile = new File(args[0]);
        } else {
            csvFile = new File("/daten/Kicker/spieler2012.csv");
        }
        CsvImporter instance = new CsvImporter();
        instance.em = Persistence.createEntityManagerFactory("bundesliga-pu").createEntityManager();
        instance.initializeDB();
        instance.importIntoDB(csvFile);

    }

    private void importIntoDB(File csvFile) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csvFile), Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        String line;
        int lineNumber = 1;
        while ((line = br.readLine()) != null) {
            String[] content = line.split(";");
            Spieler spieler = new Spieler();
            spieler.setName(content[0]);
            spieler.setPosition(this.parsePosition(content[2]));
            Besitz besitz = new Besitz();
            besitz.setSpieler(spieler);
            besitz.setBesitzer(new Kontrahent(content[3]));
            besitz.setId(lineNumber);
            Vereinszuordnung vereinZuordnung = new Vereinszuordnung();
            vereinZuordnung.setSpieler(spieler);
            vereinZuordnung.setVerein(new Verein(content[1]));
            vereinZuordnung.setId(lineNumber++);
            em.getTransaction().begin();
            em.persist(spieler);
            em.persist(besitz);
            em.persist(vereinZuordnung);
            em.getTransaction().commit();
        }
    }

    private Position parsePosition(String string) {
        if (string.startsWith("1.")) {
            return Position.TOR;
        }
        if (string.startsWith("2.")) {
            return Position.ABWEHR;
        }
        if (string.startsWith("3.")) {
            return Position.MITTELFELD;
        }
        if (string.startsWith("4.")) {
            return Position.ANGRIFF;
        }
        return null;
    }

    private void initializeDB() {
        em.getTransaction().begin();
        String[] mitspieler = new String[]{"Philippe", "Pierre", "Rémi", "Roland"};
        for (String teilnehmer : mitspieler) {
            Kontrahent kontrahent = new Kontrahent(teilnehmer);
            em.persist(kontrahent);
        }
        String[] vereine = new String[]{
            "Bayer Leverkusen",
            "Bayern München",
            "Borussia Dortmund",
            "Borussia Mönchengladbach",
            "FC Augsburg",
            "1. FC Kaiserslautern",
            "1. FC Köln",
            "FC Nürnberg",
            "Hamburger SV",
            "Hannover 96",
            "Hertha BSC",
            "Mainz 05",
            "SC Freiburg",
            "Schalke 04",
            "TSG Hoffenheim",
            "VfB Stuttgart",
            "VfL Wolfsburg",
            "Werder Bremen",
            "Fortuna Düsseldorf",
            "Greuther Fürth",
            "Eintracht Frankfurt"
        };
        for (String verein : vereine) {
            Verein toStore = new Verein(verein);
            em.persist(toStore);
        }
        em.getTransaction().commit();
    }
}
