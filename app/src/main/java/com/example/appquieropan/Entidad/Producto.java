package com.example.appquieropan.Entidad;

public class Producto {

    private String Id_producto;
    private String uid;
    private String nom_tipoSubProducto;
    private String desc_tipoSubProducto;
    private String urlSubproducto;
    private String precio;
    private String tipoVentaProducto;
    private String rut_Empresa;
    private String categoria;


    public Producto() {
    }




    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId_producto() {
        return Id_producto;
    }

    public void setId_producto(String id_producto) {
        Id_producto = id_producto;
    }

    public String getNom_tipoSubProducto() {
        return nom_tipoSubProducto;
    }

    public void setNom_tipoSubProducto(String nom_tipoSubProducto) {
        this.nom_tipoSubProducto = nom_tipoSubProducto;
    }

    public String getDesc_tipoSubProducto() {
        return desc_tipoSubProducto;
    }

    public void setDesc_tipoSubProducto(String desc_tipoSubProducto) {
        this.desc_tipoSubProducto = desc_tipoSubProducto;
    }

    public String getUrlSubproducto() {
        return urlSubproducto;
    }

    public void setUrlSubproducto(String urlSubproducto) {
        this.urlSubproducto = urlSubproducto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTipoVentaProducto() {
        return tipoVentaProducto;
    }

    public void setTipoVentaProducto(String tipoVentaProducto) {
        this.tipoVentaProducto = tipoVentaProducto;
    }

    public String getRut_Empresa() {
        return rut_Empresa;
    }

    public void setRut_Empresa(String rut_Empresa) {
        this.rut_Empresa = rut_Empresa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}