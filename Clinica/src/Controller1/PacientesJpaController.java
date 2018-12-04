/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller1;

import Modelo.Historial;
import Modelo.Pacientes;
import Controller.exceptions.IllegalOrphanException;
import Controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Marisol
 */
public class PacientesJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public PacientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public PacientesJpaController(){
    this.emf=Persistence.createEntityManagerFactory("ClinicaPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pacientes pacientes) {
        if (pacientes.getHistorialCollection() == null) {
            pacientes.setHistorialCollection(new ArrayList<Historial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Historial> attachedHistorialCollection = new ArrayList<Historial>();
            for (Historial historialCollectionHistorialToAttach : pacientes.getHistorialCollection()) {
                historialCollectionHistorialToAttach = em.getReference(historialCollectionHistorialToAttach.getClass(), historialCollectionHistorialToAttach.getNopade());
                attachedHistorialCollection.add(historialCollectionHistorialToAttach);
            }
            pacientes.setHistorialCollection(attachedHistorialCollection);
            em.persist(pacientes);
            for (Historial historialCollectionHistorial : pacientes.getHistorialCollection()) {
                Pacientes oldIdpacienteOfHistorialCollectionHistorial = historialCollectionHistorial.getIdpaciente();
                historialCollectionHistorial.setIdpaciente(pacientes);
                historialCollectionHistorial = em.merge(historialCollectionHistorial);
                if (oldIdpacienteOfHistorialCollectionHistorial != null) {
                    oldIdpacienteOfHistorialCollectionHistorial.getHistorialCollection().remove(historialCollectionHistorial);
                    oldIdpacienteOfHistorialCollectionHistorial = em.merge(oldIdpacienteOfHistorialCollectionHistorial);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pacientes pacientes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes persistentPacientes = em.find(Pacientes.class, pacientes.getId());
            Collection<Historial> historialCollectionOld = persistentPacientes.getHistorialCollection();
            Collection<Historial> historialCollectionNew = pacientes.getHistorialCollection();
            List<String> illegalOrphanMessages = null;
            for (Historial historialCollectionOldHistorial : historialCollectionOld) {
                if (!historialCollectionNew.contains(historialCollectionOldHistorial)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historial " + historialCollectionOldHistorial + " since its idpaciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Historial> attachedHistorialCollectionNew = new ArrayList<Historial>();
            for (Historial historialCollectionNewHistorialToAttach : historialCollectionNew) {
                historialCollectionNewHistorialToAttach = em.getReference(historialCollectionNewHistorialToAttach.getClass(), historialCollectionNewHistorialToAttach.getNopade());
                attachedHistorialCollectionNew.add(historialCollectionNewHistorialToAttach);
            }
            historialCollectionNew = attachedHistorialCollectionNew;
            pacientes.setHistorialCollection(historialCollectionNew);
            pacientes = em.merge(pacientes);
            for (Historial historialCollectionNewHistorial : historialCollectionNew) {
                if (!historialCollectionOld.contains(historialCollectionNewHistorial)) {
                    Pacientes oldIdpacienteOfHistorialCollectionNewHistorial = historialCollectionNewHistorial.getIdpaciente();
                    historialCollectionNewHistorial.setIdpaciente(pacientes);
                    historialCollectionNewHistorial = em.merge(historialCollectionNewHistorial);
                    if (oldIdpacienteOfHistorialCollectionNewHistorial != null && !oldIdpacienteOfHistorialCollectionNewHistorial.equals(pacientes)) {
                        oldIdpacienteOfHistorialCollectionNewHistorial.getHistorialCollection().remove(historialCollectionNewHistorial);
                        oldIdpacienteOfHistorialCollectionNewHistorial = em.merge(oldIdpacienteOfHistorialCollectionNewHistorial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pacientes.getId();
                if (findPacientes(id) == null) {
                    throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes pacientes;
            try {
                pacientes = em.getReference(Pacientes.class, id);
                pacientes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Historial> historialCollectionOrphanCheck = pacientes.getHistorialCollection();
            for (Historial historialCollectionOrphanCheckHistorial : historialCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pacientes (" + pacientes + ") cannot be destroyed since the Historial " + historialCollectionOrphanCheckHistorial + " in its historialCollection field has a non-nullable idpaciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pacientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pacientes> findPacientesEntities() {
        return findPacientesEntities(true, -1, -1);
    }

    public List<Pacientes> findPacientesEntities(int maxResults, int firstResult) {
        return findPacientesEntities(false, maxResults, firstResult);
    }

    private List<Pacientes> findPacientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pacientes.class));
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

    public Pacientes findPacientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pacientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pacientes> rt = cq.from(Pacientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
