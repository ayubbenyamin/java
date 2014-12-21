/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author idham
 */
@Entity
@Table(name = "SMS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sms.findAll", query = "SELECT s FROM Sms s"),
    @NamedQuery(name = "Sms.findByKodeSMS", query = "SELECT s FROM Sms s WHERE s.kodeSMS = :kodeSMS"),
    @NamedQuery(name = "Sms.findByBeritaTerkirim", query = "SELECT s FROM Sms s WHERE s.beritaTerkirim = :beritaTerkirim"),
    @NamedQuery(name = "Sms.findByTglPengiriman", query = "SELECT s FROM Sms s WHERE s.tglPengiriman = :tglPengiriman"),
    @NamedQuery(name = "Sms.findByNama", query = "SELECT s FROM Sms s WHERE s.nama = :nama"),
    @NamedQuery(name = "Sms.findByNoHp", query = "SELECT s FROM Sms s WHERE s.noHp = :noHp")})
public class Sms implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_SMS")
    private String kodeSMS;
    @Column(name = "Berita_Terkirim")
    private String beritaTerkirim;
    @Column(name = "Tgl_Pengiriman")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglPengiriman;
    @Lob
    @Column(name = "Isi_SMS")
    private String isiSMS;
    @Column(name = "Nama")
    private String nama;
    @Column(name = "No_Hp")
    private Long noHp;
    @OneToMany(mappedBy = "kodeSMS")
    private Collection<User> userCollection;
    @JoinColumn(name = "Kode_Peringatan", referencedColumnName = "Kode_Peringatan")
    @ManyToOne
    private Peringatan kodePeringatan;

    public Sms() {
    }

    public Sms(String kodeSMS) {
        this.kodeSMS = kodeSMS;
    }

    public String getKodeSMS() {
        return kodeSMS;
    }

    public void setKodeSMS(String kodeSMS) {
        this.kodeSMS = kodeSMS;
    }

    public String getBeritaTerkirim() {
        return beritaTerkirim;
    }

    public void setBeritaTerkirim(String beritaTerkirim) {
        this.beritaTerkirim = beritaTerkirim;
    }

    public Date getTglPengiriman() {
        return tglPengiriman;
    }

    public void setTglPengiriman(Date tglPengiriman) {
        this.tglPengiriman = tglPengiriman;
    }

    public String getIsiSMS() {
        return isiSMS;
    }

    public void setIsiSMS(String isiSMS) {
        this.isiSMS = isiSMS;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Long getNoHp() {
        return noHp;
    }

    public void setNoHp(Long noHp) {
        this.noHp = noHp;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public Peringatan getKodePeringatan() {
        return kodePeringatan;
    }

    public void setKodePeringatan(Peringatan kodePeringatan) {
        this.kodePeringatan = kodePeringatan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeSMS != null ? kodeSMS.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sms)) {
            return false;
        }
        Sms other = (Sms) object;
        if ((this.kodeSMS == null && other.kodeSMS != null) || (this.kodeSMS != null && !this.kodeSMS.equals(other.kodeSMS))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Sms[ kodeSMS=" + kodeSMS + " ]";
    }

}
