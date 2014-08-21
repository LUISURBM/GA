/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import siga.ga.controllers.exceptions.NonexistentEntityException;
import siga.ga.controllers.exceptions.PreexistingEntityException;
import siga.ga.controllers.exceptions.RollbackFailureException;
import siga.ga.entytis.Clausula;
import siga.ga.entytis.Acuerdo;
import siga.ga.entytis.Parentelaxpersona;
 import javax.persistence.PersistenceUnit;
    import javax.annotation.Resource;
    import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
/**
 *
 * @author Otros
 */
public class ParentelaxpersonaJpaController implements Serializable {

    public ParentelaxpersonaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
   
     @PersistenceUnit(unitName="GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    public ParentelaxpersonaJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(ParentelaxpersonaJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Parentelaxpersona parentelaxpersona) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clausula idpersona = parentelaxpersona.getIdpersona();
            if (idpersona != null) {
                idpersona = em.getReference(idpersona.getClass(), idpersona.getId());
                parentelaxpersona.setIdpersona(idpersona);
            }
            Acuerdo idparentela = parentelaxpersona.getIdparentela();
            if (idparentela != null) {
                idparentela = em.getReference(idparentela.getClass(), idparentela.getId());
                parentelaxpersona.setIdparentela(idparentela);
            }
            em.persist(parentelaxpersona);
            if (idpersona != null) {
                idpersona.getParentelaxpersonaCollection().add(parentelaxpersona);
                idpersona = em.merge(idpersona);
            }
            if (idparentela != null) {
                idparentela.getParentelaxpersonaCollection().add(parentelaxpersona);
                idparentela = em.merge(idparentela);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findParentelaxpersona(parentelaxpersona.getId()) != null) {
                throw new PreexistingEntityException("Parentelaxpersona " + parentelaxpersona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Parentelaxpersona parentelaxpersona) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Parentelaxpersona persistentParentelaxpersona = em.find(Parentelaxpersona.class, parentelaxpersona.getId());
            Clausula idpersonaOld = persistentParentelaxpersona.getIdpersona();
            Clausula idpersonaNew = parentelaxpersona.getIdpersona();
            Acuerdo idparentelaOld = persistentParentelaxpersona.getIdparentela();
            Acuerdo idparentelaNew = parentelaxpersona.getIdparentela();
            if (idpersonaNew != null) {
                idpersonaNew = em.getReference(idpersonaNew.getClass(), idpersonaNew.getId());
                parentelaxpersona.setIdpersona(idpersonaNew);
            }
            if (idparentelaNew != null) {
                idparentelaNew = em.getReference(idparentelaNew.getClass(), idparentelaNew.getId());
                parentelaxpersona.setIdparentela(idparentelaNew);
            }
            parentelaxpersona = em.merge(parentelaxpersona);
            if (idpersonaOld != null && !idpersonaOld.equals(idpersonaNew)) {
                idpersonaOld.getParentelaxpersonaCollection().remove(parentelaxpersona);
                idpersonaOld = em.merge(idpersonaOld);
            }
            if (idpersonaNew != null && !idpersonaNew.equals(idpersonaOld)) {
                idpersonaNew.getParentelaxpersonaCollection().add(parentelaxpersona);
                idpersonaNew = em.merge(idpersonaNew);
            }
            if (idparentelaOld != null && !idparentelaOld.equals(idparentelaNew)) {
                idparentelaOld.getParentelaxpersonaCollection().remove(parentelaxpersona);
                idparentelaOld = em.merge(idparentelaOld);
            }
            if (idparentelaNew != null && !idparentelaNew.equals(idparentelaOld)) {
                idparentelaNew.getParentelaxpersonaCollection().add(parentelaxpersona);
                idparentelaNew = em.merge(idparentelaNew);
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
                Long id = parentelaxpersona.getId();
                if (findParentelaxpersona(id) == null) {
                    throw new NonexistentEntityException("The parentelaxpersona with id " + id + " no longer exists.");
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
            Parentelaxpersona parentelaxpersona;
            try {
                parentelaxpersona = em.getReference(Parentelaxpersona.class, id);
                parentelaxpersona.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parentelaxpersona with id " + id + " no longer exists.", enfe);
            }
            Clausula idpersona = parentelaxpersona.getIdpersona();
            if (idpersona != null) {
                idpersona.getParentelaxpersonaCollection().remove(parentelaxpersona);
                idpersona = em.merge(idpersona);
            }
            Acuerdo idparentela = parentelaxpersona.getIdparentela();
            if (idparentela != null) {
                idparentela.getParentelaxpersonaCollection().remove(parentelaxpersona);
                idparentela = em.merge(idparentela);
            }
            em.remove(parentelaxpersona);
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

    public List<Parentelaxpersona> findParentelaxpersonaEntities() {
        return findParentelaxpersonaEntities(true, -1, -1);
    }

    public List<Parentelaxpersona> findParentelaxpersonaEntities(int maxResults, int firstResult) {
        return findParentelaxpersonaEntities(false, maxResults, firstResult);
    }

    private List<Parentelaxpersona> findParentelaxpersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parentelaxpersona.class));
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

    public Parentelaxpersona findParentelaxpersona(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parentelaxpersona.class, id);
        } finally {
            em.close();
        }
    }

    public int getParentelaxpersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Parentelaxpersona> rt = cq.from(Parentelaxpersona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
