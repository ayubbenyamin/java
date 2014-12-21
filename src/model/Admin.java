/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author idham
 */
@Entity
@Table(name = "Admin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Admin.findAll", query = "SELECT a FROM Admin a"),
    @NamedQuery(name = "Admin.findByIdAdmin", query = "SELECT a FROM Admin a WHERE a.idAdmin = :idAdmin"),
    @NamedQuery(name = "Admin.findByPassword", query = "SELECT a FROM Admin a WHERE a.password = :password"),
    @NamedQuery(name = "Admin.findByNamaAdmin", query = "SELECT a FROM Admin a WHERE a.namaAdmin = :namaAdmin"),
    @NamedQuery(name = "Admin.findByAlamatAdmin", query = "SELECT a FROM Admin a WHERE a.alamatAdmin = :alamatAdmin"),
    @NamedQuery(name = "Admin.findByNoHpAdmin", query = "SELECT a FROM Admin a WHERE a.noHpAdmin = :noHpAdmin")})
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Admin")
    private String idAdmin;
    @Column(name = "Password")
    private String password;
    @Column(name = "Nama_Admin")
    private String namaAdmin;
    @Column(name = "Alamat_Admin")
    private String alamatAdmin;
    @Column(name = "No_Hp_Admin")
    private String noHpAdmin;

    public Admin() {
    }

    public Admin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaAdmin() {
        return namaAdmin;
    }

    public void setNamaAdmin(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }

    public String getAlamatAdmin() {
        return alamatAdmin;
    }

    public void setAlamatAdmin(String alamatAdmin) {
        this.alamatAdmin = alamatAdmin;
    }

    public String getNoHpAdmin() {
        return noHpAdmin;
    }

    public void setNoHpAdmin(String noHpAdmin) {
        this.noHpAdmin = noHpAdmin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAdmin != null ? idAdmin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;
        if ((this.idAdmin == null && other.idAdmin != null) || (this.idAdmin != null && !this.idAdmin.equals(other.idAdmin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Admin[ idAdmin=" + idAdmin + " ]";
    }

}
