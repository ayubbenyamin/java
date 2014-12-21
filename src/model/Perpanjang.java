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
@Table(name = "Perpanjang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Perpanjang.findAll", query = "SELECT p FROM Perpanjang p"),
    @NamedQuery(name = "Perpanjang.findByKodePerpanjang", query = "SELECT p FROM Perpanjang p WHERE p.kodePerpanjang = :kodePerpanjang"),
    @NamedQuery(name = "Perpanjang.findByTglJatuhTempoPrpg", query = "SELECT p FROM Perpanjang p WHERE p.tglJatuhTempoPrpg = :tglJatuhTempoPrpg"),
    @NamedQuery(name = "Perpanjang.findByTglPerpanjang", query = "SELECT p FROM Perpanjang p WHERE p.tglPerpanjang = :tglPerpanjang"),
    @NamedQuery(name = "Perpanjang.findByStatusPerpanjang", query = "SELECT p FROM Perpanjang p WHERE p.statusPerpanjang = :statusPerpanjang")})
public class Perpanjang implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Perpanjang")
    private String kodePerpanjang;
    @Column(name = "Tgl_Jatuh_Tempo_Prpg")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglJatuhTempoPrpg;
    @Column(name = "Tgl_Perpanjang")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglPerpanjang;
    @Column(name = "Status_Perpanjang")
    private String statusPerpanjang;
    @JoinColumn(name = "Id_Admin", referencedColumnName = "Id_Admin")
    @ManyToOne
    private Admin idAdmin;
    @JoinColumn(name = "Kode_Peringatan", referencedColumnName = "Kode_Peringatan")
    @ManyToOne
    private Peringatan kodePeringatan;

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

    public Date getTglJatuhTempoPrpg() {
        return tglJatuhTempoPrpg;
    }

    public void setTglJatuhTempoPrpg(Date tglJatuhTempoPrpg) {
        this.tglJatuhTempoPrpg = tglJatuhTempoPrpg;
    }

    public Date getTglPerpanjang() {
        return tglPerpanjang;
    }

    public void setTglPerpanjang(Date tglPerpanjang) {
        this.tglPerpanjang = tglPerpanjang;
    }

    public String getStatusPerpanjang() {
        return statusPerpanjang;
    }

    public void setStatusPerpanjang(String statusPerpanjang) {
        this.statusPerpanjang = statusPerpanjang;
    }

    public Admin getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Admin idAdmin) {
        this.idAdmin = idAdmin;
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
