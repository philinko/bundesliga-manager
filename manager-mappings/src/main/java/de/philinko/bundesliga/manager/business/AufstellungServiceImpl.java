/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.philinko.bundesliga.manager.business;

import de.philinko.bundesliga.manager.business.api.AufstellungService;
import de.philinko.bundesliga.manager.mappings.Aufstellung;
import de.philinko.bundesliga.manager.mappings.Kontrahent;
import de.philinko.bundesliga.manager.mappings.Spieler;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

/**
 *
 * @author philippe
 */
public class AufstellungServiceImpl implements AufstellungService {
    public AufstellungServiceImpl() {
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

    public void aufstellungenSpeichern(Aufstellung[] aufstellungen) {
        em.getTransaction().begin();
        for (Aufstellung aufstellung : aufstellungen) {
            em.persist(aufstellung);
        }
        em.getTransaction().commit();
    }

    public Kontrahent aktuellerBesitzer(int spieltag, Spieler spieler) {
        TypedQuery<Kontrahent> query = em.createQuery("SELECT besitzer from Besitz where beginn <= :spieltag and ende >= :spieltag and spieler = :spieler", Kontrahent.class);
        query = query.setParameter("spieltag", spieltag).setParameter("spieler", spieler);
        return query.getSingleResult();
     
    }

    public Aufstellung[] aufstellungAbfragen(int spieltag, Kontrahent mitspieler) {
        TypedQuery<Aufstellung> query = em.createQuery("SELECT a from Aufstellung a where spieltag=:spieltag and mitspieler=:mitspieler", Aufstellung.class);
        query = query.setParameter("spieltag", spieltag).setParameter("mitspieler", mitspieler);
        return query.getResultList().toArray(new Aufstellung[0]);
    }

    public void aufstellungAktualisieren(int spieltag, Spieler spieler, Kontrahent mitspieler, int posInAufstellung) {
        Aufstellung aufstellung = new Aufstellung(spieltag, spieler, mitspieler, posInAufstellung);
        em.merge(aufstellung);
    }
}
