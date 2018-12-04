/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller1;

import Modelo.Doctores;
import Modelo.Historial;
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
public class DoctoresJpaController implements Serializable {

    private EntityManagerFactory emf = null;
    public DoctoresJpaController(){
    this.emf=Persistence.createEntityManagerFactory("ClinicaPU");
    }
            
    public DoctoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Doctores doctores) {
        if (doctores.getHistorialCollection() == null) {
            doctores.setHistorialCollection(new ArrayList<Historial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Historial> attachedHistorialCollection = new ArrayList<Historial>();
            for (Historial historialCollectionHistorialToAttach : doctores.getHistorialCollection()) {
                historialCollectionHistorialToAttach = em.getReference(historialCollectionHistorialToAttach.getClass(), historialCollectionHistorialToAttach.getNopade());
                attachedHistorialCollection.add(historialCollectionHistorialToAttach);
            }
            doctores.setHistorialCollection(attachedHistorialCollection);
            em.persist(doctores);
            for (Historial historialCollectionHistorial : doctores.getHistorialCollection()) {
                Doctores oldCeduladoctorOfHistorialCollectionHistorial = historialCollectionHistorial.getCeduladoctor();
                historialCollectionHistorial.setCeduladoctor(doctores);
                historialCollectionHistorial = em.merge(historialCollectionHistorial);
                if (oldCeduladoctorOfHistorialCollectionHistorial != null) {
                    oldCeduladoctorOfHistorialCollectionHistorial.getHistorialCollection().remove(historialCollectionHistorial);
                    oldCeduladoctorOfHistorialCollectionHistorial = em.merge(oldCeduladoctorOfHistorialCollectionHistorial);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Doctores doctores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Doctores persistentDoctores = em.find(Doctores.class, doctores.getCedula());
            Collection<Historial> historialCollectionOld = persistentDoctores.getHistorialCollection();
            Collection<Historial> historialCollectionNew = doctores.getHistorialCollection();
            List<String> illegalOrphanMessages = null;
            for (Historial historialCollectionOldHistorial : historialCollectionOld) {
                if (!historialCollectionNew.contains(historialCollectionOldHistorial)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historial " + historialCollectionOldHistorial + " since its ceduladoctor field is not nullable.");
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
            doctores.setHistorialCollection(historialCollectionNew);
            doctores = em.merge(doctores);
            for (Historial historialCollectionNewHistorial : historialCollectionNew) {
                if (!historialCollectionOld.contains(historialCollectionNewHistorial)) {
                    Doctores oldCeduladoctorOfHistorialCollectionNewHistorial = historialCollectionNewHistorial.getCeduladoctor();
                    historialCollectionNewHistorial.setCeduladoctor(doctores);
                    historialCollectionNewHistorial = em.merge(historialCollectionNewHistorial);
                    if (oldCeduladoctorOfHistorialCollectionNewHistorial != null && !oldCeduladoctorOfHistorialCollectionNewHistorial.equals(doctores)) {
                        oldCeduladoctorOfHistorialCollectionNewHistorial.getHistorialCollection().remove(historialCollectionNewHistorial);
                        oldCeduladoctorOfHistorialCollectionNewHistorial = em.merge(oldCeduladoctorOfHistorialCollectionNewHistorial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = doctores.getCedula();
                if (findDoctores(id) == null) {
                    throw new NonexistentEntityException("The doctores with id " + id + " no longer exists.");
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
            Doctores doctores;
            try {
                doctores = em.getReference(Doctores.class, id);
                doctores.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The doctores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Historial> historialCollectionOrphanCheck = doctores.getHistorialCollection();
            for (Historial historialCollectionOrphanCheckHistorial : historialCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Doctores (" + doctores + ") cannot be destroyed since the Historial " + historialCollectionOrphanCheckHistorial + " in its historialCollection field has a non-nullable ceduladoctor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(doctores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Doctores> findDoctoresEntities() {
        return findDoctoresEntities(true, -1, -1);
    }

    public List<Doctores> findDoctoresEntities(int maxResults, int firstResult) {
        return findDoctoresEntities(false, maxResults, firstResult);
    }

    private List<Doctores> findDoctoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Doctores.class));
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

    public Doctores findDoctores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Doctores.class, id);
        } finally {
            em.close();
        }
    }

    public int getDoctoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Doctores> rt = cq.from(Doctores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
