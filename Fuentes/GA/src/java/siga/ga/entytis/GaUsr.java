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
@Table(name = "GA_USR", catalog = "", schema = "SYSTEM")
@NamedQueries({
    @NamedQuery(name = "GaUsr.findAll", query = "SELECT g FROM GaUsr g"),
    @NamedQuery(name = "GaUsr.findByUsrId", query = "SELECT g FROM GaUsr g WHERE g.usrId = :usrId"),
    @NamedQuery(name = "GaUsr.findByUsrContrasena", query = "SELECT g FROM GaUsr g WHERE g.usrContrasena = :usrContrasena"),
    @NamedQuery(name = "GaUsr.findByUsrNombre", query = "SELECT g FROM GaUsr g WHERE g.usrNombre = :usrNombre"),
    @NamedQuery(name = "GaUsr.findByUsrMail", query = "SELECT g FROM GaUsr g WHERE g.usrMail = :usrMail")})
public class GaUsr implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USR_ID")
    private Long usrId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "USR_CONTRASENA")
    private String usrContrasena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "USR_NOMBRE")
    private String usrNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "USR_MAIL")
    private String usrMail;
    @JoinColumn(name = "USR_TIPO_USR", referencedColumnName = "TIPO_USR_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GaTipoUsr usrTipoUsr;

    public GaUsr() {
    }

    public GaUsr(Long usrId) {
        this.usrId = usrId;
    }

    public GaUsr(Long usrId, String usrContrasena, String usrNombre, String usrMail) {
        this.usrId = usrId;
        this.usrContrasena = usrContrasena;
        this.usrNombre = usrNombre;
        this.usrMail = usrMail;
    }

    public Long getUsrId() {
        return usrId;
    }

    public void setUsrId(Long usrId) {
        this.usrId = usrId;
    }

    public String getUsrContrasena() {
        return usrContrasena;
    }

    public void setUsrContrasena(String usrContrasena) {
        this.usrContrasena = usrContrasena;
    }

    public String getUsrNombre() {
        return usrNombre;
    }

    public void setUsrNombre(String usrNombre) {
        this.usrNombre = usrNombre;
    }

    public String getUsrMail() {
        return usrMail;
    }

    public void setUsrMail(String usrMail) {
        this.usrMail = usrMail;
    }

    public GaTipoUsr getUsrTipoUsr() {
        return usrTipoUsr;
    }

    public void setUsrTipoUsr(GaTipoUsr usrTipoUsr) {
        this.usrTipoUsr = usrTipoUsr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usrId != null ? usrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GaUsr)) {
            return false;
        }
        GaUsr other = (GaUsr) object;
        if ((this.usrId == null && other.usrId != null) || (this.usrId != null && !this.usrId.equals(other.usrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        System.out.println(""+usrId+usrContrasena+usrNombre);
        return "GaUsr{" + "usrId=" + usrId + ", usrContrasena=" + usrContrasena + ", usrNombre=" + usrNombre + ", usrMail=" + usrMail + ", usrTipoUsr=" + usrTipoUsr.getTipoUsrTipo() + '}';
    }

    
    
}
