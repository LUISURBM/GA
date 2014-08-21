package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.GaDcmArchivo;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:33")
@StaticMetamodel(GaArchivo.class)
public class GaArchivo_ { 

    public static volatile ListAttribute<GaArchivo, GaDcmArchivo> gaDcmArchivoList;
    public static volatile SingularAttribute<GaArchivo, Long> archivoId;
    public static volatile SingularAttribute<GaArchivo, String> archivoDireccion;
    public static volatile SingularAttribute<GaArchivo, String> archivoNombre;

}