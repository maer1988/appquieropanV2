package com.example.appquieropan.Entidad;

import java.util.Date;

public class Voucher {

    private String IDVoucher;
    private String IDcliente;
    private String IDproveedor;
    private String RUTproveedor;
    private String nombreproveedor;
    private String fechaentrega;
    private String estado="pendiente";
    private String token="";
    private String TipoVenta;
    private String valorado="0";
    private String Total;

    public Voucher() {

    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        TipoVenta = tipoVenta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getValorado() {
        return valorado;
    }

    public void setValorado(String valorado) {
        this.valorado = valorado;
    }

    public String getRUTproveedor() {
        return RUTproveedor;
    }

    public void setRUTproveedor(String RUTproveedor) {
        this.RUTproveedor = RUTproveedor;
    }

    public String getNombreproveedor() {
        return nombreproveedor;
    }

    public void setNombreproveedor(String nombreproveedor) {
        this.nombreproveedor = nombreproveedor;
    }

    public String getIDVoucher() {
        return IDVoucher;
    }

    public void setIDVoucher(String IDVoucher) {
        this.IDVoucher = IDVoucher;
    }

    public String getIDcliente() {
        return IDcliente;
    }

    public void setIDcliente(String IDcliente) {
        this.IDcliente = IDcliente;
    }

    public String getIDproveedor() {
        return IDproveedor;
    }

    public void setIDproveedor(String IDproveedor) {
        this.IDproveedor = IDproveedor;
    }

    public String getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(String fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }
}
