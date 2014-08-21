package siga.ga.entytis;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Contrato;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Parentela.class)
public class Parentela_ { 

    public static volatile SingularAttribute<Parentela, Long> id;
    public static volatile SingularAttribute<Parentela, Date> fecha;
    public static volatile SingularAttribute<Parentela, String> descripcion;
    public static volatile CollectionAttribute<Parentela, Contrato> contratoCollection;
    public static volatile SingularAttribute<Parentela, Long> idcodeudor;

}