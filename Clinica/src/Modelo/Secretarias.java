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
@Table(name = "secretarias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Secretarias.findAll", query = "SELECT s FROM Secretarias s")
    , @NamedQuery(name = "Secretarias.findByIdsec", query = "SELECT s FROM Secretarias s WHERE s.idsec = :idsec")
    , @NamedQuery(name = "Secretarias.findByNombre", query = "SELECT s FROM Secretarias s WHERE s.nombre = :nombre")
    , @NamedQuery(name = "Secretarias.findByApellido", query = "SELECT s FROM Secretarias s WHERE s.apellido = :apellido")
    , @NamedQuery(name = "Secretarias.findByTelefono", query = "SELECT s FROM Secretarias s WHERE s.telefono = :telefono")})
public class Secretarias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idsec")
    private Integer idsec;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apellido")
    private String apellido;
    @Basic(optional = false)
    @Column(name = "telefono")
    private String telefono;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idsecretaria")
    private Collection<Historial> historialCollection;

    public Secretarias() {
    }

    public Secretarias(Integer idsec) {
        this.idsec = idsec;
    }

    public Secretarias(Integer idsec, String nombre, String apellido, String telefono) {
        this.idsec = idsec;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public Integer getIdsec() {
        return idsec;
    }

    public void setIdsec(Integer idsec) {
        this.idsec = idsec;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
        hash += (idsec != null ? idsec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Secretarias)) {
            return false;
        }
        Secretarias other = (Secretarias) object;
        if ((this.idsec == null && other.idsec != null) || (this.idsec != null && !this.idsec.equals(other.idsec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controller.Secretarias[ idsec=" + idsec + " ]";
    }
    
}
