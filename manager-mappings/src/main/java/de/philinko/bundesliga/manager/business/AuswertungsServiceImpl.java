package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.manager.business.api.AuswertungsService;
import de.philinko.bundesliga.manager.mappings.Bonus;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

/**
 *
 * @author philippe
 */
public class AuswertungsServiceImpl implements AuswertungsService {

    public final int FAKTOR_NOTE = 1;
    public final int FAKTOR_VORLAGE = 1;
    public final int FAKTOR_TOR = 1;
    public final int FAKTOR_GT = 1;
    public final int FAKTOR_GELB = 1;
    public final int FAKTOR_ROT = 1;
    public final int FAKTOR_ET = 1;
    private final int BONUS_ALONE = 5;
    private final int BONUS_NOT_ALONE = 2;
    private final boolean BONUS_FOR_NOTEN = false;

    public AuswertungsServiceImpl() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("hagedorn-pu");
        }
        if (em == null) {
            em = emf.createEntityManager();
        }
    }
    @PersistenceUnit(name = "hagedorn-pu")
    private EntityManagerFactory emf;
    private EntityManager em;

    public int berechneSpieltagsPunkte(int spieltag, String mitspieler) {
        int result = 0;
        String formel = ":ftore * tore + :fvorlagen * vorlagen - :fgegentore*gegentore"
                + "- :fnote * (3.5-note) - :fgelb * gelb - :frot * rot - :feigentor * eigentore";
        Query query = em.createQuery("Select " + formel + " from Bewertung, Aufstellung where Bewertung.spieltag = :spieltag and Aufstellung.spieltag = :spieltag and Aufstellung.spieler = Bewertung.spieler and Aufstellung.mitspieler = :mitspieler");
        query.setParameter("ftore", FAKTOR_TOR);
        query.setParameter("fvorlagen", FAKTOR_VORLAGE);
        query.setParameter("fgegentore", FAKTOR_GT);
        query.setParameter("fnote", FAKTOR_NOTE);
        query.setParameter("fgelb", FAKTOR_GELB);
        query.setParameter("frot", FAKTOR_ROT);
        query.setParameter("feigentor", FAKTOR_ET);
        query.setParameter("mitspieler", mitspieler);
        List resultList = query.getResultList();
        for (Object object : resultList) {
            Integer punkte = (Integer) object;
            result += punkte;
        }
        return result;
    }

    public int berechneGesamtPunkte(String mitspieler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void berechneBonus(int spieltag) {
        Bonus b = new Bonus();
        Query query = em.createQuery("Select SUM(tore), SUM(vorlagen), SUM(gegentore), AVG(Note), mitspieler from Bewertung, Aufstellung where Bewertung.spieler = Aufstellung.spieler and Bewertung.spieltag = :spieltag and Aufstellung.spieltag = :spieltag group by Aufstellung.mitspieler");
        List result = query.getResultList();
        Set<Kontrahent> toreBonus = new HashSet<Kontrahent>();
        Set<Kontrahent> vorlagenBonus = new HashSet<Kontrahent>();
        Set<Kontrahent> gtBonus = new HashSet<Kontrahent>();
        Set<Kontrahent> notenBonus = new HashSet<Kontrahent>();
        int toreMax = 0;
        int vorlagenMax = 0;
        int gtMin = Integer.MAX_VALUE;
        BigDecimal notenMin = new BigDecimal(6L);
        for (Object data : result) {
            Object[] values = (Object[]) data;
            Integer tore = (Integer) values[0];
            if (tore > toreMax) {
                toreBonus.clear();
                toreBonus.add((Kontrahent) values[values.length - 1]);
            } else if (tore == toreMax) {
                toreBonus.add((Kontrahent) values[values.length - 1]);
            }
            Integer vorlagen = (Integer) values[1];
            if (vorlagen > vorlagenMax) {
                vorlagenBonus.clear();
                vorlagenBonus.add((Kontrahent) values[values.length - 1]);
            } else if (vorlagen == vorlagenMax) {
                vorlagenBonus.add((Kontrahent) values[values.length - 1]);
            }
            Integer gt = (Integer) values[2];
            if (gt < gtMin) {
                gtBonus.clear();
                gtBonus.add((Kontrahent) values[values.length - 1]);
            } else if (gt == gtMin) {
                gtBonus.add((Kontrahent) values[values.length - 1]);
            }
            BigDecimal noten = (BigDecimal) values[3];
            if (noten.compareTo(notenMin) < 0) {
                notenBonus.clear();
                notenBonus.add((Kontrahent) values[values.length - 1]);
            } else if (noten.compareTo(notenMin) == 0) {
                notenBonus.add((Kontrahent) values[values.length - 1]);
            }
        }
        if (toreMax == 0) {
            toreBonus.clear();
        }
        if (vorlagenMax == 0) {
            vorlagenBonus.clear();
        }
        updateBonusInDB(spieltag, toreBonus, vorlagenBonus, gtBonus, notenBonus);
    }

    private void updateBonusInDB(int spieltag, Set<Kontrahent> toreBonus, Set<Kontrahent> vorlagenBonus, Set<Kontrahent> gtBonus, Set<Kontrahent> notenBonus) {
        Map<String, Bonus> result = new HashMap<String, Bonus>();
        for (Kontrahent k : toreBonus) {
            Bonus toUpdate;
            if (result.containsKey(k.getName())) {
                toUpdate = result.get(k.getName());
            } else {
                toUpdate = new Bonus();
                toUpdate.setKontrahent(k);
                toUpdate.setSpieltag(spieltag);
            }
            toUpdate.setNotenBonus(toreBonus.size() == 1 ? BONUS_ALONE : BONUS_NOT_ALONE);
            result.put(k.getName(), toUpdate);
        }
        for (Kontrahent k : vorlagenBonus) {
            Bonus toUpdate;
            if (result.containsKey(k.getName())) {
                toUpdate = result.get(k.getName());
            } else {
                toUpdate = new Bonus();
                toUpdate.setKontrahent(k);
                toUpdate.setSpieltag(spieltag);
            }
            toUpdate.setNotenBonus(vorlagenBonus.size() == 1 ? BONUS_ALONE : BONUS_NOT_ALONE);
            result.put(k.getName(), toUpdate);
        }
        for (Kontrahent k : gtBonus) {
            Bonus toUpdate;
            if (result.containsKey(k.getName())) {
                toUpdate = result.get(k.getName());
            } else {
                toUpdate = new Bonus();
                toUpdate.setKontrahent(k);
                toUpdate.setSpieltag(spieltag);
            }
            toUpdate.setNotenBonus(gtBonus.size() == 1 ? BONUS_ALONE : BONUS_NOT_ALONE);
            result.put(k.getName(), toUpdate);
        }
        if (BONUS_FOR_NOTEN) {
            for (Kontrahent k : notenBonus) {
                Bonus toUpdate;
                if (result.containsKey(k.getName())) {
                    toUpdate = result.get(k.getName());
                } else {
                    toUpdate = new Bonus();
                    toUpdate.setKontrahent(k);
                    toUpdate.setSpieltag(spieltag);
                }
                toUpdate.setNotenBonus(notenBonus.size() == 1 ? BONUS_ALONE : BONUS_NOT_ALONE);
                result.put(k.getName(), toUpdate);
            }
        }
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Query delete = em.createQuery("Delete from Bonus where spieltag = :spieltag");
            delete.executeUpdate();
            for (Map.Entry<String, Bonus> entry : result.entrySet()) {
                Bonus bonus = entry.getValue();
                em.persist(bonus);
            }
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }
}
