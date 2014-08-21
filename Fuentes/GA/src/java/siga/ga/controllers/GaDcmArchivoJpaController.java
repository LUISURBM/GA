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
import siga.ga.entytis.GaDcm;
import siga.ga.entytis.GaArchivo;
import siga.ga.entytis.GaDcmArchivo;

/**
 *
 * @author Otros
 */
public class GaDcmArchivoJpaController implements Serializable {

    public GaDcmArchivoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
@PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;

    public GaDcmArchivoJpaController() {

            this.utx = lookUpUtx();

            this.emf = lookUpEmf();
    }
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GaDcmArchivo gaDcmArchivo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GaDcm dcmArchivoIdDcm = gaDcmArchivo.getDcmArchivoIdDcm();
            if (dcmArchivoIdDcm != null) {
                dcmArchivoIdDcm = em.getReference(dcmArchivoIdDcm.getClass(), dcmArchivoIdDcm.getDcmId());
                gaDcmArchivo.setDcmArchivoIdDcm(dcmArchivoIdDcm);
            }
            GaArchivo dcmArchivoIdArchivo = gaDcmArchivo.getDcmArchivoIdArchivo();
            if (dcmArchivoIdArchivo != null) {
                dcmArchivoIdArchivo = em.getReference(dcmArchivoIdArchivo.getClass(), dcmArchivoIdArchivo.getArchivoId());
                gaDcmArchivo.setDcmArchivoIdArchivo(dcmArchivoIdArchivo);
            }
            em.persist(gaDcmArchivo);
            if (dcmArchivoIdDcm != null) {
                dcmArchivoIdDcm.getGaDcmArchivoList().add(gaDcmArchivo);
                dcmArchivoIdDcm = em.merge(dcmArchivoIdDcm);
            }
            if (dcmArchivoIdArchivo != null) {
                dcmArchivoIdArchivo.getGaDcmArchivoList().add(gaDcmArchivo);
                dcmArchivoIdArchivo = em.merge(dcmArchivoIdArchivo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGaDcmArchivo(gaDcmArchivo.getDcmArchivoId()) != null) {
                throw new PreexistingEntityException("GaDcmArchivo " + gaDcmArchivo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GaDcmArchivo gaDcmArchivo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GaDcmArchivo persistentGaDcmArchivo = em.find(GaDcmArchivo.class, gaDcmArchivo.getDcmArchivoId());
            GaDcm dcmArchivoIdDcmOld = persistentGaDcmArchivo.getDcmArchivoIdDcm();
            GaDcm dcmArchivoIdDcmNew = gaDcmArchivo.getDcmArchivoIdDcm();
            GaArchivo dcmArchivoIdArchivoOld = persistentGaDcmArchivo.getDcmArchivoIdArchivo();
            GaArchivo dcmArchivoIdArchivoNew = gaDcmArchivo.getDcmArchivoIdArchivo();
            if (dcmArchivoIdDcmNew != null) {
                dcmArchivoIdDcmNew = em.getReference(dcmArchivoIdDcmNew.getClass(), dcmArchivoIdDcmNew.getDcmId());
                gaDcmArchivo.setDcmArchivoIdDcm(dcmArchivoIdDcmNew);
            }
            if (dcmArchivoIdArchivoNew != null) {
                dcmArchivoIdArchivoNew = em.getReference(dcmArchivoIdArchivoNew.getClass(), dcmArchivoIdArchivoNew.getArchivoId());
                gaDcmArchivo.setDcmArchivoIdArchivo(dcmArchivoIdArchivoNew);
            }
            gaDcmArchivo = em.merge(gaDcmArchivo);
            if (dcmArchivoIdDcmOld != null && !dcmArchivoIdDcmOld.equals(dcmArchivoIdDcmNew)) {
                dcmArchivoIdDcmOld.getGaDcmArchivoList().remove(gaDcmArchivo);
                dcmArchivoIdDcmOld = em.merge(dcmArchivoIdDcmOld);
            }
            if (dcmArchivoIdDcmNew != null && !dcmArchivoIdDcmNew.equals(dcmArchivoIdDcmOld)) {
                dcmArchivoIdDcmNew.getGaDcmArchivoList().add(gaDcmArchivo);
                dcmArchivoIdDcmNew = em.merge(dcmArchivoIdDcmNew);
            }
            if (dcmArchivoIdArchivoOld != null && !dcmArchivoIdArchivoOld.equals(dcmArchivoIdArchivoNew)) {
                dcmArchivoIdArchivoOld.getGaDcmArchivoList().remove(gaDcmArchivo);
                dcmArchivoIdArchivoOld = em.merge(dcmArchivoIdArchivoOld);
            }
            if (dcmArchivoIdArchivoNew != null && !dcmArchivoIdArchivoNew.equals(dcmArchivoIdArchivoOld)) {
                dcmArchivoIdArchivoNew.getGaDcmArchivoList().add(gaDcmArchivo);
                dcmArchivoIdArchivoNew = em.merge(dcmArchivoIdArchivoNew);
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
                Long id = gaDcmArchivo.getDcmArchivoId();
                if (findGaDcmArchivo(id) == null) {
                    throw new NonexistentEntityException("The gaDcmArchivo with id " + id + " no longer exists.");
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
            GaDcmArchivo gaDcmArchivo;
            try {
                gaDcmArchivo = em.getReference(GaDcmArchivo.class, id);
                gaDcmArchivo.getDcmArchivoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gaDcmArchivo with id " + id + " no longer exists.", enfe);
            }
            GaDcm dcmArchivoIdDcm = gaDcmArchivo.getDcmArchivoIdDcm();
            if (dcmArchivoIdDcm != null) {
                dcmArchivoIdDcm.getGaDcmArchivoList().remove(gaDcmArchivo);
                dcmArchivoIdDcm = em.merge(dcmArchivoIdDcm);
            }
            GaArchivo dcmArchivoIdArchivo = gaDcmArchivo.getDcmArchivoIdArchivo();
            if (dcmArchivoIdArchivo != null) {
                dcmArchivoIdArchivo.getGaDcmArchivoList().remove(gaDcmArchivo);
                dcmArchivoIdArchivo = em.merge(dcmArchivoIdArchivo);
            }
            em.remove(gaDcmArchivo);
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

    public List<GaDcmArchivo> findGaDcmArchivoEntities() {
        return findGaDcmArchivoEntities(true, -1, -1);
    }

    public List<GaDcmArchivo> findGaDcmArchivoEntities(int maxResults, int firstResult) {
        return findGaDcmArchivoEntities(false, maxResults, firstResult);
    }

    private List<GaDcmArchivo> findGaDcmArchivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GaDcmArchivo.class));
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

    public GaDcmArchivo findGaDcmArchivo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GaDcmArchivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGaDcmArchivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GaDcmArchivo> rt = cq.from(GaDcmArchivo.class);
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
            Logger.getLogger(GaDcmArchivoJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

   private static InitialContext getContext() {
        try {
            return new InitialContext();
        } catch (NamingException ex) {
            Logger.getLogger(GaDcmArchivoJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static EntityManagerFactory lookUpEmf() {
        return javax.persistence.Persistence.createEntityManagerFactory("GAPU");
    }        
}
