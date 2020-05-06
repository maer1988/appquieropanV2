package com.example.appquieropan.Entidad;

public class Despacho {

    private String id_despacho;
    private String cliente;
    private String direccion;
    private String proveedor;
    private String producto;
    private int cantidad;

    public Despacho() {
    }

    public String getId_despacho() {
        return id_despacho;
    }

    public void setId_despacho(String id_despacho) {
        this.id_despacho = id_despacho;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
