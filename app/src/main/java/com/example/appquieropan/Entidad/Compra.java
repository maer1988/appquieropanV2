package com.example.appquieropan.Entidad;

public class Compra {

    private String id_compra;
    private String desc_compra;
    private String cantidad_compra;
    private String fecha_compra;
    private String total_compra;

    public Compra() {
    }

    public String getId_compra() {
        return id_compra;
    }

    public void setId_compra(String id_compra) {
        this.id_compra = id_compra;
    }

    public String getDesc_compra() {
        return desc_compra;
    }

    public void setDesc_compra(String desc_compra) {
        this.desc_compra = desc_compra;
    }

    public String getCantidad_compra() {
        return cantidad_compra;
    }

    public void setCantidad_compra(String cantidad_compra) {
        this.cantidad_compra = cantidad_compra;
    }

    public String getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(String fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public String getTotal_compra() {
        return total_compra;
    }

    public void setTotal_compra(String total_compra) {
        this.total_compra = total_compra;
    }
}
