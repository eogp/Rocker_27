package com.ladoe.rocker.Fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.ServiciosProfesionales;
import com.ladoe.rocker.R;


public class ServicosProfFragment extends Fragment {

    private Publicacion publicacion;

    public ServicosProfFragment() {
        // Required empty public constructor
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicos_prof, container, false);

        //carga de datos
        cargaServicios(view);
        cargaPrecioHora(view);

        return view;

    }

    private void cargaServicios(View view) {
        TextView textViewServicios=view.findViewById(R.id.textViewServicios);
        String servicios="";
        if(publicacion!=null){
            ServiciosProfesionales serviciosProfesionales=(ServiciosProfesionales)publicacion;
            servicios+=serviciosProfesionales.getServioPorfesional()+"<br />";
            servicios+=serviciosProfesionales.getExperiencia()+" años de experiencia"+"<br />";
        }
        textViewServicios.setText(Html.fromHtml(servicios));
    }


    private void cargaPrecioHora(View view) {
        TextView textViewPrecioPorHora=view.findViewById(R.id.textViewPrecioPorHora);
        if(publicacion!=null){
            ServiciosProfesionales serviciosProfesionales=(ServiciosProfesionales)publicacion;
            String precioPorHora=String.format("%.2f", serviciosProfesionales.getPrecioHora());
            textViewPrecioPorHora.setText("Precio por hora $"+precioPorHora);

        }
    }

}
