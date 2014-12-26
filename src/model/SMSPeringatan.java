/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author idham
 */
@Entity
@Table(name = "SMSPeringatan")
@NamedQueries({
    @NamedQuery(name = "SMSPeringatan.findAll", query = "SELECT s FROM SMSPeringatan s"),
    @NamedQuery(name = "SMSPeringatan.findByKodeSMSPeringatan", query = "SELECT s FROM SMSPeringatan s WHERE s.kodeSMSPeringatan = :kodeSMSPeringatan"),
    @NamedQuery(name = "SMSPeringatan.findByBeritaTerkirim", query = "SELECT s FROM SMSPeringatan s WHERE s.beritaTerkirim = :beritaTerkirim"),
    @NamedQuery(name = "SMSPeringatan.findByTglPengiriman", query = "SELECT s FROM SMSPeringatan s WHERE s.tglPengiriman = :tglPengiriman"),
    @NamedQuery(name = "SMSPeringatan.findByNama", query = "SELECT s FROM SMSPeringatan s WHERE s.nama = :nama"),
    @NamedQuery(name = "SMSPeringatan.findByNoHp", query = "SELECT s FROM SMSPeringatan s WHERE s.noHp = :noHp")})
public class SMSPeringatan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_SMS_Peringatan")
    private String kodeSMSPeringatan;
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

    public SMSPeringatan() {
    }

    public SMSPeringatan(String kodeSMSPeringatan) {
        this.kodeSMSPeringatan = kodeSMSPeringatan;
    }

    public String getKodeSMSPeringatan() {
        return kodeSMSPeringatan;
    }

    public void setKodeSMSPeringatan(String kodeSMSPeringatan) {
        this.kodeSMSPeringatan = kodeSMSPeringatan;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeSMSPeringatan != null ? kodeSMSPeringatan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SMSPeringatan)) {
            return false;
        }
        SMSPeringatan other = (SMSPeringatan) object;
        if ((this.kodeSMSPeringatan == null && other.kodeSMSPeringatan != null) || (this.kodeSMSPeringatan != null && !this.kodeSMSPeringatan.equals(other.kodeSMSPeringatan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.SMSPeringatan[ kodeSMSPeringatan=" + kodeSMSPeringatan + " ]";
    }

}
