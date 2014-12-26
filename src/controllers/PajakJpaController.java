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
import model.Pajak;

/**
 *
 * @author idham
 */
public class PajakJpaController implements Serializable {

    public PajakJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pajak pajak) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pajak);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPajak(pajak.getKodePajak()) != null) {
                throw new PreexistingEntityException("Pajak " + pajak + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pajak pajak) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pajak = em.merge(pajak);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pajak.getKodePajak();
                if (findPajak(id) == null) {
                    throw new NonexistentEntityException("The pajak with id " + id + " no longer exists.");
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
            Pajak pajak;
            try {
                pajak = em.getReference(Pajak.class, id);
                pajak.getKodePajak();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pajak with id " + id + " no longer exists.", enfe);
            }
            em.remove(pajak);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pajak> findPajakEntities() {
        return findPajakEntities(true, -1, -1);
    }

    public List<Pajak> findPajakEntities(int maxResults, int firstResult) {
        return findPajakEntities(false, maxResults, firstResult);
    }

    private List<Pajak> findPajakEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pajak.class));
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

    public Pajak findPajak(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pajak.class, id);
        } finally {
            em.close();
        }
    }

    public int getPajakCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pajak> rt = cq.from(Pajak.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
