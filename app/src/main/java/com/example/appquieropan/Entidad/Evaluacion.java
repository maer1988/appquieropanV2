package com.example.appquieropan.Entidad;

public class Evaluacion {

    private String id_evaluacion;
    private String user_evaluado;
    private String desc_evaluado;
    private String nota;
    private String total_evaluacion;

    public Evaluacion() {
    }

    public String getId_evaluacion() {
        return id_evaluacion;
    }

    public void setId_evaluacion(String id_evaluacion) {
        this.id_evaluacion = id_evaluacion;
    }

    public String getUser_evaluado() {
        return user_evaluado;
    }

    public void setUser_evaluado(String user_evaluado) {
        this.user_evaluado = user_evaluado;
    }

    public String getDesc_evaluado() {
        return desc_evaluado;
    }

    public void setDesc_evaluado(String desc_evaluado) {
        this.desc_evaluado = desc_evaluado;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getTotal_evaluacion() {
        return total_evaluacion;
    }

    public void setTotal_evaluacion(String total_evaluacion) {
        this.total_evaluacion = total_evaluacion;
    }
}
