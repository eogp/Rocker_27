package com.ladoe.rocker.Fragments;


import android.app.Fragment;
import android.os.Bundle;
;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.SubTipos.Instrumento;
import com.ladoe.rocker.Entidades.VentaDeInstrumentos;
import com.ladoe.rocker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VentaInstrumentosFragment extends Fragment {

    private Publicacion publicacion;

    public VentaInstrumentosFragment() {
        // Required empty public constructor
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venta_instrumentos, container, false);

        //carga de datos
        cargaDescripcion(view);
        cargarPrecioInstrumento(view);

        return view;
    }

    private void cargaDescripcion(View view) {
        TextView textViewDescripcion=view.findViewById(R.id.textViewDescripcion);
        String descripcion="";
        if(publicacion!=null){
            VentaDeInstrumentos ventaDeInstrumentos=(VentaDeInstrumentos)publicacion;
            Instrumento instrumento=ventaDeInstrumentos.getInstrumento();
            if(instrumento.getOtro()!=null && !instrumento.getOtro().equals("null")) {
                descripcion += ventaDeInstrumentos.getInstrumento().getOtro()+"<br />";
            }else{
                descripcion += ventaDeInstrumentos.getInstrumento().getTipo()+"<br />";
                descripcion += ventaDeInstrumentos.getInstrumento().getMarca()+"<br />";
            }

            descripcion += ventaDeInstrumentos.getInstrumento().getEstado()+"<br />";
            descripcion += ventaDeInstrumentos.getInstrumento().getAnio()+"<br />";
            descripcion += ventaDeInstrumentos.getInstrumento().getPais()+"<br />";

            textViewDescripcion.setText(Html.fromHtml(descripcion));

        }
    }
    private void cargarPrecioInstrumento(View view){
        TextView textViewPrecioInstrumento= view.findViewById(R.id.textViewPrecioInstrumento);
        VentaDeInstrumentos ventaDeInstrumentos =(VentaDeInstrumentos)publicacion;
        String precioInstrumento=String.format("%.2f", ventaDeInstrumentos.getInstrumento().getValor());
        textViewPrecioInstrumento.setText(Html.fromHtml("$ " + precioInstrumento));
    }

}
