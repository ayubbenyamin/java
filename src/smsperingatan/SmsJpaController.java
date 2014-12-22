/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smsperingatan;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Peringatan;
import model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Sms;
import smsperingatan.exceptions.NonexistentEntityException;
import smsperingatan.exceptions.PreexistingEntityException;

/**
 *
 * @author Gerardo
 */
public class SmsJpaController implements Serializable {

    public SmsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sms sms) throws PreexistingEntityException, Exception {
        if (sms.getUserCollection() == null) {
            sms.setUserCollection(new ArrayList<User>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peringatan kodePeringatan = sms.getKodePeringatan();
            if (kodePeringatan != null) {
                kodePeringatan = em.getReference(kodePeringatan.getClass(), kodePeringatan.getKodePeringatan());
                sms.setKodePeringatan(kodePeringatan);
            }
            Collection<User> attachedUserCollection = new ArrayList<User>();
            for (User userCollectionUserToAttach : sms.getUserCollection()) {
                userCollectionUserToAttach = em.getReference(userCollectionUserToAttach.getClass(), userCollectionUserToAttach.getIdUser());
                attachedUserCollection.add(userCollectionUserToAttach);
            }
            sms.setUserCollection(attachedUserCollection);
            em.persist(sms);
            if (kodePeringatan != null) {
                kodePeringatan.getSmsCollection().add(sms);
                kodePeringatan = em.merge(kodePeringatan);
            }
            for (User userCollectionUser : sms.getUserCollection()) {
                Sms oldKodeSMSOfUserCollectionUser = userCollectionUser.getKodeSMS();
                userCollectionUser.setKodeSMS(sms);
                userCollectionUser = em.merge(userCollectionUser);
                if (oldKodeSMSOfUserCollectionUser != null) {
                    oldKodeSMSOfUserCollectionUser.getUserCollection().remove(userCollectionUser);
                    oldKodeSMSOfUserCollectionUser = em.merge(oldKodeSMSOfUserCollectionUser);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSms(sms.getKodeSMS()) != null) {
                throw new PreexistingEntityException("Sms " + sms + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sms sms) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sms persistentSms = em.find(Sms.class, sms.getKodeSMS());
            Peringatan kodePeringatanOld = persistentSms.getKodePeringatan();
            Peringatan kodePeringatanNew = sms.getKodePeringatan();
            Collection<User> userCollectionOld = persistentSms.getUserCollection();
            Collection<User> userCollectionNew = sms.getUserCollection();
            if (kodePeringatanNew != null) {
                kodePeringatanNew = em.getReference(kodePeringatanNew.getClass(), kodePeringatanNew.getKodePeringatan());
                sms.setKodePeringatan(kodePeringatanNew);
            }
            Collection<User> attachedUserCollectionNew = new ArrayList<User>();
            for (User userCollectionNewUserToAttach : userCollectionNew) {
                userCollectionNewUserToAttach = em.getReference(userCollectionNewUserToAttach.getClass(), userCollectionNewUserToAttach.getIdUser());
                attachedUserCollectionNew.add(userCollectionNewUserToAttach);
            }
            userCollectionNew = attachedUserCollectionNew;
            sms.setUserCollection(userCollectionNew);
            sms = em.merge(sms);
            if (kodePeringatanOld != null && !kodePeringatanOld.equals(kodePeringatanNew)) {
                kodePeringatanOld.getSmsCollection().remove(sms);
                kodePeringatanOld = em.merge(kodePeringatanOld);
            }
            if (kodePeringatanNew != null && !kodePeringatanNew.equals(kodePeringatanOld)) {
                kodePeringatanNew.getSmsCollection().add(sms);
                kodePeringatanNew = em.merge(kodePeringatanNew);
            }
            for (User userCollectionOldUser : userCollectionOld) {
                if (!userCollectionNew.contains(userCollectionOldUser)) {
                    userCollectionOldUser.setKodeSMS(null);
                    userCollectionOldUser = em.merge(userCollectionOldUser);
                }
            }
            for (User userCollectionNewUser : userCollectionNew) {
                if (!userCollectionOld.contains(userCollectionNewUser)) {
                    Sms oldKodeSMSOfUserCollectionNewUser = userCollectionNewUser.getKodeSMS();
                    userCollectionNewUser.setKodeSMS(sms);
                    userCollectionNewUser = em.merge(userCollectionNewUser);
                    if (oldKodeSMSOfUserCollectionNewUser != null && !oldKodeSMSOfUserCollectionNewUser.equals(sms)) {
                        oldKodeSMSOfUserCollectionNewUser.getUserCollection().remove(userCollectionNewUser);
                        oldKodeSMSOfUserCollectionNewUser = em.merge(oldKodeSMSOfUserCollectionNewUser);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = sms.getKodeSMS();
                if (findSms(id) == null) {
                    throw new NonexistentEntityException("The sms with id " + id + " no longer exists.");
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
            Sms sms;
            try {
                sms = em.getReference(Sms.class, id);
                sms.getKodeSMS();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sms with id " + id + " no longer exists.", enfe);
            }
            Peringatan kodePeringatan = sms.getKodePeringatan();
            if (kodePeringatan != null) {
                kodePeringatan.getSmsCollection().remove(sms);
                kodePeringatan = em.merge(kodePeringatan);
            }
            Collection<User> userCollection = sms.getUserCollection();
            for (User userCollectionUser : userCollection) {
                userCollectionUser.setKodeSMS(null);
                userCollectionUser = em.merge(userCollectionUser);
            }
            em.remove(sms);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sms> findSmsEntities() {
        return findSmsEntities(true, -1, -1);
    }

    public List<Sms> findSmsEntities(int maxResults, int firstResult) {
        return findSmsEntities(false, maxResults, firstResult);
    }

    private List<Sms> findSmsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sms.class));
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

    public Sms findSms(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sms.class, id);
        } finally {
            em.close();
        }
    }

    public int getSmsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sms> rt = cq.from(Sms.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
