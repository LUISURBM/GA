package siga.ga.entytis;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Archivo;
import siga.ga.entytis.Contrato;
import siga.ga.entytis.Documentoxpersona;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Documento.class)
public class Documento_ { 

    public static volatile SingularAttribute<Documento, Long> id;
    public static volatile SingularAttribute<Documento, String> nombre;
    public static volatile SingularAttribute<Documento, Archivo> idarchivo;
    public static volatile SingularAttribute<Documento, String> descripcion;
    public static volatile CollectionAttribute<Documento, Documentoxpersona> documentoxpersonaCollection;
    public static volatile CollectionAttribute<Documento, Contrato> contratoCollection;

}