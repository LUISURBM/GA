package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Acuerdo;
import siga.ga.entytis.Cuarto;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Servicio.class)
public class Servicio_ { 

    public static volatile SingularAttribute<Servicio, Long> id;
    public static volatile CollectionAttribute<Servicio, Cuarto> cuartoCollection;
    public static volatile CollectionAttribute<Servicio, Acuerdo> acuerdoCollection;
    public static volatile SingularAttribute<Servicio, String> descripcion;
    public static volatile SingularAttribute<Servicio, Long> nivel;

}