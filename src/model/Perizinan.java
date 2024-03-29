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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author idham
 */
@Entity
@Table(name = "Perizinan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Perizinan.findAll", query = "SELECT p FROM Perizinan p"),
    @NamedQuery(name = "Perizinan.findByKodePerizinan", query = "SELECT p FROM Perizinan p WHERE p.kodePerizinan = :kodePerizinan"),
    @NamedQuery(name = "Perizinan.findByNoPerizinan", query = "SELECT p FROM Perizinan p WHERE p.noPerizinan = :noPerizinan"),
    @NamedQuery(name = "Perizinan.findByJenisPerizinan", query = "SELECT p FROM Perizinan p WHERE p.jenisPerizinan = :jenisPerizinan"),
    @NamedQuery(name = "Perizinan.findByKegunaanPerizinan", query = "SELECT p FROM Perizinan p WHERE p.kegunaanPerizinan = :kegunaanPerizinan"),
    @NamedQuery(name = "Perizinan.findBySumberPerizinan", query = "SELECT p FROM Perizinan p WHERE p.sumberPerizinan = :sumberPerizinan"),
    @NamedQuery(name = "Perizinan.findByTglJatuhTempoPrzn", query = "SELECT p FROM Perizinan p WHERE p.tglJatuhTempoPrzn = :tglJatuhTempoPrzn")})
public class Perizinan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Perizinan")
    private String kodePerizinan;
    @Column(name = "No_Perizinan")
    private String noPerizinan;
    @Column(name = "Jenis_Perizinan")
    private String jenisPerizinan;
    @Column(name = "Kegunaan_Perizinan")
    private String kegunaanPerizinan;
    @Column(name = "Sumber_Perizinan")
    private String sumberPerizinan;
    @Column(name = "Tgl_Jatuh_Tempo_Przn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglJatuhTempoPrzn;
    @JoinColumn(name = "Id_Admin", referencedColumnName = "Id_Admin")
    @ManyToOne(optional = false)
    private Admin idAdmin;

    public Perizinan() {
    }

    public Perizinan(String kodePerizinan) {
        this.kodePerizinan = kodePerizinan;
    }

    public String getKodePerizinan() {
        return kodePerizinan;
    }

    public void setKodePerizinan(String kodePerizinan) {
        this.kodePerizinan = kodePerizinan;
    }

    public String getNoPerizinan() {
        return noPerizinan;
    }

    public void setNoPerizinan(String noPerizinan) {
        this.noPerizinan = noPerizinan;
    }

    public String getJenisPerizinan() {
        return jenisPerizinan;
    }

    public void setJenisPerizinan(String jenisPerizinan) {
        this.jenisPerizinan = jenisPerizinan;
    }

    public String getKegunaanPerizinan() {
        return kegunaanPerizinan;
    }

    public void setKegunaanPerizinan(String kegunaanPerizinan) {
        this.kegunaanPerizinan = kegunaanPerizinan;
    }

    public String getSumberPerizinan() {
        return sumberPerizinan;
    }

    public void setSumberPerizinan(String sumberPerizinan) {
        this.sumberPerizinan = sumberPerizinan;
    }

    public Date getTglJatuhTempoPrzn() {
        return tglJatuhTempoPrzn;
    }

    public void setTglJatuhTempoPrzn(Date tglJatuhTempoPrzn) {
        this.tglJatuhTempoPrzn = tglJatuhTempoPrzn;
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
        hash += (kodePerizinan != null ? kodePerizinan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perizinan)) {
            return false;
        }
        Perizinan other = (Perizinan) object;
        if ((this.kodePerizinan == null && other.kodePerizinan != null) || (this.kodePerizinan != null && !this.kodePerizinan.equals(other.kodePerizinan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Perizinan[ kodePerizinan=" + kodePerizinan + " ]";
    }

}
