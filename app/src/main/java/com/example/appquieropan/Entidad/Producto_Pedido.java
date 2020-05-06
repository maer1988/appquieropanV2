package com.example.appquieropan.Entidad;

public class Producto_Pedido {

    private String uid;
    private String idCliente;
    private String rut_proveedor;
    private String tipo_cantidad;
    private String precio;
    private String cantidad;
    private String nombre_producto;
    private String estado= "nuevo";
    private String IDvoucher="";

    public Producto_Pedido() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getRut_proveedor() {
        return rut_proveedor;
    }

    public void setRut_proveedor(String rut_proveedor) {
        this.rut_proveedor = rut_proveedor;
    }

    public String getTipo_cantidad() {
        return tipo_cantidad;
    }

    public void setTipo_cantidad(String tipo_cantidad) {
        this.tipo_cantidad = tipo_cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String total_precio(){

        int total = Integer.parseInt(this.cantidad) * Integer.parseInt(this.precio);

        return Integer.toString(total);

    }

    public String getIDvoucher() {
        return IDvoucher;
    }

    public void setIDvoucher(String IDvoucher) {
        this.IDvoucher = IDvoucher;
    }
}
