/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.entytis;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Otros
 */
@Entity
@Table(name = "GA_TIPO_USR", catalog = "", schema = "SYSTEM")
@NamedQueries({
    @NamedQuery(name = "GaTipoUsr.findAll", query = "SELECT g FROM GaTipoUsr g"),
    @NamedQuery(name = "GaTipoUsr.findByTipoUsrId", query = "SELECT g FROM GaTipoUsr g WHERE g.tipoUsrId = :tipoUsrId"),
    @NamedQuery(name = "GaTipoUsr.findByTipoUsrTipo", query = "SELECT g FROM GaTipoUsr g WHERE g.tipoUsrTipo = :tipoUsrTipo"),
    @NamedQuery(name = "GaTipoUsr.findByTipoUsrDescripcion", query = "SELECT g FROM GaTipoUsr g WHERE g.tipoUsrDescripcion = :tipoUsrDescripcion")})
public class GaTipoUsr implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIPO_USR_ID")
    private Long tipoUsrId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "TIPO_USR_TIPO")
    private String tipoUsrTipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "TIPO_USR_DESCRIPCION")
    private String tipoUsrDescripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usrTipoUsr", fetch = FetchType.LAZY)
    private List<GaUsr> gaUsrList;

    public GaTipoUsr() {
    }

    public GaTipoUsr(Long tipoUsrId) {
        this.tipoUsrId = tipoUsrId;
    }

    public GaTipoUsr(Long tipoUsrId, String tipoUsrTipo, String tipoUsrDescripcion) {
        this.tipoUsrId = tipoUsrId;
        this.tipoUsrTipo = tipoUsrTipo;
        this.tipoUsrDescripcion = tipoUsrDescripcion;
    }

    public Long getTipoUsrId() {
        return tipoUsrId;
    }

    public void setTipoUsrId(Long tipoUsrId) {
        this.tipoUsrId = tipoUsrId;
    }

    public String getTipoUsrTipo() {
        return tipoUsrTipo;
    }

    public void setTipoUsrTipo(String tipoUsrTipo) {
        this.tipoUsrTipo = tipoUsrTipo;
    }

    public String getTipoUsrDescripcion() {
        return tipoUsrDescripcion;
    }

    public void setTipoUsrDescripcion(String tipoUsrDescripcion) {
        this.tipoUsrDescripcion = tipoUsrDescripcion;
    }

    public List<GaUsr> getGaUsrList() {
        return gaUsrList;
    }

    public void setGaUsrList(List<GaUsr> gaUsrList) {
        this.gaUsrList = gaUsrList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoUsrId != null ? tipoUsrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GaTipoUsr)) {
            return false;
        }
        GaTipoUsr other = (GaTipoUsr) object;
        if ((this.tipoUsrId == null && other.tipoUsrId != null) || (this.tipoUsrId != null && !this.tipoUsrId.equals(other.tipoUsrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.GaTipoUsr[ tipoUsrId=" + tipoUsrId + " ]";
    }
    
}
