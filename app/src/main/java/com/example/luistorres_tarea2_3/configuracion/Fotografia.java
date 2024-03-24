package com.example.luistorres_tarea2_3.configuracion;

public class Fotografia {

    private String id = "Id";
    private String foto = "foto";
    private String descripcion = "descripcion";

    public Fotografia() {
    }

    public Fotografia(String id, String foto, String descripcion) {
        this.id = id;
        this.foto = foto;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
