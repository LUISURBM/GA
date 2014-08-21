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
import java.util.ArrayList;
import java.util.Collection;
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
public class ServicioJpaController implements Serializable {

    public ServicioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    
     @PersistenceUnit(unitName="GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    public ServicioJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(ServicioJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Servicio servicio) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (servicio.getAcuerdoCollection() == null) {
            servicio.setAcuerdoCollection(new ArrayList<Acuerdo>());
        }
        if (servicio.getCuartoCollection() == null) {
            servicio.setCuartoCollection(new ArrayList<Cuarto>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Acuerdo> attachedAcuerdoCollection = new ArrayList<Acuerdo>();
            for (Acuerdo acuerdoCollectionAcuerdoToAttach : servicio.getAcuerdoCollection()) {
                acuerdoCollectionAcuerdoToAttach = em.getReference(acuerdoCollectionAcuerdoToAttach.getClass(), acuerdoCollectionAcuerdoToAttach.getId());
                attachedAcuerdoCollection.add(acuerdoCollectionAcuerdoToAttach);
            }
            servicio.setAcuerdoCollection(attachedAcuerdoCollection);
            Collection<Cuarto> attachedCuartoCollection = new ArrayList<Cuarto>();
            for (Cuarto cuartoCollectionCuartoToAttach : servicio.getCuartoCollection()) {
                cuartoCollectionCuartoToAttach = em.getReference(cuartoCollectionCuartoToAttach.getClass(), cuartoCollectionCuartoToAttach.getId());
                attachedCuartoCollection.add(cuartoCollectionCuartoToAttach);
            }
            servicio.setCuartoCollection(attachedCuartoCollection);
            em.persist(servicio);
            for (Acuerdo acuerdoCollectionAcuerdo : servicio.getAcuerdoCollection()) {
                Servicio oldIdclausulaOfAcuerdoCollectionAcuerdo = acuerdoCollectionAcuerdo.getIdclausula();
                acuerdoCollectionAcuerdo.setIdclausula(servicio);
                acuerdoCollectionAcuerdo = em.merge(acuerdoCollectionAcuerdo);
                if (oldIdclausulaOfAcuerdoCollectionAcuerdo != null) {
                    oldIdclausulaOfAcuerdoCollectionAcuerdo.getAcuerdoCollection().remove(acuerdoCollectionAcuerdo);
                    oldIdclausulaOfAcuerdoCollectionAcuerdo = em.merge(oldIdclausulaOfAcuerdoCollectionAcuerdo);
                }
            }
            for (Cuarto cuartoCollectionCuarto : servicio.getCuartoCollection()) {
                Servicio oldIdservicioOfCuartoCollectionCuarto = cuartoCollectionCuarto.getIdservicio();
                cuartoCollectionCuarto.setIdservicio(servicio);
                cuartoCollectionCuarto = em.merge(cuartoCollectionCuarto);
                if (oldIdservicioOfCuartoCollectionCuarto != null) {
                    oldIdservicioOfCuartoCollectionCuarto.getCuartoCollection().remove(cuartoCollectionCuarto);
                    oldIdservicioOfCuartoCollectionCuarto = em.merge(oldIdservicioOfCuartoCollectionCuarto);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findServicio(servicio.getId()) != null) {
                throw new PreexistingEntityException("Servicio " + servicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Servicio servicio) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Servicio persistentServicio = em.find(Servicio.class, servicio.getId());
            Collection<Acuerdo> acuerdoCollectionOld = persistentServicio.getAcuerdoCollection();
            Collection<Acuerdo> acuerdoCollectionNew = servicio.getAcuerdoCollection();
            Collection<Cuarto> cuartoCollectionOld = persistentServicio.getCuartoCollection();
            Collection<Cuarto> cuartoCollectionNew = servicio.getCuartoCollection();
            List<String> illegalOrphanMessages = null;
            for (Acuerdo acuerdoCollectionOldAcuerdo : acuerdoCollectionOld) {
                if (!acuerdoCollectionNew.contains(acuerdoCollectionOldAcuerdo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Acuerdo " + acuerdoCollectionOldAcuerdo + " since its idclausula field is not nullable.");
                }
            }
            for (Cuarto cuartoCollectionOldCuarto : cuartoCollectionOld) {
                if (!cuartoCollectionNew.contains(cuartoCollectionOldCuarto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cuarto " + cuartoCollectionOldCuarto + " since its idservicio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Acuerdo> attachedAcuerdoCollectionNew = new ArrayList<Acuerdo>();
            for (Acuerdo acuerdoCollectionNewAcuerdoToAttach : acuerdoCollectionNew) {
                acuerdoCollectionNewAcuerdoToAttach = em.getReference(acuerdoCollectionNewAcuerdoToAttach.getClass(), acuerdoCollectionNewAcuerdoToAttach.getId());
                attachedAcuerdoCollectionNew.add(acuerdoCollectionNewAcuerdoToAttach);
            }
            acuerdoCollectionNew = attachedAcuerdoCollectionNew;
            servicio.setAcuerdoCollection(acuerdoCollectionNew);
            Collection<Cuarto> attachedCuartoCollectionNew = new ArrayList<Cuarto>();
            for (Cuarto cuartoCollectionNewCuartoToAttach : cuartoCollectionNew) {
                cuartoCollectionNewCuartoToAttach = em.getReference(cuartoCollectionNewCuartoToAttach.getClass(), cuartoCollectionNewCuartoToAttach.getId());
                attachedCuartoCollectionNew.add(cuartoCollectionNewCuartoToAttach);
            }
            cuartoCollectionNew = attachedCuartoCollectionNew;
            servicio.setCuartoCollection(cuartoCollectionNew);
            servicio = em.merge(servicio);
            for (Acuerdo acuerdoCollectionNewAcuerdo : acuerdoCollectionNew) {
                if (!acuerdoCollectionOld.contains(acuerdoCollectionNewAcuerdo)) {
                    Servicio oldIdclausulaOfAcuerdoCollectionNewAcuerdo = acuerdoCollectionNewAcuerdo.getIdclausula();
                    acuerdoCollectionNewAcuerdo.setIdclausula(servicio);
                    acuerdoCollectionNewAcuerdo = em.merge(acuerdoCollectionNewAcuerdo);
                    if (oldIdclausulaOfAcuerdoCollectionNewAcuerdo != null && !oldIdclausulaOfAcuerdoCollectionNewAcuerdo.equals(servicio)) {
                        oldIdclausulaOfAcuerdoCollectionNewAcuerdo.getAcuerdoCollection().remove(acuerdoCollectionNewAcuerdo);
                        oldIdclausulaOfAcuerdoCollectionNewAcuerdo = em.merge(oldIdclausulaOfAcuerdoCollectionNewAcuerdo);
                    }
                }
            }
            for (Cuarto cuartoCollectionNewCuarto : cuartoCollectionNew) {
                if (!cuartoCollectionOld.contains(cuartoCollectionNewCuarto)) {
                    Servicio oldIdservicioOfCuartoCollectionNewCuarto = cuartoCollectionNewCuarto.getIdservicio();
                    cuartoCollectionNewCuarto.setIdservicio(servicio);
                    cuartoCollectionNewCuarto = em.merge(cuartoCollectionNewCuarto);
                    if (oldIdservicioOfCuartoCollectionNewCuarto != null && !oldIdservicioOfCuartoCollectionNewCuarto.equals(servicio)) {
                        oldIdservicioOfCuartoCollectionNewCuarto.getCuartoCollection().remove(cuartoCollectionNewCuarto);
                        oldIdservicioOfCuartoCollectionNewCuarto = em.merge(oldIdservicioOfCuartoCollectionNewCuarto);
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
                Long id = servicio.getId();
                if (findServicio(id) == null) {
                    throw new NonexistentEntityException("The servicio with id " + id + " no longer exists.");
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
            Servicio servicio;
            try {
                servicio = em.getReference(Servicio.class, id);
                servicio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Acuerdo> acuerdoCollectionOrphanCheck = servicio.getAcuerdoCollection();
            for (Acuerdo acuerdoCollectionOrphanCheckAcuerdo : acuerdoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Servicio (" + servicio + ") cannot be destroyed since the Acuerdo " + acuerdoCollectionOrphanCheckAcuerdo + " in its acuerdoCollection field has a non-nullable idclausula field.");
            }
            Collection<Cuarto> cuartoCollectionOrphanCheck = servicio.getCuartoCollection();
            for (Cuarto cuartoCollectionOrphanCheckCuarto : cuartoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Servicio (" + servicio + ") cannot be destroyed since the Cuarto " + cuartoCollectionOrphanCheckCuarto + " in its cuartoCollection field has a non-nullable idservicio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(servicio);
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

    public List<Servicio> findServicioEntities() {
        return findServicioEntities(true, -1, -1);
    }

    public List<Servicio> findServicioEntities(int maxResults, int firstResult) {
        return findServicioEntities(false, maxResults, firstResult);
    }

    private List<Servicio> findServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicio.class));
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

    public Servicio findServicio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servicio> rt = cq.from(Servicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
