package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Acuerdo;
import siga.ga.entytis.Clausula;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:33")
@StaticMetamodel(Parentelaxpersona.class)
public class Parentelaxpersona_ { 

    public static volatile SingularAttribute<Parentelaxpersona, Long> id;
    public static volatile SingularAttribute<Parentelaxpersona, Clausula> idpersona;
    public static volatile SingularAttribute<Parentelaxpersona, String> descripcion;
    public static volatile SingularAttribute<Parentelaxpersona, Acuerdo> idparentela;

}