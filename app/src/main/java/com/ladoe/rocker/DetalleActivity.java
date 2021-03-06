package com.ladoe.rocker;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ladoe.rocker.Constantes.CLAVES;
import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.SubTipos.Imagen;
import com.ladoe.rocker.Entidades.TipoPublicacion;
import com.ladoe.rocker.Fragments.EstiloVidaFragment;
import com.ladoe.rocker.Fragments.ImagenFragment;
import com.ladoe.rocker.Fragments.SalasFragment;
import com.ladoe.rocker.Fragments.ServicosProfFragment;
import com.ladoe.rocker.Fragments.ShowsYEventosFragment;
import com.ladoe.rocker.Fragments.VentaInstrumentosFragment;
import com.ladoe.rocker.Fragments.VideoFragment;
import com.ladoe.rocker.Patrones.PubFactory;
import com.ladoe.rocker.Patrones.SectionsPagerAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class DetalleActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Publicacion publicacion;
    private GoogleMap mMap;
    private Button btnLLamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        //instancias
        ViewPager mViewPager=(findViewById(R.id.myViewPager));
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ImageView imageViewBack=findViewById(R.id.imageViewBack);
        btnLLamar=findViewById(R.id.buttonLLamar);

        //listener arrow back
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //obtener publicacion por id
        obtenerPublicacion();

        //listener btn llamar
        btnLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamar(publicacion);
            }
        });

        //carga de mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //deshabilito listener de click en map
        mapFragment.getView().setClickable(false);

        //carga de datos basicos
        cargaDatosBasicos();

        //carga de imagefragments y video en SectionsPagerAdapter
        generarFragmentImagenesYVideo(pagerAdapter, mViewPager);

        //carga de SectionsPagerAdapter en mViewPager
        mViewPager.setAdapter(pagerAdapter);

        //carga de fragment con datos epsecificos
        cargaDatosEspecificos();


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
    private void cargaDatosBasicos() {
        TextView textViewNombre=findViewById(R.id.textViewNombre);
        TextView textViewTipo=findViewById(R.id.textViewTipo);
        TextView textViewDistancia=findViewById(R.id.textViewDistancia);
        TextView textViewDireccion=findViewById(R.id.textViewDireccion);
        TextView textViewLocalidad=findViewById(R.id.textViewLocalidad);
        TextView textViewTelefono=findViewById(R.id.textViewTelefono);
        TextView textViewMail=findViewById(R.id.textViewMail);
        TextView textViewWeb=findViewById(R.id.textViewWeb);
        //nombre de la publciacion
        textViewNombre.setText(publicacion.getDatosBasicos().getNombre());
        //tipo de publicacion
        TipoPublicacion tipoPublicacion=PubFactory.getTipoPublicacionList().get(publicacion.getDatosBasicos().getTipoPub()-1);
        textViewTipo.setText(tipoPublicacion.getDescripcion());
        //distancia
        if(publicacion.getDistancia()==null)
            textViewDistancia.setText("Obteniendo ubicación...");
        else
            textViewDistancia.setText("Estas a "+String.format("%.2f", publicacion.getDistancia())+" km.");
        //direccion
        textViewDireccion.setText(publicacion.getDireccion().getCalle()+" "+publicacion.getDireccion().getAltura());
        //localidad y provincia
        if(publicacion.getDireccion().getLocalidad().equals(""))
            textViewLocalidad.setText(publicacion.getDireccion().getProvincia());
        else
            textViewLocalidad.setText(publicacion.getDireccion().getLocalidad()+" "+publicacion.getDireccion().getProvincia());
        //telefono
        textViewTelefono.setText(publicacion.getTelefono().getCodArea()+" "+publicacion.getTelefono().getNumero());
        //mail
        textViewMail.setText(publicacion.getDatosBasicos().getEmail());
        //web
        textViewWeb.setText(publicacion.getDatosBasicos().getWeb());
    }
    private void cargaDatosEspecificos(){
        switch (publicacion.getDatosBasicos().getTipoPub()){
            case 1:
                SalasFragment salasFragment=new SalasFragment();
                salasFragment.setPublicacion(publicacion);
                cargarFragmentDatosEspecificos(salasFragment);
                break;
            case 2:
                VentaInstrumentosFragment ventaInstrumentosFragment=new VentaInstrumentosFragment();
                ventaInstrumentosFragment.setPublicacion(publicacion);
                cargarFragmentDatosEspecificos(ventaInstrumentosFragment);
                break;
            case 3:
                EstiloVidaFragment estiloVidaFragment=new EstiloVidaFragment();
                estiloVidaFragment.setPublicacion(publicacion);
                cargarFragmentDatosEspecificos(estiloVidaFragment);
                break;
            case 4:
                ServicosProfFragment servicosProfFragment=new ServicosProfFragment();
                servicosProfFragment.setPublicacion(publicacion);
                cargarFragmentDatosEspecificos(servicosProfFragment);
                break;
            case 5:
                ShowsYEventosFragment showsYEventosFragment=new ShowsYEventosFragment();
                showsYEventosFragment.setPublicacion(publicacion);
                cargarFragmentDatosEspecificos(showsYEventosFragment);
                break;
        }
    }
    private void cargarFragmentDatosEspecificos(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contenedor, fragment).commit();

    }
    private void generarFragmentImagenesYVideo(SectionsPagerAdapter pagerAdapter, ViewPager viewPager ){
        if(publicacion!=null) {

            if (!publicacion.getVideo().equals("null")&&!publicacion.getVideo().getUri().equals("")) {
               // Log.d("video", "i"+publicacion.getVideo().getUri()+"f");
                pagerAdapter.addFragment(VideoFragment.newInstance(publicacion.getVideo().getUri(), null));
            }

            if (!publicacion.getImagenList().isEmpty()) {
                for (Imagen imagen : publicacion.getImagenList()) {
                    pagerAdapter.addFragment(ImagenFragment.newInstance(imagen.getUri(), null));
                }
            }

            if (publicacion.getImagenList().isEmpty() && publicacion.getVideo().getUri().equals("")) {
                viewPager.setVisibility(View.GONE);
            }
        }
    }


    //maps intercafe
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        cargarMarcador();

    }
    private void cargarMarcador() {
        // Marcador
        if (mMap != null){
            mMap.clear();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    imagenPorTipo(PubFactory.getTipoPublicacionList().get(publicacion.getDatosBasicos().getTipoPub()-1).getId()));


            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pua);
            LatLng latLng=new LatLng(publicacion.getDireccion().getLatitud(), publicacion.getDireccion().getLongitud());
            DecimalFormat df = new DecimalFormat("#.##");
            publicacion.setMarker(mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(publicacion.getDatosBasicos().getNombre())
                        .snippet("Distancia no disponible...")
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))));
            if (publicacion.getDistancia() != null)
                publicacion.getMarker().setSnippet("Distancia " + df.format(publicacion.getDistancia()) + " Km.");

            reubicarCamara(latLng);
        }
    }
    public int imagenPorTipo(int tipo){
        int retorno=R.mipmap.ic_logo;
        switch (tipo){
            case 1:
                retorno= R.mipmap.marker_salasyestudios;
                break;
            case 2:
                retorno= R.mipmap.marker_ventadeinstrumentos;
                break;
            case 3:
                retorno= R.mipmap.marker_estilodevida;
                break;
            case 4:
                retorno= R.mipmap.marker_serviciosprofesionales;
                break;
            case 5:
                retorno= R.mipmap.marker_fechasyeventos;
                break;
        }
        return retorno;
    }

    public void reubicarCamara(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                latLng, 15f)));

    }

    public void llamar(Publicacion publicacion){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + publicacion.getTelefono().getCodArea()+publicacion.getTelefono().getNumero()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
