package siga.ga.entytis;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.GaDcmArchivo;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:33")
@StaticMetamodel(GaDcm.class)
public class GaDcm_ { 

    public static volatile SingularAttribute<GaDcm, Date> dcmPublicacion;
    public static volatile ListAttribute<GaDcm, GaDcmArchivo> gaDcmArchivoList;
    public static volatile SingularAttribute<GaDcm, String> dcmUrl;
    public static volatile SingularAttribute<GaDcm, Long> dcmId;
    public static volatile SingularAttribute<GaDcm, String> dcmTitulo;

}