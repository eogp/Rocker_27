package com.ladoe.rocker.Entidades;

import com.ladoe.rocker.Entidades.SubTipos.Equipos;

import java.util.List;



/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class SalasYEstudios extends Publicacion{
    private int salas;
    private double precioHora;
    private List<Equipos> equiposList;
    private List<String> serviciosList;

    public int getSalas() {
        return salas;
    }

    public void setSalas(int salas) {
        this.salas = salas;
    }

    public double getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(double precioHora) {
        this.precioHora = precioHora;
    }

    public List<Equipos> getEquiposList() {
        return equiposList;
    }

    public void setEquiposList(List<Equipos> equiposList) {
        this.equiposList = equiposList;
    }

    public List<String> getServiciosList() {
        return serviciosList;
    }

    public void setServiciosList(List<String> serviciosList) {
        this.serviciosList = serviciosList;
    }



}
