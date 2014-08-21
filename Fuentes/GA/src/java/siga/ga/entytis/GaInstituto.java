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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Otros
 */
@Entity
@Table(name = "GA_INSTITUTO", catalog = "", schema = "SYSTEM")
@NamedQueries({
    @NamedQuery(name = "GaInstituto.findAll", query = "SELECT g FROM GaInstituto g"),
    @NamedQuery(name = "GaInstituto.findByInstitutoId", query = "SELECT g FROM GaInstituto g WHERE g.institutoId = :institutoId"),
    @NamedQuery(name = "GaInstituto.findByInstitutoDescripcion", query = "SELECT g FROM GaInstituto g WHERE g.institutoDescripcion = :institutoDescripcion"),
    @NamedQuery(name = "GaInstituto.findByInstitutoDireccion", query = "SELECT g FROM GaInstituto g WHERE g.institutoDireccion = :institutoDireccion"),
    @NamedQuery(name = "GaInstituto.findByInstitutoNombre", query = "SELECT g FROM GaInstituto g WHERE g.institutoNombre = :institutoNombre")})
public class GaInstituto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSTITUTO_ID")
    private Long institutoId;
    @Size(max = 4000)
    @Column(name = "INSTITUTO_DESCRIPCION")
    private String institutoDescripcion;
    @Size(max = 4000)
    @Column(name = "INSTITUTO_DIRECCION")
    private String institutoDireccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "INSTITUTO_NOMBRE")
    private String institutoNombre;

    public GaInstituto() {
    }

    public GaInstituto(Long institutoId) {
        this.institutoId = institutoId;
    }

    public GaInstituto(Long institutoId, String institutoNombre) {
        this.institutoId = institutoId;
        this.institutoNombre = institutoNombre;
    }

    public Long getInstitutoId() {
        return institutoId;
    }

    public void setInstitutoId(Long institutoId) {
        this.institutoId = institutoId;
    }

    public String getInstitutoDescripcion() {
        return institutoDescripcion;
    }

    public void setInstitutoDescripcion(String institutoDescripcion) {
        this.institutoDescripcion = institutoDescripcion;
    }

    public String getInstitutoDireccion() {
        return institutoDireccion;
    }

    public void setInstitutoDireccion(String institutoDireccion) {
        this.institutoDireccion = institutoDireccion;
    }

    public String getInstitutoNombre() {
        return institutoNombre;
    }

    public void setInstitutoNombre(String institutoNombre) {
        this.institutoNombre = institutoNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (institutoId != null ? institutoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GaInstituto)) {
            return false;
        }
        GaInstituto other = (GaInstituto) object;
        if ((this.institutoId == null && other.institutoId != null) || (this.institutoId != null && !this.institutoId.equals(other.institutoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.GaInstituto[ institutoId=" + institutoId + " ]";
    }
    
}
