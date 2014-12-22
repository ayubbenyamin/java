/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Sms;
import model.User;
import user.exceptions.NonexistentEntityException;
import user.exceptions.PreexistingEntityException;

/**
 *
 * @author Gerardo
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sms kodeSMS = user.getKodeSMS();
            if (kodeSMS != null) {
                kodeSMS = em.getReference(kodeSMS.getClass(), kodeSMS.getKodeSMS());
                user.setKodeSMS(kodeSMS);
            }
            em.persist(user);
            if (kodeSMS != null) {
                kodeSMS.getUserCollection().add(user);
                kodeSMS = em.merge(kodeSMS);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUser(user.getIdUser()) != null) {
                throw new PreexistingEntityException("User " + user + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getIdUser());
            Sms kodeSMSOld = persistentUser.getKodeSMS();
            Sms kodeSMSNew = user.getKodeSMS();
            if (kodeSMSNew != null) {
                kodeSMSNew = em.getReference(kodeSMSNew.getClass(), kodeSMSNew.getKodeSMS());
                user.setKodeSMS(kodeSMSNew);
            }
            user = em.merge(user);
            if (kodeSMSOld != null && !kodeSMSOld.equals(kodeSMSNew)) {
                kodeSMSOld.getUserCollection().remove(user);
                kodeSMSOld = em.merge(kodeSMSOld);
            }
            if (kodeSMSNew != null && !kodeSMSNew.equals(kodeSMSOld)) {
                kodeSMSNew.getUserCollection().add(user);
                kodeSMSNew = em.merge(kodeSMSNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = user.getIdUser();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getIdUser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            Sms kodeSMS = user.getKodeSMS();
            if (kodeSMS != null) {
                kodeSMS.getUserCollection().remove(user);
                kodeSMS = em.merge(kodeSMS);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
