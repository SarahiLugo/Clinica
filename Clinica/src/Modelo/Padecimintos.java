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
@Table(name = "padecimintos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Padecimintos.findAll", query = "SELECT p FROM Padecimintos p")
    , @NamedQuery(name = "Padecimintos.findByPadeci", query = "SELECT p FROM Padecimintos p WHERE p.padeci = :padeci")
    , @NamedQuery(name = "Padecimintos.findByDescripcion", query = "SELECT p FROM Padecimintos p WHERE p.descripcion = :descripcion")})
public class Padecimintos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "padeci")
    private Integer padeci;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nodepadecimiento")
    private Collection<Historial> historialCollection;

    public Padecimintos() {
    }

    public Padecimintos(Integer padeci) {
        this.padeci = padeci;
    }

    public Padecimintos(Integer padeci, String descripcion) {
        this.padeci = padeci;
        this.descripcion = descripcion;
    }

    public Integer getPadeci() {
        return padeci;
    }

    public void setPadeci(Integer padeci) {
        this.padeci = padeci;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        hash += (padeci != null ? padeci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Padecimintos)) {
            return false;
        }
        Padecimintos other = (Padecimintos) object;
        if ((this.padeci == null && other.padeci != null) || (this.padeci != null && !this.padeci.equals(other.padeci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controller.Padecimintos[ padeci=" + padeci + " ]";
    }
    
}
