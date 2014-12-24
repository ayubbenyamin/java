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
        if (sertifikasi.getPerpanjangCollection() == null) {
            sertifikasi.setPerpanjangCollection(new ArrayList<Perpanjang>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Perpanjang> attachedPerpanjangCollection = new ArrayList<Perpanjang>();
            for (Perpanjang perpanjangCollectionPerpanjangToAttach : sertifikasi.getPerpanjangCollection()) {
                perpanjangCollectionPerpanjangToAttach = em.getReference(perpanjangCollectionPerpanjangToAttach.getClass(), perpanjangCollectionPerpanjangToAttach.getKodePerpanjang());
                attachedPerpanjangCollection.add(perpanjangCollectionPerpanjangToAttach);
            }
            sertifikasi.setPerpanjangCollection(attachedPerpanjangCollection);
            em.persist(sertifikasi);
            for (Perpanjang perpanjangCollectionPerpanjang : sertifikasi.getPerpanjangCollection()) {
                Sertifikasi oldKodeSertifikasiOfPerpanjangCollectionPerpanjang = perpanjangCollectionPerpanjang.getKodeSertifikasi();
                perpanjangCollectionPerpanjang.setKodeSertifikasi(sertifikasi);
                perpanjangCollectionPerpanjang = em.merge(perpanjangCollectionPerpanjang);
                if (oldKodeSertifikasiOfPerpanjangCollectionPerpanjang != null) {
                    oldKodeSertifikasiOfPerpanjangCollectionPerpanjang.getPerpanjangCollection().remove(perpanjangCollectionPerpanjang);
                    oldKodeSertifikasiOfPerpanjangCollectionPerpanjang = em.merge(oldKodeSertifikasiOfPerpanjangCollectionPerpanjang);
                }
            }
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
            Sertifikasi persistentSertifikasi = em.find(Sertifikasi.class, sertifikasi.getKodeSertifikasi());
            Collection<Perpanjang> perpanjangCollectionOld = persistentSertifikasi.getPerpanjangCollection();
            Collection<Perpanjang> perpanjangCollectionNew = sertifikasi.getPerpanjangCollection();
            Collection<Perpanjang> attachedPerpanjangCollectionNew = new ArrayList<Perpanjang>();
            for (Perpanjang perpanjangCollectionNewPerpanjangToAttach : perpanjangCollectionNew) {
                perpanjangCollectionNewPerpanjangToAttach = em.getReference(perpanjangCollectionNewPerpanjangToAttach.getClass(), perpanjangCollectionNewPerpanjangToAttach.getKodePerpanjang());
                attachedPerpanjangCollectionNew.add(perpanjangCollectionNewPerpanjangToAttach);
            }
            perpanjangCollectionNew = attachedPerpanjangCollectionNew;
            sertifikasi.setPerpanjangCollection(perpanjangCollectionNew);
            sertifikasi = em.merge(sertifikasi);
            for (Perpanjang perpanjangCollectionOldPerpanjang : perpanjangCollectionOld) {
                if (!perpanjangCollectionNew.contains(perpanjangCollectionOldPerpanjang)) {
                    perpanjangCollectionOldPerpanjang.setKodeSertifikasi(null);
                    perpanjangCollectionOldPerpanjang = em.merge(perpanjangCollectionOldPerpanjang);
                }
            }
            for (Perpanjang perpanjangCollectionNewPerpanjang : perpanjangCollectionNew) {
                if (!perpanjangCollectionOld.contains(perpanjangCollectionNewPerpanjang)) {
                    Sertifikasi oldKodeSertifikasiOfPerpanjangCollectionNewPerpanjang = perpanjangCollectionNewPerpanjang.getKodeSertifikasi();
                    perpanjangCollectionNewPerpanjang.setKodeSertifikasi(sertifikasi);
                    perpanjangCollectionNewPerpanjang = em.merge(perpanjangCollectionNewPerpanjang);
                    if (oldKodeSertifikasiOfPerpanjangCollectionNewPerpanjang != null && !oldKodeSertifikasiOfPerpanjangCollectionNewPerpanjang.equals(sertifikasi)) {
                        oldKodeSertifikasiOfPerpanjangCollectionNewPerpanjang.getPerpanjangCollection().remove(perpanjangCollectionNewPerpanjang);
                        oldKodeSertifikasiOfPerpanjangCollectionNewPerpanjang = em.merge(oldKodeSertifikasiOfPerpanjangCollectionNewPerpanjang);
                    }
                }
            }
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
            Collection<Perpanjang> perpanjangCollection = sertifikasi.getPerpanjangCollection();
            for (Perpanjang perpanjangCollectionPerpanjang : perpanjangCollection) {
                perpanjangCollectionPerpanjang.setKodeSertifikasi(null);
                perpanjangCollectionPerpanjang = em.merge(perpanjangCollectionPerpanjang);
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
