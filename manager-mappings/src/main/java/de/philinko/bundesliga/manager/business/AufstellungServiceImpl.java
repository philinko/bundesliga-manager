package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.manager.business.api.AufstellungService;
import de.philinko.bundesliga.manager.mappings.Aufstellung;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author philippe
 */
public class AufstellungServiceImpl implements AufstellungService {
    public AufstellungServiceImpl() {
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

    public void aufstellungenSpeichern(int spieltag, Aufstellung[] aufstellungen, Kontrahent kontrahent) {
        em.getTransaction().begin();
        Query query = em.createQuery("Delete from Aufstellung a where a.spieltag = :spieltag and a.mitspieler=:mitspieler");
        query = query.setParameter("spieltag", spieltag).setParameter("mitspieler", kontrahent);
        query.executeUpdate();
        for (Aufstellung aufstellung : aufstellungen) {
            em.persist(aufstellung);
        }
        em.getTransaction().commit();
    }

    public Kontrahent aktuellerBesitzer(int spieltag, Spieler spieler) {
        TypedQuery<Kontrahent> query = em.createQuery("SELECT b.besitzer from Besitz b where b.beginn <= :spieltag and b.ende >= :spieltag and b.spieler = :spieler", Kontrahent.class);
        query = query.setParameter("spieltag", spieltag).setParameter("spieler", spieler);
        return query.getSingleResult();
     
    }

    public Aufstellung[] aufstellungAbfragen(int spieltag, Kontrahent mitspieler) {
        TypedQuery<Aufstellung> query = em.createQuery("SELECT a from Aufstellung a where a.spieltag= :spieltag and a.mitspieler= :mitspieler", Aufstellung.class);
        query = query.setParameter("spieltag", spieltag).setParameter("mitspieler", mitspieler);
        return query.getResultList().toArray(new Aufstellung[0]);
    }

    public void aufstellungAktualisieren(int spieltag, Spieler spieler, Kontrahent mitspieler, int posInAufstellung) {
        Aufstellung aufstellung = new Aufstellung(spieltag, spieler, mitspieler, posInAufstellung);
        em.merge(aufstellung);
    }

    public Spieler[] aktuelleSpieler(int spieltag, Kontrahent mitspieler) {
        TypedQuery<Spieler> query = em.createQuery("Select b.spieler from Besitz b where b.beginn <= :spieltag and b.ende >= :spieltag and b.besitzer = :mitspieler order by b.spieler.position asc", Spieler.class);
        query = query.setParameter("spieltag", spieltag).setParameter("mitspieler", mitspieler);
        Spieler[] result = query.getResultList().toArray(new Spieler[22]);
        return result;
    }

    public List<Kontrahent> mitspielerListe() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Kontrahent> query = cb.createQuery(Kontrahent.class);
        query.from(Kontrahent.class);
        return em.createQuery(query).getResultList();
    }

    public String vereinVonSpieler(Spieler spieler) {
        TypedQuery<String> query = em.createQuery("Select v.verein.name from Vereinszuordnung v where v.spieler=:spieler", String.class);
        return query.setParameter("spieler", spieler).getSingleResult();
    }
}
