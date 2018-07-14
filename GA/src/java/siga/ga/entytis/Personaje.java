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
@Table(name = "PERSONAJE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personaje.findAll", query = "SELECT p FROM Personaje p"),
    @NamedQuery(name = "Personaje.findById", query = "SELECT p FROM Personaje p WHERE p.id = :id"),
    @NamedQuery(name = "Personaje.findByNombres", query = "SELECT p FROM Personaje p WHERE p.nombres = :nombres"),
    @NamedQuery(name = "Personaje.findByCedula", query = "SELECT p FROM Personaje p WHERE p.cedula = :cedula"),
    @NamedQuery(name = "Personaje.findByRolid", query = "SELECT p FROM Personaje p WHERE p.rolid = :rolid"),
    @NamedQuery(name = "Personaje.findByTelefono", query = "SELECT p FROM Personaje p WHERE p.telefono = :telefono"),
    @NamedQuery(name = "Personaje.findByFbid", query = "SELECT p FROM Personaje p WHERE p.fbid = :fbid")})
public class Personaje implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idpersona")
    private Collection<Documentoxpersona> documentoxpersonaCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "NOMBRES")
    private String nombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "CEDULA")
    private String cedula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLID")
    private long rolid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "TELEFONO")
    private String telefono;
    @Size(max = 4000)
    @Column(name = "FBID")
    private String fbid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idarrendador")
    private Collection<Contrato> contratoCollection;
    


    public Personaje() {
            this.id = null;
        this.nombres = "";
        this.cedula = "";
        this.rolid = 0L;
        this.fbid = "";
        this.telefono = "";
    }

    public Personaje(Long id) {
        this.id = id;
    }

    public Personaje(Long id,String fbid, String nombres, String cedula, long rolid, String telefono) {
        this.id = id;
        this.fbid =fbid;
        this.nombres = nombres;
        this.cedula = cedula;
        this.rolid = rolid;
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public long getRolid() {
        return rolid;
    }

    public void setRolid(long rolid) {
        this.rolid = rolid;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    @XmlTransient
    public Collection<Contrato> getContratoCollection() {
        return contratoCollection;
    }

    public void setContratoCollection(Collection<Contrato> contratoCollection) {
        this.contratoCollection = contratoCollection;
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
        if (!(object instanceof Personaje)) {
            return false;
        }
        Personaje other = (Personaje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Personaje [ "+this.id+", "+this.fbid+", "+this.nombres+", "+this.cedula+", "+this.telefono+", "+this.rolid+"]";
    }

    @XmlTransient
    public Collection<Documentoxpersona> getDocumentoxpersonaCollection() {
        return documentoxpersonaCollection;
    }

    public void setDocumentoxpersonaCollection(Collection<Documentoxpersona> documentoxpersonaCollection) {
        this.documentoxpersonaCollection = documentoxpersonaCollection;
    }




    
}
