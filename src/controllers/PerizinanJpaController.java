/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(perizinan);
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
            perizinan = em.merge(perizinan);
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
