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
import siga.ga.entytis.Contrato;
import siga.ga.entytis.Personaje;
import siga.ga.entytis.Parentela;
import siga.ga.entytis.Documento;
import siga.ga.entytis.Acuerdo;

/**
 *
 * @author Otros
 */
public class ContratoJpaController implements Serializable {

    public ContratoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    
  
     @PersistenceUnit(unitName="GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    public ContratoJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(ContratoJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contrato contrato) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Personaje idarrendador = contrato.getIdarrendador();
            if (idarrendador != null) {
                idarrendador = em.getReference(idarrendador.getClass(), idarrendador.getId());
                contrato.setIdarrendador(idarrendador);
            }
            Parentela idparentela = contrato.getIdparentela();
            if (idparentela != null) {
                idparentela = em.getReference(idparentela.getClass(), idparentela.getId());
                contrato.setIdparentela(idparentela);
            }
            Documento iddocumento = contrato.getIddocumento();
            if (iddocumento != null) {
                iddocumento = em.getReference(iddocumento.getClass(), iddocumento.getId());
                contrato.setIddocumento(iddocumento);
            }
            Acuerdo idacuerdo = contrato.getIdacuerdo();
            if (idacuerdo != null) {
                idacuerdo = em.getReference(idacuerdo.getClass(), idacuerdo.getId());
                contrato.setIdacuerdo(idacuerdo);
            }
            em.persist(contrato);
            if (idarrendador != null) {
                idarrendador.getContratoCollection().add(contrato);
                idarrendador = em.merge(idarrendador);
            }
            if (idparentela != null) {
                idparentela.getContratoCollection().add(contrato);
                idparentela = em.merge(idparentela);
            }
            if (iddocumento != null) {
                iddocumento.getContratoCollection().add(contrato);
                iddocumento = em.merge(iddocumento);
            }
            if (idacuerdo != null) {
                idacuerdo.getContratoCollection().add(contrato);
                idacuerdo = em.merge(idacuerdo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findContrato(contrato.getId()) != null) {
                throw new PreexistingEntityException("Contrato " + contrato + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contrato contrato) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Contrato persistentContrato = em.find(Contrato.class, contrato.getId());
            Personaje idarrendadorOld = persistentContrato.getIdarrendador();
            Personaje idarrendadorNew = contrato.getIdarrendador();
            Parentela idparentelaOld = persistentContrato.getIdparentela();
            Parentela idparentelaNew = contrato.getIdparentela();
            Documento iddocumentoOld = persistentContrato.getIddocumento();
            Documento iddocumentoNew = contrato.getIddocumento();
            Acuerdo idacuerdoOld = persistentContrato.getIdacuerdo();
            Acuerdo idacuerdoNew = contrato.getIdacuerdo();
            if (idarrendadorNew != null) {
                idarrendadorNew = em.getReference(idarrendadorNew.getClass(), idarrendadorNew.getId());
                contrato.setIdarrendador(idarrendadorNew);
            }
            if (idparentelaNew != null) {
                idparentelaNew = em.getReference(idparentelaNew.getClass(), idparentelaNew.getId());
                contrato.setIdparentela(idparentelaNew);
            }
            if (iddocumentoNew != null) {
                iddocumentoNew = em.getReference(iddocumentoNew.getClass(), iddocumentoNew.getId());
                contrato.setIddocumento(iddocumentoNew);
            }
            if (idacuerdoNew != null) {
                idacuerdoNew = em.getReference(idacuerdoNew.getClass(), idacuerdoNew.getId());
                contrato.setIdacuerdo(idacuerdoNew);
            }
            contrato = em.merge(contrato);
            if (idarrendadorOld != null && !idarrendadorOld.equals(idarrendadorNew)) {
                idarrendadorOld.getContratoCollection().remove(contrato);
                idarrendadorOld = em.merge(idarrendadorOld);
            }
            if (idarrendadorNew != null && !idarrendadorNew.equals(idarrendadorOld)) {
                idarrendadorNew.getContratoCollection().add(contrato);
                idarrendadorNew = em.merge(idarrendadorNew);
            }
            if (idparentelaOld != null && !idparentelaOld.equals(idparentelaNew)) {
                idparentelaOld.getContratoCollection().remove(contrato);
                idparentelaOld = em.merge(idparentelaOld);
            }
            if (idparentelaNew != null && !idparentelaNew.equals(idparentelaOld)) {
                idparentelaNew.getContratoCollection().add(contrato);
                idparentelaNew = em.merge(idparentelaNew);
            }
            if (iddocumentoOld != null && !iddocumentoOld.equals(iddocumentoNew)) {
                iddocumentoOld.getContratoCollection().remove(contrato);
                iddocumentoOld = em.merge(iddocumentoOld);
            }
            if (iddocumentoNew != null && !iddocumentoNew.equals(iddocumentoOld)) {
                iddocumentoNew.getContratoCollection().add(contrato);
                iddocumentoNew = em.merge(iddocumentoNew);
            }
            if (idacuerdoOld != null && !idacuerdoOld.equals(idacuerdoNew)) {
                idacuerdoOld.getContratoCollection().remove(contrato);
                idacuerdoOld = em.merge(idacuerdoOld);
            }
            if (idacuerdoNew != null && !idacuerdoNew.equals(idacuerdoOld)) {
                idacuerdoNew.getContratoCollection().add(contrato);
                idacuerdoNew = em.merge(idacuerdoNew);
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
                Long id = contrato.getId();
                if (findContrato(id) == null) {
                    throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.");
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
            Contrato contrato;
            try {
                contrato = em.getReference(Contrato.class, id);
                contrato.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.", enfe);
            }
            Personaje idarrendador = contrato.getIdarrendador();
            if (idarrendador != null) {
                idarrendador.getContratoCollection().remove(contrato);
                idarrendador = em.merge(idarrendador);
            }
            Parentela idparentela = contrato.getIdparentela();
            if (idparentela != null) {
                idparentela.getContratoCollection().remove(contrato);
                idparentela = em.merge(idparentela);
            }
            Documento iddocumento = contrato.getIddocumento();
            if (iddocumento != null) {
                iddocumento.getContratoCollection().remove(contrato);
                iddocumento = em.merge(iddocumento);
            }
            Acuerdo idacuerdo = contrato.getIdacuerdo();
            if (idacuerdo != null) {
                idacuerdo.getContratoCollection().remove(contrato);
                idacuerdo = em.merge(idacuerdo);
            }
            em.remove(contrato);
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

    public List<Contrato> findContratoEntities() {
        return findContratoEntities(true, -1, -1);
    }

    public List<Contrato> findContratoEntities(int maxResults, int firstResult) {
        return findContratoEntities(false, maxResults, firstResult);
    }

    private List<Contrato> findContratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contrato.class));
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

    public Contrato findContrato(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getContratoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contrato> rt = cq.from(Contrato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
