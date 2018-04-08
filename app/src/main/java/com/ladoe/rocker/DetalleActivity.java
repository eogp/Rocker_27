package com.ladoe.rocker;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ladoe.rocker.Constantes.CLAVES;
import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.SubTipos.Imagen;
import com.ladoe.rocker.Fragments.ImagenFragment;
import com.ladoe.rocker.Patrones.PubFactory;
import com.ladoe.rocker.Patrones.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetalleActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Publicacion publicacion;
    private SectionsPagerAdapter pagerAdapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        obtenerPublicacion();

        mViewPager=findViewById(R.id.myViewPager);

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        generarFragmentImagenes();
        //generarFragmentVideo();

        mViewPager.setAdapter(pagerAdapter);
    }

    private void obtenerPublicacion(){
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(CLAVES.ID);

        List<Publicacion> listadoCompleto=new ArrayList<>();
        listadoCompleto.addAll(PubFactory.getEstiloDeVidaList());
        listadoCompleto.addAll(PubFactory.getShowsYEventosList());
        listadoCompleto.addAll(PubFactory.getServiciosProfesionalesList());
        listadoCompleto.addAll(PubFactory.getVentaDeInstrumentosList());
        listadoCompleto.addAll(PubFactory.getSalasYEstudiosList());

        for (Publicacion pub: listadoCompleto) {
            if(pub.getDatosBasicos().getId()==id){
                publicacion=pub;
            }
        }

    }

    private void generarFragmentImagenes(){
        if(publicacion!=null){
            if(publicacion.getImagenList().size()==0){
                mViewPager.setVisibility(View.GONE);
            }else {
                for (Imagen imagen : publicacion.getImagenList()) {
                    pagerAdapter.addFragment(ImagenFragment.newInstance(imagen.getUri(), null));

                }
            }
        }
    }

}
