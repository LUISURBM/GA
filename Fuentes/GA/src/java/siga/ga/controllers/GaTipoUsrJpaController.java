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
import siga.ga.entytis.GaTipoUsr;
import siga.ga.entytis.GaUsr;
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
public class GaTipoUsrJpaController implements Serializable {

    public GaTipoUsrJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
@PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;

    public GaTipoUsrJpaController() {

            this.utx = lookUpUtx();

            this.emf = lookUpEmf();
    }
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GaTipoUsr gaTipoUsr) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (gaTipoUsr.getGaUsrList() == null) {
            gaTipoUsr.setGaUsrList(new ArrayList<GaUsr>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<GaUsr> attachedGaUsrList = new ArrayList<GaUsr>();
            for (GaUsr gaUsrListGaUsrToAttach : gaTipoUsr.getGaUsrList()) {
                gaUsrListGaUsrToAttach = em.getReference(gaUsrListGaUsrToAttach.getClass(), gaUsrListGaUsrToAttach.getUsrId());
                attachedGaUsrList.add(gaUsrListGaUsrToAttach);
            }
            gaTipoUsr.setGaUsrList(attachedGaUsrList);
            em.persist(gaTipoUsr);
            for (GaUsr gaUsrListGaUsr : gaTipoUsr.getGaUsrList()) {
                GaTipoUsr oldUsrTipoUsrOfGaUsrListGaUsr = gaUsrListGaUsr.getUsrTipoUsr();
                gaUsrListGaUsr.setUsrTipoUsr(gaTipoUsr);
                gaUsrListGaUsr = em.merge(gaUsrListGaUsr);
                if (oldUsrTipoUsrOfGaUsrListGaUsr != null) {
                    oldUsrTipoUsrOfGaUsrListGaUsr.getGaUsrList().remove(gaUsrListGaUsr);
                    oldUsrTipoUsrOfGaUsrListGaUsr = em.merge(oldUsrTipoUsrOfGaUsrListGaUsr);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGaTipoUsr(gaTipoUsr.getTipoUsrId()) != null) {
                throw new PreexistingEntityException("GaTipoUsr " + gaTipoUsr + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GaTipoUsr gaTipoUsr) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GaTipoUsr persistentGaTipoUsr = em.find(GaTipoUsr.class, gaTipoUsr.getTipoUsrId());
            List<GaUsr> gaUsrListOld = persistentGaTipoUsr.getGaUsrList();
            List<GaUsr> gaUsrListNew = gaTipoUsr.getGaUsrList();
            List<String> illegalOrphanMessages = null;
            for (GaUsr gaUsrListOldGaUsr : gaUsrListOld) {
                if (!gaUsrListNew.contains(gaUsrListOldGaUsr)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GaUsr " + gaUsrListOldGaUsr + " since its usrTipoUsr field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GaUsr> attachedGaUsrListNew = new ArrayList<GaUsr>();
            for (GaUsr gaUsrListNewGaUsrToAttach : gaUsrListNew) {
                gaUsrListNewGaUsrToAttach = em.getReference(gaUsrListNewGaUsrToAttach.getClass(), gaUsrListNewGaUsrToAttach.getUsrId());
                attachedGaUsrListNew.add(gaUsrListNewGaUsrToAttach);
            }
            gaUsrListNew = attachedGaUsrListNew;
            gaTipoUsr.setGaUsrList(gaUsrListNew);
            gaTipoUsr = em.merge(gaTipoUsr);
            for (GaUsr gaUsrListNewGaUsr : gaUsrListNew) {
                if (!gaUsrListOld.contains(gaUsrListNewGaUsr)) {
                    GaTipoUsr oldUsrTipoUsrOfGaUsrListNewGaUsr = gaUsrListNewGaUsr.getUsrTipoUsr();
                    gaUsrListNewGaUsr.setUsrTipoUsr(gaTipoUsr);
                    gaUsrListNewGaUsr = em.merge(gaUsrListNewGaUsr);
                    if (oldUsrTipoUsrOfGaUsrListNewGaUsr != null && !oldUsrTipoUsrOfGaUsrListNewGaUsr.equals(gaTipoUsr)) {
                        oldUsrTipoUsrOfGaUsrListNewGaUsr.getGaUsrList().remove(gaUsrListNewGaUsr);
                        oldUsrTipoUsrOfGaUsrListNewGaUsr = em.merge(oldUsrTipoUsrOfGaUsrListNewGaUsr);
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
                Long id = gaTipoUsr.getTipoUsrId();
                if (findGaTipoUsr(id) == null) {
                    throw new NonexistentEntityException("The gaTipoUsr with id " + id + " no longer exists.");
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
            GaTipoUsr gaTipoUsr;
            try {
                gaTipoUsr = em.getReference(GaTipoUsr.class, id);
                gaTipoUsr.getTipoUsrId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gaTipoUsr with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GaUsr> gaUsrListOrphanCheck = gaTipoUsr.getGaUsrList();
            for (GaUsr gaUsrListOrphanCheckGaUsr : gaUsrListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GaTipoUsr (" + gaTipoUsr + ") cannot be destroyed since the GaUsr " + gaUsrListOrphanCheckGaUsr + " in its gaUsrList field has a non-nullable usrTipoUsr field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gaTipoUsr);
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

    public List<GaTipoUsr> findGaTipoUsrEntities() {
        return findGaTipoUsrEntities(true, -1, -1);
    }

    public List<GaTipoUsr> findGaTipoUsrEntities(int maxResults, int firstResult) {
        return findGaTipoUsrEntities(false, maxResults, firstResult);
    }

    private List<GaTipoUsr> findGaTipoUsrEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GaTipoUsr.class));
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

    public GaTipoUsr findGaTipoUsr(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GaTipoUsr.class, id);
        } finally {
            em.close();
        }
    }

    public int getGaTipoUsrCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GaTipoUsr> rt = cq.from(GaTipoUsr.class);
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
            Logger.getLogger(GaTipoUsrJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

   private static InitialContext getContext() {
        try {
            return new InitialContext();
        } catch (NamingException ex) {
            Logger.getLogger(GaTipoUsrJpaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static EntityManagerFactory lookUpEmf() {
        return javax.persistence.Persistence.createEntityManagerFactory("GAPU");
    }
}
