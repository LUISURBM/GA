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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ACUERDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Acuerdo.findAll", query = "SELECT a FROM Acuerdo a"),
    @NamedQuery(name = "Acuerdo.findById", query = "SELECT a FROM Acuerdo a WHERE a.id = :id"),
    @NamedQuery(name = "Acuerdo.findByIdservicio", query = "SELECT a FROM Acuerdo a WHERE a.idservicio = :idservicio"),
    @NamedQuery(name = "Acuerdo.findByFecha", query = "SELECT a FROM Acuerdo a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "Acuerdo.findByDescipcion", query = "SELECT a FROM Acuerdo a WHERE a.descipcion = :descipcion")})
public class Acuerdo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDSERVICIO")
    private long idservicio;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 4000)
    @Column(name = "DESCIPCION")
    private String descipcion;
    @JoinColumn(name = "IDCLAUSULA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Servicio idclausula;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idacuerdo")
    private Collection<Contrato> contratoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idparentela")
    private Collection<Parentelaxpersona> parentelaxpersonaCollection;

    public Acuerdo() {
    }

    public Acuerdo(Long id) {
        this.id = id;
    }

    public Acuerdo(Long id, long idservicio) {
        this.id = id;
        this.idservicio = idservicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdservicio() {
        return idservicio;
    }

    public void setIdservicio(long idservicio) {
        this.idservicio = idservicio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public Servicio getIdclausula() {
        return idclausula;
    }

    public void setIdclausula(Servicio idclausula) {
        this.idclausula = idclausula;
    }

    @XmlTransient
    public Collection<Contrato> getContratoCollection() {
        return contratoCollection;
    }

    public void setContratoCollection(Collection<Contrato> contratoCollection) {
        this.contratoCollection = contratoCollection;
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
        if (!(object instanceof Acuerdo)) {
            return false;
        }
        Acuerdo other = (Acuerdo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.Acuerdo[ id=" + id + " ]";
    }
    
}
