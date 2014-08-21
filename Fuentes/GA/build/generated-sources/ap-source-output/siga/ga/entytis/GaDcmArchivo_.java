package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.GaArchivo;
import siga.ga.entytis.GaDcm;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(GaDcmArchivo.class)
public class GaDcmArchivo_ { 

    public static volatile SingularAttribute<GaDcmArchivo, String> dcmArchivoDescripcion;
    public static volatile SingularAttribute<GaDcmArchivo, GaArchivo> dcmArchivoIdArchivo;
    public static volatile SingularAttribute<GaDcmArchivo, Long> dcmArchivoId;
    public static volatile SingularAttribute<GaDcmArchivo, GaDcm> dcmArchivoIdDcm;

}