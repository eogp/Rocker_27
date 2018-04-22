package com.ladoe.rocker.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ladoe.rocker.Constantes.CLAVES;
import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.TipoPublicacion;
import com.ladoe.rocker.MapActivity;
import com.ladoe.rocker.Patrones.PubFactory;
import com.ladoe.rocker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemDetalleFragment extends Fragment {

    private Publicacion publicacion;
    private MapActivity mapActivity;

    public ItemDetalleFragment() {
        // Required empty public constructor
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public void setAppCompatActivity(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_item_detalle, container, false);



        cargarDatos(view);
        cargarListeners(view);

        return view;
    }

    private void cargarListeners(View view) {
        TextView textViewCloseItemDetalle=view.findViewById(R.id.textViewloseItemDetalle);
        TextView textViewDetalle=view.findViewById(R.id.textViewDetalle);
        TextView textViewLlamar=view.findViewById(R.id.textViewLlamar);
        textViewCloseItemDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapActivity.quitarItemDetalle();

            }
        });
        textViewDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapActivity.mostrarDetalleActivity(publicacion);


            }
        });

    }

    private void cargarDatos(View view) {
        TextView textViewNombre=view.findViewById(R.id.textViewNombre);
        TextView textViewTipo=view.findViewById(R.id.textViewTipo);
        TextView textViewDistancia=view.findViewById(R.id.textViewDistancia);
        TextView textViewDireccion=view.findViewById(R.id.textViewDireccion);
        TextView textViewLocalidad=view.findViewById(R.id.textViewLocalidad);
        TextView textViewTelefono=view.findViewById(R.id.textViewTelefono);



        TipoPublicacion tipoPublicacion= PubFactory.getTipoPublicacionList().get(publicacion.getDatosBasicos().getTipoPub()-1);
        textViewNombre.setText(publicacion.getDatosBasicos().getNombre());
        textViewTipo.setText(tipoPublicacion.getDescripcion());
        if(publicacion.getDistancia()==null)
            textViewDistancia.setText("Obteniendo ubicación...");
        else
            textViewDistancia.setText("Estas a "+String.format("%.2f", publicacion.getDistancia())+" km.");
        if(publicacion.getDireccion().getLocalidad().equals(""))
            textViewLocalidad.setText(publicacion.getDireccion().getProvincia());
        else
            textViewLocalidad.setText(publicacion.getDireccion().getLocalidad()+" "+publicacion.getDireccion().getProvincia());

        textViewDireccion.setText(publicacion.getDireccion().getCalle()+" "+publicacion.getDireccion().getAltura());
        textViewTelefono.setText(publicacion.getTelefono().getCodArea()+" "+publicacion.getTelefono().getNumero());


    }

}
