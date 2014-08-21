package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Contrato;
import siga.ga.entytis.Documentoxpersona;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Personaje.class)
public class Personaje_ { 

    public static volatile SingularAttribute<Personaje, Long> id;
    public static volatile SingularAttribute<Personaje, String> nombres;
    public static volatile SingularAttribute<Personaje, String> telefono;
    public static volatile CollectionAttribute<Personaje, Documentoxpersona> documentoxpersonaCollection;
    public static volatile SingularAttribute<Personaje, Long> rolid;
    public static volatile SingularAttribute<Personaje, String> fbid;
    public static volatile CollectionAttribute<Personaje, Contrato> contratoCollection;
    public static volatile SingularAttribute<Personaje, String> cedula;

}