/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sertifikasi;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Peringatan;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Sertifikasi;
import sertifikasi.exceptions.NonexistentEntityException;
import sertifikasi.exceptions.PreexistingEntityException;

/**
 *
 * @author Gerardo
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
        if (sertifikasi.getPeringatanCollection() == null) {
            sertifikasi.setPeringatanCollection(new ArrayList<Peringatan>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Peringatan> attachedPeringatanCollection = new ArrayList<Peringatan>();
            for (Peringatan peringatanCollectionPeringatanToAttach : sertifikasi.getPeringatanCollection()) {
                peringatanCollectionPeringatanToAttach = em.getReference(peringatanCollectionPeringatanToAttach.getClass(), peringatanCollectionPeringatanToAttach.getKodePeringatan());
                attachedPeringatanCollection.add(peringatanCollectionPeringatanToAttach);
            }
            sertifikasi.setPeringatanCollection(attachedPeringatanCollection);
            em.persist(sertifikasi);
            for (Peringatan peringatanCollectionPeringatan : sertifikasi.getPeringatanCollection()) {
                Sertifikasi oldKodeSertifikasiOfPeringatanCollectionPeringatan = peringatanCollectionPeringatan.getKodeSertifikasi();
                peringatanCollectionPeringatan.setKodeSertifikasi(sertifikasi);
                peringatanCollectionPeringatan = em.merge(peringatanCollectionPeringatan);
                if (oldKodeSertifikasiOfPeringatanCollectionPeringatan != null) {
                    oldKodeSertifikasiOfPeringatanCollectionPeringatan.getPeringatanCollection().remove(peringatanCollectionPeringatan);
                    oldKodeSertifikasiOfPeringatanCollectionPeringatan = em.merge(oldKodeSertifikasiOfPeringatanCollectionPeringatan);
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
            Collection<Peringatan> peringatanCollectionOld = persistentSertifikasi.getPeringatanCollection();
            Collection<Peringatan> peringatanCollectionNew = sertifikasi.getPeringatanCollection();
            Collection<Peringatan> attachedPeringatanCollectionNew = new ArrayList<Peringatan>();
            for (Peringatan peringatanCollectionNewPeringatanToAttach : peringatanCollectionNew) {
                peringatanCollectionNewPeringatanToAttach = em.getReference(peringatanCollectionNewPeringatanToAttach.getClass(), peringatanCollectionNewPeringatanToAttach.getKodePeringatan());
                attachedPeringatanCollectionNew.add(peringatanCollectionNewPeringatanToAttach);
            }
            peringatanCollectionNew = attachedPeringatanCollectionNew;
            sertifikasi.setPeringatanCollection(peringatanCollectionNew);
            sertifikasi = em.merge(sertifikasi);
            for (Peringatan peringatanCollectionOldPeringatan : peringatanCollectionOld) {
                if (!peringatanCollectionNew.contains(peringatanCollectionOldPeringatan)) {
                    peringatanCollectionOldPeringatan.setKodeSertifikasi(null);
                    peringatanCollectionOldPeringatan = em.merge(peringatanCollectionOldPeringatan);
                }
            }
            for (Peringatan peringatanCollectionNewPeringatan : peringatanCollectionNew) {
                if (!peringatanCollectionOld.contains(peringatanCollectionNewPeringatan)) {
                    Sertifikasi oldKodeSertifikasiOfPeringatanCollectionNewPeringatan = peringatanCollectionNewPeringatan.getKodeSertifikasi();
                    peringatanCollectionNewPeringatan.setKodeSertifikasi(sertifikasi);
                    peringatanCollectionNewPeringatan = em.merge(peringatanCollectionNewPeringatan);
                    if (oldKodeSertifikasiOfPeringatanCollectionNewPeringatan != null && !oldKodeSertifikasiOfPeringatanCollectionNewPeringatan.equals(sertifikasi)) {
                        oldKodeSertifikasiOfPeringatanCollectionNewPeringatan.getPeringatanCollection().remove(peringatanCollectionNewPeringatan);
                        oldKodeSertifikasiOfPeringatanCollectionNewPeringatan = em.merge(oldKodeSertifikasiOfPeringatanCollectionNewPeringatan);
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
            Collection<Peringatan> peringatanCollection = sertifikasi.getPeringatanCollection();
            for (Peringatan peringatanCollectionPeringatan : peringatanCollection) {
                peringatanCollectionPeringatan.setKodeSertifikasi(null);
                peringatanCollectionPeringatan = em.merge(peringatanCollectionPeringatan);
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
