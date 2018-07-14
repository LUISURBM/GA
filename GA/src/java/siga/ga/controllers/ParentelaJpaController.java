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
import siga.ga.entytis.Contrato;
import java.util.ArrayList;
import java.util.Collection;
import siga.ga.entytis.Parentela;
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
public class ParentelaJpaController implements Serializable {

    public ParentelaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

     @PersistenceUnit(unitName="GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    public ParentelaJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(ParentelaJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Parentela parentela) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (parentela.getContratoCollection() == null) {
            parentela.setContratoCollection(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Contrato> attachedContratoCollection = new ArrayList<Contrato>();
            for (Contrato contratoCollectionContratoToAttach : parentela.getContratoCollection()) {
                contratoCollectionContratoToAttach = em.getReference(contratoCollectionContratoToAttach.getClass(), contratoCollectionContratoToAttach.getId());
                attachedContratoCollection.add(contratoCollectionContratoToAttach);
            }
            parentela.setContratoCollection(attachedContratoCollection);
            em.persist(parentela);
            for (Contrato contratoCollectionContrato : parentela.getContratoCollection()) {
                Parentela oldIdparentelaOfContratoCollectionContrato = contratoCollectionContrato.getIdparentela();
                contratoCollectionContrato.setIdparentela(parentela);
                contratoCollectionContrato = em.merge(contratoCollectionContrato);
                if (oldIdparentelaOfContratoCollectionContrato != null) {
                    oldIdparentelaOfContratoCollectionContrato.getContratoCollection().remove(contratoCollectionContrato);
                    oldIdparentelaOfContratoCollectionContrato = em.merge(oldIdparentelaOfContratoCollectionContrato);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findParentela(parentela.getId()) != null) {
                throw new PreexistingEntityException("Parentela " + parentela + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Parentela parentela) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Parentela persistentParentela = em.find(Parentela.class, parentela.getId());
            Collection<Contrato> contratoCollectionOld = persistentParentela.getContratoCollection();
            Collection<Contrato> contratoCollectionNew = parentela.getContratoCollection();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoCollectionOldContrato : contratoCollectionOld) {
                if (!contratoCollectionNew.contains(contratoCollectionOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contrato " + contratoCollectionOldContrato + " since its idparentela field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Contrato> attachedContratoCollectionNew = new ArrayList<Contrato>();
            for (Contrato contratoCollectionNewContratoToAttach : contratoCollectionNew) {
                contratoCollectionNewContratoToAttach = em.getReference(contratoCollectionNewContratoToAttach.getClass(), contratoCollectionNewContratoToAttach.getId());
                attachedContratoCollectionNew.add(contratoCollectionNewContratoToAttach);
            }
            contratoCollectionNew = attachedContratoCollectionNew;
            parentela.setContratoCollection(contratoCollectionNew);
            parentela = em.merge(parentela);
            for (Contrato contratoCollectionNewContrato : contratoCollectionNew) {
                if (!contratoCollectionOld.contains(contratoCollectionNewContrato)) {
                    Parentela oldIdparentelaOfContratoCollectionNewContrato = contratoCollectionNewContrato.getIdparentela();
                    contratoCollectionNewContrato.setIdparentela(parentela);
                    contratoCollectionNewContrato = em.merge(contratoCollectionNewContrato);
                    if (oldIdparentelaOfContratoCollectionNewContrato != null && !oldIdparentelaOfContratoCollectionNewContrato.equals(parentela)) {
                        oldIdparentelaOfContratoCollectionNewContrato.getContratoCollection().remove(contratoCollectionNewContrato);
                        oldIdparentelaOfContratoCollectionNewContrato = em.merge(oldIdparentelaOfContratoCollectionNewContrato);
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
                Long id = parentela.getId();
                if (findParentela(id) == null) {
                    throw new NonexistentEntityException("The parentela with id " + id + " no longer exists.");
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
            Parentela parentela;
            try {
                parentela = em.getReference(Parentela.class, id);
                parentela.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parentela with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contrato> contratoCollectionOrphanCheck = parentela.getContratoCollection();
            for (Contrato contratoCollectionOrphanCheckContrato : contratoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Parentela (" + parentela + ") cannot be destroyed since the Contrato " + contratoCollectionOrphanCheckContrato + " in its contratoCollection field has a non-nullable idparentela field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(parentela);
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

    public List<Parentela> findParentelaEntities() {
        return findParentelaEntities(true, -1, -1);
    }

    public List<Parentela> findParentelaEntities(int maxResults, int firstResult) {
        return findParentelaEntities(false, maxResults, firstResult);
    }

    private List<Parentela> findParentelaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parentela.class));
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

    public Parentela findParentela(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parentela.class, id);
        } finally {
            em.close();
        }
    }

    public int getParentelaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Parentela> rt = cq.from(Parentela.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
