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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author idham
 */
@Entity
@Table(name = "User")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByIdUser", query = "SELECT u FROM User u WHERE u.idUser = :idUser"),
    @NamedQuery(name = "User.findByNamaUser", query = "SELECT u FROM User u WHERE u.namaUser = :namaUser"),
    @NamedQuery(name = "User.findByNoHpUser", query = "SELECT u FROM User u WHERE u.noHpUser = :noHpUser"),
    @NamedQuery(name = "User.findByAlamatUser", query = "SELECT u FROM User u WHERE u.alamatUser = :alamatUser")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_User")
    private String idUser;
    @Column(name = "Nama_User")
    private String namaUser;
    @Column(name = "No_Hp_User")
    private String noHpUser;
    @Column(name = "Alamat_User")
    private String alamatUser;
    @JoinColumn(name = "Kode_SMS_Peringatan", referencedColumnName = "Kode_SMS_Peringatan")
    @ManyToOne
    private SMSPeringatan kodeSMSPeringatan;

    public User() {
    }

    public User(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNoHpUser() {
        return noHpUser;
    }

    public void setNoHpUser(String noHpUser) {
        this.noHpUser = noHpUser;
    }

    public String getAlamatUser() {
        return alamatUser;
    }

    public void setAlamatUser(String alamatUser) {
        this.alamatUser = alamatUser;
    }

    public SMSPeringatan getKodeSMSPeringatan() {
        return kodeSMSPeringatan;
    }

    public void setKodeSMSPeringatan(SMSPeringatan kodeSMSPeringatan) {
        this.kodeSMSPeringatan = kodeSMSPeringatan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.User[ idUser=" + idUser + " ]";
    }

}
