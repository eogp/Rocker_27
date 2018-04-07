package com.ladoe.rocker.Entidades;

import java.util.List;

/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class ShowsYEventos extends Publicacion {
    private String showEvento;
    private List<String> fechas;
    private String bandas;
    private Double valor;

    public String getShowEvento() {
        return showEvento;
    }

    public void setShowEvento(String showEvento) {
        this.showEvento = showEvento;
    }

    public List<String> getFechas() {
        return fechas;
    }

    public void setFechas(List<String> fechas) {
        this.fechas = fechas;
    }

    public String getBandas() {
        return bandas;
    }

    public void setBandas(String bandas) {
        this.bandas = bandas;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
