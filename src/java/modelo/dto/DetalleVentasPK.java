/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author od948
 */
@Embeddable
public class DetalleVentasPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "IdDetalleVentas")
    private int idDetalleVentas;
    @Basic(optional = false)
    @Column(name = "IdVentas")
    private int idVentas;
    @Basic(optional = false)
    @Column(name = "IdProducto")
    private int idProducto;

    public DetalleVentasPK() {
    }

    public DetalleVentasPK(int idDetalleVentas, int idVentas, int idProducto) {
        this.idDetalleVentas = idDetalleVentas;
        this.idVentas = idVentas;
        this.idProducto = idProducto;
    }

    public int getIdDetalleVentas() {
        return idDetalleVentas;
    }

    public void setIdDetalleVentas(int idDetalleVentas) {
        this.idDetalleVentas = idDetalleVentas;
    }

    public int getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(int idVentas) {
        this.idVentas = idVentas;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDetalleVentas;
        hash += (int) idVentas;
        hash += (int) idProducto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleVentasPK)) {
            return false;
        }
        DetalleVentasPK other = (DetalleVentasPK) object;
        if (this.idDetalleVentas != other.idDetalleVentas) {
            return false;
        }
        if (this.idVentas != other.idVentas) {
            return false;
        }
        if (this.idProducto != other.idProducto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.DetalleVentasPK[ idDetalleVentas=" + idDetalleVentas + ", idVentas=" + idVentas + ", idProducto=" + idProducto + " ]";
    }
    
}
