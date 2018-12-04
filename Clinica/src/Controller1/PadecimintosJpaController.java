/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller1;

import Modelo.Historial;
import Modelo.Padecimintos;
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
public class PadecimintosJpaController implements Serializable {

     private EntityManagerFactory emf = null;
    public PadecimintosJpaController(){
    this.emf=Persistence.createEntityManagerFactory("ClinicaPU");
    }
    public PadecimintosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
   

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Padecimintos padecimintos) {
        if (padecimintos.getHistorialCollection() == null) {
            padecimintos.setHistorialCollection(new ArrayList<Historial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Historial> attachedHistorialCollection = new ArrayList<Historial>();
            for (Historial historialCollectionHistorialToAttach : padecimintos.getHistorialCollection()) {
                historialCollectionHistorialToAttach = em.getReference(historialCollectionHistorialToAttach.getClass(), historialCollectionHistorialToAttach.getNopade());
                attachedHistorialCollection.add(historialCollectionHistorialToAttach);
            }
            padecimintos.setHistorialCollection(attachedHistorialCollection);
            em.persist(padecimintos);
            for (Historial historialCollectionHistorial : padecimintos.getHistorialCollection()) {
                Padecimintos oldNodepadecimientoOfHistorialCollectionHistorial = historialCollectionHistorial.getNodepadecimiento();
                historialCollectionHistorial.setNodepadecimiento(padecimintos);
                historialCollectionHistorial = em.merge(historialCollectionHistorial);
                if (oldNodepadecimientoOfHistorialCollectionHistorial != null) {
                    oldNodepadecimientoOfHistorialCollectionHistorial.getHistorialCollection().remove(historialCollectionHistorial);
                    oldNodepadecimientoOfHistorialCollectionHistorial = em.merge(oldNodepadecimientoOfHistorialCollectionHistorial);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Padecimintos padecimintos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Padecimintos persistentPadecimintos = em.find(Padecimintos.class, padecimintos.getPadeci());
            Collection<Historial> historialCollectionOld = persistentPadecimintos.getHistorialCollection();
            Collection<Historial> historialCollectionNew = padecimintos.getHistorialCollection();
            List<String> illegalOrphanMessages = null;
            for (Historial historialCollectionOldHistorial : historialCollectionOld) {
                if (!historialCollectionNew.contains(historialCollectionOldHistorial)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historial " + historialCollectionOldHistorial + " since its nodepadecimiento field is not nullable.");
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
            padecimintos.setHistorialCollection(historialCollectionNew);
            padecimintos = em.merge(padecimintos);
            for (Historial historialCollectionNewHistorial : historialCollectionNew) {
                if (!historialCollectionOld.contains(historialCollectionNewHistorial)) {
                    Padecimintos oldNodepadecimientoOfHistorialCollectionNewHistorial = historialCollectionNewHistorial.getNodepadecimiento();
                    historialCollectionNewHistorial.setNodepadecimiento(padecimintos);
                    historialCollectionNewHistorial = em.merge(historialCollectionNewHistorial);
                    if (oldNodepadecimientoOfHistorialCollectionNewHistorial != null && !oldNodepadecimientoOfHistorialCollectionNewHistorial.equals(padecimintos)) {
                        oldNodepadecimientoOfHistorialCollectionNewHistorial.getHistorialCollection().remove(historialCollectionNewHistorial);
                        oldNodepadecimientoOfHistorialCollectionNewHistorial = em.merge(oldNodepadecimientoOfHistorialCollectionNewHistorial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = padecimintos.getPadeci();
                if (findPadecimintos(id) == null) {
                    throw new NonexistentEntityException("The padecimintos with id " + id + " no longer exists.");
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
            Padecimintos padecimintos;
            try {
                padecimintos = em.getReference(Padecimintos.class, id);
                padecimintos.getPadeci();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The padecimintos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Historial> historialCollectionOrphanCheck = padecimintos.getHistorialCollection();
            for (Historial historialCollectionOrphanCheckHistorial : historialCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Padecimintos (" + padecimintos + ") cannot be destroyed since the Historial " + historialCollectionOrphanCheckHistorial + " in its historialCollection field has a non-nullable nodepadecimiento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(padecimintos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Padecimintos> findPadecimintosEntities() {
        return findPadecimintosEntities(true, -1, -1);
    }

    public List<Padecimintos> findPadecimintosEntities(int maxResults, int firstResult) {
        return findPadecimintosEntities(false, maxResults, firstResult);
    }

    private List<Padecimintos> findPadecimintosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Padecimintos.class));
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

    public Padecimintos findPadecimintos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Padecimintos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPadecimintosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Padecimintos> rt = cq.from(Padecimintos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
