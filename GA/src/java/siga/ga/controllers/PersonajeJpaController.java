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
import siga.ga.entytis.Contrato;
import java.util.ArrayList;
import java.util.Collection;
import siga.ga.entytis.Personaje;
import javax.persistence.PersistenceUnit;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ParameterExpression;
import siga.ga.dao.FBUser;
import siga.ga.jsfclasses.util.JsfUtil;

/**
 *
 * @author Otros
 */
public class PersonajeJpaController implements Serializable {

    public static Personaje finByFbId(String id) {
        PersonajeJpaController personaController = new PersonajeJpaController();

        Personaje p = personaController.findPersonajeByFbId(id);
        FBUser fetchObject = JsfUtil.obtenerFBUser();

            System.out.println("JsfUtil validarSesion :: usuario activo " + p.getId() + " " + fetchObject.getName() + " CC: " + fetchObject.getCedula() + " TEL: " + fetchObject.getTelefono());

            if (p.getId() == null) {
                
                p = new Personaje(0L, fetchObject.getId(), fetchObject.getName(), fetchObject.getCedula(), 5, fetchObject.getTelefono());
                System.out.println("JsfUtil validar Session :: p recreate " + p.toString());
                
                if ("".equals(p.getCedula()) || "".equals(p.getTelefono())) {
                System.out.println("PersonajeJpaController finByFbId persona :: actualizar cedula telefono");    
                } else {
                try {
                    personaController.create(p);
                } catch (PreexistingEntityException ex) {
                    Logger.getLogger(PersonajeJpaController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RollbackFailureException ex) {
                    Logger.getLogger(PersonajeJpaController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(PersonajeJpaController.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                }
            }
            return p;
    }

    public PersonajeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    @PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;

    public PersonajeJpaController() {
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(PersonajeJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personaje personaje) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (personaje.getContratoCollection() == null) {
            personaje.setContratoCollection(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Contrato> attachedContratoCollection = new ArrayList<Contrato>();
            for (Contrato contratoCollectionContratoToAttach : personaje.getContratoCollection()) {
                contratoCollectionContratoToAttach = em.getReference(contratoCollectionContratoToAttach.getClass(), contratoCollectionContratoToAttach.getId());
                attachedContratoCollection.add(contratoCollectionContratoToAttach);
            }
            personaje.setContratoCollection(attachedContratoCollection);
            em.persist(personaje);
            for (Contrato contratoCollectionContrato : personaje.getContratoCollection()) {
                Personaje oldIdarrendadorOfContratoCollectionContrato = contratoCollectionContrato.getIdarrendador();
                contratoCollectionContrato.setIdarrendador(personaje);
                contratoCollectionContrato = em.merge(contratoCollectionContrato);
                if (oldIdarrendadorOfContratoCollectionContrato != null) {
                    oldIdarrendadorOfContratoCollectionContrato.getContratoCollection().remove(contratoCollectionContrato);
                    oldIdarrendadorOfContratoCollectionContrato = em.merge(oldIdarrendadorOfContratoCollectionContrato);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPersonaje(personaje.getId()) != null) {
                throw new PreexistingEntityException("Personaje " + personaje + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personaje personaje) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Personaje persistentPersonaje = em.find(Personaje.class, personaje.getId());
            Collection<Contrato> contratoCollectionOld = persistentPersonaje.getContratoCollection();
            Collection<Contrato> contratoCollectionNew = personaje.getContratoCollection();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoCollectionOldContrato : contratoCollectionOld) {
                if (!contratoCollectionNew.contains(contratoCollectionOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contrato " + contratoCollectionOldContrato + " since its idarrendador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Contrato> attachedContratoCollectionNew = new ArrayList<Contrato>();
            for (Contrato contratoCollectionNewContratoToAttach : contratoCollectionNew) {
                contratoCollectionNewContratoToAttach = em.getReference(contratoCollectionNewContratoToAttach.getClass(), contratoCollectionNewContratoToAttach.getId());
                attachedContratoCollectionNew.add(contratoCollectionNewContratoToAttach);
            }
            contratoCollectionNew = attachedContratoCollectionNew;
            personaje.setContratoCollection(contratoCollectionNew);
            personaje = em.merge(personaje);
            for (Contrato contratoCollectionNewContrato : contratoCollectionNew) {
                if (!contratoCollectionOld.contains(contratoCollectionNewContrato)) {
                    Personaje oldIdarrendadorOfContratoCollectionNewContrato = contratoCollectionNewContrato.getIdarrendador();
                    contratoCollectionNewContrato.setIdarrendador(personaje);
                    contratoCollectionNewContrato = em.merge(contratoCollectionNewContrato);
                    if (oldIdarrendadorOfContratoCollectionNewContrato != null && !oldIdarrendadorOfContratoCollectionNewContrato.equals(personaje)) {
                        oldIdarrendadorOfContratoCollectionNewContrato.getContratoCollection().remove(contratoCollectionNewContrato);
                        oldIdarrendadorOfContratoCollectionNewContrato = em.merge(oldIdarrendadorOfContratoCollectionNewContrato);
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
                Long id = personaje.getId();
                if (findPersonaje(id) == null) {
                    throw new NonexistentEntityException("The personaje with id " + id + " no longer exists.");
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
            Personaje personaje;
            try {
                personaje = em.getReference(Personaje.class, id);
                personaje.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personaje with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contrato> contratoCollectionOrphanCheck = personaje.getContratoCollection();
            for (Contrato contratoCollectionOrphanCheckContrato : contratoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personaje (" + personaje + ") cannot be destroyed since the Contrato " + contratoCollectionOrphanCheckContrato + " in its contratoCollection field has a non-nullable idarrendador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(personaje);
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

    public List<Personaje> findPersonajeEntities() {
        return findPersonajeEntities(true, -1, -1);
    }

    public List<Personaje> findPersonajeEntities(int maxResults, int firstResult) {
        return findPersonajeEntities(false, maxResults, firstResult);
    }

    private List<Personaje> findPersonajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personaje.class));
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

    public Personaje findPersonajeByFbId(String idFb) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Personaje> criteriaQuery = cb.createQuery(Personaje.class);
            Root<Personaje> personaje = criteriaQuery.from(Personaje.class);
            ParameterExpression<String> nameParameter = cb.parameter(String.class, "fbid");
            criteriaQuery.select(personaje).where(cb.equal(personaje.get("fbid"), nameParameter));
            return em.createQuery(criteriaQuery).setParameter("fbid", idFb).getSingleResult();

        } catch(Exception e){
            System.out.println("PersonajeJpaController findPersonajeByFbId :: no registro interno");
        }finally {
            em.close();
        }
        return new Personaje();
    }

    public Personaje findPersonaje(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personaje> rt = cq.from(Personaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
