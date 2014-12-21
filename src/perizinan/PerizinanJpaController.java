/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perizinan;

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
import model.Perizinan;
import perizinan.exceptions.NonexistentEntityException;
import perizinan.exceptions.PreexistingEntityException;

/**
 *
 * @author Gerardo
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
        if (perizinan.getPeringatanCollection() == null) {
            perizinan.setPeringatanCollection(new ArrayList<Peringatan>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Peringatan> attachedPeringatanCollection = new ArrayList<Peringatan>();
            for (Peringatan peringatanCollectionPeringatanToAttach : perizinan.getPeringatanCollection()) {
                peringatanCollectionPeringatanToAttach = em.getReference(peringatanCollectionPeringatanToAttach.getClass(), peringatanCollectionPeringatanToAttach.getKodePeringatan());
                attachedPeringatanCollection.add(peringatanCollectionPeringatanToAttach);
            }
            perizinan.setPeringatanCollection(attachedPeringatanCollection);
            em.persist(perizinan);
            for (Peringatan peringatanCollectionPeringatan : perizinan.getPeringatanCollection()) {
                Perizinan oldKodePerizinanOfPeringatanCollectionPeringatan = peringatanCollectionPeringatan.getKodePerizinan();
                peringatanCollectionPeringatan.setKodePerizinan(perizinan);
                peringatanCollectionPeringatan = em.merge(peringatanCollectionPeringatan);
                if (oldKodePerizinanOfPeringatanCollectionPeringatan != null) {
                    oldKodePerizinanOfPeringatanCollectionPeringatan.getPeringatanCollection().remove(peringatanCollectionPeringatan);
                    oldKodePerizinanOfPeringatanCollectionPeringatan = em.merge(oldKodePerizinanOfPeringatanCollectionPeringatan);
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
            Collection<Peringatan> peringatanCollectionOld = persistentPerizinan.getPeringatanCollection();
            Collection<Peringatan> peringatanCollectionNew = perizinan.getPeringatanCollection();
            Collection<Peringatan> attachedPeringatanCollectionNew = new ArrayList<Peringatan>();
            for (Peringatan peringatanCollectionNewPeringatanToAttach : peringatanCollectionNew) {
                peringatanCollectionNewPeringatanToAttach = em.getReference(peringatanCollectionNewPeringatanToAttach.getClass(), peringatanCollectionNewPeringatanToAttach.getKodePeringatan());
                attachedPeringatanCollectionNew.add(peringatanCollectionNewPeringatanToAttach);
            }
            peringatanCollectionNew = attachedPeringatanCollectionNew;
            perizinan.setPeringatanCollection(peringatanCollectionNew);
            perizinan = em.merge(perizinan);
            for (Peringatan peringatanCollectionOldPeringatan : peringatanCollectionOld) {
                if (!peringatanCollectionNew.contains(peringatanCollectionOldPeringatan)) {
                    peringatanCollectionOldPeringatan.setKodePerizinan(null);
                    peringatanCollectionOldPeringatan = em.merge(peringatanCollectionOldPeringatan);
                }
            }
            for (Peringatan peringatanCollectionNewPeringatan : peringatanCollectionNew) {
                if (!peringatanCollectionOld.contains(peringatanCollectionNewPeringatan)) {
                    Perizinan oldKodePerizinanOfPeringatanCollectionNewPeringatan = peringatanCollectionNewPeringatan.getKodePerizinan();
                    peringatanCollectionNewPeringatan.setKodePerizinan(perizinan);
                    peringatanCollectionNewPeringatan = em.merge(peringatanCollectionNewPeringatan);
                    if (oldKodePerizinanOfPeringatanCollectionNewPeringatan != null && !oldKodePerizinanOfPeringatanCollectionNewPeringatan.equals(perizinan)) {
                        oldKodePerizinanOfPeringatanCollectionNewPeringatan.getPeringatanCollection().remove(peringatanCollectionNewPeringatan);
                        oldKodePerizinanOfPeringatanCollectionNewPeringatan = em.merge(oldKodePerizinanOfPeringatanCollectionNewPeringatan);
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
            Collection<Peringatan> peringatanCollection = perizinan.getPeringatanCollection();
            for (Peringatan peringatanCollectionPeringatan : peringatanCollection) {
                peringatanCollectionPeringatan.setKodePerizinan(null);
                peringatanCollectionPeringatan = em.merge(peringatanCollectionPeringatan);
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
