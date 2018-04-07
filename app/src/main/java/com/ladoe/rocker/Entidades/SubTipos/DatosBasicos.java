package com.ladoe.rocker.Entidades.SubTipos;

/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class DatosBasicos {
    private int id;
    private String nombre;
    private String descripcion;
    private String email;
    private int tipoPub;
    private String web;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTipoPub() {
        return tipoPub;
    }

    public void setTipoPub(int tipoPub) {
        this.tipoPub = tipoPub;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
}
