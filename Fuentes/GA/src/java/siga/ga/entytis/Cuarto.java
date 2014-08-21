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
@Table(name = "CUARTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuarto.findAll", query = "SELECT c FROM Cuarto c"),
    @NamedQuery(name = "Cuarto.findById", query = "SELECT c FROM Cuarto c WHERE c.id = :id"),
    @NamedQuery(name = "Cuarto.findByDescripcion", query = "SELECT c FROM Cuarto c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Cuarto.findByFecha", query = "SELECT c FROM Cuarto c WHERE c.fecha = :fecha")})
public class Cuarto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 4000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "IDSERVICIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Servicio idservicio;

    public Cuarto() {
    }

    public Cuarto(Long id) {
        this.id = id;
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

    public Servicio getIdservicio() {
        return idservicio;
    }

    public void setIdservicio(Servicio idservicio) {
        this.idservicio = idservicio;
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
        if (!(object instanceof Cuarto)) {
            return false;
        }
        Cuarto other = (Cuarto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.Cuarto[ id=" + id + " ]";
    }
    
}
