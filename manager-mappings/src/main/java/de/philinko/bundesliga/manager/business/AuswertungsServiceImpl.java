package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.dto.AuswertungDTO;
import de.philinko.bundesliga.dto.GesamtDTO;
import de.philinko.bundesliga.manager.business.api.AuswertungsService;
import de.philinko.bundesliga.manager.mappings.Auswertung;
import de.philinko.bundesliga.manager.mappings.Bewertung;
import de.philinko.bundesliga.manager.mappings.Bonus;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;
import de.philinko.bundesliga.utility.CommonFunctions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    
    public List<AuswertungDTO> getFussballerAuswertungGesamt(Kontrahent mitspieler) {
    	Query query;
        String queryText = "Select a.spieler, sum(b.tore), sum(b.vorlagen), sum(b.gegentore), avg(b.note), sum(b.punkte)  from Bewertung b, Aufstellung a where a.spieltag=b.spieltag and a.spieler=b.spieler ";
        if (mitspieler != null) {
            queryText += " and kontrahent = :mitspieler ";
        }
        queryText += " group by a.spieler order by sum(b.punkte) desc";
        query = em.createQuery(queryText);
        if (mitspieler != null) {
            query = query.setParameter("mitspieler", mitspieler);
        }
        List resultList = query.getResultList();
        List<AuswertungDTO> result = new ArrayList<AuswertungDTO>(resultList.size());
        for (Iterator it = resultList.iterator(); it.hasNext();) {
            Object[] row = (Object[]) it.next();
            AuswertungDTO toInsert = new AuswertungDTO();
            toInsert.setSpieler((Spieler) row[0]);
            toInsert.setTore(((Long)row[1]).intValue());
            toInsert.setVorlagen(((Long)row[2]).intValue());
            toInsert.setGegentore(((Long)row[3]).intValue());
            BigDecimal schnitt = new BigDecimal((Double) row[4]);
            schnitt = schnitt.setScale(3, RoundingMode.HALF_UP);
            toInsert.setNotenschnitt(schnitt);
            toInsert.setGesamtpunkte(((Long)row[5]).intValue());
            result.add(toInsert);
        }
        return result;
    }

    public void berechneAuswertungen(int letzterSpieltag) {
        this.berechneBonus(letzterSpieltag);
        em.getTransaction().begin();
        Query delete = em.createQuery("Delete from Auswertung where spieltag=:spieltag");
        delete = delete.setParameter("spieltag", letzterSpieltag);
        delete.executeUpdate();
        Query query = em.createQuery("Select a.mitspieler, sum(b.tore), sum(b.vorlagen), sum(b.gegentore), avg(b.note), sum(b.punkte) from Bewertung b, Aufstellung a where b.spieltag = :spieltag and a.spieltag=b.spieltag and a.spieler=b.spieler group by a.mitspieler");
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
            schnitt = schnitt.setScale(3, RoundingMode.HALF_UP);
            auswertung.setNotenschnitt(schnitt);
            auswertung.setGesamtpunkte(((Long)row[5]).intValue());
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
        TypedQuery<Auswertung> query = em.createQuery("Select a from Auswertung a order by a.spieltag desc, a.mitspieler.name asc", Auswertung.class);
        return query.getResultList();
    }

    public List<GesamtDTO> getGesamt() {
        Query query = em.createQuery("Select a.mitspieler, sum(a.tore), sum(a.vorlagen), sum(a.gegentore), avg(a.notenschnitt), sum(a.gesamtpunkte), sum(a.bonuspunkte) from Auswertung a group by a.mitspieler");
        List<Object> gesamtStand = query.getResultList();
        List<GesamtDTO> result = new ArrayList(gesamtStand.size());
        for (Object item : gesamtStand) {
            Object[] row = (Object[]) item;
            Kontrahent kontrahent = (Kontrahent) row[0];
            GesamtDTO auswertung = new GesamtDTO();
            auswertung.setMitspieler(kontrahent);
            auswertung.setTore(((Long)row[1]).intValue());
            auswertung.setVorlagen(((Long)row[2]).intValue());
            auswertung.setGegentore(((Long)row[3]).intValue());
            BigDecimal schnitt = new BigDecimal(row[4].toString());
            schnitt = schnitt.setScale(3, RoundingMode.HALF_UP);
            auswertung.setNotenschnitt(schnitt);
            auswertung.setGesamtpunkte(((Long)row[5]).intValue());
            auswertung.setBonuspunkte(((Long)row[6]).intValue());
            result.add(auswertung);
        }
        return result;
    }

    public int gesamtPunkteSpieler(Spieler spieler) {
        Query query = em.createQuery("Select sum(b.punkte) from Bewertung b where b.spieler = :spieler");
        query = query.setParameter("spieler", spieler);
        Long result = (Long) query.getSingleResult();
        return (result == null ? 0 : result.intValue());
    }
    
	public int letzte3PunkteSpieler(Spieler spieler) {
        Query query = em.createQuery("Select sum(b.punkte) from Bewertung b where b.spieler = :spieler and b.spieltag >= :spieltag");
        query = query.setParameter("spieler", spieler);
        query = query.setParameter("spieltag", aktuellerSpieltag()-3);
        Long result = (Long) query.getSingleResult();
        return (result == null ? 0 : result.intValue());
	}

	public List<Bewertung> getFussballerAuswertungen(int spieltag, Kontrahent mitspieler) {
        TypedQuery<Bewertung> query = em.createQuery("Select a from Bewertung a, Besitz b where a.spieltag = :spieltag and a.spieler=b.spieler and b.besitzer=:mitspieler and b.beginn<=:spieltag and b.ende>=:spieltag order by a.spieler.position", Bewertung.class);
        query = query.setParameter("spieltag", spieltag);
        query = query.setParameter("mitspieler", mitspieler);
        List<Bewertung> bewertungen = query.getResultList();
        return bewertungen;
    }
}
