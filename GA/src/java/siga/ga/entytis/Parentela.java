/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.entytis;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Otros
 */
@Entity
@Table(name = "PARENTELA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parentela.findAll", query = "SELECT p FROM Parentela p"),
    @NamedQuery(name = "Parentela.findById", query = "SELECT p FROM Parentela p WHERE p.id = :id"),
    @NamedQuery(name = "Parentela.findByDescripcion", query = "SELECT p FROM Parentela p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Parentela.findByFecha", query = "SELECT p FROM Parentela p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Parentela.findByIdcodeudor", query = "SELECT p FROM Parentela p WHERE p.idcodeudor = :idcodeudor")})
public class Parentela implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "IDCODEUDOR")
    private Long idcodeudor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idparentela")
    private Collection<Contrato> contratoCollection;

    public Parentela() {
    }

    public Parentela(Long id) {
        this.id = id;
    }

    public Parentela(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getIdcodeudor() {
        return idcodeudor;
    }

    public void setIdcodeudor(Long idcodeudor) {
        this.idcodeudor = idcodeudor;
    }

    @XmlTransient
    public Collection<Contrato> getContratoCollection() {
        return contratoCollection;
    }

    public void setContratoCollection(Collection<Contrato> contratoCollection) {
        this.contratoCollection = contratoCollection;
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
        if (!(object instanceof Parentela)) {
            return false;
        }
        Parentela other = (Parentela) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.Parentela[ id=" + id + " ]";
    }
    
}
