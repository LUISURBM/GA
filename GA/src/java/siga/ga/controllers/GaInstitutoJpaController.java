/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.controllers;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import siga.ga.controllers.exceptions.NonexistentEntityException;
import siga.ga.controllers.exceptions.PreexistingEntityException;
import siga.ga.controllers.exceptions.RollbackFailureException;
import siga.ga.entytis.GaInstituto;

/**
 *
 * @author Otros
 */
public class GaInstitutoJpaController implements Serializable {

    public GaInstitutoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
@PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;

    public GaInstitutoJpaController() {

            this.utx = lookUpUtx();

            this.emf = lookUpEmf();
    }
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GaInstituto gaInstituto) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(gaInstituto);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGaInstituto(gaInstituto.getInstitutoId()) != null) {
                throw new PreexistingEntityException("GaInstituto " + gaInstituto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GaInstituto gaInstituto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            gaInstituto = em.merge(gaInstituto);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gaInstituto.getInstitutoId();
                if (findGaInstituto(id) == null) {
                    throw new NonexistentEntityException("The gaInstituto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GaInstituto gaInstituto;
            try {
                gaInstituto = em.getReference(GaInstituto.class, id);
                gaInstituto.getInstitutoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gaInstituto with id " + id + " no longer exists.", enfe);
            }
            em.remove(gaInstituto);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GaInstituto> findGaInstitutoEntities() {
        return findGaInstitutoEntities(true, -1, -1);
    }

    public List<GaInstituto> findGaInstitutoEntities(int maxResults, int firstResult) {
        return findGaInstitutoEntities(false, maxResults, firstResult);
    }

    private List<GaInstituto> findGaInstitutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GaInstituto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public GaInstituto findGaInstituto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GaInstituto.class, id);
        } finally {
            em.close();
        }
    }

    public int getGaInstitutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GaInstituto> rt = cq.from(GaInstituto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
private UserTransaction lookUpUtx() {
        try {
            return (UserTransaction) getContext().lookup("java:comp/UserTransaction");
        } catch (NamingException ex) {
            Logger.getLogger(GaUsrJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

   private static InitialContext getContext() {
        try {
            return new InitialContext();
        } catch (NamingException ex) {
            Logger.getLogger(GaUsrJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static EntityManagerFactory lookUpEmf() {
        return javax.persistence.Persistence.createEntityManagerFactory("GAPU");
    }    
}
