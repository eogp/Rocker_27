package com.ladoe.rocker.Entidades.SubTipos;

/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class Telefono {
    private int numero;
    private int codArea;
    private int codPais;
    private boolean celular;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCodArea() {
        return codArea;
    }

    public void setCodArea(int codArea) {
        this.codArea = codArea;
    }

    public int getCodPais() {
        return codPais;
    }

    public void setCodPais(int codPais) {
        this.codPais = codPais;
    }

    public boolean isCelular() {
        return celular;
    }

    public void setCelular(boolean celular) {
        this.celular = celular;
    }
}
