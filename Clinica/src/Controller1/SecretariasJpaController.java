/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller1;

import Modelo.Historial;
import Modelo.Secretarias;
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
public class SecretariasJpaController implements Serializable {

    private EntityManagerFactory emf = null;
    public SecretariasJpaController(){
    this.emf=Persistence.createEntityManagerFactory("ClinicaPU");
    }
    
    public SecretariasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Secretarias secretarias) {
        if (secretarias.getHistorialCollection() == null) {
            secretarias.setHistorialCollection(new ArrayList<Historial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Historial> attachedHistorialCollection = new ArrayList<Historial>();
            for (Historial historialCollectionHistorialToAttach : secretarias.getHistorialCollection()) {
                historialCollectionHistorialToAttach = em.getReference(historialCollectionHistorialToAttach.getClass(), historialCollectionHistorialToAttach.getNopade());
                attachedHistorialCollection.add(historialCollectionHistorialToAttach);
            }
            secretarias.setHistorialCollection(attachedHistorialCollection);
            em.persist(secretarias);
            for (Historial historialCollectionHistorial : secretarias.getHistorialCollection()) {
                Secretarias oldIdsecretariaOfHistorialCollectionHistorial = historialCollectionHistorial.getIdsecretaria();
                historialCollectionHistorial.setIdsecretaria(secretarias);
                historialCollectionHistorial = em.merge(historialCollectionHistorial);
                if (oldIdsecretariaOfHistorialCollectionHistorial != null) {
                    oldIdsecretariaOfHistorialCollectionHistorial.getHistorialCollection().remove(historialCollectionHistorial);
                    oldIdsecretariaOfHistorialCollectionHistorial = em.merge(oldIdsecretariaOfHistorialCollectionHistorial);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Secretarias secretarias) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Secretarias persistentSecretarias = em.find(Secretarias.class, secretarias.getIdsec());
            Collection<Historial> historialCollectionOld = persistentSecretarias.getHistorialCollection();
            Collection<Historial> historialCollectionNew = secretarias.getHistorialCollection();
            List<String> illegalOrphanMessages = null;
            for (Historial historialCollectionOldHistorial : historialCollectionOld) {
                if (!historialCollectionNew.contains(historialCollectionOldHistorial)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historial " + historialCollectionOldHistorial + " since its idsecretaria field is not nullable.");
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
            secretarias.setHistorialCollection(historialCollectionNew);
            secretarias = em.merge(secretarias);
            for (Historial historialCollectionNewHistorial : historialCollectionNew) {
                if (!historialCollectionOld.contains(historialCollectionNewHistorial)) {
                    Secretarias oldIdsecretariaOfHistorialCollectionNewHistorial = historialCollectionNewHistorial.getIdsecretaria();
                    historialCollectionNewHistorial.setIdsecretaria(secretarias);
                    historialCollectionNewHistorial = em.merge(historialCollectionNewHistorial);
                    if (oldIdsecretariaOfHistorialCollectionNewHistorial != null && !oldIdsecretariaOfHistorialCollectionNewHistorial.equals(secretarias)) {
                        oldIdsecretariaOfHistorialCollectionNewHistorial.getHistorialCollection().remove(historialCollectionNewHistorial);
                        oldIdsecretariaOfHistorialCollectionNewHistorial = em.merge(oldIdsecretariaOfHistorialCollectionNewHistorial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = secretarias.getIdsec();
                if (findSecretarias(id) == null) {
                    throw new NonexistentEntityException("The secretarias with id " + id + " no longer exists.");
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
            Secretarias secretarias;
            try {
                secretarias = em.getReference(Secretarias.class, id);
                secretarias.getIdsec();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The secretarias with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Historial> historialCollectionOrphanCheck = secretarias.getHistorialCollection();
            for (Historial historialCollectionOrphanCheckHistorial : historialCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Secretarias (" + secretarias + ") cannot be destroyed since the Historial " + historialCollectionOrphanCheckHistorial + " in its historialCollection field has a non-nullable idsecretaria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(secretarias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Secretarias> findSecretariasEntities() {
        return findSecretariasEntities(true, -1, -1);
    }

    public List<Secretarias> findSecretariasEntities(int maxResults, int firstResult) {
        return findSecretariasEntities(false, maxResults, firstResult);
    }

    private List<Secretarias> findSecretariasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Secretarias.class));
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

    public Secretarias findSecretarias(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Secretarias.class, id);
        } finally {
            em.close();
        }
    }

    public int getSecretariasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Secretarias> rt = cq.from(Secretarias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
