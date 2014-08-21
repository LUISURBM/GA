/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import siga.ga.entytis.Documento;

/**
 *
 * @author Otros
 */
@Stateless
public class DocumentoFacade extends AbstractFacade<Documento> {
    @PersistenceContext(unitName = "GAPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoFacade() {
        super(Documento.class);
    }
    
}
