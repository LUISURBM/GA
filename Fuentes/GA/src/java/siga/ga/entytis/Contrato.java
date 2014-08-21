/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.entytis;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Otros
 */
@Entity
@Table(name = "CONTRATO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contrato.findAll", query = "SELECT c FROM Contrato c"),
    @NamedQuery(name = "Contrato.findById", query = "SELECT c FROM Contrato c WHERE c.id = :id"),
    @NamedQuery(name = "Contrato.findByFechainicio", query = "SELECT c FROM Contrato c WHERE c.fechainicio = :fechainicio"),
    @NamedQuery(name = "Contrato.findByFechacreacion", query = "SELECT c FROM Contrato c WHERE c.fechacreacion = :fechacreacion"),
    @NamedQuery(name = "Contrato.findByDescripcion", query = "SELECT c FROM Contrato c WHERE c.descripcion = :descripcion")})
public class Contrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicio;
    @Column(name = "FECHACREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacreacion;
    @Size(max = 4000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "IDARRENDADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Personaje idarrendador;
    @JoinColumn(name = "IDPARENTELA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Parentela idparentela;
    @JoinColumn(name = "IDDOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Documento iddocumento;
    @JoinColumn(name = "IDACUERDO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Acuerdo idacuerdo;

    public Contrato() {
    }

    public Contrato(Long id) {
        this.id = id;
    }

    public Contrato(Long id, Date fechainicio) {
        this.id = id;
        this.fechainicio = fechainicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Personaje getIdarrendador() {
        return idarrendador;
    }

    public void setIdarrendador(Personaje idarrendador) {
        this.idarrendador = idarrendador;
    }

    public Parentela getIdparentela() {
        return idparentela;
    }

    public void setIdparentela(Parentela idparentela) {
        this.idparentela = idparentela;
    }

    public Documento getIddocumento() {
        return iddocumento;
    }

    public void setIddocumento(Documento iddocumento) {
        this.iddocumento = iddocumento;
    }

    public Acuerdo getIdacuerdo() {
        return idacuerdo;
    }

    public void setIdacuerdo(Acuerdo idacuerdo) {
        this.idacuerdo = idacuerdo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contrato)) {
            return false;
        }
        Contrato other = (Contrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.Contrato[ id=" + id + " ]";
    }
    
}
