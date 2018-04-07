package com.ladoe.rocker.Entidades;

import com.ladoe.rocker.Entidades.SubTipos.Horarios;

import java.util.List;



/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class EstiloDeVida extends Publicacion{
    private String estiloVida;
    private int estiloVidaId;
    private List<String> productos;
    private List<Horarios> horarios;

    public int getEstiloVidaId() {
        return estiloVidaId;
    }

    public void setEstiloVidaId(int estiloVidaId) {
        this.estiloVidaId = estiloVidaId;
    }

    public String getEstiloVida() {
        return estiloVida;
    }

    public void setEstiloVida(String estiloVida) {
        this.estiloVida = estiloVida;
    }

    public List<String> getProductos() {
        return productos;
    }

    public void setProductos(List<String> productos) {
        this.productos = productos;
    }

    public List<Horarios> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horarios> horarios) {
        this.horarios = horarios;
    }
}
