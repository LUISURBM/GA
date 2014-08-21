package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Documento;
import siga.ga.entytis.Personaje;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Documentoxpersona.class)
public class Documentoxpersona_ { 

    public static volatile SingularAttribute<Documentoxpersona, Long> id;
    public static volatile SingularAttribute<Documentoxpersona, Personaje> idpersona;
    public static volatile SingularAttribute<Documentoxpersona, Boolean> selected;
    public static volatile SingularAttribute<Documentoxpersona, String> descripcion;
    public static volatile SingularAttribute<Documentoxpersona, Documento> iddocumento;

}