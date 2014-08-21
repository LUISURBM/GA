package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.GaUsr;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(GaTipoUsr.class)
public class GaTipoUsr_ { 

    public static volatile SingularAttribute<GaTipoUsr, String> tipoUsrDescripcion;
    public static volatile SingularAttribute<GaTipoUsr, Long> tipoUsrId;
    public static volatile ListAttribute<GaTipoUsr, GaUsr> gaUsrList;
    public static volatile SingularAttribute<GaTipoUsr, String> tipoUsrTipo;

}