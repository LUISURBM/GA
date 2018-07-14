/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import siga.ga.entytis.Clausula;

/**
 *
 * @author Otros
 */
@Stateless
public class ClausulaFacade extends AbstractFacade<Clausula> {
    @PersistenceContext(unitName = "GAPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ClausulaFacade() {
        super(Clausula.class);
    }
    
}
