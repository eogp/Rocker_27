package com.ladoe.rocker.Entidades.SubTipos;

/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class Equipos {

    private String tipo;
    private String marca;

    public Equipos(String tipo, String marca) {
        this.tipo = tipo;
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}
