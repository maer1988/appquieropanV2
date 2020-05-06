package com.example.appquieropan.Entidad;

public class Proveedor {

    private String uid;
    private String rut_proveedor="";
    private String nom_proveedor="";
    private String direccion_proveedor="";
    private String Sucursal="matriz";
    private String email_proveedor;
    private String fono1_proveedor;
    private String fono2_proveedor;
    private String url_proveedor;
    private String cod_postal_proveedor;
    private String passwordProveedor;
    private String nomContacto_proveedor;
    private String nota_proveeedor;
    private String tipo_Despacho_Proveedor="local";
    private String tipo_Pago_Proveedor="efectivo";
    private String latitud="-33.4379352";
    private String longitud="-70.6503999";


    public Proveedor() {
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String sucursal) {
        Sucursal = sucursal;
    }

    public String getDireccion_proveedor() {
        return direccion_proveedor;
    }

    public void setDireccion_proveedor(String direccion_proveedor) {
        this.direccion_proveedor = direccion_proveedor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRut_proveedor() {
        return rut_proveedor;
    }

    public void setRut_proveedor(String rut_proveedor) {
        this.rut_proveedor = rut_proveedor;
    }

    public String getNom_proveedor() {
        return nom_proveedor;
    }

    public void setNom_proveedor(String nom_proveedor) {
        this.nom_proveedor = nom_proveedor;
    }

    public String getEmail_proveedor() {
        return email_proveedor;
    }

    public void setEmail_proveedor(String email_proveedor) {
        this.email_proveedor = email_proveedor;
    }

    public String getFono1_proveedor() {
        return fono1_proveedor;
    }

    public void setFono1_proveedor(String fono1_proveedor) {
        this.fono1_proveedor = fono1_proveedor;
    }

    public String getFono2_proveedor() {
        return fono2_proveedor;
    }

    public void setFono2_proveedor(String fono2_proveedor) {
        this.fono2_proveedor = fono2_proveedor;
    }

    public String getUrl_proveedor() {
        return url_proveedor;
    }

    public void setUrl_proveedor(String url_proveedor) {
        this.url_proveedor = url_proveedor;
    }

    public String getCod_postal_proveedor() {
        return cod_postal_proveedor;
    }

    public void setCod_postal_proveedor(String cod_postal_proveedor) {
        this.cod_postal_proveedor = cod_postal_proveedor;
    }

    public String getPasswordProveedor() {
        return passwordProveedor;
    }

    public void setPasswordProveedor(String passwordProveedor) {
        this.passwordProveedor = passwordProveedor;
    }

    public String getNomContacto_proveedor() {
        return nomContacto_proveedor;
    }

    public void setNomContacto_proveedor(String nomContacto_proveedor) {
        this.nomContacto_proveedor = nomContacto_proveedor;
    }

    public String getNota_proveeedor() {
        return nota_proveeedor;
    }

    public void setNota_proveeedor(String nota_proveeedor) {
        this.nota_proveeedor = nota_proveeedor;
    }

    public String getTipo_Despacho_Proveedor() {
        return tipo_Despacho_Proveedor;
    }

    public void setTipo_Despacho_Proveedor(String tipo_Despacho_Proveedor) {
        this.tipo_Despacho_Proveedor = tipo_Despacho_Proveedor;
    }

    public String getTipo_Pago_Proveedor() {
        return tipo_Pago_Proveedor;
    }

    public void setTipo_Pago_Proveedor(String tipo_Pago_Proveedor) {
        this.tipo_Pago_Proveedor = tipo_Pago_Proveedor;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
