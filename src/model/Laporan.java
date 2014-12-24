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
@Table(name = "Laporan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Laporan.findAll", query = "SELECT l FROM Laporan l"),
    @NamedQuery(name = "Laporan.findByKodeLaporan", query = "SELECT l FROM Laporan l WHERE l.kodeLaporan = :kodeLaporan"),
    @NamedQuery(name = "Laporan.findByJenis", query = "SELECT l FROM Laporan l WHERE l.jenis = :jenis"),
    @NamedQuery(name = "Laporan.findBySumber", query = "SELECT l FROM Laporan l WHERE l.sumber = :sumber"),
    @NamedQuery(name = "Laporan.findByTglJatuhTempolp", query = "SELECT l FROM Laporan l WHERE l.tglJatuhTempolp = :tglJatuhTempolp")})
public class Laporan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Laporan")
    private String kodeLaporan;
    @Column(name = "Jenis")
    private String jenis;
    @Column(name = "Sumber")
    private String sumber;
    @Column(name = "Tgl_Jatuh_Tempolp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglJatuhTempolp;
    @JoinColumn(name = "Kode_Perpanjang", referencedColumnName = "Kode_Perpanjang")
    @ManyToOne
    private Perpanjang kodePerpanjang;

    public Laporan() {
    }

    public Laporan(String kodeLaporan) {
        this.kodeLaporan = kodeLaporan;
    }

    public String getKodeLaporan() {
        return kodeLaporan;
    }

    public void setKodeLaporan(String kodeLaporan) {
        this.kodeLaporan = kodeLaporan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }

    public Date getTglJatuhTempolp() {
        return tglJatuhTempolp;
    }

    public void setTglJatuhTempolp(Date tglJatuhTempolp) {
        this.tglJatuhTempolp = tglJatuhTempolp;
    }

    public Perpanjang getKodePerpanjang() {
        return kodePerpanjang;
    }

    public void setKodePerpanjang(Perpanjang kodePerpanjang) {
        this.kodePerpanjang = kodePerpanjang;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeLaporan != null ? kodeLaporan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Laporan)) {
            return false;
        }
        Laporan other = (Laporan) object;
        if ((this.kodeLaporan == null && other.kodeLaporan != null) || (this.kodeLaporan != null && !this.kodeLaporan.equals(other.kodeLaporan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Laporan[ kodeLaporan=" + kodeLaporan + " ]";
    }

}
