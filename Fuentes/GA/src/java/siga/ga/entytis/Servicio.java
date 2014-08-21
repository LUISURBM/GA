/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.entytis;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Otros
 */
@Entity
@Table(name = "SERVICIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicio.findAll", query = "SELECT s FROM Servicio s"),
    @NamedQuery(name = "Servicio.findById", query = "SELECT s FROM Servicio s WHERE s.id = :id"),
    @NamedQuery(name = "Servicio.findByDescripcion", query = "SELECT s FROM Servicio s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "Servicio.findByNivel", query = "SELECT s FROM Servicio s WHERE s.nivel = :nivel")})
public class Servicio implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "NIVEL")
    private long nivel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idclausula")
    private Collection<Acuerdo> acuerdoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idservicio")
    private Collection<Cuarto> cuartoCollection;

    public Servicio() {
    }

    public Servicio(Long id) {
        this.id = id;
    }

    public Servicio(Long id, String descripcion, long nivel) {
        this.id = id;
        this.descripcion = descripcion;
        this.nivel = nivel;
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

    public long getNivel() {
        return nivel;
    }

    public void setNivel(long nivel) {
        this.nivel = nivel;
    }

    @XmlTransient
    public Collection<Acuerdo> getAcuerdoCollection() {
        return acuerdoCollection;
    }

    public void setAcuerdoCollection(Collection<Acuerdo> acuerdoCollection) {
        this.acuerdoCollection = acuerdoCollection;
    }

    @XmlTransient
    public Collection<Cuarto> getCuartoCollection() {
        return cuartoCollection;
    }

    public void setCuartoCollection(Collection<Cuarto> cuartoCollection) {
        this.cuartoCollection = cuartoCollection;
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
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.Servicio[ id=" + id + " ]";
    }
    
}
