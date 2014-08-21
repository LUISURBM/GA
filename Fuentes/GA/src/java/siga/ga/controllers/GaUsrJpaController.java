/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
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
import siga.ga.entytis.GaTipoUsr;
import siga.ga.entytis.GaUsr;

/**
 *
 * @author Otros
 */
public class GaUsrJpaController implements Serializable {
    
    private static EntityManager getEmf() {
        return lookUpEmf().createEntityManager();
    }
    
    public GaUsrJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    
    public static GaUsr getUsuario() {
        
        EntityManager em = getEmf();
        GaUsr usr = null;
        try {
            String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
            if (user == null) {
                System.out.println("Usuario nulo :: cargar formulario login");
                FacesContext.getCurrentInstance().getExternalContext().redirect("/GA/login.html");
            }
            usr = (GaUsr) em.createNamedQuery("GaUsr.findByUsrNombre").setParameter("usrNombre", user).getSingleResult();
            System.out.println("GaUsrJpaController :: getUsuario :: " + usr.toString());
        } catch (IOException ex) {
            Logger.getLogger(GaUsrJpaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
        return usr;
    }
    @PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    
    public GaUsrJpaController() {
        
        this.utx = lookUpUtx();
        
        this.emf = lookUpEmf();
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public void create(GaUsr gaUsr) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GaTipoUsr usrTipoUsr = gaUsr.getUsrTipoUsr();
            if (usrTipoUsr != null) {
                usrTipoUsr = em.getReference(usrTipoUsr.getClass(), usrTipoUsr.getTipoUsrId());
                gaUsr.setUsrTipoUsr(usrTipoUsr);
            }
            em.persist(gaUsr);
            if (usrTipoUsr != null) {
                usrTipoUsr.getGaUsrList().add(gaUsr);
                usrTipoUsr = em.merge(usrTipoUsr);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGaUsr(gaUsr.getUsrId()) != null) {
                throw new PreexistingEntityException("GaUsr " + gaUsr + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void edit(GaUsr gaUsr) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GaUsr persistentGaUsr = em.find(GaUsr.class, gaUsr.getUsrId());
            GaTipoUsr usrTipoUsrOld = persistentGaUsr.getUsrTipoUsr();
            GaTipoUsr usrTipoUsrNew = gaUsr.getUsrTipoUsr();
            if (usrTipoUsrNew != null) {
                usrTipoUsrNew = em.getReference(usrTipoUsrNew.getClass(), usrTipoUsrNew.getTipoUsrId());
                gaUsr.setUsrTipoUsr(usrTipoUsrNew);
            }
            gaUsr = em.merge(gaUsr);
            if (usrTipoUsrOld != null && !usrTipoUsrOld.equals(usrTipoUsrNew)) {
                usrTipoUsrOld.getGaUsrList().remove(gaUsr);
                usrTipoUsrOld = em.merge(usrTipoUsrOld);
            }
            if (usrTipoUsrNew != null && !usrTipoUsrNew.equals(usrTipoUsrOld)) {
                usrTipoUsrNew.getGaUsrList().add(gaUsr);
                usrTipoUsrNew = em.merge(usrTipoUsrNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gaUsr.getUsrId();
                if (findGaUsr(id) == null) {
                    throw new NonexistentEntityException("The gaUsr with id " + id + " no longer exists.");
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
            GaUsr gaUsr;
            try {
                gaUsr = em.getReference(GaUsr.class, id);
                gaUsr.getUsrId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gaUsr with id " + id + " no longer exists.", enfe);
            }
            GaTipoUsr usrTipoUsr = gaUsr.getUsrTipoUsr();
            if (usrTipoUsr != null) {
                usrTipoUsr.getGaUsrList().remove(gaUsr);
                usrTipoUsr = em.merge(usrTipoUsr);
            }
            em.remove(gaUsr);
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
    
    public List<GaUsr> findGaUsrEntities() {
        return findGaUsrEntities(true, -1, -1);
    }
    
    public List<GaUsr> findGaUsrEntities(int maxResults, int firstResult) {
        return findGaUsrEntities(false, maxResults, firstResult);
    }
    
    private List<GaUsr> findGaUsrEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GaUsr.class));
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
    
    public GaUsr findGaUsr(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GaUsr.class, id);
        } finally {
            em.close();
        }
    }
    
    public int getGaUsrCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GaUsr> rt = cq.from(GaUsr.class);
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
