package com.asindem.gestiondocumental.dto;

public class RelacionClienteFormView {
    private int relacionId;
    private String relacionNombre;
    private String clienteId;
    private String clienteNombre;


    public RelacionClienteFormView( ) {
     }



    public RelacionClienteFormView(Relacion relacion) {
        this.relacionId=relacion.getId();
        this.relacionNombre=relacion.getNombre();
    }

    public int getRelacionId() {
        return relacionId;
    }

    public void setRelacionId(int relacionId) {
        this.relacionId = relacionId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getRelacionNombre() {
        return relacionNombre;
    }

    public void setRelacionNombre(String relacionNombre) {
        this.relacionNombre = relacionNombre;
    }
}
