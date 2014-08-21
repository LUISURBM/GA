package siga.ga.entytis;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import siga.ga.entytis.Acuerdo;
import siga.ga.entytis.Documento;
import siga.ga.entytis.Parentela;
import siga.ga.entytis.Personaje;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2014-03-02T21:34:34")
@StaticMetamodel(Contrato.class)
public class Contrato_ { 

    public static volatile SingularAttribute<Contrato, Long> id;
    public static volatile SingularAttribute<Contrato, Date> fechacreacion;
    public static volatile SingularAttribute<Contrato, Acuerdo> idacuerdo;
    public static volatile SingularAttribute<Contrato, String> descripcion;
    public static volatile SingularAttribute<Contrato, Documento> iddocumento;
    public static volatile SingularAttribute<Contrato, Personaje> idarrendador;
    public static volatile SingularAttribute<Contrato, Date> fechainicio;
    public static volatile SingularAttribute<Contrato, Parentela> idparentela;

}