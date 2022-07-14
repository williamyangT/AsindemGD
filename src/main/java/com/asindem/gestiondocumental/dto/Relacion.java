package com.asindem.gestiondocumental.dto;

public class Relacion {
    private int id;
    private String nombre;

    public Relacion() {
    }

    public Relacion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
