package com.example.appquieropan.Entidad;

public class DetalleVenta {

    private String id_detalleVenta;
    private String cant_detalleVenta;
    private String precio_unitario;
    private String fecha_detalleVenta;
    private String iva;
    private String precio_total;

    public DetalleVenta() {
    }

    public String getId_detalleVenta() {
        return id_detalleVenta;
    }

    public void setId_detalleVenta(String id_detalleVenta) {
        this.id_detalleVenta = id_detalleVenta;
    }

    public String getCant_detalleVenta() {
        return cant_detalleVenta;
    }

    public void setCant_detalleVenta(String cant_detalleVenta) {
        this.cant_detalleVenta = cant_detalleVenta;
    }

    public String getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(String precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public String getFecha_detalleVenta() {
        return fecha_detalleVenta;
    }

    public void setFecha_detalleVenta(String fecha_detalleVenta) {
        this.fecha_detalleVenta = fecha_detalleVenta;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(String precio_total) {
        this.precio_total = precio_total;
    }
}
