package com.ladoe.rocker;


import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.ladoe.rocker.Fragments.ImagenFragment;
import com.ladoe.rocker.Fragments.SalasFragment;
import com.ladoe.rocker.Patrones.PubFactory;
import com.ladoe.rocker.Patrones.SectionsPagerAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class DetalleActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Publicacion publicacion;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        //instancias
        ViewPager mViewPager=(findViewById(R.id.myViewPager));
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ImageView imageViewBack=findViewById(R.id.imageViewBack);

        //listener arrow back
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //obtener publicacion por id
        obtenerPublicacion();

        //carga de mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //carga de datos basicos
        cargaDatosBasicos();

        //carga de imagefragments en SectionsPagerAdapter
        generarFragmentImagenes(pagerAdapter);

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
                FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.contenedor, salasFragment).commit();
                break;
        }
    }
    private void generarFragmentImagenes(SectionsPagerAdapter pagerAdapter ){
        if(publicacion!=null){
            for(Imagen imagen:publicacion.getImagenList()){
                pagerAdapter.addFragment(ImagenFragment.newInstance(imagen.getUri(),null));

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
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pua);
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
    public void reubicarCamara(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                latLng, 15f)));

    }
}
