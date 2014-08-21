/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.entytis;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "GA_DCM_ARCHIVO", catalog = "", schema = "SYSTEM")
@NamedQueries({
    @NamedQuery(name = "GaDcmArchivo.findAll", query = "SELECT g FROM GaDcmArchivo g"),
    @NamedQuery(name = "GaDcmArchivo.findByDcmArchivoId", query = "SELECT g FROM GaDcmArchivo g WHERE g.dcmArchivoId = :dcmArchivoId"),
    @NamedQuery(name = "GaDcmArchivo.findByDcmArchivoDescripcion", query = "SELECT g FROM GaDcmArchivo g WHERE g.dcmArchivoDescripcion = :dcmArchivoDescripcion")})
public class GaDcmArchivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DCM_ARCHIVO_ID")
    private Long dcmArchivoId;
    @Size(max = 4000)
    @Column(name = "DCM_ARCHIVO_DESCRIPCION")
    private String dcmArchivoDescripcion;
    @JoinColumn(name = "DCM_ARCHIVO_ID_DCM", referencedColumnName = "DCM_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GaDcm dcmArchivoIdDcm;
    @JoinColumn(name = "DCM_ARCHIVO_ID_ARCHIVO", referencedColumnName = "ARCHIVO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GaArchivo dcmArchivoIdArchivo;

    public GaDcmArchivo() {
    }

    public GaDcmArchivo(Long dcmArchivoId) {
        this.dcmArchivoId = dcmArchivoId;
    }

    public Long getDcmArchivoId() {
        return dcmArchivoId;
    }

    public void setDcmArchivoId(Long dcmArchivoId) {
        this.dcmArchivoId = dcmArchivoId;
    }

    public String getDcmArchivoDescripcion() {
        return dcmArchivoDescripcion;
    }

    public void setDcmArchivoDescripcion(String dcmArchivoDescripcion) {
        this.dcmArchivoDescripcion = dcmArchivoDescripcion;
    }

    public GaDcm getDcmArchivoIdDcm() {
        return dcmArchivoIdDcm;
    }

    public void setDcmArchivoIdDcm(GaDcm dcmArchivoIdDcm) {
        this.dcmArchivoIdDcm = dcmArchivoIdDcm;
    }

    public GaArchivo getDcmArchivoIdArchivo() {
        return dcmArchivoIdArchivo;
    }

    public void setDcmArchivoIdArchivo(GaArchivo dcmArchivoIdArchivo) {
        this.dcmArchivoIdArchivo = dcmArchivoIdArchivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dcmArchivoId != null ? dcmArchivoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GaDcmArchivo)) {
            return false;
        }
        GaDcmArchivo other = (GaDcmArchivo) object;
        if ((this.dcmArchivoId == null && other.dcmArchivoId != null) || (this.dcmArchivoId != null && !this.dcmArchivoId.equals(other.dcmArchivoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.GaDcmArchivo[ dcmArchivoId=" + dcmArchivoId + " ]";
    }
    
}
