/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.controllers;

import java.io.Serializable;
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
import siga.ga.entytis.GaArchivo;
import siga.ga.entytis.GaDcmArchivo;
import java.util.ArrayList;
import java.util.List;
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
public class GaArchivoJpaController implements Serializable {

    public GaArchivoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    @PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;

    public GaArchivoJpaController() {

            this.utx = lookUpUtx();

            this.emf = lookUpEmf();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GaArchivo gaArchivo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (gaArchivo.getGaDcmArchivoList() == null) {
            gaArchivo.setGaDcmArchivoList(new ArrayList<GaDcmArchivo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<GaDcmArchivo> attachedGaDcmArchivoList = new ArrayList<GaDcmArchivo>();
            for (GaDcmArchivo gaDcmArchivoListGaDcmArchivoToAttach : gaArchivo.getGaDcmArchivoList()) {
                gaDcmArchivoListGaDcmArchivoToAttach = em.getReference(gaDcmArchivoListGaDcmArchivoToAttach.getClass(), gaDcmArchivoListGaDcmArchivoToAttach.getDcmArchivoId());
                attachedGaDcmArchivoList.add(gaDcmArchivoListGaDcmArchivoToAttach);
            }
            gaArchivo.setGaDcmArchivoList(attachedGaDcmArchivoList);
            em.persist(gaArchivo);
            for (GaDcmArchivo gaDcmArchivoListGaDcmArchivo : gaArchivo.getGaDcmArchivoList()) {
                GaArchivo oldDcmArchivoIdArchivoOfGaDcmArchivoListGaDcmArchivo = gaDcmArchivoListGaDcmArchivo.getDcmArchivoIdArchivo();
                gaDcmArchivoListGaDcmArchivo.setDcmArchivoIdArchivo(gaArchivo);
                gaDcmArchivoListGaDcmArchivo = em.merge(gaDcmArchivoListGaDcmArchivo);
                if (oldDcmArchivoIdArchivoOfGaDcmArchivoListGaDcmArchivo != null) {
                    oldDcmArchivoIdArchivoOfGaDcmArchivoListGaDcmArchivo.getGaDcmArchivoList().remove(gaDcmArchivoListGaDcmArchivo);
                    oldDcmArchivoIdArchivoOfGaDcmArchivoListGaDcmArchivo = em.merge(oldDcmArchivoIdArchivoOfGaDcmArchivoListGaDcmArchivo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGaArchivo(gaArchivo.getArchivoId()) != null) {
                throw new PreexistingEntityException("GaArchivo " + gaArchivo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GaArchivo gaArchivo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GaArchivo persistentGaArchivo = em.find(GaArchivo.class, gaArchivo.getArchivoId());
            List<GaDcmArchivo> gaDcmArchivoListOld = persistentGaArchivo.getGaDcmArchivoList();
            List<GaDcmArchivo> gaDcmArchivoListNew = gaArchivo.getGaDcmArchivoList();
            List<String> illegalOrphanMessages = null;
            for (GaDcmArchivo gaDcmArchivoListOldGaDcmArchivo : gaDcmArchivoListOld) {
                if (!gaDcmArchivoListNew.contains(gaDcmArchivoListOldGaDcmArchivo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GaDcmArchivo " + gaDcmArchivoListOldGaDcmArchivo + " since its dcmArchivoIdArchivo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GaDcmArchivo> attachedGaDcmArchivoListNew = new ArrayList<GaDcmArchivo>();
            for (GaDcmArchivo gaDcmArchivoListNewGaDcmArchivoToAttach : gaDcmArchivoListNew) {
                gaDcmArchivoListNewGaDcmArchivoToAttach = em.getReference(gaDcmArchivoListNewGaDcmArchivoToAttach.getClass(), gaDcmArchivoListNewGaDcmArchivoToAttach.getDcmArchivoId());
                attachedGaDcmArchivoListNew.add(gaDcmArchivoListNewGaDcmArchivoToAttach);
            }
            gaDcmArchivoListNew = attachedGaDcmArchivoListNew;
            gaArchivo.setGaDcmArchivoList(gaDcmArchivoListNew);
            gaArchivo = em.merge(gaArchivo);
            for (GaDcmArchivo gaDcmArchivoListNewGaDcmArchivo : gaDcmArchivoListNew) {
                if (!gaDcmArchivoListOld.contains(gaDcmArchivoListNewGaDcmArchivo)) {
                    GaArchivo oldDcmArchivoIdArchivoOfGaDcmArchivoListNewGaDcmArchivo = gaDcmArchivoListNewGaDcmArchivo.getDcmArchivoIdArchivo();
                    gaDcmArchivoListNewGaDcmArchivo.setDcmArchivoIdArchivo(gaArchivo);
                    gaDcmArchivoListNewGaDcmArchivo = em.merge(gaDcmArchivoListNewGaDcmArchivo);
                    if (oldDcmArchivoIdArchivoOfGaDcmArchivoListNewGaDcmArchivo != null && !oldDcmArchivoIdArchivoOfGaDcmArchivoListNewGaDcmArchivo.equals(gaArchivo)) {
                        oldDcmArchivoIdArchivoOfGaDcmArchivoListNewGaDcmArchivo.getGaDcmArchivoList().remove(gaDcmArchivoListNewGaDcmArchivo);
                        oldDcmArchivoIdArchivoOfGaDcmArchivoListNewGaDcmArchivo = em.merge(oldDcmArchivoIdArchivoOfGaDcmArchivoListNewGaDcmArchivo);
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
                Long id = gaArchivo.getArchivoId();
                if (findGaArchivo(id) == null) {
                    throw new NonexistentEntityException("The gaArchivo with id " + id + " no longer exists.");
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
            GaArchivo gaArchivo;
            try {
                gaArchivo = em.getReference(GaArchivo.class, id);
                gaArchivo.getArchivoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gaArchivo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GaDcmArchivo> gaDcmArchivoListOrphanCheck = gaArchivo.getGaDcmArchivoList();
            for (GaDcmArchivo gaDcmArchivoListOrphanCheckGaDcmArchivo : gaDcmArchivoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GaArchivo (" + gaArchivo + ") cannot be destroyed since the GaDcmArchivo " + gaDcmArchivoListOrphanCheckGaDcmArchivo + " in its gaDcmArchivoList field has a non-nullable dcmArchivoIdArchivo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gaArchivo);
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

    public List<GaArchivo> findGaArchivoEntities() {
        return findGaArchivoEntities(true, -1, -1);
    }

    public List<GaArchivo> findGaArchivoEntities(int maxResults, int firstResult) {
        return findGaArchivoEntities(false, maxResults, firstResult);
    }

    private List<GaArchivo> findGaArchivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GaArchivo.class));
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

    public GaArchivo findGaArchivo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GaArchivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGaArchivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GaArchivo> rt = cq.from(GaArchivo.class);
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
            Logger.getLogger(GaArchivoJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

   private static InitialContext getContext() {
        try {
            return new InitialContext();
        } catch (NamingException ex) {
            Logger.getLogger(GaArchivoJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static EntityManagerFactory lookUpEmf() {
        return javax.persistence.Persistence.createEntityManagerFactory("GAPU");
    }        
}
