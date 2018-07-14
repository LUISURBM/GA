/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import siga.ga.entytis.GaArchivo;

/**
 *
 * @author Otros
 */
@Stateless
public class GaArchivoFacade extends AbstractFacade<GaArchivo> {
    @PersistenceContext(unitName = "GAPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public GaArchivoFacade() {
        super(GaArchivo.class);
    }
    
}
