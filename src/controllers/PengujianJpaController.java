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
import model.Pengujian;

/**
 *
 * @author idham
 */
public class PengujianJpaController implements Serializable {

    public PengujianJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pengujian pengujian) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pengujian);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPengujian(pengujian.getKodePengujian()) != null) {
                throw new PreexistingEntityException("Pengujian " + pengujian + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pengujian pengujian) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pengujian = em.merge(pengujian);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pengujian.getKodePengujian();
                if (findPengujian(id) == null) {
                    throw new NonexistentEntityException("The pengujian with id " + id + " no longer exists.");
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
            Pengujian pengujian;
            try {
                pengujian = em.getReference(Pengujian.class, id);
                pengujian.getKodePengujian();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pengujian with id " + id + " no longer exists.", enfe);
            }
            em.remove(pengujian);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pengujian> findPengujianEntities() {
        return findPengujianEntities(true, -1, -1);
    }

    public List<Pengujian> findPengujianEntities(int maxResults, int firstResult) {
        return findPengujianEntities(false, maxResults, firstResult);
    }

    private List<Pengujian> findPengujianEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pengujian.class));
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

    public Pengujian findPengujian(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pengujian.class, id);
        } finally {
            em.close();
        }
    }

    public int getPengujianCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pengujian> rt = cq.from(Pengujian.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
