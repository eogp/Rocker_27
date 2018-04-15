package com.ladoe.rocker.Fragments;


import android.app.Fragment;
import android.os.Bundle;


import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ladoe.rocker.Entidades.EstiloDeVida;
import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.SubTipos.Horarios;
import com.ladoe.rocker.R;

public class EstiloVidaFragment extends Fragment {

    private Publicacion publicacion;

    public EstiloVidaFragment() {
        // Required empty public constructor
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estilo_vida, container, false);

        //carga de datos
        cargaDescripcion(view);
        cargaProctutosYServicios(view);

        return view;

    }

    //tipo de establecimiento y horarios
    private void cargaDescripcion(View view) {
        TextView textViewDescripcion= view.findViewById(R.id.textViewDescripcion);
        String descripcion="";
        if(publicacion!=null){
            EstiloDeVida estiloDeVida=(EstiloDeVida)publicacion;
            descripcion=estiloDeVida.getEstiloVida()+"<br />";
            descripcion+=obtenerHorarios(estiloDeVida);
        }
        textViewDescripcion.setText(Html.fromHtml(descripcion));
    }
    private String obtenerHorarios(EstiloDeVida estiloDeVida){
        String retorno="";
        for(Horarios horario : estiloDeVida.getHorarios()){
            if(horario.getDesdeDia().equals(horario.getHastaDia())){
                retorno+=horario.getDesdeDia();
            }else{
                retorno+=horario.getDesdeDia()+" a "+horario.getHastaDia();
            }
            retorno+=", de "+horario.getDesdeHora() + " a "+horario.getHastaHora()+" hs. <br />";
        }
        return retorno;
    }

    private void cargaProctutosYServicios(View view) {
        TextView textViewEProductosYServicios = view.findViewById(R.id.textViewEProductosYServicios);
        String productosYServicios="";

        if (publicacion != null) {
            EstiloDeVida estiloDeVida = (EstiloDeVida) publicacion;
            if(estiloDeVida.getProductos().isEmpty()) {
                view.findViewById(R.id.dividerProductosYServicios).setVisibility(View.GONE);
                view.findViewById(R.id.textViewTituloProductosYServicios).setVisibility(View.GONE);
                textViewEProductosYServicios.setVisibility(View.GONE);
            }else {
                productosYServicios = obtenerProductos(estiloDeVida);
            }
        }

        textViewEProductosYServicios.setText(Html.fromHtml(productosYServicios));
    }
    private String obtenerProductos(EstiloDeVida estiloDeVida) {
        String retorno="";
        for(String producto:estiloDeVida.getProductos()){
            retorno+=producto+"<br />";
        }
        return retorno;
    }
}
