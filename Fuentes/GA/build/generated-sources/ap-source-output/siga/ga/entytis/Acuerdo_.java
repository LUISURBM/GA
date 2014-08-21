package siga.ga.entytis;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Contrato;
import siga.ga.entytis.Parentelaxpersona;
import siga.ga.entytis.Servicio;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Acuerdo.class)
public class Acuerdo_ { 

    public static volatile SingularAttribute<Acuerdo, Long> id;
    public static volatile SingularAttribute<Acuerdo, Servicio> idclausula;
    public static volatile SingularAttribute<Acuerdo, String> descipcion;
    public static volatile SingularAttribute<Acuerdo, Date> fecha;
    public static volatile CollectionAttribute<Acuerdo, Parentelaxpersona> parentelaxpersonaCollection;
    public static volatile SingularAttribute<Acuerdo, Long> idservicio;
    public static volatile CollectionAttribute<Acuerdo, Contrato> contratoCollection;

}