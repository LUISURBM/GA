package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Documento;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Archivo.class)
public class Archivo_ { 

    public static volatile SingularAttribute<Archivo, Long> id;
    public static volatile SingularAttribute<Archivo, String> nombre;
    public static volatile SingularAttribute<Archivo, String> direccion;
    public static volatile CollectionAttribute<Archivo, Documento> documentoCollection;
    public static volatile SingularAttribute<Archivo, String> descripcion;
    public static volatile SingularAttribute<Archivo, String> ubicacion;

}