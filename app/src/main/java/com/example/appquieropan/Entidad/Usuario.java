package com.example.appquieropan.Entidad;

public class Usuario {

    private String id_Usuario;
    private String nom_usuario;
    private String pwd_usuario;
    private String fecha_creacion;
    private String fecha_modificacion;
    private String direccion;
    private String fono;
    private String mail;

    public Usuario(String pwd_usuario, String mail) {
        this.pwd_usuario = pwd_usuario;
        this.mail = mail;
    }

    public Usuario() {
    }

    public String getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(String id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public String getNom_usuario() {
        return nom_usuario;
    }

    public void setNom_usuario(String nom_usuario) {
        this.nom_usuario = nom_usuario;
    }

    public String getPwd_usuario() {
        return pwd_usuario;
    }

    public void setPwd_usuario(String pwd_usuario) {
        this.pwd_usuario = pwd_usuario;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(String fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFono() {
        return fono;
    }

    public void setFono(String fono) {
        this.fono = fono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
