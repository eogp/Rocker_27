package com.ladoe.rocker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ladoe.rocker.CargaAsincorinca.LoadImage;
import com.ladoe.rocker.Constantes.CLAVES;
import com.ladoe.rocker.Entidades.EstiloDeVida;
import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.TipoPublicacion;
import com.ladoe.rocker.Patrones.Divisor;
import com.ladoe.rocker.Patrones.MyAdapter;
import com.ladoe.rocker.Patrones.PubFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private LoadImage loadImage;
    private LoadImage loadImage2;
    private AlertDialog alert;
    private DrawerLayout mDrawerLayout;
    private ScrollView scrollViewMenu;
    private EditText editTextNombre;
    private EditText editTextMail;
    private ImageView imageViewPerfil;
    private TextView textViewFiltros;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ImageView imageViewOpenDrawer;
    private List<Publicacion> listadoActual;
    //POSISIONAMIENTO
    private FusedLocationProviderClient mFusedLocationClient;
    //CRITERIOS DE ORDEN LISTADO
    private int orden= CLAVES.DISTANCIA;
    //FILTRO
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        //instancias
        mDrawerLayout = findViewById(R.id.drawer_layout);
        imageViewOpenDrawer = findViewById(R.id.imageViewOpenDrawer);
        TextView textViewMapa=findViewById(R.id.textViewMapa);
        textViewFiltros=findViewById(R.id.textViewFiltros);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPref = getSharedPreferences(CLAVES.FILTRO, MODE_PRIVATE);

        //instancias menu
        ImageView imageViewCloseDrawer =findViewById(R.id.imageViewCloseDrawer);
        scrollViewMenu=findViewById(R.id.scrollViewMenu);
        TextView textViewCerrarSesion=findViewById(R.id.textViewCerrarSesion);
        editTextNombre=findViewById(R.id.editTextNombre);
        editTextMail= findViewById(R.id.editTextMail);
        imageViewPerfil=findViewById(R.id.imageViewPerfil) ;


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
        textViewMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loadImage!=null)
                    loadImage.cancel(true);
                startActivity(new Intent(ListActivity.this, MapActivity.class));
                finish();
            }
        });
        textViewFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, FiltroActivity.class);
                intent.putExtra(CLAVES.ORIGEN, CLAVES.LISTACTIVITY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        verificarPermisosLocalizacion();

        //obtencion de orden para listdao
        obtenerOrden();

        //carga de contenido

        cargarDatosUsuario();
        cargarListado();
        cargarRecyclerView(listadoActual,PubFactory.getTipoPublicacionList());
        activarLocalizacion();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(loadImage!=null)
            loadImage.cancel(true);
    }
    @Override
    public void onBackPressed() {
        dialogoSalir();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verificarPermisosLocalizacion();

    }

    //DIALOGOS
    public void dialogoCerrarSesion(){
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
    public void dialogoSalir(){
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
                        solicitarPermisosLocalizacion();
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
    private void cargarDatosUsuario()  {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        Log.d("main","autologin: "+sharedPref.getInt("login",0) );
        Log.d("main","nombre: "+sharedPref.getString("nombre","") );
        Log.d("main","email: "+sharedPref.getString("email","") );
        Log.d("main","faceid: "+sharedPref.getString("faceid","") );

        if(sharedPref.getInt("autologin",0)!=1){
            editTextNombre.setEnabled(false);
            editTextMail.setEnabled(false);
        }
        if(!sharedPref.getString("nombre","").equals("null"))  editTextNombre.setText(sharedPref.getString("nombre",""));
        if(!sharedPref.getString("email","").equals("null")) editTextMail.setText(sharedPref.getString("email",""));
        if(!sharedPref.getString("faceid","").equals("null")) {
            loadImage=new LoadImage(imageViewPerfil, this);
            loadImage.execute("https://graph.facebook.com/" + sharedPref.getString("faceid","") + "/picture?width=500&height=500");
            loadImage2=new LoadImage(imageViewOpenDrawer,this);
            loadImage2.execute("https://graph.facebook.com/" + sharedPref.getString("faceid", "") + "/picture?width=500&height=500");
        }
    }

    //CARGA Y Y FILTRADO DE LISTADO
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
                    distanciaMaxima = 3.00;
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
        if(subcategoria!=0) {
            for (Publicacion publicacion : PubFactory.getEstiloDeVidaList()) {
                EstiloDeVida estiloDeVida = (EstiloDeVida) publicacion;
                if (estiloDeVida.getEstiloVidaId() == subcategoria) {
                    listadoActual.add(publicacion);
                }
            }
        }else {
            listadoActual.addAll(PubFactory.getEstiloDeVidaList());
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

    //CARGA INICIAL DE RECYCLER VIEW
    private void cargarRecyclerView(List<Publicacion> myDataset, List<TipoPublicacion> tipoPublicacionList){
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Divisor divisor= new Divisor(ContextCompat.getDrawable(this, R.drawable.divider));
        mRecyclerView.addItemDecoration(divisor);


        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset, tipoPublicacionList, this, new Intent(ListActivity.this, DetalleActivity.class));
        mRecyclerView.setAdapter(mAdapter);
    }
    //---------------------------------------------------------

    //PERMISOS LOCATION
    private void verificarPermisosLocalizacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("map", "Grupo de permisos otorgado: LOCACTION");
                activarLocalizacion();

            } else {
                dialogoFaltanPermisos();
            }

        }
    }
    private void solicitarPermisosLocalizacion(){
        ActivityCompat.requestPermissions(
                this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1222);
    }

    //LOCALIZACION Y ACTUALIZACION DE LISTADOS
    private void activarLocalizacion(){

        //implementacion obligatoria para verificar permisos de loclaizacion
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
                            obtenerDistancias(listadoActual,location);
                            filtrarListadoPorDistanciaMaxima();
                            ordenarListado(listadoActual);
                            actualizarRecyclerView(listadoActual);
                        }else{
                            Toast toast= Toast.makeText(ListActivity.this, "Esperado ubicación.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM,0,200);
                            toast.show();
                        }
                    }
                });


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
    private void ordenarListado(List<Publicacion> publicacionList){
        switch (orden){
            case CLAVES.DISTANCIA:
                Collections.sort(publicacionList, new Comparator() {
                    @Override
                    public int compare(Object obj1, Object obj2) {
                        Publicacion pub1 = (Publicacion) obj1;
                        Publicacion pub2 = (Publicacion) obj2;
                        return pub1.getDistancia().compareTo(pub2.getDistancia());
                    }
                });
                break;
            case CLAVES.NOMBRE:
                Collections.sort(publicacionList, new Comparator() {
                    @Override
                    public int compare(Object obj1, Object obj2) {
                        Publicacion pub1 = (Publicacion) obj1;
                        Publicacion pub2 = (Publicacion) obj2;
                        return pub1.getDatosBasicos().getNombre().compareTo(pub2.getDatosBasicos().getNombre());
                    }
                });
                break;
            case CLAVES.TIPO:

                Collections.sort(publicacionList, new Comparator() {
                    @Override
                    public int compare(Object obj1, Object obj2) {
                        Publicacion pub1 = (Publicacion) obj1;
                        Publicacion pub2 = (Publicacion) obj2;

                        return PubFactory.getTipoPublicacionList().get(pub1.getDatosBasicos().getTipoPub()-1).getDescripcion().compareTo(
                                PubFactory.getTipoPublicacionList().get(pub2.getDatosBasicos().getTipoPub()-1).getDescripcion());
                    }
                });
                break;
        }


    }
    private void actualizarRecyclerView(List<Publicacion> publicacionList){
        mAdapter.setmDataset(publicacionList);
        mAdapter.notifyDataSetChanged();
    }

    //FILTROS Y ORDEN LISTADO
    private void obtenerOrden() {
        if(sharedPref.getBoolean(CLAVES.FILTRAR, false)){
            orden=sharedPref.getInt(CLAVES.ORDEN, 0);
        }

    }

    //BORRA LA SESION Y LANZA LOGINACTIVITY
    private void cerrarSesion(){
        SharedPreferences sharedPref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("autologin", 0);
        editor.putString("id","");
        editor.putString("email","");
        editor.putString("nombre","");
        editor.putString("faceid","");
        editor.putString("imageURI","");
        editor.apply();
        startActivity(new Intent(ListActivity.this, LoginActivity.class));
        finish();
    }

    public void llamar(Publicacion publicacion){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + publicacion.getTelefono().getCodArea()+publicacion.getTelefono().getNumero()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
