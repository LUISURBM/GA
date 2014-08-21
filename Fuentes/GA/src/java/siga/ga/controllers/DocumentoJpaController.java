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
import siga.ga.entytis.Archivo;
import siga.ga.entytis.Contrato;
import java.util.ArrayList;
import java.util.Collection;
import siga.ga.entytis.Documento;
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
public class DocumentoJpaController implements Serializable {

    public DocumentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    
     @PersistenceUnit(unitName="GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    public DocumentoJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(DocumentoJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documento documento) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (documento.getContratoCollection() == null) {
            documento.setContratoCollection(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Archivo idarchivo = documento.getIdarchivo();
            if (idarchivo != null) {
                idarchivo = em.getReference(idarchivo.getClass(), idarchivo.getId());
                documento.setIdarchivo(idarchivo);
            }
            Collection<Contrato> attachedContratoCollection = new ArrayList<Contrato>();
            for (Contrato contratoCollectionContratoToAttach : documento.getContratoCollection()) {
                contratoCollectionContratoToAttach = em.getReference(contratoCollectionContratoToAttach.getClass(), contratoCollectionContratoToAttach.getId());
                attachedContratoCollection.add(contratoCollectionContratoToAttach);
            }
            documento.setContratoCollection(attachedContratoCollection);
            em.persist(documento);
            if (idarchivo != null) {
                idarchivo.getDocumentoCollection().add(documento);
                idarchivo = em.merge(idarchivo);
            }
            for (Contrato contratoCollectionContrato : documento.getContratoCollection()) {
                Documento oldIddocumentoOfContratoCollectionContrato = contratoCollectionContrato.getIddocumento();
                contratoCollectionContrato.setIddocumento(documento);
                contratoCollectionContrato = em.merge(contratoCollectionContrato);
                if (oldIddocumentoOfContratoCollectionContrato != null) {
                    oldIddocumentoOfContratoCollectionContrato.getContratoCollection().remove(contratoCollectionContrato);
                    oldIddocumentoOfContratoCollectionContrato = em.merge(oldIddocumentoOfContratoCollectionContrato);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDocumento(documento.getId()) != null) {
                throw new PreexistingEntityException("Documento " + documento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documento documento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Documento persistentDocumento = em.find(Documento.class, documento.getId());
            Archivo idarchivoOld = persistentDocumento.getIdarchivo();
            Archivo idarchivoNew = documento.getIdarchivo();
            Collection<Contrato> contratoCollectionOld = persistentDocumento.getContratoCollection();
            Collection<Contrato> contratoCollectionNew = documento.getContratoCollection();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoCollectionOldContrato : contratoCollectionOld) {
                if (!contratoCollectionNew.contains(contratoCollectionOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contrato " + contratoCollectionOldContrato + " since its iddocumento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idarchivoNew != null) {
                idarchivoNew = em.getReference(idarchivoNew.getClass(), idarchivoNew.getId());
                documento.setIdarchivo(idarchivoNew);
            }
            Collection<Contrato> attachedContratoCollectionNew = new ArrayList<Contrato>();
            for (Contrato contratoCollectionNewContratoToAttach : contratoCollectionNew) {
                contratoCollectionNewContratoToAttach = em.getReference(contratoCollectionNewContratoToAttach.getClass(), contratoCollectionNewContratoToAttach.getId());
                attachedContratoCollectionNew.add(contratoCollectionNewContratoToAttach);
            }
            contratoCollectionNew = attachedContratoCollectionNew;
            documento.setContratoCollection(contratoCollectionNew);
            documento = em.merge(documento);
            if (idarchivoOld != null && !idarchivoOld.equals(idarchivoNew)) {
                idarchivoOld.getDocumentoCollection().remove(documento);
                idarchivoOld = em.merge(idarchivoOld);
            }
            if (idarchivoNew != null && !idarchivoNew.equals(idarchivoOld)) {
                idarchivoNew.getDocumentoCollection().add(documento);
                idarchivoNew = em.merge(idarchivoNew);
            }
            for (Contrato contratoCollectionNewContrato : contratoCollectionNew) {
                if (!contratoCollectionOld.contains(contratoCollectionNewContrato)) {
                    Documento oldIddocumentoOfContratoCollectionNewContrato = contratoCollectionNewContrato.getIddocumento();
                    contratoCollectionNewContrato.setIddocumento(documento);
                    contratoCollectionNewContrato = em.merge(contratoCollectionNewContrato);
                    if (oldIddocumentoOfContratoCollectionNewContrato != null && !oldIddocumentoOfContratoCollectionNewContrato.equals(documento)) {
                        oldIddocumentoOfContratoCollectionNewContrato.getContratoCollection().remove(contratoCollectionNewContrato);
                        oldIddocumentoOfContratoCollectionNewContrato = em.merge(oldIddocumentoOfContratoCollectionNewContrato);
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
                Long id = documento.getId();
                if (findDocumento(id) == null) {
                    throw new NonexistentEntityException("The documento with id " + id + " no longer exists.");
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
            Documento documento;
            try {
                documento = em.getReference(Documento.class, id);
                documento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contrato> contratoCollectionOrphanCheck = documento.getContratoCollection();
            for (Contrato contratoCollectionOrphanCheckContrato : contratoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Documento (" + documento + ") cannot be destroyed since the Contrato " + contratoCollectionOrphanCheckContrato + " in its contratoCollection field has a non-nullable iddocumento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Archivo idarchivo = documento.getIdarchivo();
            if (idarchivo != null) {
                idarchivo.getDocumentoCollection().remove(documento);
                idarchivo = em.merge(idarchivo);
            }
            em.remove(documento);
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

    public List<Documento> findDocumentoEntities() {
        return findDocumentoEntities(true, -1, -1);
    }

    public List<Documento> findDocumentoEntities(int maxResults, int firstResult) {
        return findDocumentoEntities(false, maxResults, firstResult);
    }

    private List<Documento> findDocumentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documento.class));
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

    public Documento findDocumento(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documento> rt = cq.from(Documento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
