package com.example.appquieropan.Entidad;

public class Precio {

    private String id_precio;
    private String desc_precio;
    private String subProducto;

    public Precio() {
    }

    public String getId_precio() {
        return id_precio;
    }

    public void setId_precio(String id_precio) {
        this.id_precio = id_precio;
    }

    public String getDesc_precio() {
        return desc_precio;
    }

    public void setDesc_precio(String desc_precio) {
        this.desc_precio = desc_precio;
    }

    public String getSubProducto() {
        return subProducto;
    }

    public void setSubProducto(String subProducto) {
        this.subProducto = subProducto;
    }
}
