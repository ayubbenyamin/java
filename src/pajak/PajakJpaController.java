/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pajak;

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
import model.Pajak;
import pajak.exceptions.IllegalOrphanException;
import pajak.exceptions.NonexistentEntityException;
import pajak.exceptions.PreexistingEntityException;

/**
 *
 * @author Gerardo
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
        if (pajak.getPeringatanCollection() == null) {
            pajak.setPeringatanCollection(new ArrayList<Peringatan>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Peringatan> attachedPeringatanCollection = new ArrayList<Peringatan>();
            for (Peringatan peringatanCollectionPeringatanToAttach : pajak.getPeringatanCollection()) {
                peringatanCollectionPeringatanToAttach = em.getReference(peringatanCollectionPeringatanToAttach.getClass(), peringatanCollectionPeringatanToAttach.getKodePeringatan());
                attachedPeringatanCollection.add(peringatanCollectionPeringatanToAttach);
            }
            pajak.setPeringatanCollection(attachedPeringatanCollection);
            em.persist(pajak);
            for (Peringatan peringatanCollectionPeringatan : pajak.getPeringatanCollection()) {
                Pajak oldKodePajakOfPeringatanCollectionPeringatan = peringatanCollectionPeringatan.getKodePajak();
                peringatanCollectionPeringatan.setKodePajak(pajak);
                peringatanCollectionPeringatan = em.merge(peringatanCollectionPeringatan);
                if (oldKodePajakOfPeringatanCollectionPeringatan != null) {
                    oldKodePajakOfPeringatanCollectionPeringatan.getPeringatanCollection().remove(peringatanCollectionPeringatan);
                    oldKodePajakOfPeringatanCollectionPeringatan = em.merge(oldKodePajakOfPeringatanCollectionPeringatan);
                }
            }
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

    public void edit(Pajak pajak) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pajak persistentPajak = em.find(Pajak.class, pajak.getKodePajak());
            Collection<Peringatan> peringatanCollectionOld = persistentPajak.getPeringatanCollection();
            Collection<Peringatan> peringatanCollectionNew = pajak.getPeringatanCollection();
            List<String> illegalOrphanMessages = null;
            for (Peringatan peringatanCollectionOldPeringatan : peringatanCollectionOld) {
                if (!peringatanCollectionNew.contains(peringatanCollectionOldPeringatan)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Peringatan " + peringatanCollectionOldPeringatan + " since its kodePajak field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Peringatan> attachedPeringatanCollectionNew = new ArrayList<Peringatan>();
            for (Peringatan peringatanCollectionNewPeringatanToAttach : peringatanCollectionNew) {
                peringatanCollectionNewPeringatanToAttach = em.getReference(peringatanCollectionNewPeringatanToAttach.getClass(), peringatanCollectionNewPeringatanToAttach.getKodePeringatan());
                attachedPeringatanCollectionNew.add(peringatanCollectionNewPeringatanToAttach);
            }
            peringatanCollectionNew = attachedPeringatanCollectionNew;
            pajak.setPeringatanCollection(peringatanCollectionNew);
            pajak = em.merge(pajak);
            for (Peringatan peringatanCollectionNewPeringatan : peringatanCollectionNew) {
                if (!peringatanCollectionOld.contains(peringatanCollectionNewPeringatan)) {
                    Pajak oldKodePajakOfPeringatanCollectionNewPeringatan = peringatanCollectionNewPeringatan.getKodePajak();
                    peringatanCollectionNewPeringatan.setKodePajak(pajak);
                    peringatanCollectionNewPeringatan = em.merge(peringatanCollectionNewPeringatan);
                    if (oldKodePajakOfPeringatanCollectionNewPeringatan != null && !oldKodePajakOfPeringatanCollectionNewPeringatan.equals(pajak)) {
                        oldKodePajakOfPeringatanCollectionNewPeringatan.getPeringatanCollection().remove(peringatanCollectionNewPeringatan);
                        oldKodePajakOfPeringatanCollectionNewPeringatan = em.merge(oldKodePajakOfPeringatanCollectionNewPeringatan);
                    }
                }
            }
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<Peringatan> peringatanCollectionOrphanCheck = pajak.getPeringatanCollection();
            for (Peringatan peringatanCollectionOrphanCheckPeringatan : peringatanCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pajak (" + pajak + ") cannot be destroyed since the Peringatan " + peringatanCollectionOrphanCheckPeringatan + " in its peringatanCollection field has a non-nullable kodePajak field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
