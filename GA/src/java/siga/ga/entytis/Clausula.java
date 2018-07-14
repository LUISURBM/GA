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
@Table(name = "CLAUSULA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clausula.findAll", query = "SELECT c FROM Clausula c"),
    @NamedQuery(name = "Clausula.findById", query = "SELECT c FROM Clausula c WHERE c.id = :id"),
    @NamedQuery(name = "Clausula.findByDescripcion", query = "SELECT c FROM Clausula c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Clausula.findByFecha", query = "SELECT c FROM Clausula c WHERE c.fecha = :fecha")})
public class Clausula implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idpersona")
    private Collection<Parentelaxpersona> parentelaxpersonaCollection;

    public Clausula() {
    }

    public Clausula(Long id) {
        this.id = id;
    }

    public Clausula(Long id, String descripcion) {
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

    @XmlTransient
    public Collection<Parentelaxpersona> getParentelaxpersonaCollection() {
        return parentelaxpersonaCollection;
    }

    public void setParentelaxpersonaCollection(Collection<Parentelaxpersona> parentelaxpersonaCollection) {
        this.parentelaxpersonaCollection = parentelaxpersonaCollection;
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
        if (!(object instanceof Clausula)) {
            return false;
        }
        Clausula other = (Clausula) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.Clausula[ id=" + id + " ]";
    }
    
}
