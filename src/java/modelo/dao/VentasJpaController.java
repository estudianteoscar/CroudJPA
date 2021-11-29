/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.dto.Empleado;
import modelo.dto.Cliente;
import modelo.dto.DetalleVentas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dao.exceptions.IllegalOrphanException;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.dto.Ventas;

/**
 *
 * @author od948
 */
public class VentasJpaController implements Serializable {

    public VentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ventas ventas) {
        if (ventas.getDetalleVentasList() == null) {
            ventas.setDetalleVentasList(new ArrayList<DetalleVentas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = ventas.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                ventas.setIdEmpleado(idEmpleado);
            }
            Cliente idCliente = ventas.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                ventas.setIdCliente(idCliente);
            }
            List<DetalleVentas> attachedDetalleVentasList = new ArrayList<DetalleVentas>();
            for (DetalleVentas detalleVentasListDetalleVentasToAttach : ventas.getDetalleVentasList()) {
                detalleVentasListDetalleVentasToAttach = em.getReference(detalleVentasListDetalleVentasToAttach.getClass(), detalleVentasListDetalleVentasToAttach.getDetalleVentasPK());
                attachedDetalleVentasList.add(detalleVentasListDetalleVentasToAttach);
            }
            ventas.setDetalleVentasList(attachedDetalleVentasList);
            em.persist(ventas);
            if (idEmpleado != null) {
                idEmpleado.getVentasList().add(ventas);
                idEmpleado = em.merge(idEmpleado);
            }
            if (idCliente != null) {
                idCliente.getVentasList().add(ventas);
                idCliente = em.merge(idCliente);
            }
            for (DetalleVentas detalleVentasListDetalleVentas : ventas.getDetalleVentasList()) {
                Ventas oldVentasOfDetalleVentasListDetalleVentas = detalleVentasListDetalleVentas.getVentas();
                detalleVentasListDetalleVentas.setVentas(ventas);
                detalleVentasListDetalleVentas = em.merge(detalleVentasListDetalleVentas);
                if (oldVentasOfDetalleVentasListDetalleVentas != null) {
                    oldVentasOfDetalleVentasListDetalleVentas.getDetalleVentasList().remove(detalleVentasListDetalleVentas);
                    oldVentasOfDetalleVentasListDetalleVentas = em.merge(oldVentasOfDetalleVentasListDetalleVentas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ventas ventas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas persistentVentas = em.find(Ventas.class, ventas.getIdVentas());
            Empleado idEmpleadoOld = persistentVentas.getIdEmpleado();
            Empleado idEmpleadoNew = ventas.getIdEmpleado();
            Cliente idClienteOld = persistentVentas.getIdCliente();
            Cliente idClienteNew = ventas.getIdCliente();
            List<DetalleVentas> detalleVentasListOld = persistentVentas.getDetalleVentasList();
            List<DetalleVentas> detalleVentasListNew = ventas.getDetalleVentasList();
            List<String> illegalOrphanMessages = null;
            for (DetalleVentas detalleVentasListOldDetalleVentas : detalleVentasListOld) {
                if (!detalleVentasListNew.contains(detalleVentasListOldDetalleVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleVentas " + detalleVentasListOldDetalleVentas + " since its ventas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                ventas.setIdEmpleado(idEmpleadoNew);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                ventas.setIdCliente(idClienteNew);
            }
            List<DetalleVentas> attachedDetalleVentasListNew = new ArrayList<DetalleVentas>();
            for (DetalleVentas detalleVentasListNewDetalleVentasToAttach : detalleVentasListNew) {
                detalleVentasListNewDetalleVentasToAttach = em.getReference(detalleVentasListNewDetalleVentasToAttach.getClass(), detalleVentasListNewDetalleVentasToAttach.getDetalleVentasPK());
                attachedDetalleVentasListNew.add(detalleVentasListNewDetalleVentasToAttach);
            }
            detalleVentasListNew = attachedDetalleVentasListNew;
            ventas.setDetalleVentasList(detalleVentasListNew);
            ventas = em.merge(ventas);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getVentasList().remove(ventas);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getVentasList().add(ventas);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getVentasList().remove(ventas);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getVentasList().add(ventas);
                idClienteNew = em.merge(idClienteNew);
            }
            for (DetalleVentas detalleVentasListNewDetalleVentas : detalleVentasListNew) {
                if (!detalleVentasListOld.contains(detalleVentasListNewDetalleVentas)) {
                    Ventas oldVentasOfDetalleVentasListNewDetalleVentas = detalleVentasListNewDetalleVentas.getVentas();
                    detalleVentasListNewDetalleVentas.setVentas(ventas);
                    detalleVentasListNewDetalleVentas = em.merge(detalleVentasListNewDetalleVentas);
                    if (oldVentasOfDetalleVentasListNewDetalleVentas != null && !oldVentasOfDetalleVentasListNewDetalleVentas.equals(ventas)) {
                        oldVentasOfDetalleVentasListNewDetalleVentas.getDetalleVentasList().remove(detalleVentasListNewDetalleVentas);
                        oldVentasOfDetalleVentasListNewDetalleVentas = em.merge(oldVentasOfDetalleVentasListNewDetalleVentas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ventas.getIdVentas();
                if (findVentas(id) == null) {
                    throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.");
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
            Ventas ventas;
            try {
                ventas = em.getReference(Ventas.class, id);
                ventas.getIdVentas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleVentas> detalleVentasListOrphanCheck = ventas.getDetalleVentasList();
            for (DetalleVentas detalleVentasListOrphanCheckDetalleVentas : detalleVentasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ventas (" + ventas + ") cannot be destroyed since the DetalleVentas " + detalleVentasListOrphanCheckDetalleVentas + " in its detalleVentasList field has a non-nullable ventas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empleado idEmpleado = ventas.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getVentasList().remove(ventas);
                idEmpleado = em.merge(idEmpleado);
            }
            Cliente idCliente = ventas.getIdCliente();
            if (idCliente != null) {
                idCliente.getVentasList().remove(ventas);
                idCliente = em.merge(idCliente);
            }
            em.remove(ventas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ventas> findVentasEntities() {
        return findVentasEntities(true, -1, -1);
    }

    public List<Ventas> findVentasEntities(int maxResults, int firstResult) {
        return findVentasEntities(false, maxResults, firstResult);
    }

    private List<Ventas> findVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ventas.class));
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

    public Ventas findVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ventas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ventas> rt = cq.from(Ventas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
