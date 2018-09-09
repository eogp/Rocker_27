package com.ladoe.rocker;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.ladoe.rocker.Constantes.CLAVES;
import com.ladoe.rocker.Fragments.SplashFragment_1;
import com.ladoe.rocker.Fragments.SplashFragment_2;
import com.ladoe.rocker.Fragments.SplashFragment_3;
import com.ladoe.rocker.Patrones.PubFactory;
import com.ladoe.rocker.Patrones.SectionsPagerAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class SplashActivity extends AppCompatActivity {

    private AsyncHttpClient oHttpClient;
    private ViewPager viewPager;
    private SectionsPagerAdapter pagerAdapter;
    private ImageView arrow;
    private List<ImageView> indicador=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        cargarPager();

        try {
            cargarPublicaciones();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        oHttpClient.cancelAllRequests(true);
        finish();
    }

    private void cargarPager() {
        SplashFragment_1 splashFragment_1=new SplashFragment_1();
        SplashFragment_2 splashFragment_2=new SplashFragment_2();
        SplashFragment_3 splashFragment_3=new SplashFragment_3();

        this.viewPager=findViewById(R.id.pager);
        this.pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.pagerAdapter.addFragment(splashFragment_1);
        this.pagerAdapter.addFragment(splashFragment_2);
        this.pagerAdapter.addFragment(splashFragment_3);
        this.viewPager.setAdapter(pagerAdapter);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicarPosicion(position);
            }

            @Override
            public void onPageSelected(int position) {
                indicarPosicion(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
    }

    //CARGA INICIAL-------------------------------------------
    //OBTENCION DE JSON DEL BACKEND
    private void cargarPublicaciones() throws JSONException, UnsupportedEncodingException {
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("user","Super_User@rockerapp.com");
        oJSONObject.put("pass","Super_Pass");
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        oHttpClient = new AsyncHttpClient();

        oHttpClient.post(getApplicationContext(),"https://www.rockerapp.com/publicacionesApp.php", oEntity, "application/json" ,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                System.out.println(statusCode);
                try {
                    String content = new String(responseBody, "UTF-8");
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("status").equals("1")) {
                        //obtencion de listas de publicaciones, y cargar inicial recyclerView
                        parseoPublicaicones(obj);

                    }else {

                        dialogoErrorCargaPublicaciones();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialogoErrorCargaPublicaciones();
                switch (statusCode){
                    case 404:
                        Log.d("login", "Error 404 !");
                        break;
                    case 500:
                        Log.d("login", "Error 500 !");
                        break;
                    case 403:
                        Log.d("login", "Error 403 !");
                        break;
                }
            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                System.out.println(retryNo);
            }
        });
    }

    //PARSEO JSON A LIST PUBLICACIONES
    private void parseoPublicaicones(JSONObject obj){
        PubFactory.generarListas(obj);
        //iniciarApp();
        habilitarAcceso();
    }

    //HABILITA BTN DE ACCESO
    private void habilitarAcceso(){
        arrow=findViewById(R.id.imageViewArrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarApp();
            }
        });
        arrow.setVisibility(View.VISIBLE);
    }
    
    //CAMBIA INDICADOR DE POSICION SWIPE
    public void indicarPosicion(int position){
        if(indicador.isEmpty()){
            indicador.add((ImageView)findViewById(R.id.circulo1));
            indicador.add((ImageView)findViewById(R.id.circulo2));
            indicador.add((ImageView)findViewById(R.id.circulo3));
        }
        for (ImageView imageView:indicador) {
            imageView.setImageResource(R.mipmap.punto_splash);
        }
        indicador.get(position).setImageResource(R.mipmap.punto_splash_seleccionado);
    }

    //INICIO APP
    private void iniciarApp(){
        /*
         * ESTADOS DE LOGUEO
         * 0	=> no loguin
         * 1	=> login / registro
         * 2	=> login face
         */
        borrarFiltro();
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        int autologin = sharedPref.getInt("autologin", 0);
        Log.d("splash", "autologin: "+autologin);
        if (autologin != 0) {
            startActivity(new Intent(SplashActivity.this, ListActivity.class));

        }else{
            startActivity(new Intent(SplashActivity.this, LoginActivity2.class));

        }
        finish();
    }

    //BORRADO DE FILTRO
    private void borrarFiltro() {
        SharedPreferences sharedPrefFiltro = getSharedPreferences(CLAVES.FILTRO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefFiltro.edit();
        editor.putBoolean(CLAVES.FILTRAR, false);
        editor.apply();
    }

    //DIALOGOS
    public void dialogoErrorCargaPublicaciones() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Rocker no se puede sincronizar. Verifica tu conexion a internet.")
                .setCancelable(false)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        try {
                            cargarPublicaciones();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        finish();

                    }
                }
        );
        AlertDialog alert = builder.create();
        alert.show();

    }
}
