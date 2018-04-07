package com.ladoe.rocker.Entidades;


import com.ladoe.rocker.Entidades.SubTipos.Instrumento;

/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class VentaDeInstrumentos extends Publicacion{
    private Instrumento instrumento;

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }
}
