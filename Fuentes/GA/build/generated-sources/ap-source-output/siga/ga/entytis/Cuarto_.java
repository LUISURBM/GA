package siga.ga.entytis;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Servicio;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Cuarto.class)
public class Cuarto_ { 

    public static volatile SingularAttribute<Cuarto, Long> id;
    public static volatile SingularAttribute<Cuarto, Date> fecha;
    public static volatile SingularAttribute<Cuarto, String> descripcion;
    public static volatile SingularAttribute<Cuarto, Servicio> idservicio;

}