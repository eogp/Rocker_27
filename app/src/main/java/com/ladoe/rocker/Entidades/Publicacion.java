package com.ladoe.rocker.Entidades;


import com.google.android.gms.maps.model.Marker;
import com.ladoe.rocker.Entidades.SubTipos.DatosBasicos;
import com.ladoe.rocker.Entidades.SubTipos.Direccion;
import com.ladoe.rocker.Entidades.SubTipos.Imagen;
import com.ladoe.rocker.Entidades.SubTipos.Telefono;
import com.ladoe.rocker.Entidades.SubTipos.Video;

import java.util.List;

public class Publicacion {

    private DatosBasicos datosBasicos;
    private Direccion direccion;
    private Telefono telefono;
    private Video video;
    private List<Imagen> imagenList;
    private Double distancia;
    private Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public DatosBasicos getDatosBasicos() {
        return datosBasicos;
    }

    public void setDatosBasicos(DatosBasicos datosBasicos) {
        this.datosBasicos = datosBasicos;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public List<Imagen> getImagenList() {
        return imagenList;
    }

    public void setImagenList(List<Imagen> imagenList) {
        this.imagenList = imagenList;
    }
}
