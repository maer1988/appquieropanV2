package com.example.appquieropan.Entidad;

public class Direccion {
    private String id_Direccion;
    private String desc_Direccion;
    private double longitud;
    private double latitud;

    public Direccion() {
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getId_Direccion() {
        return id_Direccion;
    }

    public void setId_Direccion(String id_Direccion) {
        this.id_Direccion = id_Direccion;
    }

    public String getDesc_Direccion() {
        return desc_Direccion;
    }

    public void setDesc_Direccion(String desc_Direccion) {
        this.desc_Direccion = desc_Direccion;
    }
}
