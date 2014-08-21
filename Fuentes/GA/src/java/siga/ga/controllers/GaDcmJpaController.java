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
import siga.ga.entytis.GaDcm;
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
public class GaDcmJpaController implements Serializable {

    public GaDcmJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
@PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;

    public GaDcmJpaController() {

            this.utx = lookUpUtx();

            this.emf = lookUpEmf();
    }
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GaDcm gaDcm) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (gaDcm.getGaDcmArchivoList() == null) {
            gaDcm.setGaDcmArchivoList(new ArrayList<GaDcmArchivo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<GaDcmArchivo> attachedGaDcmArchivoList = new ArrayList<GaDcmArchivo>();
            for (GaDcmArchivo gaDcmArchivoListGaDcmArchivoToAttach : gaDcm.getGaDcmArchivoList()) {
                gaDcmArchivoListGaDcmArchivoToAttach = em.getReference(gaDcmArchivoListGaDcmArchivoToAttach.getClass(), gaDcmArchivoListGaDcmArchivoToAttach.getDcmArchivoId());
                attachedGaDcmArchivoList.add(gaDcmArchivoListGaDcmArchivoToAttach);
            }
            gaDcm.setGaDcmArchivoList(attachedGaDcmArchivoList);
            em.persist(gaDcm);
            for (GaDcmArchivo gaDcmArchivoListGaDcmArchivo : gaDcm.getGaDcmArchivoList()) {
                GaDcm oldDcmArchivoIdDcmOfGaDcmArchivoListGaDcmArchivo = gaDcmArchivoListGaDcmArchivo.getDcmArchivoIdDcm();
                gaDcmArchivoListGaDcmArchivo.setDcmArchivoIdDcm(gaDcm);
                gaDcmArchivoListGaDcmArchivo = em.merge(gaDcmArchivoListGaDcmArchivo);
                if (oldDcmArchivoIdDcmOfGaDcmArchivoListGaDcmArchivo != null) {
                    oldDcmArchivoIdDcmOfGaDcmArchivoListGaDcmArchivo.getGaDcmArchivoList().remove(gaDcmArchivoListGaDcmArchivo);
                    oldDcmArchivoIdDcmOfGaDcmArchivoListGaDcmArchivo = em.merge(oldDcmArchivoIdDcmOfGaDcmArchivoListGaDcmArchivo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGaDcm(gaDcm.getDcmId()) != null) {
                throw new PreexistingEntityException("GaDcm " + gaDcm + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GaDcm gaDcm) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GaDcm persistentGaDcm = em.find(GaDcm.class, gaDcm.getDcmId());
            List<GaDcmArchivo> gaDcmArchivoListOld = persistentGaDcm.getGaDcmArchivoList();
            List<GaDcmArchivo> gaDcmArchivoListNew = gaDcm.getGaDcmArchivoList();
            List<String> illegalOrphanMessages = null;
            for (GaDcmArchivo gaDcmArchivoListOldGaDcmArchivo : gaDcmArchivoListOld) {
                if (!gaDcmArchivoListNew.contains(gaDcmArchivoListOldGaDcmArchivo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GaDcmArchivo " + gaDcmArchivoListOldGaDcmArchivo + " since its dcmArchivoIdDcm field is not nullable.");
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
            gaDcm.setGaDcmArchivoList(gaDcmArchivoListNew);
            gaDcm = em.merge(gaDcm);
            for (GaDcmArchivo gaDcmArchivoListNewGaDcmArchivo : gaDcmArchivoListNew) {
                if (!gaDcmArchivoListOld.contains(gaDcmArchivoListNewGaDcmArchivo)) {
                    GaDcm oldDcmArchivoIdDcmOfGaDcmArchivoListNewGaDcmArchivo = gaDcmArchivoListNewGaDcmArchivo.getDcmArchivoIdDcm();
                    gaDcmArchivoListNewGaDcmArchivo.setDcmArchivoIdDcm(gaDcm);
                    gaDcmArchivoListNewGaDcmArchivo = em.merge(gaDcmArchivoListNewGaDcmArchivo);
                    if (oldDcmArchivoIdDcmOfGaDcmArchivoListNewGaDcmArchivo != null && !oldDcmArchivoIdDcmOfGaDcmArchivoListNewGaDcmArchivo.equals(gaDcm)) {
                        oldDcmArchivoIdDcmOfGaDcmArchivoListNewGaDcmArchivo.getGaDcmArchivoList().remove(gaDcmArchivoListNewGaDcmArchivo);
                        oldDcmArchivoIdDcmOfGaDcmArchivoListNewGaDcmArchivo = em.merge(oldDcmArchivoIdDcmOfGaDcmArchivoListNewGaDcmArchivo);
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
                Long id = gaDcm.getDcmId();
                if (findGaDcm(id) == null) {
                    throw new NonexistentEntityException("The gaDcm with id " + id + " no longer exists.");
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
            GaDcm gaDcm;
            try {
                gaDcm = em.getReference(GaDcm.class, id);
                gaDcm.getDcmId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gaDcm with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GaDcmArchivo> gaDcmArchivoListOrphanCheck = gaDcm.getGaDcmArchivoList();
            for (GaDcmArchivo gaDcmArchivoListOrphanCheckGaDcmArchivo : gaDcmArchivoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GaDcm (" + gaDcm + ") cannot be destroyed since the GaDcmArchivo " + gaDcmArchivoListOrphanCheckGaDcmArchivo + " in its gaDcmArchivoList field has a non-nullable dcmArchivoIdDcm field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gaDcm);
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

    public List<GaDcm> findGaDcmEntities() {
        return findGaDcmEntities(true, -1, -1);
    }

    public List<GaDcm> findGaDcmEntities(int maxResults, int firstResult) {
        return findGaDcmEntities(false, maxResults, firstResult);
    }

    private List<GaDcm> findGaDcmEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GaDcm.class));
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

    public GaDcm findGaDcm(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GaDcm.class, id);
        } finally {
            em.close();
        }
    }

    public int getGaDcmCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GaDcm> rt = cq.from(GaDcm.class);
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
            Logger.getLogger(GaDcmJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

   private static InitialContext getContext() {
        try {
            return new InitialContext();
        } catch (NamingException ex) {
            Logger.getLogger(GaDcmJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static EntityManagerFactory lookUpEmf() {
        return javax.persistence.Persistence.createEntityManagerFactory("GAPU");
    }    
}
