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
@Table(name = "Pengujian")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pengujian.findAll", query = "SELECT p FROM Pengujian p"),
    @NamedQuery(name = "Pengujian.findByKodePengujian", query = "SELECT p FROM Pengujian p WHERE p.kodePengujian = :kodePengujian"),
    @NamedQuery(name = "Pengujian.findByNoPengujian", query = "SELECT p FROM Pengujian p WHERE p.noPengujian = :noPengujian"),
    @NamedQuery(name = "Pengujian.findByJenisPengujian", query = "SELECT p FROM Pengujian p WHERE p.jenisPengujian = :jenisPengujian"),
    @NamedQuery(name = "Pengujian.findByMetode", query = "SELECT p FROM Pengujian p WHERE p.metode = :metode"),
    @NamedQuery(name = "Pengujian.findByTglKetetapanPgjn", query = "SELECT p FROM Pengujian p WHERE p.tglKetetapanPgjn = :tglKetetapanPgjn"),
    @NamedQuery(name = "Pengujian.findByTglJatuhTempoPgjn", query = "SELECT p FROM Pengujian p WHERE p.tglJatuhTempoPgjn = :tglJatuhTempoPgjn"),
    @NamedQuery(name = "Pengujian.findBySumberPengujian", query = "SELECT p FROM Pengujian p WHERE p.sumberPengujian = :sumberPengujian"),
    @NamedQuery(name = "Pengujian.findByStatusPgjn", query = "SELECT p FROM Pengujian p WHERE p.statusPgjn = :statusPgjn")})
public class Pengujian implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Pengujian")
    private String kodePengujian;
    @Column(name = "No_Pengujian")
    private String noPengujian;
    @Column(name = "Jenis_Pengujian")
    private String jenisPengujian;
    @Column(name = "Metode")
    private String metode;
    @Column(name = "Tgl_Ketetapan_Pgjn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglKetetapanPgjn;
    @Column(name = "Tgl_Jatuh_Tempo_Pgjn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglJatuhTempoPgjn;
    @Column(name = "Sumber_Pengujian")
    private String sumberPengujian;
    @Column(name = "Status_Pgjn")
    private String statusPgjn;
    @OneToMany(mappedBy = "kodePengujian")
    private Collection<Peringatan> peringatanCollection;
    @JoinColumn(name = "Id_Admin", referencedColumnName = "Id_Admin")
    @ManyToOne(optional = false)
    private Admin idAdmin;

    public Pengujian() {
    }

    public Pengujian(String kodePengujian) {
        this.kodePengujian = kodePengujian;
    }

    public String getKodePengujian() {
        return kodePengujian;
    }

    public void setKodePengujian(String kodePengujian) {
        this.kodePengujian = kodePengujian;
    }

    public String getNoPengujian() {
        return noPengujian;
    }

    public void setNoPengujian(String noPengujian) {
        this.noPengujian = noPengujian;
    }

    public String getJenisPengujian() {
        return jenisPengujian;
    }

    public void setJenisPengujian(String jenisPengujian) {
        this.jenisPengujian = jenisPengujian;
    }

    public String getMetode() {
        return metode;
    }

    public void setMetode(String metode) {
        this.metode = metode;
    }

    public Date getTglKetetapanPgjn() {
        return tglKetetapanPgjn;
    }

    public void setTglKetetapanPgjn(Date tglKetetapanPgjn) {
        this.tglKetetapanPgjn = tglKetetapanPgjn;
    }

    public Date getTglJatuhTempoPgjn() {
        return tglJatuhTempoPgjn;
    }

    public void setTglJatuhTempoPgjn(Date tglJatuhTempoPgjn) {
        this.tglJatuhTempoPgjn = tglJatuhTempoPgjn;
    }

    public String getSumberPengujian() {
        return sumberPengujian;
    }

    public void setSumberPengujian(String sumberPengujian) {
        this.sumberPengujian = sumberPengujian;
    }

    public String getStatusPgjn() {
        return statusPgjn;
    }

    public void setStatusPgjn(String statusPgjn) {
        this.statusPgjn = statusPgjn;
    }

    @XmlTransient
    public Collection<Peringatan> getPeringatanCollection() {
        return peringatanCollection;
    }

    public void setPeringatanCollection(Collection<Peringatan> peringatanCollection) {
        this.peringatanCollection = peringatanCollection;
    }

    public Admin getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Admin idAdmin) {
        this.idAdmin = idAdmin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodePengujian != null ? kodePengujian.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pengujian)) {
            return false;
        }
        Pengujian other = (Pengujian) object;
        if ((this.kodePengujian == null && other.kodePengujian != null) || (this.kodePengujian != null && !this.kodePengujian.equals(other.kodePengujian))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Pengujian[ kodePengujian=" + kodePengujian + " ]";
    }

}
