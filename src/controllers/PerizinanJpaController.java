/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Perpanjang;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Perizinan;

/**
 *
 * @author idham
 */
public class PerizinanJpaController implements Serializable {

    public PerizinanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Perizinan perizinan) throws PreexistingEntityException, Exception {
        if (perizinan.getPerpanjangCollection() == null) {
            perizinan.setPerpanjangCollection(new ArrayList<Perpanjang>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Perpanjang> attachedPerpanjangCollection = new ArrayList<Perpanjang>();
            for (Perpanjang perpanjangCollectionPerpanjangToAttach : perizinan.getPerpanjangCollection()) {
                perpanjangCollectionPerpanjangToAttach = em.getReference(perpanjangCollectionPerpanjangToAttach.getClass(), perpanjangCollectionPerpanjangToAttach.getKodePerpanjang());
                attachedPerpanjangCollection.add(perpanjangCollectionPerpanjangToAttach);
            }
            perizinan.setPerpanjangCollection(attachedPerpanjangCollection);
            em.persist(perizinan);
            for (Perpanjang perpanjangCollectionPerpanjang : perizinan.getPerpanjangCollection()) {
                Perizinan oldKodePerizinanOfPerpanjangCollectionPerpanjang = perpanjangCollectionPerpanjang.getKodePerizinan();
                perpanjangCollectionPerpanjang.setKodePerizinan(perizinan);
                perpanjangCollectionPerpanjang = em.merge(perpanjangCollectionPerpanjang);
                if (oldKodePerizinanOfPerpanjangCollectionPerpanjang != null) {
                    oldKodePerizinanOfPerpanjangCollectionPerpanjang.getPerpanjangCollection().remove(perpanjangCollectionPerpanjang);
                    oldKodePerizinanOfPerpanjangCollectionPerpanjang = em.merge(oldKodePerizinanOfPerpanjangCollectionPerpanjang);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerizinan(perizinan.getKodePerizinan()) != null) {
                throw new PreexistingEntityException("Perizinan " + perizinan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Perizinan perizinan) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perizinan persistentPerizinan = em.find(Perizinan.class, perizinan.getKodePerizinan());
            Collection<Perpanjang> perpanjangCollectionOld = persistentPerizinan.getPerpanjangCollection();
            Collection<Perpanjang> perpanjangCollectionNew = perizinan.getPerpanjangCollection();
            Collection<Perpanjang> attachedPerpanjangCollectionNew = new ArrayList<Perpanjang>();
            for (Perpanjang perpanjangCollectionNewPerpanjangToAttach : perpanjangCollectionNew) {
                perpanjangCollectionNewPerpanjangToAttach = em.getReference(perpanjangCollectionNewPerpanjangToAttach.getClass(), perpanjangCollectionNewPerpanjangToAttach.getKodePerpanjang());
                attachedPerpanjangCollectionNew.add(perpanjangCollectionNewPerpanjangToAttach);
            }
            perpanjangCollectionNew = attachedPerpanjangCollectionNew;
            perizinan.setPerpanjangCollection(perpanjangCollectionNew);
            perizinan = em.merge(perizinan);
            for (Perpanjang perpanjangCollectionOldPerpanjang : perpanjangCollectionOld) {
                if (!perpanjangCollectionNew.contains(perpanjangCollectionOldPerpanjang)) {
                    perpanjangCollectionOldPerpanjang.setKodePerizinan(null);
                    perpanjangCollectionOldPerpanjang = em.merge(perpanjangCollectionOldPerpanjang);
                }
            }
            for (Perpanjang perpanjangCollectionNewPerpanjang : perpanjangCollectionNew) {
                if (!perpanjangCollectionOld.contains(perpanjangCollectionNewPerpanjang)) {
                    Perizinan oldKodePerizinanOfPerpanjangCollectionNewPerpanjang = perpanjangCollectionNewPerpanjang.getKodePerizinan();
                    perpanjangCollectionNewPerpanjang.setKodePerizinan(perizinan);
                    perpanjangCollectionNewPerpanjang = em.merge(perpanjangCollectionNewPerpanjang);
                    if (oldKodePerizinanOfPerpanjangCollectionNewPerpanjang != null && !oldKodePerizinanOfPerpanjangCollectionNewPerpanjang.equals(perizinan)) {
                        oldKodePerizinanOfPerpanjangCollectionNewPerpanjang.getPerpanjangCollection().remove(perpanjangCollectionNewPerpanjang);
                        oldKodePerizinanOfPerpanjangCollectionNewPerpanjang = em.merge(oldKodePerizinanOfPerpanjangCollectionNewPerpanjang);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = perizinan.getKodePerizinan();
                if (findPerizinan(id) == null) {
                    throw new NonexistentEntityException("The perizinan with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perizinan perizinan;
            try {
                perizinan = em.getReference(Perizinan.class, id);
                perizinan.getKodePerizinan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perizinan with id " + id + " no longer exists.", enfe);
            }
            Collection<Perpanjang> perpanjangCollection = perizinan.getPerpanjangCollection();
            for (Perpanjang perpanjangCollectionPerpanjang : perpanjangCollection) {
                perpanjangCollectionPerpanjang.setKodePerizinan(null);
                perpanjangCollectionPerpanjang = em.merge(perpanjangCollectionPerpanjang);
            }
            em.remove(perizinan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Perizinan> findPerizinanEntities() {
        return findPerizinanEntities(true, -1, -1);
    }

    public List<Perizinan> findPerizinanEntities(int maxResults, int firstResult) {
        return findPerizinanEntities(false, maxResults, firstResult);
    }

    private List<Perizinan> findPerizinanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Perizinan.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Perizinan findPerizinan(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Perizinan.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerizinanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Perizinan> rt = cq.from(Perizinan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
