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
import model.Pajak;
import model.Pengujian;
import model.Perizinan;
import model.Sertifikasi;
import model.Laporan;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Perpanjang;
import model.SMSPeringatan;

/**
 *
 * @author idham
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
        if (perpanjang.getLaporanCollection() == null) {
            perpanjang.setLaporanCollection(new ArrayList<Laporan>());
        }
        if (perpanjang.getSMSPeringatanCollection() == null) {
            perpanjang.setSMSPeringatanCollection(new ArrayList<SMSPeringatan>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pajak kodePajak = perpanjang.getKodePajak();
            if (kodePajak != null) {
                kodePajak = em.getReference(kodePajak.getClass(), kodePajak.getKodePajak());
                perpanjang.setKodePajak(kodePajak);
            }
            Pengujian kodePengujian = perpanjang.getKodePengujian();
            if (kodePengujian != null) {
                kodePengujian = em.getReference(kodePengujian.getClass(), kodePengujian.getKodePengujian());
                perpanjang.setKodePengujian(kodePengujian);
            }
            Perizinan kodePerizinan = perpanjang.getKodePerizinan();
            if (kodePerizinan != null) {
                kodePerizinan = em.getReference(kodePerizinan.getClass(), kodePerizinan.getKodePerizinan());
                perpanjang.setKodePerizinan(kodePerizinan);
            }
            Sertifikasi kodeSertifikasi = perpanjang.getKodeSertifikasi();
            if (kodeSertifikasi != null) {
                kodeSertifikasi = em.getReference(kodeSertifikasi.getClass(), kodeSertifikasi.getKodeSertifikasi());
                perpanjang.setKodeSertifikasi(kodeSertifikasi);
            }
            Collection<Laporan> attachedLaporanCollection = new ArrayList<Laporan>();
            for (Laporan laporanCollectionLaporanToAttach : perpanjang.getLaporanCollection()) {
                laporanCollectionLaporanToAttach = em.getReference(laporanCollectionLaporanToAttach.getClass(), laporanCollectionLaporanToAttach.getKodeLaporan());
                attachedLaporanCollection.add(laporanCollectionLaporanToAttach);
            }
            perpanjang.setLaporanCollection(attachedLaporanCollection);
            Collection<SMSPeringatan> attachedSMSPeringatanCollection = new ArrayList<SMSPeringatan>();
            for (SMSPeringatan SMSPeringatanCollectionSMSPeringatanToAttach : perpanjang.getSMSPeringatanCollection()) {
                SMSPeringatanCollectionSMSPeringatanToAttach = em.getReference(SMSPeringatanCollectionSMSPeringatanToAttach.getClass(), SMSPeringatanCollectionSMSPeringatanToAttach.getKodeSMSPeringatan());
                attachedSMSPeringatanCollection.add(SMSPeringatanCollectionSMSPeringatanToAttach);
            }
            perpanjang.setSMSPeringatanCollection(attachedSMSPeringatanCollection);
            em.persist(perpanjang);
            if (kodePajak != null) {
                kodePajak.getPerpanjangCollection().add(perpanjang);
                kodePajak = em.merge(kodePajak);
            }
            if (kodePengujian != null) {
                kodePengujian.getPerpanjangCollection().add(perpanjang);
                kodePengujian = em.merge(kodePengujian);
            }
            if (kodePerizinan != null) {
                kodePerizinan.getPerpanjangCollection().add(perpanjang);
                kodePerizinan = em.merge(kodePerizinan);
            }
            if (kodeSertifikasi != null) {
                kodeSertifikasi.getPerpanjangCollection().add(perpanjang);
                kodeSertifikasi = em.merge(kodeSertifikasi);
            }
            for (Laporan laporanCollectionLaporan : perpanjang.getLaporanCollection()) {
                Perpanjang oldKodePerpanjangOfLaporanCollectionLaporan = laporanCollectionLaporan.getKodePerpanjang();
                laporanCollectionLaporan.setKodePerpanjang(perpanjang);
                laporanCollectionLaporan = em.merge(laporanCollectionLaporan);
                if (oldKodePerpanjangOfLaporanCollectionLaporan != null) {
                    oldKodePerpanjangOfLaporanCollectionLaporan.getLaporanCollection().remove(laporanCollectionLaporan);
                    oldKodePerpanjangOfLaporanCollectionLaporan = em.merge(oldKodePerpanjangOfLaporanCollectionLaporan);
                }
            }
            for (SMSPeringatan SMSPeringatanCollectionSMSPeringatan : perpanjang.getSMSPeringatanCollection()) {
                Perpanjang oldKodePerpanjangOfSMSPeringatanCollectionSMSPeringatan = SMSPeringatanCollectionSMSPeringatan.getKodePerpanjang();
                SMSPeringatanCollectionSMSPeringatan.setKodePerpanjang(perpanjang);
                SMSPeringatanCollectionSMSPeringatan = em.merge(SMSPeringatanCollectionSMSPeringatan);
                if (oldKodePerpanjangOfSMSPeringatanCollectionSMSPeringatan != null) {
                    oldKodePerpanjangOfSMSPeringatanCollectionSMSPeringatan.getSMSPeringatanCollection().remove(SMSPeringatanCollectionSMSPeringatan);
                    oldKodePerpanjangOfSMSPeringatanCollectionSMSPeringatan = em.merge(oldKodePerpanjangOfSMSPeringatanCollectionSMSPeringatan);
                }
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
            Pajak kodePajakOld = persistentPerpanjang.getKodePajak();
            Pajak kodePajakNew = perpanjang.getKodePajak();
            Pengujian kodePengujianOld = persistentPerpanjang.getKodePengujian();
            Pengujian kodePengujianNew = perpanjang.getKodePengujian();
            Perizinan kodePerizinanOld = persistentPerpanjang.getKodePerizinan();
            Perizinan kodePerizinanNew = perpanjang.getKodePerizinan();
            Sertifikasi kodeSertifikasiOld = persistentPerpanjang.getKodeSertifikasi();
            Sertifikasi kodeSertifikasiNew = perpanjang.getKodeSertifikasi();
            Collection<Laporan> laporanCollectionOld = persistentPerpanjang.getLaporanCollection();
            Collection<Laporan> laporanCollectionNew = perpanjang.getLaporanCollection();
            Collection<SMSPeringatan> SMSPeringatanCollectionOld = persistentPerpanjang.getSMSPeringatanCollection();
            Collection<SMSPeringatan> SMSPeringatanCollectionNew = perpanjang.getSMSPeringatanCollection();
            if (kodePajakNew != null) {
                kodePajakNew = em.getReference(kodePajakNew.getClass(), kodePajakNew.getKodePajak());
                perpanjang.setKodePajak(kodePajakNew);
            }
            if (kodePengujianNew != null) {
                kodePengujianNew = em.getReference(kodePengujianNew.getClass(), kodePengujianNew.getKodePengujian());
                perpanjang.setKodePengujian(kodePengujianNew);
            }
            if (kodePerizinanNew != null) {
                kodePerizinanNew = em.getReference(kodePerizinanNew.getClass(), kodePerizinanNew.getKodePerizinan());
                perpanjang.setKodePerizinan(kodePerizinanNew);
            }
            if (kodeSertifikasiNew != null) {
                kodeSertifikasiNew = em.getReference(kodeSertifikasiNew.getClass(), kodeSertifikasiNew.getKodeSertifikasi());
                perpanjang.setKodeSertifikasi(kodeSertifikasiNew);
            }
            Collection<Laporan> attachedLaporanCollectionNew = new ArrayList<Laporan>();
            for (Laporan laporanCollectionNewLaporanToAttach : laporanCollectionNew) {
                laporanCollectionNewLaporanToAttach = em.getReference(laporanCollectionNewLaporanToAttach.getClass(), laporanCollectionNewLaporanToAttach.getKodeLaporan());
                attachedLaporanCollectionNew.add(laporanCollectionNewLaporanToAttach);
            }
            laporanCollectionNew = attachedLaporanCollectionNew;
            perpanjang.setLaporanCollection(laporanCollectionNew);
            Collection<SMSPeringatan> attachedSMSPeringatanCollectionNew = new ArrayList<SMSPeringatan>();
            for (SMSPeringatan SMSPeringatanCollectionNewSMSPeringatanToAttach : SMSPeringatanCollectionNew) {
                SMSPeringatanCollectionNewSMSPeringatanToAttach = em.getReference(SMSPeringatanCollectionNewSMSPeringatanToAttach.getClass(), SMSPeringatanCollectionNewSMSPeringatanToAttach.getKodeSMSPeringatan());
                attachedSMSPeringatanCollectionNew.add(SMSPeringatanCollectionNewSMSPeringatanToAttach);
            }
            SMSPeringatanCollectionNew = attachedSMSPeringatanCollectionNew;
            perpanjang.setSMSPeringatanCollection(SMSPeringatanCollectionNew);
            perpanjang = em.merge(perpanjang);
            if (kodePajakOld != null && !kodePajakOld.equals(kodePajakNew)) {
                kodePajakOld.getPerpanjangCollection().remove(perpanjang);
                kodePajakOld = em.merge(kodePajakOld);
            }
            if (kodePajakNew != null && !kodePajakNew.equals(kodePajakOld)) {
                kodePajakNew.getPerpanjangCollection().add(perpanjang);
                kodePajakNew = em.merge(kodePajakNew);
            }
            if (kodePengujianOld != null && !kodePengujianOld.equals(kodePengujianNew)) {
                kodePengujianOld.getPerpanjangCollection().remove(perpanjang);
                kodePengujianOld = em.merge(kodePengujianOld);
            }
            if (kodePengujianNew != null && !kodePengujianNew.equals(kodePengujianOld)) {
                kodePengujianNew.getPerpanjangCollection().add(perpanjang);
                kodePengujianNew = em.merge(kodePengujianNew);
            }
            if (kodePerizinanOld != null && !kodePerizinanOld.equals(kodePerizinanNew)) {
                kodePerizinanOld.getPerpanjangCollection().remove(perpanjang);
                kodePerizinanOld = em.merge(kodePerizinanOld);
            }
            if (kodePerizinanNew != null && !kodePerizinanNew.equals(kodePerizinanOld)) {
                kodePerizinanNew.getPerpanjangCollection().add(perpanjang);
                kodePerizinanNew = em.merge(kodePerizinanNew);
            }
            if (kodeSertifikasiOld != null && !kodeSertifikasiOld.equals(kodeSertifikasiNew)) {
                kodeSertifikasiOld.getPerpanjangCollection().remove(perpanjang);
                kodeSertifikasiOld = em.merge(kodeSertifikasiOld);
            }
            if (kodeSertifikasiNew != null && !kodeSertifikasiNew.equals(kodeSertifikasiOld)) {
                kodeSertifikasiNew.getPerpanjangCollection().add(perpanjang);
                kodeSertifikasiNew = em.merge(kodeSertifikasiNew);
            }
            for (Laporan laporanCollectionOldLaporan : laporanCollectionOld) {
                if (!laporanCollectionNew.contains(laporanCollectionOldLaporan)) {
                    laporanCollectionOldLaporan.setKodePerpanjang(null);
                    laporanCollectionOldLaporan = em.merge(laporanCollectionOldLaporan);
                }
            }
            for (Laporan laporanCollectionNewLaporan : laporanCollectionNew) {
                if (!laporanCollectionOld.contains(laporanCollectionNewLaporan)) {
                    Perpanjang oldKodePerpanjangOfLaporanCollectionNewLaporan = laporanCollectionNewLaporan.getKodePerpanjang();
                    laporanCollectionNewLaporan.setKodePerpanjang(perpanjang);
                    laporanCollectionNewLaporan = em.merge(laporanCollectionNewLaporan);
                    if (oldKodePerpanjangOfLaporanCollectionNewLaporan != null && !oldKodePerpanjangOfLaporanCollectionNewLaporan.equals(perpanjang)) {
                        oldKodePerpanjangOfLaporanCollectionNewLaporan.getLaporanCollection().remove(laporanCollectionNewLaporan);
                        oldKodePerpanjangOfLaporanCollectionNewLaporan = em.merge(oldKodePerpanjangOfLaporanCollectionNewLaporan);
                    }
                }
            }
            for (SMSPeringatan SMSPeringatanCollectionOldSMSPeringatan : SMSPeringatanCollectionOld) {
                if (!SMSPeringatanCollectionNew.contains(SMSPeringatanCollectionOldSMSPeringatan)) {
                    SMSPeringatanCollectionOldSMSPeringatan.setKodePerpanjang(null);
                    SMSPeringatanCollectionOldSMSPeringatan = em.merge(SMSPeringatanCollectionOldSMSPeringatan);
                }
            }
            for (SMSPeringatan SMSPeringatanCollectionNewSMSPeringatan : SMSPeringatanCollectionNew) {
                if (!SMSPeringatanCollectionOld.contains(SMSPeringatanCollectionNewSMSPeringatan)) {
                    Perpanjang oldKodePerpanjangOfSMSPeringatanCollectionNewSMSPeringatan = SMSPeringatanCollectionNewSMSPeringatan.getKodePerpanjang();
                    SMSPeringatanCollectionNewSMSPeringatan.setKodePerpanjang(perpanjang);
                    SMSPeringatanCollectionNewSMSPeringatan = em.merge(SMSPeringatanCollectionNewSMSPeringatan);
                    if (oldKodePerpanjangOfSMSPeringatanCollectionNewSMSPeringatan != null && !oldKodePerpanjangOfSMSPeringatanCollectionNewSMSPeringatan.equals(perpanjang)) {
                        oldKodePerpanjangOfSMSPeringatanCollectionNewSMSPeringatan.getSMSPeringatanCollection().remove(SMSPeringatanCollectionNewSMSPeringatan);
                        oldKodePerpanjangOfSMSPeringatanCollectionNewSMSPeringatan = em.merge(oldKodePerpanjangOfSMSPeringatanCollectionNewSMSPeringatan);
                    }
                }
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
            Pajak kodePajak = perpanjang.getKodePajak();
            if (kodePajak != null) {
                kodePajak.getPerpanjangCollection().remove(perpanjang);
                kodePajak = em.merge(kodePajak);
            }
            Pengujian kodePengujian = perpanjang.getKodePengujian();
            if (kodePengujian != null) {
                kodePengujian.getPerpanjangCollection().remove(perpanjang);
                kodePengujian = em.merge(kodePengujian);
            }
            Perizinan kodePerizinan = perpanjang.getKodePerizinan();
            if (kodePerizinan != null) {
                kodePerizinan.getPerpanjangCollection().remove(perpanjang);
                kodePerizinan = em.merge(kodePerizinan);
            }
            Sertifikasi kodeSertifikasi = perpanjang.getKodeSertifikasi();
            if (kodeSertifikasi != null) {
                kodeSertifikasi.getPerpanjangCollection().remove(perpanjang);
                kodeSertifikasi = em.merge(kodeSertifikasi);
            }
            Collection<Laporan> laporanCollection = perpanjang.getLaporanCollection();
            for (Laporan laporanCollectionLaporan : laporanCollection) {
                laporanCollectionLaporan.setKodePerpanjang(null);
                laporanCollectionLaporan = em.merge(laporanCollectionLaporan);
            }
            Collection<SMSPeringatan> SMSPeringatanCollection = perpanjang.getSMSPeringatanCollection();
            for (SMSPeringatan SMSPeringatanCollectionSMSPeringatan : SMSPeringatanCollection) {
                SMSPeringatanCollectionSMSPeringatan.setKodePerpanjang(null);
                SMSPeringatanCollectionSMSPeringatan = em.merge(SMSPeringatanCollectionSMSPeringatan);
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
