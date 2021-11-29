/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.dao.exceptions.PreexistingEntityException;
import modelo.dto.DetalleVentas;
import modelo.dto.DetalleVentasPK;
import modelo.dto.Ventas;
import modelo.dto.Producto;

/**
 *
 * @author od948
 */
public class DetalleVentasJpaController implements Serializable {

    public DetalleVentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleVentas detalleVentas) throws PreexistingEntityException, Exception {
        if (detalleVentas.getDetalleVentasPK() == null) {
            detalleVentas.setDetalleVentasPK(new DetalleVentasPK());
        }
        detalleVentas.getDetalleVentasPK().setIdProducto(detalleVentas.getProducto().getIdProducto());
        detalleVentas.getDetalleVentasPK().setIdVentas(detalleVentas.getVentas().getIdVentas());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas ventas = detalleVentas.getVentas();
            if (ventas != null) {
                ventas = em.getReference(ventas.getClass(), ventas.getIdVentas());
                detalleVentas.setVentas(ventas);
            }
            Producto producto = detalleVentas.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getIdProducto());
                detalleVentas.setProducto(producto);
            }
            em.persist(detalleVentas);
            if (ventas != null) {
                ventas.getDetalleVentasList().add(detalleVentas);
                ventas = em.merge(ventas);
            }
            if (producto != null) {
                producto.getDetalleVentasList().add(detalleVentas);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleVentas(detalleVentas.getDetalleVentasPK()) != null) {
                throw new PreexistingEntityException("DetalleVentas " + detalleVentas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleVentas detalleVentas) throws NonexistentEntityException, Exception {
        detalleVentas.getDetalleVentasPK().setIdProducto(detalleVentas.getProducto().getIdProducto());
        detalleVentas.getDetalleVentasPK().setIdVentas(detalleVentas.getVentas().getIdVentas());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleVentas persistentDetalleVentas = em.find(DetalleVentas.class, detalleVentas.getDetalleVentasPK());
            Ventas ventasOld = persistentDetalleVentas.getVentas();
            Ventas ventasNew = detalleVentas.getVentas();
            Producto productoOld = persistentDetalleVentas.getProducto();
            Producto productoNew = detalleVentas.getProducto();
            if (ventasNew != null) {
                ventasNew = em.getReference(ventasNew.getClass(), ventasNew.getIdVentas());
                detalleVentas.setVentas(ventasNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getIdProducto());
                detalleVentas.setProducto(productoNew);
            }
            detalleVentas = em.merge(detalleVentas);
            if (ventasOld != null && !ventasOld.equals(ventasNew)) {
                ventasOld.getDetalleVentasList().remove(detalleVentas);
                ventasOld = em.merge(ventasOld);
            }
            if (ventasNew != null && !ventasNew.equals(ventasOld)) {
                ventasNew.getDetalleVentasList().add(detalleVentas);
                ventasNew = em.merge(ventasNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getDetalleVentasList().remove(detalleVentas);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getDetalleVentasList().add(detalleVentas);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DetalleVentasPK id = detalleVentas.getDetalleVentasPK();
                if (findDetalleVentas(id) == null) {
                    throw new NonexistentEntityException("The detalleVentas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetalleVentasPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleVentas detalleVentas;
            try {
                detalleVentas = em.getReference(DetalleVentas.class, id);
                detalleVentas.getDetalleVentasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleVentas with id " + id + " no longer exists.", enfe);
            }
            Ventas ventas = detalleVentas.getVentas();
            if (ventas != null) {
                ventas.getDetalleVentasList().remove(detalleVentas);
                ventas = em.merge(ventas);
            }
            Producto producto = detalleVentas.getProducto();
            if (producto != null) {
                producto.getDetalleVentasList().remove(detalleVentas);
                producto = em.merge(producto);
            }
            em.remove(detalleVentas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleVentas> findDetalleVentasEntities() {
        return findDetalleVentasEntities(true, -1, -1);
    }

    public List<DetalleVentas> findDetalleVentasEntities(int maxResults, int firstResult) {
        return findDetalleVentasEntities(false, maxResults, firstResult);
    }

    private List<DetalleVentas> findDetalleVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleVentas.class));
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

    public DetalleVentas findDetalleVentas(DetalleVentasPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleVentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleVentas> rt = cq.from(DetalleVentas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
