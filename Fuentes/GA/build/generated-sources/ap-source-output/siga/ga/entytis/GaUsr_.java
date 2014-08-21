package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.GaTipoUsr;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(GaUsr.class)
public class GaUsr_ { 

    public static volatile SingularAttribute<GaUsr, String> usrNombre;
    public static volatile SingularAttribute<GaUsr, Long> usrId;
    public static volatile SingularAttribute<GaUsr, String> usrMail;
    public static volatile SingularAttribute<GaUsr, GaTipoUsr> usrTipoUsr;
    public static volatile SingularAttribute<GaUsr, String> usrContrasena;

}