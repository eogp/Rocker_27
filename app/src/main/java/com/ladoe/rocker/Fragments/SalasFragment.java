package com.ladoe.rocker.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.SalasYEstudios;
import com.ladoe.rocker.Entidades.SubTipos.Equipos;
import com.ladoe.rocker.R;


public class SalasFragment extends Fragment {

    private Publicacion publicacion;

    public SalasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_salas, container, false);

        //carga de datos
        cargarServicios(view);
        cargarEquipos(view);
        cargarPrecioHora(view);
        return view;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    private void cargarServicios(View view){
        TextView textViewServicios= view.findViewById(R.id.textViewServicios);
        String servicios="";
        if(publicacion!=null){
            SalasYEstudios salasYEstudios =(SalasYEstudios)publicacion;
            servicios+=salasYEstudios.getSalas()+" salas<br />";
            for (String servicio:salasYEstudios.getServiciosList()){
                servicios+=servicio+"<br />";
            }
        }
        textViewServicios.setText(Html.fromHtml(servicios));
    }

    private void cargarEquipos(View view){
        TextView textViewEquipamiento= view.findViewById(R.id.textViewEquipamiento);
        String equipos="";
        if(publicacion!=null){
            SalasYEstudios salasYEstudios =(SalasYEstudios)publicacion;
            for (Equipos equipo:salasYEstudios.getEquiposList()){
                equipos+=equipo.getTipo()+" "+equipo.getMarca()+"<br />";
            }
        }
        textViewEquipamiento.setText(Html.fromHtml(equipos));
    }

    private void cargarPrecioHora(View view){
        TextView textViewPrecioHora= view.findViewById(R.id.textViewPrecioHora);
        SalasYEstudios salasYEstudios =(SalasYEstudios)publicacion;
        String precioHora=String.format("%.2f", salasYEstudios.getPrecioHora());
        textViewPrecioHora.setText(Html.fromHtml("Desde $" + precioHora));
    }





}
