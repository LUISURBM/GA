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
@Table(name = "GA_ARCHIVO", catalog = "", schema = "SYSTEM")
@NamedQueries({
    @NamedQuery(name = "GaArchivo.findAll", query = "SELECT g FROM GaArchivo g"),
    @NamedQuery(name = "GaArchivo.findByArchivoId", query = "SELECT g FROM GaArchivo g WHERE g.archivoId = :archivoId"),
    @NamedQuery(name = "GaArchivo.findByArchivoDireccion", query = "SELECT g FROM GaArchivo g WHERE g.archivoDireccion = :archivoDireccion"),
    @NamedQuery(name = "GaArchivo.findByArchivoNombre", query = "SELECT g FROM GaArchivo g WHERE g.archivoNombre = :archivoNombre")})
public class GaArchivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ARCHIVO_ID")
    private Long archivoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "ARCHIVO_DIRECCION")
    private String archivoDireccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "ARCHIVO_NOMBRE")
    private String archivoNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dcmArchivoIdArchivo", fetch = FetchType.LAZY)
    private List<GaDcmArchivo> gaDcmArchivoList;

    public GaArchivo() {
    }

    public GaArchivo(Long archivoId) {
        this.archivoId = archivoId;
    }

    public GaArchivo(Long archivoId, String archivoDireccion, String archivoNombre) {
        this.archivoId = archivoId;
        this.archivoDireccion = archivoDireccion;
        this.archivoNombre = archivoNombre;
    }

    public Long getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(Long archivoId) {
        this.archivoId = archivoId;
    }

    public String getArchivoDireccion() {
        return archivoDireccion;
    }

    public void setArchivoDireccion(String archivoDireccion) {
        this.archivoDireccion = archivoDireccion;
    }

    public String getArchivoNombre() {
        return archivoNombre;
    }

    public void setArchivoNombre(String archivoNombre) {
        this.archivoNombre = archivoNombre;
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
        hash += (archivoId != null ? archivoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GaArchivo)) {
            return false;
        }
        GaArchivo other = (GaArchivo) object;
        if ((this.archivoId == null && other.archivoId != null) || (this.archivoId != null && !this.archivoId.equals(other.archivoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siga.ga.entytis.GaArchivo[ archivoId=" + archivoId + " ]";
    }
    
}
