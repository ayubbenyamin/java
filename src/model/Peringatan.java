/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author idham
 */
@Entity
@Table(name = "Peringatan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Peringatan.findAll", query = "SELECT p FROM Peringatan p"),
    @NamedQuery(name = "Peringatan.findByKodePeringatan", query = "SELECT p FROM Peringatan p WHERE p.kodePeringatan = :kodePeringatan"),
    @NamedQuery(name = "Peringatan.findByJenisPeringatan", query = "SELECT p FROM Peringatan p WHERE p.jenisPeringatan = :jenisPeringatan"),
    @NamedQuery(name = "Peringatan.findByTglJatuhTempo", query = "SELECT p FROM Peringatan p WHERE p.tglJatuhTempo = :tglJatuhTempo"),
    @NamedQuery(name = "Peringatan.findByTglKetetapan", query = "SELECT p FROM Peringatan p WHERE p.tglKetetapan = :tglKetetapan"),
    @NamedQuery(name = "Peringatan.findByStatus", query = "SELECT p FROM Peringatan p WHERE p.status = :status")})
public class Peringatan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Peringatan")
    private String kodePeringatan;
    @Column(name = "Jenis_Peringatan")
    private String jenisPeringatan;
    @Column(name = "Tgl_Jatuh_Tempo")
    private String tglJatuhTempo;
    @Column(name = "Tgl_Ketetapan")
    private String tglKetetapan;
    @Column(name = "Status")
    private String status;
    @JoinColumn(name = "Kode_Pajak", referencedColumnName = "Kode_Pajak")
    @ManyToOne(optional = false)
    private Pajak kodePajak;
    @JoinColumn(name = "Kode_Pengujian", referencedColumnName = "Kode_Pengujian")
    @ManyToOne
    private Pengujian kodePengujian;
    @JoinColumn(name = "Kode_Perizinan", referencedColumnName = "Kode_Perizinan")
    @ManyToOne
    private Perizinan kodePerizinan;
    @JoinColumn(name = "Kode_Sertifikasi", referencedColumnName = "Kode_Sertifikasi")
    @ManyToOne
    private Sertifikasi kodeSertifikasi;
    @OneToMany(mappedBy = "kodePeringatan")
    private Collection<Perpanjang> perpanjangCollection;
    @OneToMany(mappedBy = "kodePeringatan")
    private Collection<Sms> smsCollection;

    public Peringatan() {
    }

    public Peringatan(String kodePeringatan) {
        this.kodePeringatan = kodePeringatan;
    }

    public String getKodePeringatan() {
        return kodePeringatan;
    }

    public void setKodePeringatan(String kodePeringatan) {
        this.kodePeringatan = kodePeringatan;
    }

    public String getJenisPeringatan() {
        return jenisPeringatan;
    }

    public void setJenisPeringatan(String jenisPeringatan) {
        this.jenisPeringatan = jenisPeringatan;
    }

    public String getTglJatuhTempo() {
        return tglJatuhTempo;
    }

    public void setTglJatuhTempo(String tglJatuhTempo) {
        this.tglJatuhTempo = tglJatuhTempo;
    }

    public String getTglKetetapan() {
        return tglKetetapan;
    }

    public void setTglKetetapan(String tglKetetapan) {
        this.tglKetetapan = tglKetetapan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pajak getKodePajak() {
        return kodePajak;
    }

    public void setKodePajak(Pajak kodePajak) {
        this.kodePajak = kodePajak;
    }

    public Pengujian getKodePengujian() {
        return kodePengujian;
    }

    public void setKodePengujian(Pengujian kodePengujian) {
        this.kodePengujian = kodePengujian;
    }

    public Perizinan getKodePerizinan() {
        return kodePerizinan;
    }

    public void setKodePerizinan(Perizinan kodePerizinan) {
        this.kodePerizinan = kodePerizinan;
    }

    public Sertifikasi getKodeSertifikasi() {
        return kodeSertifikasi;
    }

    public void setKodeSertifikasi(Sertifikasi kodeSertifikasi) {
        this.kodeSertifikasi = kodeSertifikasi;
    }

    @XmlTransient
    public Collection<Perpanjang> getPerpanjangCollection() {
        return perpanjangCollection;
    }

    public void setPerpanjangCollection(Collection<Perpanjang> perpanjangCollection) {
        this.perpanjangCollection = perpanjangCollection;
    }

    @XmlTransient
    public Collection<Sms> getSmsCollection() {
        return smsCollection;
    }

    public void setSmsCollection(Collection<Sms> smsCollection) {
        this.smsCollection = smsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodePeringatan != null ? kodePeringatan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Peringatan)) {
            return false;
        }
        Peringatan other = (Peringatan) object;
        if ((this.kodePeringatan == null && other.kodePeringatan != null) || (this.kodePeringatan != null && !this.kodePeringatan.equals(other.kodePeringatan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Peringatan[ kodePeringatan=" + kodePeringatan + " ]";
    }

}
