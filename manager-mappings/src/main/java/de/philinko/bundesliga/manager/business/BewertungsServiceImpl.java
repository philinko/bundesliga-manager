package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.manager.business.api.BewertungsService;
import de.philinko.bundesliga.manager.mappings.Bewertung;
import de.philinko.bundesliga.manager.mappings.Spieler;
import de.philinko.bundesliga.manager.mappings.Verein;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class BewertungsServiceImpl implements BewertungsService {

    public BewertungsServiceImpl() {
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

    public void bewertungEintragen(List<Bewertung> bewertungen) {
        em.getTransaction().begin();
        Query query = em.createQuery("Select b from Bewertung b where b.spieltag = :spieltag"
                + " and b.spieler=:spieler");
        for (Bewertung bewertung : bewertungen) {
            query = query.setParameter("spieltag", bewertung.getSpieltag());
            query = query.setParameter("spieler", bewertung.getSpieler());
            if (query.getResultList().isEmpty()) {
                em.persist(bewertung);
            } else {
                em.merge(bewertung);
            }
        }
        em.getTransaction().commit();
    }

    public List<Verein> ladeVereine() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Verein> query = cb.createQuery(Verein.class);
        query.from(Verein.class);
        return em.createQuery(query).getResultList();
    }

    public Set<Bewertung> bewertungenLaden(int spieltag, Verein verein) {
        TypedQuery<Bewertung> query = em.createQuery("Select b from Bewertung b, Vereinszuordnung v, Besitz be where "
                + "b.spieltag=:spieltag and v.verein=:verein and v.spieler=b.spieler "
                + "and v.beginn<=:spieltag and v.ende>=:spieltag and be.spieler=b.spieler "
                + "and be.beginn<=:spieltag and be.ende>=:spieltag", Bewertung.class);
        query = query.setParameter("spieltag", spieltag).setParameter("verein", verein);
        Set<Bewertung> result = new HashSet<Bewertung>();
        result.addAll(query.getResultList());
        return result;
    }

    public List<Spieler> spielerVonVereinLaden(int spieltag, Verein verein) {
        TypedQuery<Spieler> query = em.createQuery("Select v.spieler from Vereinszuordnung v, Besitz be where "
                + "v.verein=:verein and v.beginn<=:spieltag and v.ende>=:spieltag "
                + "and be.spieler=v.spieler and be.beginn<=:spieltag and be.ende>=:spieltag", Spieler.class);
        query = query.setParameter("spieltag", spieltag).setParameter("verein", verein);
        return query.getResultList();
    }
    
}
