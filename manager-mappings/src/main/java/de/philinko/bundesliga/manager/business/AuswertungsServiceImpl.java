package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.manager.business.api.AuswertungsService;
import de.philinko.bundesliga.manager.mappings.Auswertung;
import de.philinko.bundesliga.manager.mappings.Bonus;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.utility.CommonFunctions;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.persistence.TypedQuery;

/**
 *
 * @author philippe
 */
public class AuswertungsServiceImpl implements AuswertungsService {

    private final int BONUS_ALONE = 5;
    private final int BONUS_NOT_ALONE = 2;
    private final boolean BONUS_FOR_NOTEN = false;

    public AuswertungsServiceImpl() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("bundesliga-pu");
        }
        if (em == null) {
            em = emf.createEntityManager();
        }
    }
    @PersistenceUnit(name = "bundesliga-pu")
    private EntityManagerFactory emf;
    private EntityManager em;

    public int aktuellerSpieltag() {
        return CommonFunctions.aktuellerSpieltag(em);
    }

    public void berechneAuswertungen(int letzterSpieltag) {
        this.berechneBonus(letzterSpieltag);
        em.getTransaction().begin();
        Query delete = em.createQuery("Delete from Auswertung where spieltag=:spieltag");
        delete = delete.setParameter("spieltag", letzterSpieltag);
        delete.executeUpdate();
        Query query = em.createQuery("Select a.mitspieler, sum(b.tore), sum(b.vorlagen), sum(b.gegentore), avg(b.note), sum(b.eigentore), sum(b.punkte), sum(b.gelb), sum(b.rot) from Bewertung b, Aufstellung a where b.spieltag = :spieltag and a.spieltag=b.spieltag and a.spieler=b.spieler group by a.mitspieler");
        query = query.setParameter("spieltag", letzterSpieltag);
        Map<Kontrahent, Auswertung> auswertungen = new HashMap<Kontrahent, Auswertung>();
        List resultList = query.getResultList();
        for (Object item : resultList) {
            Object[] row = (Object[]) item;
            Kontrahent kontrahent = (Kontrahent) row[0];
            Auswertung auswertung;
            if (auswertungen.containsKey(kontrahent)) {
                auswertung = auswertungen.get(kontrahent);
            } else {
                auswertung = new Auswertung(letzterSpieltag, kontrahent);
                auswertungen.put(kontrahent, auswertung);
            }
            auswertung.setTore(((Long)row[1]).intValue());
            auswertung.setVorlagen(((Long)row[2]).intValue());
            auswertung.setGegentore(((Long)row[3]).intValue());
            BigDecimal schnitt = new BigDecimal((Double) row[4]);
            schnitt.setScale(3, RoundingMode.HALF_UP);
            auswertung.setNotenschnitt(schnitt);
            auswertung.setEigentore(((Long)row[5]).intValue());
            auswertung.setGesamtpunkte(((Long)row[6]).intValue());
            auswertung.setGelbeKarten(((Long)row[7]).intValue());
            auswertung.setRoteKarten(((Long)row[8]).intValue());
        }
        TypedQuery<Bonus> bonusQuery = em.createQuery("Select b from Bonus b where b.spieltag = :spieltag", Bonus.class);
        bonusQuery = bonusQuery.setParameter("spieltag", letzterSpieltag);
        List<Bonus> bonusList = bonusQuery.getResultList();
        for (Bonus bonus : bonusList) {
            Kontrahent kontrahent = bonus.getKontrahent();
            Auswertung auswertung;
            if (auswertungen.containsKey(kontrahent)) {
                auswertung = auswertungen.get(kontrahent);
            } else {
                auswertung = new Auswertung(letzterSpieltag, kontrahent);
                auswertungen.put(kontrahent, auswertung);
            }
            auswertung.setBonuspunkte(bonus.getGesamtBonus());
            auswertung.setGesamtpunkte(auswertung.getGesamtpunkte()+bonus.getGesamtBonus());
        }
        for (Map.Entry<Kontrahent, Auswertung> entry : auswertungen.entrySet()) {
            Auswertung auswertung = entry.getValue();
            em.persist(auswertung);
        }
        em.getTransaction().commit();
    }

    public int berechneGesamtPunkte(String mitspieler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void berechneBonus(int spieltag) {
        Query query = em.createQuery("Select SUM(b.tore), SUM(b.vorlagen), SUM(b.gegentore), AVG(b.note), a.mitspieler from Bewertung b, Aufstellung a where b.spieler = a.spieler and b.spieltag = :spieltag and a.spieltag = :spieltag group by a.mitspieler");
        query.setParameter("spieltag", spieltag);
        List result = query.getResultList();
        Set<Kontrahent> toreBonus = new HashSet<Kontrahent>();
        Set<Kontrahent> vorlagenBonus = new HashSet<Kontrahent>();
        Set<Kontrahent> gtBonus = new HashSet<Kontrahent>();
        Set<Kontrahent> notenBonus = new HashSet<Kontrahent>();
        long toreMax = 0;
        long vorlagenMax = 0;
        long gtMin = Long.MAX_VALUE;
        Double notenMin = 6.0;
        for (Object data : result) {
            Object[] values = (Object[]) data;
            Long tore = (Long) values[0];
            if (tore > toreMax) {
                toreBonus.clear();
                toreBonus.add((Kontrahent) values[values.length - 1]);
                toreMax = tore;
            } else if (tore == toreMax) {
                toreBonus.add((Kontrahent) values[values.length - 1]);
            }
            Long vorlagen = (Long) values[1];
            if (vorlagen > vorlagenMax) {
                vorlagenBonus.clear();
                vorlagenBonus.add((Kontrahent) values[values.length - 1]);
                vorlagenMax = vorlagen;
            } else if (vorlagen == vorlagenMax) {
                vorlagenBonus.add((Kontrahent) values[values.length - 1]);
            }
            Long gt = (Long) values[2];
            if (gt < gtMin) {
                gtBonus.clear();
                gtBonus.add((Kontrahent) values[values.length - 1]);
                gtMin = gt;
            } else if (gt == gtMin) {
                gtBonus.add((Kontrahent) values[values.length - 1]);
            }
            Double noten = (Double) values[3];
            if (noten < notenMin) {
                notenBonus.clear();
                notenBonus.add((Kontrahent) values[values.length - 1]);
                notenMin = noten;
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
            toUpdate.setTorBonus(toreBonus.size() == 1 ? BONUS_ALONE : BONUS_NOT_ALONE);
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
            toUpdate.setVorlagenBonus(vorlagenBonus.size() == 1 ? BONUS_ALONE : BONUS_NOT_ALONE);
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
            toUpdate.setGegentorBonus(gtBonus.size() == 1 ? BONUS_ALONE : BONUS_NOT_ALONE);
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
            delete = delete.setParameter("spieltag", spieltag);
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

    public List<Auswertung> getAuswertungen() {
        TypedQuery<Auswertung> query = em.createQuery("Select a from Auswertung a order by a.spieltag asc, a.mitspieler.name asc", Auswertung.class);
        return query.getResultList();
    }
}
