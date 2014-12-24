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
@Table(name = "Pajak")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pajak.findAll", query = "SELECT p FROM Pajak p"),
    @NamedQuery(name = "Pajak.findByKodePajak", query = "SELECT p FROM Pajak p WHERE p.kodePajak = :kodePajak"),
    @NamedQuery(name = "Pajak.findByNoNpwp", query = "SELECT p FROM Pajak p WHERE p.noNpwp = :noNpwp"),
    @NamedQuery(name = "Pajak.findByJenisPajak", query = "SELECT p FROM Pajak p WHERE p.jenisPajak = :jenisPajak"),
    @NamedQuery(name = "Pajak.findBySumberPajak", query = "SELECT p FROM Pajak p WHERE p.sumberPajak = :sumberPajak"),
    @NamedQuery(name = "Pajak.findByPokokPajak", query = "SELECT p FROM Pajak p WHERE p.pokokPajak = :pokokPajak"),
    @NamedQuery(name = "Pajak.findByTglJatuhTempoPjk", query = "SELECT p FROM Pajak p WHERE p.tglJatuhTempoPjk = :tglJatuhTempoPjk")})
public class Pajak implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Pajak")
    private String kodePajak;
    @Column(name = "No_Npwp")
    private String noNpwp;
    @Column(name = "Jenis_Pajak")
    private String jenisPajak;
    @Column(name = "Sumber_Pajak")
    private String sumberPajak;
    @Column(name = "Pokok_Pajak")
    private String pokokPajak;
    @Column(name = "Tgl_Jatuh_Tempo_Pjk")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglJatuhTempoPjk;
    @JoinColumn(name = "Id_Admin", referencedColumnName = "Id_Admin")
    @ManyToOne(optional = false)
    private Admin idAdmin;
    @OneToMany(mappedBy = "kodePajak")
    private Collection<Perpanjang> perpanjangCollection;

    public Pajak() {
    }

    public Pajak(String kodePajak) {
        this.kodePajak = kodePajak;
    }

    public String getKodePajak() {
        return kodePajak;
    }

    public void setKodePajak(String kodePajak) {
        this.kodePajak = kodePajak;
    }

    public String getNoNpwp() {
        return noNpwp;
    }

    public void setNoNpwp(String noNpwp) {
        this.noNpwp = noNpwp;
    }

    public String getJenisPajak() {
        return jenisPajak;
    }

    public void setJenisPajak(String jenisPajak) {
        this.jenisPajak = jenisPajak;
    }

    public String getSumberPajak() {
        return sumberPajak;
    }

    public void setSumberPajak(String sumberPajak) {
        this.sumberPajak = sumberPajak;
    }

    public String getPokokPajak() {
        return pokokPajak;
    }

    public void setPokokPajak(String pokokPajak) {
        this.pokokPajak = pokokPajak;
    }

    public Date getTglJatuhTempoPjk() {
        return tglJatuhTempoPjk;
    }

    public void setTglJatuhTempoPjk(Date tglJatuhTempoPjk) {
        this.tglJatuhTempoPjk = tglJatuhTempoPjk;
    }

    public Admin getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Admin idAdmin) {
        this.idAdmin = idAdmin;
    }

    @XmlTransient
    public Collection<Perpanjang> getPerpanjangCollection() {
        return perpanjangCollection;
    }

    public void setPerpanjangCollection(Collection<Perpanjang> perpanjangCollection) {
        this.perpanjangCollection = perpanjangCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodePajak != null ? kodePajak.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pajak)) {
            return false;
        }
        Pajak other = (Pajak) object;
        if ((this.kodePajak == null && other.kodePajak != null) || (this.kodePajak != null && !this.kodePajak.equals(other.kodePajak))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Pajak[ kodePajak=" + kodePajak + " ]";
    }

}
