package siga.ga.entytis;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Parentelaxpersona;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Clausula.class)
public class Clausula_ { 

    public static volatile SingularAttribute<Clausula, Long> id;
    public static volatile SingularAttribute<Clausula, Date> fecha;
    public static volatile SingularAttribute<Clausula, String> descripcion;
    public static volatile CollectionAttribute<Clausula, Parentelaxpersona> parentelaxpersonaCollection;

}