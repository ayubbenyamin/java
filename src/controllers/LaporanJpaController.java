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
import model.Laporan;
import model.Perpanjang;

/**
 *
 * @author idham
 */
public class LaporanJpaController implements Serializable {

    public LaporanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Laporan laporan) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perpanjang kodePerpanjang = laporan.getKodePerpanjang();
            if (kodePerpanjang != null) {
                kodePerpanjang = em.getReference(kodePerpanjang.getClass(), kodePerpanjang.getKodePerpanjang());
                laporan.setKodePerpanjang(kodePerpanjang);
            }
            em.persist(laporan);
            if (kodePerpanjang != null) {
                kodePerpanjang.getLaporanCollection().add(laporan);
                kodePerpanjang = em.merge(kodePerpanjang);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLaporan(laporan.getKodeLaporan()) != null) {
                throw new PreexistingEntityException("Laporan " + laporan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Laporan laporan) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Laporan persistentLaporan = em.find(Laporan.class, laporan.getKodeLaporan());
            Perpanjang kodePerpanjangOld = persistentLaporan.getKodePerpanjang();
            Perpanjang kodePerpanjangNew = laporan.getKodePerpanjang();
            if (kodePerpanjangNew != null) {
                kodePerpanjangNew = em.getReference(kodePerpanjangNew.getClass(), kodePerpanjangNew.getKodePerpanjang());
                laporan.setKodePerpanjang(kodePerpanjangNew);
            }
            laporan = em.merge(laporan);
            if (kodePerpanjangOld != null && !kodePerpanjangOld.equals(kodePerpanjangNew)) {
                kodePerpanjangOld.getLaporanCollection().remove(laporan);
                kodePerpanjangOld = em.merge(kodePerpanjangOld);
            }
            if (kodePerpanjangNew != null && !kodePerpanjangNew.equals(kodePerpanjangOld)) {
                kodePerpanjangNew.getLaporanCollection().add(laporan);
                kodePerpanjangNew = em.merge(kodePerpanjangNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = laporan.getKodeLaporan();
                if (findLaporan(id) == null) {
                    throw new NonexistentEntityException("The laporan with id " + id + " no longer exists.");
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
            Laporan laporan;
            try {
                laporan = em.getReference(Laporan.class, id);
                laporan.getKodeLaporan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The laporan with id " + id + " no longer exists.", enfe);
            }
            Perpanjang kodePerpanjang = laporan.getKodePerpanjang();
            if (kodePerpanjang != null) {
                kodePerpanjang.getLaporanCollection().remove(laporan);
                kodePerpanjang = em.merge(kodePerpanjang);
            }
            em.remove(laporan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Laporan> findLaporanEntities() {
        return findLaporanEntities(true, -1, -1);
    }

    public List<Laporan> findLaporanEntities(int maxResults, int firstResult) {
        return findLaporanEntities(false, maxResults, firstResult);
    }

    private List<Laporan> findLaporanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Laporan.class));
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

    public Laporan findLaporan(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Laporan.class, id);
        } finally {
            em.close();
        }
    }

    public int getLaporanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Laporan> rt = cq.from(Laporan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
