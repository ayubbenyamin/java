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
@Table(name = "Perpanjang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Perpanjang.findAll", query = "SELECT p FROM Perpanjang p"),
    @NamedQuery(name = "Perpanjang.findByKodePerpanjang", query = "SELECT p FROM Perpanjang p WHERE p.kodePerpanjang = :kodePerpanjang"),
    @NamedQuery(name = "Perpanjang.findByJenisPeringatan", query = "SELECT p FROM Perpanjang p WHERE p.jenisPeringatan = :jenisPeringatan"),
    @NamedQuery(name = "Perpanjang.findByTglJatuhTempo", query = "SELECT p FROM Perpanjang p WHERE p.tglJatuhTempo = :tglJatuhTempo"),
    @NamedQuery(name = "Perpanjang.findByTglPerpanjang", query = "SELECT p FROM Perpanjang p WHERE p.tglPerpanjang = :tglPerpanjang")})
public class Perpanjang implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Perpanjang")
    private String kodePerpanjang;
    @Column(name = "Jenis_Peringatan")
    private String jenisPeringatan;
    @Column(name = "Tgl_Jatuh_Tempo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglJatuhTempo;
    @Column(name = "Tgl_Perpanjang")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglPerpanjang;
    @OneToMany(mappedBy = "kodePerpanjang")
    private Collection<Laporan> laporanCollection;
    @JoinColumn(name = "Id_Admin", referencedColumnName = "Id_Admin")
    @ManyToOne
    private Admin idAdmin;
    @JoinColumn(name = "Kode_Pajak", referencedColumnName = "Kode_Pajak")
    @ManyToOne
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
    @OneToMany(mappedBy = "kodePerpanjang")
    private Collection<SMSPeringatan> sMSPeringatanCollection;

    public Perpanjang() {
    }

    public Perpanjang(String kodePerpanjang) {
        this.kodePerpanjang = kodePerpanjang;
    }

    public String getKodePerpanjang() {
        return kodePerpanjang;
    }

    public void setKodePerpanjang(String kodePerpanjang) {
        this.kodePerpanjang = kodePerpanjang;
    }

    public String getJenisPeringatan() {
        return jenisPeringatan;
    }

    public void setJenisPeringatan(String jenisPeringatan) {
        this.jenisPeringatan = jenisPeringatan;
    }

    public Date getTglJatuhTempo() {
        return tglJatuhTempo;
    }

    public void setTglJatuhTempo(Date tglJatuhTempo) {
        this.tglJatuhTempo = tglJatuhTempo;
    }

    public Date getTglPerpanjang() {
        return tglPerpanjang;
    }

    public void setTglPerpanjang(Date tglPerpanjang) {
        this.tglPerpanjang = tglPerpanjang;
    }

    @XmlTransient
    public Collection<Laporan> getLaporanCollection() {
        return laporanCollection;
    }

    public void setLaporanCollection(Collection<Laporan> laporanCollection) {
        this.laporanCollection = laporanCollection;
    }

    public Admin getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Admin idAdmin) {
        this.idAdmin = idAdmin;
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
    public Collection<SMSPeringatan> getSMSPeringatanCollection() {
        return sMSPeringatanCollection;
    }

    public void setSMSPeringatanCollection(Collection<SMSPeringatan> sMSPeringatanCollection) {
        this.sMSPeringatanCollection = sMSPeringatanCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodePerpanjang != null ? kodePerpanjang.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perpanjang)) {
            return false;
        }
        Perpanjang other = (Perpanjang) object;
        if ((this.kodePerpanjang == null && other.kodePerpanjang != null) || (this.kodePerpanjang != null && !this.kodePerpanjang.equals(other.kodePerpanjang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Perpanjang[ kodePerpanjang=" + kodePerpanjang + " ]";
    }

}
