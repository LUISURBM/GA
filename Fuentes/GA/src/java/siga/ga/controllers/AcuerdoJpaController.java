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
import siga.ga.entytis.Acuerdo;
import siga.ga.entytis.Servicio;
import siga.ga.entytis.Contrato;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceUnit;
import siga.ga.entytis.Parentelaxpersona;

/**
 *
 * @author Otros
 */
public class AcuerdoJpaController implements Serializable {

    public AcuerdoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    @PersistenceUnit(unitName="GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    public AcuerdoJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(AcuerdoJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Acuerdo acuerdo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (acuerdo.getContratoCollection() == null) {
            acuerdo.setContratoCollection(new ArrayList<Contrato>());
        }
        if (acuerdo.getParentelaxpersonaCollection() == null) {
            acuerdo.setParentelaxpersonaCollection(new ArrayList<Parentelaxpersona>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Servicio idclausula = acuerdo.getIdclausula();
            if (idclausula != null) {
                idclausula = em.getReference(idclausula.getClass(), idclausula.getId());
                acuerdo.setIdclausula(idclausula);
            }
            Collection<Contrato> attachedContratoCollection = new ArrayList<Contrato>();
            for (Contrato contratoCollectionContratoToAttach : acuerdo.getContratoCollection()) {
                contratoCollectionContratoToAttach = em.getReference(contratoCollectionContratoToAttach.getClass(), contratoCollectionContratoToAttach.getId());
                attachedContratoCollection.add(contratoCollectionContratoToAttach);
            }
            acuerdo.setContratoCollection(attachedContratoCollection);
            Collection<Parentelaxpersona> attachedParentelaxpersonaCollection = new ArrayList<Parentelaxpersona>();
            for (Parentelaxpersona parentelaxpersonaCollectionParentelaxpersonaToAttach : acuerdo.getParentelaxpersonaCollection()) {
                parentelaxpersonaCollectionParentelaxpersonaToAttach = em.getReference(parentelaxpersonaCollectionParentelaxpersonaToAttach.getClass(), parentelaxpersonaCollectionParentelaxpersonaToAttach.getId());
                attachedParentelaxpersonaCollection.add(parentelaxpersonaCollectionParentelaxpersonaToAttach);
            }
            acuerdo.setParentelaxpersonaCollection(attachedParentelaxpersonaCollection);
            em.persist(acuerdo);
            if (idclausula != null) {
                idclausula.getAcuerdoCollection().add(acuerdo);
                idclausula = em.merge(idclausula);
            }
            for (Contrato contratoCollectionContrato : acuerdo.getContratoCollection()) {
                Acuerdo oldIdacuerdoOfContratoCollectionContrato = contratoCollectionContrato.getIdacuerdo();
                contratoCollectionContrato.setIdacuerdo(acuerdo);
                contratoCollectionContrato = em.merge(contratoCollectionContrato);
                if (oldIdacuerdoOfContratoCollectionContrato != null) {
                    oldIdacuerdoOfContratoCollectionContrato.getContratoCollection().remove(contratoCollectionContrato);
                    oldIdacuerdoOfContratoCollectionContrato = em.merge(oldIdacuerdoOfContratoCollectionContrato);
                }
            }
            for (Parentelaxpersona parentelaxpersonaCollectionParentelaxpersona : acuerdo.getParentelaxpersonaCollection()) {
                Acuerdo oldIdparentelaOfParentelaxpersonaCollectionParentelaxpersona = parentelaxpersonaCollectionParentelaxpersona.getIdparentela();
                parentelaxpersonaCollectionParentelaxpersona.setIdparentela(acuerdo);
                parentelaxpersonaCollectionParentelaxpersona = em.merge(parentelaxpersonaCollectionParentelaxpersona);
                if (oldIdparentelaOfParentelaxpersonaCollectionParentelaxpersona != null) {
                    oldIdparentelaOfParentelaxpersonaCollectionParentelaxpersona.getParentelaxpersonaCollection().remove(parentelaxpersonaCollectionParentelaxpersona);
                    oldIdparentelaOfParentelaxpersonaCollectionParentelaxpersona = em.merge(oldIdparentelaOfParentelaxpersonaCollectionParentelaxpersona);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAcuerdo(acuerdo.getId()) != null) {
                throw new PreexistingEntityException("Acuerdo " + acuerdo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Acuerdo acuerdo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Acuerdo persistentAcuerdo = em.find(Acuerdo.class, acuerdo.getId());
            Servicio idclausulaOld = persistentAcuerdo.getIdclausula();
            Servicio idclausulaNew = acuerdo.getIdclausula();
            Collection<Contrato> contratoCollectionOld = persistentAcuerdo.getContratoCollection();
            Collection<Contrato> contratoCollectionNew = acuerdo.getContratoCollection();
            Collection<Parentelaxpersona> parentelaxpersonaCollectionOld = persistentAcuerdo.getParentelaxpersonaCollection();
            Collection<Parentelaxpersona> parentelaxpersonaCollectionNew = acuerdo.getParentelaxpersonaCollection();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoCollectionOldContrato : contratoCollectionOld) {
                if (!contratoCollectionNew.contains(contratoCollectionOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contrato " + contratoCollectionOldContrato + " since its idacuerdo field is not nullable.");
                }
            }
            for (Parentelaxpersona parentelaxpersonaCollectionOldParentelaxpersona : parentelaxpersonaCollectionOld) {
                if (!parentelaxpersonaCollectionNew.contains(parentelaxpersonaCollectionOldParentelaxpersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Parentelaxpersona " + parentelaxpersonaCollectionOldParentelaxpersona + " since its idparentela field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idclausulaNew != null) {
                idclausulaNew = em.getReference(idclausulaNew.getClass(), idclausulaNew.getId());
                acuerdo.setIdclausula(idclausulaNew);
            }
            Collection<Contrato> attachedContratoCollectionNew = new ArrayList<Contrato>();
            for (Contrato contratoCollectionNewContratoToAttach : contratoCollectionNew) {
                contratoCollectionNewContratoToAttach = em.getReference(contratoCollectionNewContratoToAttach.getClass(), contratoCollectionNewContratoToAttach.getId());
                attachedContratoCollectionNew.add(contratoCollectionNewContratoToAttach);
            }
            contratoCollectionNew = attachedContratoCollectionNew;
            acuerdo.setContratoCollection(contratoCollectionNew);
            Collection<Parentelaxpersona> attachedParentelaxpersonaCollectionNew = new ArrayList<Parentelaxpersona>();
            for (Parentelaxpersona parentelaxpersonaCollectionNewParentelaxpersonaToAttach : parentelaxpersonaCollectionNew) {
                parentelaxpersonaCollectionNewParentelaxpersonaToAttach = em.getReference(parentelaxpersonaCollectionNewParentelaxpersonaToAttach.getClass(), parentelaxpersonaCollectionNewParentelaxpersonaToAttach.getId());
                attachedParentelaxpersonaCollectionNew.add(parentelaxpersonaCollectionNewParentelaxpersonaToAttach);
            }
            parentelaxpersonaCollectionNew = attachedParentelaxpersonaCollectionNew;
            acuerdo.setParentelaxpersonaCollection(parentelaxpersonaCollectionNew);
            acuerdo = em.merge(acuerdo);
            if (idclausulaOld != null && !idclausulaOld.equals(idclausulaNew)) {
                idclausulaOld.getAcuerdoCollection().remove(acuerdo);
                idclausulaOld = em.merge(idclausulaOld);
            }
            if (idclausulaNew != null && !idclausulaNew.equals(idclausulaOld)) {
                idclausulaNew.getAcuerdoCollection().add(acuerdo);
                idclausulaNew = em.merge(idclausulaNew);
            }
            for (Contrato contratoCollectionNewContrato : contratoCollectionNew) {
                if (!contratoCollectionOld.contains(contratoCollectionNewContrato)) {
                    Acuerdo oldIdacuerdoOfContratoCollectionNewContrato = contratoCollectionNewContrato.getIdacuerdo();
                    contratoCollectionNewContrato.setIdacuerdo(acuerdo);
                    contratoCollectionNewContrato = em.merge(contratoCollectionNewContrato);
                    if (oldIdacuerdoOfContratoCollectionNewContrato != null && !oldIdacuerdoOfContratoCollectionNewContrato.equals(acuerdo)) {
                        oldIdacuerdoOfContratoCollectionNewContrato.getContratoCollection().remove(contratoCollectionNewContrato);
                        oldIdacuerdoOfContratoCollectionNewContrato = em.merge(oldIdacuerdoOfContratoCollectionNewContrato);
                    }
                }
            }
            for (Parentelaxpersona parentelaxpersonaCollectionNewParentelaxpersona : parentelaxpersonaCollectionNew) {
                if (!parentelaxpersonaCollectionOld.contains(parentelaxpersonaCollectionNewParentelaxpersona)) {
                    Acuerdo oldIdparentelaOfParentelaxpersonaCollectionNewParentelaxpersona = parentelaxpersonaCollectionNewParentelaxpersona.getIdparentela();
                    parentelaxpersonaCollectionNewParentelaxpersona.setIdparentela(acuerdo);
                    parentelaxpersonaCollectionNewParentelaxpersona = em.merge(parentelaxpersonaCollectionNewParentelaxpersona);
                    if (oldIdparentelaOfParentelaxpersonaCollectionNewParentelaxpersona != null && !oldIdparentelaOfParentelaxpersonaCollectionNewParentelaxpersona.equals(acuerdo)) {
                        oldIdparentelaOfParentelaxpersonaCollectionNewParentelaxpersona.getParentelaxpersonaCollection().remove(parentelaxpersonaCollectionNewParentelaxpersona);
                        oldIdparentelaOfParentelaxpersonaCollectionNewParentelaxpersona = em.merge(oldIdparentelaOfParentelaxpersonaCollectionNewParentelaxpersona);
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
                Long id = acuerdo.getId();
                if (findAcuerdo(id) == null) {
                    throw new NonexistentEntityException("The acuerdo with id " + id + " no longer exists.");
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
            Acuerdo acuerdo;
            try {
                acuerdo = em.getReference(Acuerdo.class, id);
                acuerdo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The acuerdo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contrato> contratoCollectionOrphanCheck = acuerdo.getContratoCollection();
            for (Contrato contratoCollectionOrphanCheckContrato : contratoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Acuerdo (" + acuerdo + ") cannot be destroyed since the Contrato " + contratoCollectionOrphanCheckContrato + " in its contratoCollection field has a non-nullable idacuerdo field.");
            }
            Collection<Parentelaxpersona> parentelaxpersonaCollectionOrphanCheck = acuerdo.getParentelaxpersonaCollection();
            for (Parentelaxpersona parentelaxpersonaCollectionOrphanCheckParentelaxpersona : parentelaxpersonaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Acuerdo (" + acuerdo + ") cannot be destroyed since the Parentelaxpersona " + parentelaxpersonaCollectionOrphanCheckParentelaxpersona + " in its parentelaxpersonaCollection field has a non-nullable idparentela field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Servicio idclausula = acuerdo.getIdclausula();
            if (idclausula != null) {
                idclausula.getAcuerdoCollection().remove(acuerdo);
                idclausula = em.merge(idclausula);
            }
            em.remove(acuerdo);
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

    public List<Acuerdo> findAcuerdoEntities() {
        return findAcuerdoEntities(true, -1, -1);
    }

    public List<Acuerdo> findAcuerdoEntities(int maxResults, int firstResult) {
        return findAcuerdoEntities(false, maxResults, firstResult);
    }

    private List<Acuerdo> findAcuerdoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Acuerdo.class));
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

    public Acuerdo findAcuerdo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Acuerdo.class, id);
        } finally {
            em.close();
        }
    }

    public int getAcuerdoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Acuerdo> rt = cq.from(Acuerdo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
