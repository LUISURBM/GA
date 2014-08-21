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
import siga.ga.controllers.exceptions.IllegalOrphanException;
import siga.ga.controllers.exceptions.NonexistentEntityException;
import siga.ga.controllers.exceptions.PreexistingEntityException;
import siga.ga.controllers.exceptions.RollbackFailureException;
import siga.ga.entytis.Clausula;
import siga.ga.entytis.Parentelaxpersona;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Otros
 */
public class ClausulaJpaController implements Serializable {

    public ClausulaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
   @PersistenceUnit(unitName="GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    public ClausulaJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(ClausulaJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clausula clausula) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (clausula.getParentelaxpersonaCollection() == null) {
            clausula.setParentelaxpersonaCollection(new ArrayList<Parentelaxpersona>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Parentelaxpersona> attachedParentelaxpersonaCollection = new ArrayList<Parentelaxpersona>();
            for (Parentelaxpersona parentelaxpersonaCollectionParentelaxpersonaToAttach : clausula.getParentelaxpersonaCollection()) {
                parentelaxpersonaCollectionParentelaxpersonaToAttach = em.getReference(parentelaxpersonaCollectionParentelaxpersonaToAttach.getClass(), parentelaxpersonaCollectionParentelaxpersonaToAttach.getId());
                attachedParentelaxpersonaCollection.add(parentelaxpersonaCollectionParentelaxpersonaToAttach);
            }
            clausula.setParentelaxpersonaCollection(attachedParentelaxpersonaCollection);
            em.persist(clausula);
            for (Parentelaxpersona parentelaxpersonaCollectionParentelaxpersona : clausula.getParentelaxpersonaCollection()) {
                Clausula oldIdpersonaOfParentelaxpersonaCollectionParentelaxpersona = parentelaxpersonaCollectionParentelaxpersona.getIdpersona();
                parentelaxpersonaCollectionParentelaxpersona.setIdpersona(clausula);
                parentelaxpersonaCollectionParentelaxpersona = em.merge(parentelaxpersonaCollectionParentelaxpersona);
                if (oldIdpersonaOfParentelaxpersonaCollectionParentelaxpersona != null) {
                    oldIdpersonaOfParentelaxpersonaCollectionParentelaxpersona.getParentelaxpersonaCollection().remove(parentelaxpersonaCollectionParentelaxpersona);
                    oldIdpersonaOfParentelaxpersonaCollectionParentelaxpersona = em.merge(oldIdpersonaOfParentelaxpersonaCollectionParentelaxpersona);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findClausula(clausula.getId()) != null) {
                throw new PreexistingEntityException("Clausula " + clausula + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clausula clausula) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clausula persistentClausula = em.find(Clausula.class, clausula.getId());
            Collection<Parentelaxpersona> parentelaxpersonaCollectionOld = persistentClausula.getParentelaxpersonaCollection();
            Collection<Parentelaxpersona> parentelaxpersonaCollectionNew = clausula.getParentelaxpersonaCollection();
            List<String> illegalOrphanMessages = null;
            for (Parentelaxpersona parentelaxpersonaCollectionOldParentelaxpersona : parentelaxpersonaCollectionOld) {
                if (!parentelaxpersonaCollectionNew.contains(parentelaxpersonaCollectionOldParentelaxpersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Parentelaxpersona " + parentelaxpersonaCollectionOldParentelaxpersona + " since its idpersona field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Parentelaxpersona> attachedParentelaxpersonaCollectionNew = new ArrayList<Parentelaxpersona>();
            for (Parentelaxpersona parentelaxpersonaCollectionNewParentelaxpersonaToAttach : parentelaxpersonaCollectionNew) {
                parentelaxpersonaCollectionNewParentelaxpersonaToAttach = em.getReference(parentelaxpersonaCollectionNewParentelaxpersonaToAttach.getClass(), parentelaxpersonaCollectionNewParentelaxpersonaToAttach.getId());
                attachedParentelaxpersonaCollectionNew.add(parentelaxpersonaCollectionNewParentelaxpersonaToAttach);
            }
            parentelaxpersonaCollectionNew = attachedParentelaxpersonaCollectionNew;
            clausula.setParentelaxpersonaCollection(parentelaxpersonaCollectionNew);
            clausula = em.merge(clausula);
            for (Parentelaxpersona parentelaxpersonaCollectionNewParentelaxpersona : parentelaxpersonaCollectionNew) {
                if (!parentelaxpersonaCollectionOld.contains(parentelaxpersonaCollectionNewParentelaxpersona)) {
                    Clausula oldIdpersonaOfParentelaxpersonaCollectionNewParentelaxpersona = parentelaxpersonaCollectionNewParentelaxpersona.getIdpersona();
                    parentelaxpersonaCollectionNewParentelaxpersona.setIdpersona(clausula);
                    parentelaxpersonaCollectionNewParentelaxpersona = em.merge(parentelaxpersonaCollectionNewParentelaxpersona);
                    if (oldIdpersonaOfParentelaxpersonaCollectionNewParentelaxpersona != null && !oldIdpersonaOfParentelaxpersonaCollectionNewParentelaxpersona.equals(clausula)) {
                        oldIdpersonaOfParentelaxpersonaCollectionNewParentelaxpersona.getParentelaxpersonaCollection().remove(parentelaxpersonaCollectionNewParentelaxpersona);
                        oldIdpersonaOfParentelaxpersonaCollectionNewParentelaxpersona = em.merge(oldIdpersonaOfParentelaxpersonaCollectionNewParentelaxpersona);
                    }
                }
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
                Long id = clausula.getId();
                if (findClausula(id) == null) {
                    throw new NonexistentEntityException("The clausula with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clausula clausula;
            try {
                clausula = em.getReference(Clausula.class, id);
                clausula.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clausula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Parentelaxpersona> parentelaxpersonaCollectionOrphanCheck = clausula.getParentelaxpersonaCollection();
            for (Parentelaxpersona parentelaxpersonaCollectionOrphanCheckParentelaxpersona : parentelaxpersonaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clausula (" + clausula + ") cannot be destroyed since the Parentelaxpersona " + parentelaxpersonaCollectionOrphanCheckParentelaxpersona + " in its parentelaxpersonaCollection field has a non-nullable idpersona field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clausula);
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

    public List<Clausula> findClausulaEntities() {
        return findClausulaEntities(true, -1, -1);
    }

    public List<Clausula> findClausulaEntities(int maxResults, int firstResult) {
        return findClausulaEntities(false, maxResults, firstResult);
    }

    private List<Clausula> findClausulaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clausula.class));
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

    public Clausula findClausula(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clausula.class, id);
        } finally {
            em.close();
        }
    }

    public int getClausulaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clausula> rt = cq.from(Clausula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
