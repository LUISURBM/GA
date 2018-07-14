/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import siga.ga.entytis.GaDcm;

/**
 *
 * @author Otros
 */
@Stateless
public class GaDcmFacade extends AbstractFacade<GaDcm> {
    @PersistenceContext(unitName = "GAPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public GaDcmFacade() {
        super(GaDcm.class);
    }
    
}
