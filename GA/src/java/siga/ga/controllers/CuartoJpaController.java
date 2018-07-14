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
import siga.ga.controllers.exceptions.NonexistentEntityException;
import siga.ga.controllers.exceptions.PreexistingEntityException;
import siga.ga.controllers.exceptions.RollbackFailureException;
import siga.ga.entytis.Cuarto;
import siga.ga.entytis.Servicio;
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
public class CuartoJpaController implements Serializable {

    public CuartoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    @PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;

    public CuartoJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(CuartoJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuarto cuarto) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Servicio idservicio = cuarto.getIdservicio();
            if (idservicio != null) {
                idservicio = em.getReference(idservicio.getClass(), idservicio.getId());
                cuarto.setIdservicio(idservicio);
            }
            em.persist(cuarto);
            if (idservicio != null) {
                idservicio.getCuartoCollection().add(cuarto);
                idservicio = em.merge(idservicio);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCuarto(cuarto.getId()) != null) {
                throw new PreexistingEntityException("Cuarto " + cuarto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuarto cuarto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cuarto persistentCuarto = em.find(Cuarto.class, cuarto.getId());
            Servicio idservicioOld = persistentCuarto.getIdservicio();
            Servicio idservicioNew = cuarto.getIdservicio();
            if (idservicioNew != null) {
                idservicioNew = em.getReference(idservicioNew.getClass(), idservicioNew.getId());
                cuarto.setIdservicio(idservicioNew);
            }
            cuarto = em.merge(cuarto);
            if (idservicioOld != null && !idservicioOld.equals(idservicioNew)) {
                idservicioOld.getCuartoCollection().remove(cuarto);
                idservicioOld = em.merge(idservicioOld);
            }
            if (idservicioNew != null && !idservicioNew.equals(idservicioOld)) {
                idservicioNew.getCuartoCollection().add(cuarto);
                idservicioNew = em.merge(idservicioNew);
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
                Long id = cuarto.getId();
                if (findCuarto(id) == null) {
                    throw new NonexistentEntityException("The cuarto with id " + id + " no longer exists.");
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
            Cuarto cuarto;
            try {
                cuarto = em.getReference(Cuarto.class, id);
                cuarto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuarto with id " + id + " no longer exists.", enfe);
            }
            Servicio idservicio = cuarto.getIdservicio();
            if (idservicio != null) {
                idservicio.getCuartoCollection().remove(cuarto);
                idservicio = em.merge(idservicio);
            }
            em.remove(cuarto);
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

    public List<Cuarto> findCuartoEntities() {
        return findCuartoEntities(true, -1, -1);
    }

    public List<Cuarto> findCuartoEntities(int maxResults, int firstResult) {
        return findCuartoEntities(false, maxResults, firstResult);
    }

    private List<Cuarto> findCuartoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuarto.class));
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

    public Cuarto findCuarto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuarto.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuartoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuarto> rt = cq.from(Cuarto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
