package com.example.appquieropan.Entidad;

public class factura {

    private String numeroFactura;
    private String fechaFactura;
    private String rutaFactura;
    private String rutProveedor;

    public factura() {
    }

    public String getRutProveedor() {
        return rutProveedor;
    }

    public void setRutProveedor(String rutProveedor) {
        this.rutProveedor = rutProveedor;
    }


    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getRutaFactura() {
        return rutaFactura;
    }

    public void setRutaFactura(String rutaFactura) {
        this.rutaFactura = rutaFactura;
    }

    @Override
    public String toString() {
        return "factura{" +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", fechaFactura='" + fechaFactura + '\'' +
                ", rutaFactura='" + rutaFactura + '\'' +
                '}';
    }
}
