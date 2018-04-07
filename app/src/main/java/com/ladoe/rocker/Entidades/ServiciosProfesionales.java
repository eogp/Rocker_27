package com.ladoe.rocker.Entidades;

/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class ServiciosProfesionales extends Publicacion {
    private String experiencia;
    private String servioPorfesional;
    private double precioHora;

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getServioPorfesional() {
        return servioPorfesional;
    }

    public void setServioPorfesional(String servioPorfesional) {
        this.servioPorfesional = servioPorfesional;
    }

    public double getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(double precioHora) {
        this.precioHora = precioHora;
    }
}
