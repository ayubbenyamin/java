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
@Table(name = "Sertifikasi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sertifikasi.findAll", query = "SELECT s FROM Sertifikasi s"),
    @NamedQuery(name = "Sertifikasi.findByKodeSertifikasi", query = "SELECT s FROM Sertifikasi s WHERE s.kodeSertifikasi = :kodeSertifikasi"),
    @NamedQuery(name = "Sertifikasi.findByNoSertifikasi", query = "SELECT s FROM Sertifikasi s WHERE s.noSertifikasi = :noSertifikasi"),
    @NamedQuery(name = "Sertifikasi.findByJenisSertifikasi", query = "SELECT s FROM Sertifikasi s WHERE s.jenisSertifikasi = :jenisSertifikasi"),
    @NamedQuery(name = "Sertifikasi.findBySumberSertifikasi", query = "SELECT s FROM Sertifikasi s WHERE s.sumberSertifikasi = :sumberSertifikasi"),
    @NamedQuery(name = "Sertifikasi.findByTglJatuhTempoSrks", query = "SELECT s FROM Sertifikasi s WHERE s.tglJatuhTempoSrks = :tglJatuhTempoSrks")})
public class Sertifikasi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Sertifikasi")
    private String kodeSertifikasi;
    @Column(name = "No_Sertifikasi")
    private String noSertifikasi;
    @Column(name = "Jenis_Sertifikasi")
    private String jenisSertifikasi;
    @Column(name = "Sumber_Sertifikasi")
    private String sumberSertifikasi;
    @Column(name = "Tgl_Jatuh_Tempo_Srks")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglJatuhTempoSrks;
    @JoinColumn(name = "Id_Admin", referencedColumnName = "Id_Admin")
    @ManyToOne(optional = false)
    private Admin idAdmin;

    public Sertifikasi() {
    }

    public Sertifikasi(String kodeSertifikasi) {
        this.kodeSertifikasi = kodeSertifikasi;
    }

    public String getKodeSertifikasi() {
        return kodeSertifikasi;
    }

    public void setKodeSertifikasi(String kodeSertifikasi) {
        this.kodeSertifikasi = kodeSertifikasi;
    }

    public String getNoSertifikasi() {
        return noSertifikasi;
    }

    public void setNoSertifikasi(String noSertifikasi) {
        this.noSertifikasi = noSertifikasi;
    }

    public String getJenisSertifikasi() {
        return jenisSertifikasi;
    }

    public void setJenisSertifikasi(String jenisSertifikasi) {
        this.jenisSertifikasi = jenisSertifikasi;
    }

    public String getSumberSertifikasi() {
        return sumberSertifikasi;
    }

    public void setSumberSertifikasi(String sumberSertifikasi) {
        this.sumberSertifikasi = sumberSertifikasi;
    }

    public Date getTglJatuhTempoSrks() {
        return tglJatuhTempoSrks;
    }

    public void setTglJatuhTempoSrks(Date tglJatuhTempoSrks) {
        this.tglJatuhTempoSrks = tglJatuhTempoSrks;
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
        hash += (kodeSertifikasi != null ? kodeSertifikasi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sertifikasi)) {
            return false;
        }
        Sertifikasi other = (Sertifikasi) object;
        if ((this.kodeSertifikasi == null && other.kodeSertifikasi != null) || (this.kodeSertifikasi != null && !this.kodeSertifikasi.equals(other.kodeSertifikasi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Sertifikasi[ kodeSertifikasi=" + kodeSertifikasi + " ]";
    }

}
