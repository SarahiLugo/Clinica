/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Marisol
 */
@Entity
@Table(name = "doctores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Doctores.findAll", query = "SELECT d FROM Doctores d")
    , @NamedQuery(name = "Doctores.findByCedula", query = "SELECT d FROM Doctores d WHERE d.cedula = :cedula")
    , @NamedQuery(name = "Doctores.findByNombre", query = "SELECT d FROM Doctores d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Doctores.findByTelefono", query = "SELECT d FROM Doctores d WHERE d.telefono = :telefono")
    , @NamedQuery(name = "Doctores.findByMail", query = "SELECT d FROM Doctores d WHERE d.mail = :mail")
    , @NamedQuery(name = "Doctores.findByDomicilio", query = "SELECT d FROM Doctores d WHERE d.domicilio = :domicilio")})
public class Doctores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cedula")
    private Integer cedula;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "mail")
    private String mail;
    @Basic(optional = false)
    @Column(name = "domicilio")
    private String domicilio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ceduladoctor")
    private Collection<Historial> historialCollection;

    public Doctores() {
    }

    public Doctores(Integer cedula) {
        this.cedula = cedula;
    }

    public Doctores(Integer cedula, String nombre, String telefono, String mail, String domicilio) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.mail = mail;
        this.domicilio = domicilio;
    }

    public Integer getCedula() {
        return cedula;
    }

    public void setCedula(Integer cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    @XmlTransient
    public Collection<Historial> getHistorialCollection() {
        return historialCollection;
    }

    public void setHistorialCollection(Collection<Historial> historialCollection) {
        this.historialCollection = historialCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedula != null ? cedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Doctores)) {
            return false;
        }
        Doctores other = (Doctores) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controller.Doctores[ cedula=" + cedula + " ]";
    }
    
}
