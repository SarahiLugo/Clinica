/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marisol
 */
@Entity
@Table(name = "historial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historial.findAll", query = "SELECT h FROM Historial h")
    , @NamedQuery(name = "Historial.findByNopade", query = "SELECT h FROM Historial h WHERE h.nopade = :nopade")
    , @NamedQuery(name = "Historial.findByFechaconsulta", query = "SELECT h FROM Historial h WHERE h.fechaconsulta = :fechaconsulta")
    , @NamedQuery(name = "Historial.findByCosto", query = "SELECT h FROM Historial h WHERE h.costo = :costo")
    , @NamedQuery(name = "Historial.findByFechaalta", query = "SELECT h FROM Historial h WHERE h.fechaalta = :fechaalta")})
public class Historial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nopade")
    private Integer nopade;
    @Basic(optional = false)
    @Column(name = "fechaconsulta")
    private String fechaconsulta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "costo")
    private BigDecimal costo;
    @Basic(optional = false)
    @Column(name = "fechaalta")
    private String fechaalta;
    @JoinColumn(name = "ceduladoctor", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Doctores ceduladoctor;
    @JoinColumn(name = "idpaciente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pacientes idpaciente;
    @JoinColumn(name = "nodepadecimiento", referencedColumnName = "padeci")
    @ManyToOne(optional = false)
    private Padecimintos nodepadecimiento;
    @JoinColumn(name = "idsecretaria", referencedColumnName = "idsec")
    @ManyToOne(optional = false)
    private Secretarias idsecretaria;

    public Historial() {
    }

    public Historial(Integer nopade) {
        this.nopade = nopade;
    }

    public Historial(Integer nopade, String fechaconsulta, BigDecimal costo, String fechaalta) {
        this.nopade = nopade;
        this.fechaconsulta = fechaconsulta;
        this.costo = costo;
        this.fechaalta = fechaalta;
    }

    public Integer getNopade() {
        return nopade;
    }

    public void setNopade(Integer nopade) {
        this.nopade = nopade;
    }

    public String getFechaconsulta() {
        return fechaconsulta;
    }

    public void setFechaconsulta(String fechaconsulta) {
        this.fechaconsulta = fechaconsulta;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getFechaalta() {
        return fechaalta;
    }

    public void setFechaalta(String fechaalta) {
        this.fechaalta = fechaalta;
    }

    public Doctores getCeduladoctor() {
        return ceduladoctor;
    }

    public void setCeduladoctor(Doctores ceduladoctor) {
        this.ceduladoctor = ceduladoctor;
    }

    public Pacientes getIdpaciente() {
        return idpaciente;
    }

    public void setIdpaciente(Pacientes idpaciente) {
        this.idpaciente = idpaciente;
    }

    public Padecimintos getNodepadecimiento() {
        return nodepadecimiento;
    }

    public void setNodepadecimiento(Padecimintos nodepadecimiento) {
        this.nodepadecimiento = nodepadecimiento;
    }

    public Secretarias getIdsecretaria() {
        return idsecretaria;
    }

    public void setIdsecretaria(Secretarias idsecretaria) {
        this.idsecretaria = idsecretaria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nopade != null ? nopade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historial)) {
            return false;
        }
        Historial other = (Historial) object;
        if ((this.nopade == null && other.nopade != null) || (this.nopade != null && !this.nopade.equals(other.nopade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controller.Historial[ nopade=" + nopade + " ]";
    }
    
}
