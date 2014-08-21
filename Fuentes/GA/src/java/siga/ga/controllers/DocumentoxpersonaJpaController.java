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
import siga.ga.entytis.Documentoxpersona;
import siga.ga.entytis.Personaje;
import siga.ga.entytis.Documento;

/**
 *
 * @author Otros
 */
public class DocumentoxpersonaJpaController implements Serializable {

    public DocumentoxpersonaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    @PersistenceUnit(unitName="GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    public DocumentoxpersonaJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(DocumentoxpersonaJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documentoxpersona documentoxpersona) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Personaje idpersona = documentoxpersona.getIdpersona();
            if (idpersona != null) {
                idpersona = em.getReference(idpersona.getClass(), idpersona.getId());
                documentoxpersona.setIdpersona(idpersona);
            }
            Documento iddocumento = documentoxpersona.getIddocumento();
            if (iddocumento != null) {
                iddocumento = em.getReference(iddocumento.getClass(), iddocumento.getId());
                documentoxpersona.setIddocumento(iddocumento);
            }
            em.persist(documentoxpersona);
            if (idpersona != null) {
                idpersona.getDocumentoxpersonaCollection().add(documentoxpersona);
                idpersona = em.merge(idpersona);
            }
            if (iddocumento != null) {
                iddocumento.getDocumentoxpersonaCollection().add(documentoxpersona);
                iddocumento = em.merge(iddocumento);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDocumentoxpersona(documentoxpersona.getId()) != null) {
                throw new PreexistingEntityException("Documentoxpersona " + documentoxpersona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documentoxpersona documentoxpersona) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Documentoxpersona persistentDocumentoxpersona = em.find(Documentoxpersona.class, documentoxpersona.getId());
            Personaje idpersonaOld = persistentDocumentoxpersona.getIdpersona();
            Personaje idpersonaNew = documentoxpersona.getIdpersona();
            Documento iddocumentoOld = persistentDocumentoxpersona.getIddocumento();
            Documento iddocumentoNew = documentoxpersona.getIddocumento();
            if (idpersonaNew != null) {
                idpersonaNew = em.getReference(idpersonaNew.getClass(), idpersonaNew.getId());
                documentoxpersona.setIdpersona(idpersonaNew);
            }
            if (iddocumentoNew != null) {
                iddocumentoNew = em.getReference(iddocumentoNew.getClass(), iddocumentoNew.getId());
                documentoxpersona.setIddocumento(iddocumentoNew);
            }
            documentoxpersona = em.merge(documentoxpersona);
            if (idpersonaOld != null && !idpersonaOld.equals(idpersonaNew)) {
                idpersonaOld.getDocumentoxpersonaCollection().remove(documentoxpersona);
                idpersonaOld = em.merge(idpersonaOld);
            }
            if (idpersonaNew != null && !idpersonaNew.equals(idpersonaOld)) {
                idpersonaNew.getDocumentoxpersonaCollection().add(documentoxpersona);
                idpersonaNew = em.merge(idpersonaNew);
            }
            if (iddocumentoOld != null && !iddocumentoOld.equals(iddocumentoNew)) {
                iddocumentoOld.getDocumentoxpersonaCollection().remove(documentoxpersona);
                iddocumentoOld = em.merge(iddocumentoOld);
            }
            if (iddocumentoNew != null && !iddocumentoNew.equals(iddocumentoOld)) {
                iddocumentoNew.getDocumentoxpersonaCollection().add(documentoxpersona);
                iddocumentoNew = em.merge(iddocumentoNew);
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
                Long id = documentoxpersona.getId();
                if (findDocumentoxpersona(id) == null) {
                    throw new NonexistentEntityException("The documentoxpersona with id " + id + " no longer exists.");
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
            Documentoxpersona documentoxpersona;
            try {
                documentoxpersona = em.getReference(Documentoxpersona.class, id);
                documentoxpersona.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentoxpersona with id " + id + " no longer exists.", enfe);
            }
            Personaje idpersona = documentoxpersona.getIdpersona();
            if (idpersona != null) {
                idpersona.getDocumentoxpersonaCollection().remove(documentoxpersona);
                idpersona = em.merge(idpersona);
            }
            Documento iddocumento = documentoxpersona.getIddocumento();
            if (iddocumento != null) {
                iddocumento.getDocumentoxpersonaCollection().remove(documentoxpersona);
                iddocumento = em.merge(iddocumento);
            }
            em.remove(documentoxpersona);
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

    public List<Documentoxpersona> findDocumentoxpersonaEntities() {
        return findDocumentoxpersonaEntities(true, -1, -1);
    }

    public List<Documentoxpersona> findDocumentoxpersonaEntities(int maxResults, int firstResult) {
        return findDocumentoxpersonaEntities(false, maxResults, firstResult);
    }

    private List<Documentoxpersona> findDocumentoxpersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documentoxpersona.class));
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

    public Documentoxpersona findDocumentoxpersona(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documentoxpersona.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoxpersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documentoxpersona> rt = cq.from(Documentoxpersona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
