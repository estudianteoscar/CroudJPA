/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author od948
 */
@Entity
@Table(name = "detalle_ventas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleVentas.findAll", query = "SELECT d FROM DetalleVentas d"),
    @NamedQuery(name = "DetalleVentas.findByIdDetalleVentas", query = "SELECT d FROM DetalleVentas d WHERE d.detalleVentasPK.idDetalleVentas = :idDetalleVentas"),
    @NamedQuery(name = "DetalleVentas.findByIdVentas", query = "SELECT d FROM DetalleVentas d WHERE d.detalleVentasPK.idVentas = :idVentas"),
    @NamedQuery(name = "DetalleVentas.findByIdProducto", query = "SELECT d FROM DetalleVentas d WHERE d.detalleVentasPK.idProducto = :idProducto"),
    @NamedQuery(name = "DetalleVentas.findByCantidad", query = "SELECT d FROM DetalleVentas d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleVentas.findByPrecioVenta", query = "SELECT d FROM DetalleVentas d WHERE d.precioVenta = :precioVenta")})
public class DetalleVentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleVentasPK detalleVentasPK;
    @Column(name = "Cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PrecioVenta")
    private Double precioVenta;
    @JoinColumn(name = "IdVentas", referencedColumnName = "IdVentas", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Ventas ventas;
    @JoinColumn(name = "IdProducto", referencedColumnName = "IdProducto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    public DetalleVentas() {
    }

    public DetalleVentas(DetalleVentasPK detalleVentasPK) {
        this.detalleVentasPK = detalleVentasPK;
    }

    public DetalleVentas(int idDetalleVentas, int idVentas, int idProducto) {
        this.detalleVentasPK = new DetalleVentasPK(idDetalleVentas, idVentas, idProducto);
    }

    public DetalleVentasPK getDetalleVentasPK() {
        return detalleVentasPK;
    }

    public void setDetalleVentasPK(DetalleVentasPK detalleVentasPK) {
        this.detalleVentasPK = detalleVentasPK;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Ventas getVentas() {
        return ventas;
    }

    public void setVentas(Ventas ventas) {
        this.ventas = ventas;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleVentasPK != null ? detalleVentasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleVentas)) {
            return false;
        }
        DetalleVentas other = (DetalleVentas) object;
        if ((this.detalleVentasPK == null && other.detalleVentasPK != null) || (this.detalleVentasPK != null && !this.detalleVentasPK.equals(other.detalleVentasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.DetalleVentas[ detalleVentasPK=" + detalleVentasPK + " ]";
    }
    
}
