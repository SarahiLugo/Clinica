/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller1;

import Modelo.Doctores;
import Modelo.Historial;
import Modelo.Pacientes;
import Modelo.Padecimintos;
import Modelo.Secretarias;
import Controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Marisol
 */
public class HistorialJpaController implements Serializable {
private EntityManagerFactory emf = null;
    public HistorialJpaController(){
    this.emf=Persistence.createEntityManagerFactory("ClinicaPU");
    }
    public HistorialJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historial historial) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Doctores ceduladoctor = historial.getCeduladoctor();
            if (ceduladoctor != null) {
                ceduladoctor = em.getReference(ceduladoctor.getClass(), ceduladoctor.getCedula());
                historial.setCeduladoctor(ceduladoctor);
            }
            Pacientes idpaciente = historial.getIdpaciente();
            if (idpaciente != null) {
                idpaciente = em.getReference(idpaciente.getClass(), idpaciente.getId());
                historial.setIdpaciente(idpaciente);
            }
            Padecimintos nodepadecimiento = historial.getNodepadecimiento();
            if (nodepadecimiento != null) {
                nodepadecimiento = em.getReference(nodepadecimiento.getClass(), nodepadecimiento.getPadeci());
                historial.setNodepadecimiento(nodepadecimiento);
            }
            Secretarias idsecretaria = historial.getIdsecretaria();
            if (idsecretaria != null) {
                idsecretaria = em.getReference(idsecretaria.getClass(), idsecretaria.getIdsec());
                historial.setIdsecretaria(idsecretaria);
            }
            em.persist(historial);
            if (ceduladoctor != null) {
                ceduladoctor.getHistorialCollection().add(historial);
                ceduladoctor = em.merge(ceduladoctor);
            }
            if (idpaciente != null) {
                idpaciente.getHistorialCollection().add(historial);
                idpaciente = em.merge(idpaciente);
            }
            if (nodepadecimiento != null) {
                nodepadecimiento.getHistorialCollection().add(historial);
                nodepadecimiento = em.merge(nodepadecimiento);
            }
            if (idsecretaria != null) {
                idsecretaria.getHistorialCollection().add(historial);
                idsecretaria = em.merge(idsecretaria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Historial historial) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historial persistentHistorial = em.find(Historial.class, historial.getNopade());
            Doctores ceduladoctorOld = persistentHistorial.getCeduladoctor();
            Doctores ceduladoctorNew = historial.getCeduladoctor();
            Pacientes idpacienteOld = persistentHistorial.getIdpaciente();
            Pacientes idpacienteNew = historial.getIdpaciente();
            Padecimintos nodepadecimientoOld = persistentHistorial.getNodepadecimiento();
            Padecimintos nodepadecimientoNew = historial.getNodepadecimiento();
            Secretarias idsecretariaOld = persistentHistorial.getIdsecretaria();
            Secretarias idsecretariaNew = historial.getIdsecretaria();
            if (ceduladoctorNew != null) {
                ceduladoctorNew = em.getReference(ceduladoctorNew.getClass(), ceduladoctorNew.getCedula());
                historial.setCeduladoctor(ceduladoctorNew);
            }
            if (idpacienteNew != null) {
                idpacienteNew = em.getReference(idpacienteNew.getClass(), idpacienteNew.getId());
                historial.setIdpaciente(idpacienteNew);
            }
            if (nodepadecimientoNew != null) {
                nodepadecimientoNew = em.getReference(nodepadecimientoNew.getClass(), nodepadecimientoNew.getPadeci());
                historial.setNodepadecimiento(nodepadecimientoNew);
            }
            if (idsecretariaNew != null) {
                idsecretariaNew = em.getReference(idsecretariaNew.getClass(), idsecretariaNew.getIdsec());
                historial.setIdsecretaria(idsecretariaNew);
            }
            historial = em.merge(historial);
            if (ceduladoctorOld != null && !ceduladoctorOld.equals(ceduladoctorNew)) {
                ceduladoctorOld.getHistorialCollection().remove(historial);
                ceduladoctorOld = em.merge(ceduladoctorOld);
            }
            if (ceduladoctorNew != null && !ceduladoctorNew.equals(ceduladoctorOld)) {
                ceduladoctorNew.getHistorialCollection().add(historial);
                ceduladoctorNew = em.merge(ceduladoctorNew);
            }
            if (idpacienteOld != null && !idpacienteOld.equals(idpacienteNew)) {
                idpacienteOld.getHistorialCollection().remove(historial);
                idpacienteOld = em.merge(idpacienteOld);
            }
            if (idpacienteNew != null && !idpacienteNew.equals(idpacienteOld)) {
                idpacienteNew.getHistorialCollection().add(historial);
                idpacienteNew = em.merge(idpacienteNew);
            }
            if (nodepadecimientoOld != null && !nodepadecimientoOld.equals(nodepadecimientoNew)) {
                nodepadecimientoOld.getHistorialCollection().remove(historial);
                nodepadecimientoOld = em.merge(nodepadecimientoOld);
            }
            if (nodepadecimientoNew != null && !nodepadecimientoNew.equals(nodepadecimientoOld)) {
                nodepadecimientoNew.getHistorialCollection().add(historial);
                nodepadecimientoNew = em.merge(nodepadecimientoNew);
            }
            if (idsecretariaOld != null && !idsecretariaOld.equals(idsecretariaNew)) {
                idsecretariaOld.getHistorialCollection().remove(historial);
                idsecretariaOld = em.merge(idsecretariaOld);
            }
            if (idsecretariaNew != null && !idsecretariaNew.equals(idsecretariaOld)) {
                idsecretariaNew.getHistorialCollection().add(historial);
                idsecretariaNew = em.merge(idsecretariaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historial.getNopade();
                if (findHistorial(id) == null) {
                    throw new NonexistentEntityException("The historial with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historial historial;
            try {
                historial = em.getReference(Historial.class, id);
                historial.getNopade();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historial with id " + id + " no longer exists.", enfe);
            }
            Doctores ceduladoctor = historial.getCeduladoctor();
            if (ceduladoctor != null) {
                ceduladoctor.getHistorialCollection().remove(historial);
                ceduladoctor = em.merge(ceduladoctor);
            }
            Pacientes idpaciente = historial.getIdpaciente();
            if (idpaciente != null) {
                idpaciente.getHistorialCollection().remove(historial);
                idpaciente = em.merge(idpaciente);
            }
            Padecimintos nodepadecimiento = historial.getNodepadecimiento();
            if (nodepadecimiento != null) {
                nodepadecimiento.getHistorialCollection().remove(historial);
                nodepadecimiento = em.merge(nodepadecimiento);
            }
            Secretarias idsecretaria = historial.getIdsecretaria();
            if (idsecretaria != null) {
                idsecretaria.getHistorialCollection().remove(historial);
                idsecretaria = em.merge(idsecretaria);
            }
            em.remove(historial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Historial> findHistorialEntities() {
        return findHistorialEntities(true, -1, -1);
    }

    public List<Historial> findHistorialEntities(int maxResults, int firstResult) {
        return findHistorialEntities(false, maxResults, firstResult);
    }

    private List<Historial> findHistorialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historial.class));
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

    public Historial findHistorial(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historial.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historial> rt = cq.from(Historial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
