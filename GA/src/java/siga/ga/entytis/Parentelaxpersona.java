/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.entytis;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Otros
 */
@Entity
@Table(name = "PARENTELAXPERSONA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parentelaxpersona.findAll", query = "SELECT p FROM Parentelaxpersona p"),
    @NamedQuery(name = "Parentelaxpersona.findById", query = "SELECT p FROM Parentelaxpersona p WHERE p.id = :id"),
    @NamedQuery(name = "Parentelaxpersona.findByDescripcion", query = "SELECT p FROM Parentelaxpersona p WHERE p.descripcion = :descripcion")})
public class Parentelaxpersona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 4000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "IDPERSONA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Clausula idpersona;
    @JoinColumn(name = "IDPARENTELA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Acuerdo idparentela;

    public Parentelaxpersona() {
    }

    public Parentelaxpersona(Long id) {
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

    public Clausula getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(Clausula idpersona) {
        this.idpersona = idpersona;
    }

    public Acuerdo getIdparentela() {
        return idparentela;
    }

    public void setIdparentela(Acuerdo idparentela) {
        this.idparentela = idparentela;
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
        if (!(object instanceof Parentelaxpersona)) {
            return false;
        }
        Parentelaxpersona other = (Parentelaxpersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.Parentelaxpersona[ id=" + id + " ]";
    }
    
}
