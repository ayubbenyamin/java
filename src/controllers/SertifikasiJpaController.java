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
import model.Sertifikasi;

/**
 *
 * @author idham
 */
public class SertifikasiJpaController implements Serializable {

    public SertifikasiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sertifikasi sertifikasi) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(sertifikasi);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSertifikasi(sertifikasi.getKodeSertifikasi()) != null) {
                throw new PreexistingEntityException("Sertifikasi " + sertifikasi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sertifikasi sertifikasi) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            sertifikasi = em.merge(sertifikasi);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = sertifikasi.getKodeSertifikasi();
                if (findSertifikasi(id) == null) {
                    throw new NonexistentEntityException("The sertifikasi with id " + id + " no longer exists.");
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
            Sertifikasi sertifikasi;
            try {
                sertifikasi = em.getReference(Sertifikasi.class, id);
                sertifikasi.getKodeSertifikasi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sertifikasi with id " + id + " no longer exists.", enfe);
            }
            em.remove(sertifikasi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sertifikasi> findSertifikasiEntities() {
        return findSertifikasiEntities(true, -1, -1);
    }

    public List<Sertifikasi> findSertifikasiEntities(int maxResults, int firstResult) {
        return findSertifikasiEntities(false, maxResults, firstResult);
    }

    private List<Sertifikasi> findSertifikasiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sertifikasi.class));
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

    public Sertifikasi findSertifikasi(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sertifikasi.class, id);
        } finally {
            em.close();
        }
    }

    public int getSertifikasiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sertifikasi> rt = cq.from(Sertifikasi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
