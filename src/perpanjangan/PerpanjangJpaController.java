/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perpanjangan;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Peringatan;
import model.Perpanjang;
import perpanjangan.exceptions.NonexistentEntityException;
import perpanjangan.exceptions.PreexistingEntityException;

/**
 *
 * @author Gerardo
 */
public class PerpanjangJpaController implements Serializable {

    public PerpanjangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Perpanjang perpanjang) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peringatan kodePeringatan = perpanjang.getKodePeringatan();
            if (kodePeringatan != null) {
                kodePeringatan = em.getReference(kodePeringatan.getClass(), kodePeringatan.getKodePeringatan());
                perpanjang.setKodePeringatan(kodePeringatan);
            }
            em.persist(perpanjang);
            if (kodePeringatan != null) {
                kodePeringatan.getPerpanjangCollection().add(perpanjang);
                kodePeringatan = em.merge(kodePeringatan);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerpanjang(perpanjang.getKodePerpanjang()) != null) {
                throw new PreexistingEntityException("Perpanjang " + perpanjang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Perpanjang perpanjang) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perpanjang persistentPerpanjang = em.find(Perpanjang.class, perpanjang.getKodePerpanjang());
            Peringatan kodePeringatanOld = persistentPerpanjang.getKodePeringatan();
            Peringatan kodePeringatanNew = perpanjang.getKodePeringatan();
            if (kodePeringatanNew != null) {
                kodePeringatanNew = em.getReference(kodePeringatanNew.getClass(), kodePeringatanNew.getKodePeringatan());
                perpanjang.setKodePeringatan(kodePeringatanNew);
            }
            perpanjang = em.merge(perpanjang);
            if (kodePeringatanOld != null && !kodePeringatanOld.equals(kodePeringatanNew)) {
                kodePeringatanOld.getPerpanjangCollection().remove(perpanjang);
                kodePeringatanOld = em.merge(kodePeringatanOld);
            }
            if (kodePeringatanNew != null && !kodePeringatanNew.equals(kodePeringatanOld)) {
                kodePeringatanNew.getPerpanjangCollection().add(perpanjang);
                kodePeringatanNew = em.merge(kodePeringatanNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = perpanjang.getKodePerpanjang();
                if (findPerpanjang(id) == null) {
                    throw new NonexistentEntityException("The perpanjang with id " + id + " no longer exists.");
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
            Perpanjang perpanjang;
            try {
                perpanjang = em.getReference(Perpanjang.class, id);
                perpanjang.getKodePerpanjang();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perpanjang with id " + id + " no longer exists.", enfe);
            }
            Peringatan kodePeringatan = perpanjang.getKodePeringatan();
            if (kodePeringatan != null) {
                kodePeringatan.getPerpanjangCollection().remove(perpanjang);
                kodePeringatan = em.merge(kodePeringatan);
            }
            em.remove(perpanjang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Perpanjang> findPerpanjangEntities() {
        return findPerpanjangEntities(true, -1, -1);
    }

    public List<Perpanjang> findPerpanjangEntities(int maxResults, int firstResult) {
        return findPerpanjangEntities(false, maxResults, firstResult);
    }

    private List<Perpanjang> findPerpanjangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Perpanjang.class));
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

    public Perpanjang findPerpanjang(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Perpanjang.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerpanjangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Perpanjang> rt = cq.from(Perpanjang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
