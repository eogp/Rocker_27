package com.ladoe.rocker;

import android.Manifest;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ladoe.rocker.CargaAsincorinca.LoadImage;
import com.ladoe.rocker.Constantes.CLAVES;
import com.ladoe.rocker.Entidades.EstiloDeVida;
import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Fragments.ItemDetalleFragment;
import com.ladoe.rocker.Patrones.PubFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMyLocationButtonClickListener {


    private LoadImage loadImage;
    private LoadImage loadImage2;

    private AlertDialog alert;
    private DrawerLayout mDrawerLayout;
    private ScrollView scrollViewMenu;
    private ImageView imageViewOpenDrawer;
    private ImageView imageViewCloseDrawer;
    private TextView textViewCerrarSesion;
    private EditText editTextNombre;
    private EditText editTextMail;
    private ImageView imageViewPerfil;
    private TextView textViewListado;
    private TextView textViewFiltros;
    private GoogleMap mMap;
    private FrameLayout frameLayout;

    private List<Publicacion> listadoActual;
    //POSISIONAMIENTO
    private FusedLocationProviderClient mFusedLocationClient;
    //FILTRO
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        //instancias principal
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        imageViewOpenDrawer =  findViewById(R.id.imageViewOpenDrawer);
        textViewListado = findViewById(R.id.textViewListado);
        textViewFiltros = findViewById(R.id.textViewFiltros);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPref = getSharedPreferences(CLAVES.FILTRO, MODE_PRIVATE);
        frameLayout=findViewById(R.id.itemDetalle);

        //instancias menu
        imageViewCloseDrawer =  findViewById(R.id.imageViewCloseDrawer);
        scrollViewMenu =  findViewById(R.id.scrollViewMenu);
        textViewCerrarSesion = findViewById(R.id.textViewCerrarSesion);
        editTextNombre =  findViewById(R.id.editTextNombre);
        editTextMail = findViewById(R.id.editTextMail);
        imageViewPerfil = findViewById(R.id.imageViewPerfil);


        //listeners
        imageViewOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(scrollViewMenu);
            }
        });
        imageViewCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });
        textViewCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoCerrarSesion();
            }
        });
        textViewListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loadImage!=null)
                    loadImage.cancel(true);
                startActivity(new Intent(MapActivity.this, ListActivity.class));
                finish();
            }
        });
        textViewFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, FiltroActivity.class);
                intent.putExtra(CLAVES.ORIGEN, CLAVES.MAPACTIVITY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        //veficacion de permisos
        verificarPermisosLocation();
        //carga de contenido
        cargarDatosUsuario();
        cargarListado();
        //carga de mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(loadImage!=null)
            loadImage.cancel(true);
    }
    @Override
    public void onBackPressed() {
        dialogoSalir();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        verificarPermisosLocation();

    }

    //DIALOGOS
    public void dialogoCerrarSesion() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas cerrar tu sesión?.")
                .setCancelable(false)
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        cerrarSesion();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                }
        );
        alert = builder.create();
        alert.show();

    }
    public void dialogoSalir() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas salir de Rocker?.")
                .setCancelable(false)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        finish();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                }
        );
        alert = builder.create();
        alert.show();

    }
    public void dialogoFaltanPermisos() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Rocker necesita acceder a tu ubicacion para funcionar.")
                .setCancelable(false)
                .setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        solicitarPermisosLocation();
                    }
                }).setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        finish();

                    }
                }
        );
        alert = builder.create();
        alert.show();

    }

    //CARGA DE DATOS DE USUARIO
    private void cargarDatosUsuario(){
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        Log.d("map", "autologin: " + sharedPref.getInt("login", 0));
        Log.d("map", "nombre: " + sharedPref.getString("nombre", ""));
        Log.d("map", "email: " + sharedPref.getString("email", ""));
        Log.d("map", "faceid: " + sharedPref.getString("faceid", ""));
        if (sharedPref.getInt("autologin", 0) != 1) {
            editTextNombre.setEnabled(false);
            editTextMail.setEnabled(false);
        }
        if (!sharedPref.getString("nombre", "").equals("null"))
            editTextNombre.setText(sharedPref.getString("nombre", ""));
        if (!sharedPref.getString("email", "").equals("null"))
            editTextMail.setText(sharedPref.getString("email", ""));
        if (!sharedPref.getString("faceid", "").equals("null")) {
            loadImage = new LoadImage(imageViewPerfil, this);
            loadImage.execute("https://graph.facebook.com/" + sharedPref.getString("faceid", "") + "/picture?width=500&height=500");
            loadImage2=new LoadImage(imageViewOpenDrawer,this);
            loadImage2.execute("https://graph.facebook.com/" + sharedPref.getString("faceid", "") + "/picture?width=500&height=500");

        }
    }

    //CARGA, FILTRADO Y REESCTRUCTURACION DE LISTADO
    private void cargarListado(){
        //FILTRADO DE PUBLICACION POR TIPO Y SUBTIPO
        if(sharedPref.getBoolean(CLAVES.FILTRAR, false)){
            textViewFiltros.setTextColor(Color.MAGENTA);
            switch (sharedPref.getInt(CLAVES.CATEGORIA, 0)) {
                case 0:
                    cargarListadoCompleto();
                    break;
                case 1:
                    cargarListadoSalasYEstudios();
                    break;
                case 2:
                    cargarListadoVentaDeInstrumentos();
                    break;
                case 3:
                    cargarListadoEstiloDeVida(sharedPref.getInt(CLAVES.SUB_CATEGORIA, 0));
                    break;
                case 4:
                    cargarListadoServiciosProfesionales();
                    break;
                case 5:
                    cargarListadoShowsYEventos();
                    break;
            }
            //FILTRADO POR PALABRA CLAVE
            String palabra = sharedPref.getString(CLAVES.PALABRA_CLAVE, "");
            if(!palabra.equals(""))
                filtrarListadoPorPalabraClave(palabra);

        }else{
            cargarListadoCompleto();

        }

    }
    private void filtrarListadoPorDistanciaMaxima() {
        List<Publicacion> auxList =new ArrayList<>();
        Double distanciaMaxima=0.00;
        if(sharedPref.getBoolean(CLAVES.FILTRAR, false)) {
            //FILTRADO POR DISTANCIA MAXIMA
            // 1->1 km,  2->2 km, 3->5 km, 4->10 km, 5->15 km
            switch (sharedPref.getInt(CLAVES.DISTANCIA_MAXIMA, 0)) {
                case 1:
                    distanciaMaxima = 1.00;
                    break;
                case 2:
                    distanciaMaxima = 2.00;
                    break;
                case 3:
                    distanciaMaxima = 5.00;
                    break;
                case 4:
                    distanciaMaxima = 10.00;
                    break;
                case 5:
                    distanciaMaxima = 15.00;
                    break;
            }

            if (distanciaMaxima > 0) {
                for (Publicacion publicacion : listadoActual) {
                    if (publicacion.getDistancia() <= distanciaMaxima) {
                        auxList.add(publicacion);
                    }
                }
                listadoActual = auxList;
            }
        }
    }
    private void filtrarListadoPorPalabraClave(String palabra) {
        List<Publicacion> auxList =new ArrayList<>();
        if(sharedPref.getBoolean(CLAVES.FILTRAR, false)) {
            for (Publicacion publicacion : listadoActual) {
                if (publicacion.getDatosBasicos().getNombre().toLowerCase().contains(palabra.toLowerCase()) ||
                        publicacion.getDireccion().getCalle().toLowerCase().contains(palabra.toLowerCase()) ||
                        publicacion.getDireccion().getCiudad().toLowerCase().contains(palabra.toLowerCase()) ||
                        publicacion.getDireccion().getLocalidad().toLowerCase().contains(palabra.toLowerCase()) ||
                        contienePalabra(publicacion.getDatosBasicos().getTipoPub(), palabra)) {
                    auxList.add(publicacion);
                }
            }
            listadoActual = auxList;
        }
    }
    private boolean contienePalabra(int id, String palabra ){
        return PubFactory.getTipoPublicacionList().get(id-1).getDescripcion().contains(palabra);
    }
    private void cargarListadoCompleto(){
        listadoActual =new ArrayList<>();
        listadoActual.addAll(PubFactory.getEstiloDeVidaList());
        listadoActual.addAll(PubFactory.getSalasYEstudiosList());
        listadoActual.addAll(PubFactory.getServiciosProfesionalesList());
        listadoActual.addAll(PubFactory.getShowsYEventosList());
        listadoActual.addAll(PubFactory.getVentaDeInstrumentosList());

    }
    private void cargarListadoSalasYEstudios(){
        listadoActual = new ArrayList<>();
        listadoActual.addAll(PubFactory.getSalasYEstudiosList());
    }
    private void cargarListadoVentaDeInstrumentos(){
        listadoActual = new ArrayList<>();
        listadoActual.addAll(PubFactory.getVentaDeInstrumentosList());
    }
    private void cargarListadoEstiloDeVida(int subcategoria){
        listadoActual = new ArrayList<>();
        for (Publicacion publicacion:PubFactory.getEstiloDeVidaList()) {
            EstiloDeVida estiloDeVida = (EstiloDeVida) publicacion;
            if (estiloDeVida.getEstiloVidaId() == subcategoria) {
                listadoActual.add(publicacion);
            }
        }

    }
    private void cargarListadoServiciosProfesionales(){
        listadoActual = new ArrayList<>();
        listadoActual.addAll(PubFactory.getServiciosProfesionalesList());
    }
    private void cargarListadoShowsYEventos(){
        listadoActual = new ArrayList<>();
        listadoActual.addAll(PubFactory.getShowsYEventosList());
    }
    private void obtenerDistancias(List<Publicacion> publicacionList, Location location) {
        float[] results = new float[3];
        Double distancia;
        if (location != null) {
            for (Publicacion item : publicacionList) {
                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                        item.getDireccion().getLatitud(), item.getDireccion().getLongitud(),
                        results);
                distancia = (double) results[0] / 1000;
                item.setDistancia(distancia);
            }
        }
    }
    //-----------------

    //BORRA LA SESION Y LANZA LOGINACTIVITY
    private void cerrarSesion() {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("autologin", 0);
        editor.putString("id", "");
        editor.putString("email", "");
        editor.putString("nombre", "");
        editor.putString("faceid", "");
        editor.putString("imageURI", "");
        editor.apply();
        startActivity(new Intent(MapActivity.this, LoginActivity.class));
        finish();
    }

    //PERMISOS
    private void verificarPermisosLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("map", "Grupo de permisos otorgado: LOCACTION");
                activarLocalizacion(true);

            } else {
                dialogoFaltanPermisos();
            }
        }
    }
    private void solicitarPermisosLocation(){
        ActivityCompat.requestPermissions(
                this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1222);
    }

    //MAPS INTERFACE
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //habilita boton myLocation
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //habilita todos los gestos
        //mMap.getUiSettings().setAllGesturesEnabled(true);
        //implementacion obligatoria google maps para verificar permisos de loclaizacion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            dialogoFaltanPermisos();
            return;
        }
        //habilita la localizacion de maps
        mMap.setMyLocationEnabled(true);
        activarLocalizacion(true);
        cargarMarcadores();
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mostrarItemDetalle(marker);
                return true;
            }
        });
    }
    @Override
    public boolean onMyLocationButtonClick() {
        activarLocalizacion(true);
        return true;
    }
    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            activarLocalizacion(false);
        }
    }

    //ITEM DETALLE
    private void mostrarItemDetalle(Marker marker) {
        ItemDetalleFragment itemDetalleFragment=new ItemDetalleFragment();
        itemDetalleFragment.setAppCompatActivity(this);
        for(Publicacion publicacion:listadoActual){
            if(publicacion.getMarker().equals(marker)){
                itemDetalleFragment.setPublicacion(publicacion);
            }
        }
        FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.itemDetalle, itemDetalleFragment).commit();
        frameLayout.setVisibility(View.VISIBLE);
    }
    public void quitarItemDetalle(){
        frameLayout.setVisibility(View.GONE);
    }

    public void reubicarCamara(Location location) {
        Log.d("reubicarCamara: ", "se ejecuto reubicarCamara()");

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 15f)));

    }
    private void cargarMarcadores() {
        // Marcadores
        if (mMap != null){
            mMap.clear();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pua);
            for (Publicacion publicacion : listadoActual) {
                DecimalFormat df = new DecimalFormat("#.##");
                publicacion.setMarker(mMap.addMarker(new MarkerOptions().position(
                        new LatLng(publicacion.getDireccion().getLatitud(),
                                publicacion.getDireccion().getLongitud()))
                        .title(publicacion.getDatosBasicos().getNombre())
                        .snippet("Distancia no disponible...")
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))));
                if (publicacion.getDistancia() != null)
                    publicacion.getMarker().setSnippet("Distancia " + df.format(publicacion.getDistancia()) + " Km.");
                
            }
        }
    }

    //LOCATION
    public void activarLocalizacion(boolean activar){
        if(activar) {

            //implementacion obligatoria google maps para verificar permisos de loclaizacion
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                dialogoFaltanPermisos();
                return;
            }
            //habilita la localizacion de play services
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                reubicarCamara(location);
                                obtenerDistancias(listadoActual, location);
                                filtrarListadoPorDistanciaMaxima();
                                cargarMarcadores();
                            }else{
                                Toast toast= Toast.makeText(MapActivity.this, "Esperado ubicación.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM,0,200);
                                toast.show();
                            }
                        }
                    });
        }
        else{
            //habilita la localizacion de play services
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                //reubicarCamara(location);
                                obtenerDistancias(listadoActual, location);
                                filtrarListadoPorDistanciaMaxima();
                                cargarMarcadores();
                            }else{
                                Toast toast= Toast.makeText(MapActivity.this, "Esperado ubicación.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM,0,200);
                                toast.show();
                            }
                        }
                    });
        }
    }

    public void mostrarDetalleActivity(Publicacion publicacion){
        Intent intent=new Intent(MapActivity.this, DetalleActivity.class);
        intent.putExtra(CLAVES.ID,publicacion.getDatosBasicos().getId());
        startActivity(intent);
    }
    public void llamar(Publicacion publicacion){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + publicacion.getTelefono().getCodArea()+publicacion.getTelefono().getNumero()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



}
