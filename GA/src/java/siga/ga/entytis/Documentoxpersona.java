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
@Table(name = "DOCUMENTOXPERSONA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documentoxpersona.findAll", query = "SELECT d FROM Documentoxpersona d"),
    @NamedQuery(name = "Documentoxpersona.findById", query = "SELECT d FROM Documentoxpersona d WHERE d.id = :id"),
    @NamedQuery(name = "Documentoxpersona.findByDescripcion", query = "SELECT d FROM Documentoxpersona d WHERE d.descripcion = :descripcion")})
public class Documentoxpersona implements Serializable {
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
    private Personaje idpersona;
    @JoinColumn(name = "IDDOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Documento iddocumento;
    
    private boolean selected;

    public Documentoxpersona() {
    }

    public Documentoxpersona(Long id) {
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

    public Personaje getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(Personaje idpersona) {
        this.idpersona = idpersona;
    }

    public Documento getIddocumento() {
        return iddocumento;
    }

    public void setIddocumento(Documento iddocumento) {
        this.iddocumento = iddocumento;
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
        if (!(object instanceof Documentoxpersona)) {
            return false;
        }
        Documentoxpersona other = (Documentoxpersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.Documentoxpersona[ id=" + id + " ]";
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    
}
