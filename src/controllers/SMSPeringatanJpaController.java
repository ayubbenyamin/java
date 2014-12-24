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
import model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.SMSPeringatan;

/**
 *
 * @author idham
 */
public class SMSPeringatanJpaController implements Serializable {

    public SMSPeringatanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SMSPeringatan SMSPeringatan) throws PreexistingEntityException, Exception {
        if (SMSPeringatan.getUserCollection() == null) {
            SMSPeringatan.setUserCollection(new ArrayList<User>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perpanjang kodePerpanjang = SMSPeringatan.getKodePerpanjang();
            if (kodePerpanjang != null) {
                kodePerpanjang = em.getReference(kodePerpanjang.getClass(), kodePerpanjang.getKodePerpanjang());
                SMSPeringatan.setKodePerpanjang(kodePerpanjang);
            }
            Collection<User> attachedUserCollection = new ArrayList<User>();
            for (User userCollectionUserToAttach : SMSPeringatan.getUserCollection()) {
                userCollectionUserToAttach = em.getReference(userCollectionUserToAttach.getClass(), userCollectionUserToAttach.getIdUser());
                attachedUserCollection.add(userCollectionUserToAttach);
            }
            SMSPeringatan.setUserCollection(attachedUserCollection);
            em.persist(SMSPeringatan);
            if (kodePerpanjang != null) {
                kodePerpanjang.getSMSPeringatanCollection().add(SMSPeringatan);
                kodePerpanjang = em.merge(kodePerpanjang);
            }
            for (User userCollectionUser : SMSPeringatan.getUserCollection()) {
                SMSPeringatan oldKodeSMSPeringatanOfUserCollectionUser = userCollectionUser.getKodeSMSPeringatan();
                userCollectionUser.setKodeSMSPeringatan(SMSPeringatan);
                userCollectionUser = em.merge(userCollectionUser);
                if (oldKodeSMSPeringatanOfUserCollectionUser != null) {
                    oldKodeSMSPeringatanOfUserCollectionUser.getUserCollection().remove(userCollectionUser);
                    oldKodeSMSPeringatanOfUserCollectionUser = em.merge(oldKodeSMSPeringatanOfUserCollectionUser);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSMSPeringatan(SMSPeringatan.getKodeSMSPeringatan()) != null) {
                throw new PreexistingEntityException("SMSPeringatan " + SMSPeringatan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SMSPeringatan SMSPeringatan) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SMSPeringatan persistentSMSPeringatan = em.find(SMSPeringatan.class, SMSPeringatan.getKodeSMSPeringatan());
            Perpanjang kodePerpanjangOld = persistentSMSPeringatan.getKodePerpanjang();
            Perpanjang kodePerpanjangNew = SMSPeringatan.getKodePerpanjang();
            Collection<User> userCollectionOld = persistentSMSPeringatan.getUserCollection();
            Collection<User> userCollectionNew = SMSPeringatan.getUserCollection();
            if (kodePerpanjangNew != null) {
                kodePerpanjangNew = em.getReference(kodePerpanjangNew.getClass(), kodePerpanjangNew.getKodePerpanjang());
                SMSPeringatan.setKodePerpanjang(kodePerpanjangNew);
            }
            Collection<User> attachedUserCollectionNew = new ArrayList<User>();
            for (User userCollectionNewUserToAttach : userCollectionNew) {
                userCollectionNewUserToAttach = em.getReference(userCollectionNewUserToAttach.getClass(), userCollectionNewUserToAttach.getIdUser());
                attachedUserCollectionNew.add(userCollectionNewUserToAttach);
            }
            userCollectionNew = attachedUserCollectionNew;
            SMSPeringatan.setUserCollection(userCollectionNew);
            SMSPeringatan = em.merge(SMSPeringatan);
            if (kodePerpanjangOld != null && !kodePerpanjangOld.equals(kodePerpanjangNew)) {
                kodePerpanjangOld.getSMSPeringatanCollection().remove(SMSPeringatan);
                kodePerpanjangOld = em.merge(kodePerpanjangOld);
            }
            if (kodePerpanjangNew != null && !kodePerpanjangNew.equals(kodePerpanjangOld)) {
                kodePerpanjangNew.getSMSPeringatanCollection().add(SMSPeringatan);
                kodePerpanjangNew = em.merge(kodePerpanjangNew);
            }
            for (User userCollectionOldUser : userCollectionOld) {
                if (!userCollectionNew.contains(userCollectionOldUser)) {
                    userCollectionOldUser.setKodeSMSPeringatan(null);
                    userCollectionOldUser = em.merge(userCollectionOldUser);
                }
            }
            for (User userCollectionNewUser : userCollectionNew) {
                if (!userCollectionOld.contains(userCollectionNewUser)) {
                    SMSPeringatan oldKodeSMSPeringatanOfUserCollectionNewUser = userCollectionNewUser.getKodeSMSPeringatan();
                    userCollectionNewUser.setKodeSMSPeringatan(SMSPeringatan);
                    userCollectionNewUser = em.merge(userCollectionNewUser);
                    if (oldKodeSMSPeringatanOfUserCollectionNewUser != null && !oldKodeSMSPeringatanOfUserCollectionNewUser.equals(SMSPeringatan)) {
                        oldKodeSMSPeringatanOfUserCollectionNewUser.getUserCollection().remove(userCollectionNewUser);
                        oldKodeSMSPeringatanOfUserCollectionNewUser = em.merge(oldKodeSMSPeringatanOfUserCollectionNewUser);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = SMSPeringatan.getKodeSMSPeringatan();
                if (findSMSPeringatan(id) == null) {
                    throw new NonexistentEntityException("The sMSPeringatan with id " + id + " no longer exists.");
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
            SMSPeringatan SMSPeringatan;
            try {
                SMSPeringatan = em.getReference(SMSPeringatan.class, id);
                SMSPeringatan.getKodeSMSPeringatan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The SMSPeringatan with id " + id + " no longer exists.", enfe);
            }
            Perpanjang kodePerpanjang = SMSPeringatan.getKodePerpanjang();
            if (kodePerpanjang != null) {
                kodePerpanjang.getSMSPeringatanCollection().remove(SMSPeringatan);
                kodePerpanjang = em.merge(kodePerpanjang);
            }
            Collection<User> userCollection = SMSPeringatan.getUserCollection();
            for (User userCollectionUser : userCollection) {
                userCollectionUser.setKodeSMSPeringatan(null);
                userCollectionUser = em.merge(userCollectionUser);
            }
            em.remove(SMSPeringatan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SMSPeringatan> findSMSPeringatanEntities() {
        return findSMSPeringatanEntities(true, -1, -1);
    }

    public List<SMSPeringatan> findSMSPeringatanEntities(int maxResults, int firstResult) {
        return findSMSPeringatanEntities(false, maxResults, firstResult);
    }

    private List<SMSPeringatan> findSMSPeringatanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SMSPeringatan.class));
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

    public SMSPeringatan findSMSPeringatan(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SMSPeringatan.class, id);
        } finally {
            em.close();
        }
    }

    public int getSMSPeringatanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SMSPeringatan> rt = cq.from(SMSPeringatan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
