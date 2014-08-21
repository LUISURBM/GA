/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.entytis;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Otros
 */
@Entity
@Table(name = "GA_DCM", catalog = "", schema = "SYSTEM")
@NamedQueries({
    @NamedQuery(name = "GaDcm.findAll", query = "SELECT g FROM GaDcm g"),
    @NamedQuery(name = "GaDcm.findByDcmId", query = "SELECT g FROM GaDcm g WHERE g.dcmId = :dcmId"),
    @NamedQuery(name = "GaDcm.findByDcmPublicacion", query = "SELECT g FROM GaDcm g WHERE g.dcmPublicacion = :dcmPublicacion"),
    @NamedQuery(name = "GaDcm.findByDcmTitulo", query = "SELECT g FROM GaDcm g WHERE g.dcmTitulo = :dcmTitulo"),
    @NamedQuery(name = "GaDcm.findByDcmUrl", query = "SELECT g FROM GaDcm g WHERE g.dcmUrl = :dcmUrl")})
public class GaDcm implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DCM_ID")
    private Long dcmId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DCM_PUBLICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dcmPublicacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "DCM_TITULO")
    private String dcmTitulo;
    @Size(max = 4000)
    @Column(name = "DCM_URL")
    private String dcmUrl;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dcmArchivoIdDcm", fetch = FetchType.LAZY)
    private List<GaDcmArchivo> gaDcmArchivoList;

    public GaDcm() {
    }

    public GaDcm(Long dcmId) {
        this.dcmId = dcmId;
    }

    public GaDcm(Long dcmId, Date dcmPublicacion, String dcmTitulo) {
        this.dcmId = dcmId;
        this.dcmPublicacion = dcmPublicacion;
        this.dcmTitulo = dcmTitulo;
    }
    
    public GaDcm(String dcmTitulo) {
        this.dcmId = 0l;
        this.dcmPublicacion = new Date();
        this.dcmTitulo = dcmTitulo;
    }

    public Long getDcmId() {
        return dcmId;
    }

    public void setDcmId(Long dcmId) {
        this.dcmId = dcmId;
    }

    public Date getDcmPublicacion() {
        return dcmPublicacion;
    }

    public void setDcmPublicacion(Date dcmPublicacion) {
        this.dcmPublicacion = dcmPublicacion;
    }

    public String getDcmTitulo() {
        return dcmTitulo;
    }

    public void setDcmTitulo(String dcmTitulo) {
        this.dcmTitulo = dcmTitulo;
    }

    public String getDcmUrl() {
        return dcmUrl;
    }

    public void setDcmUrl(String dcmUrl) {
        this.dcmUrl = dcmUrl;
    }

    public List<GaDcmArchivo> getGaDcmArchivoList() {
        return gaDcmArchivoList;
    }

    public void setGaDcmArchivoList(List<GaDcmArchivo> gaDcmArchivoList) {
        this.gaDcmArchivoList = gaDcmArchivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dcmId != null ? dcmId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GaDcm)) {
            return false;
        }
        GaDcm other = (GaDcm) object;
        if ((this.dcmId == null && other.dcmId != null) || (this.dcmId != null && !this.dcmId.equals(other.dcmId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.GaDcm[ dcmId=" + dcmId + " ]";
    }
    
}
