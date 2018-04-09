package com.ladoe.rocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ladoe.rocker.Constantes.CLAVES;
import com.ladoe.rocker.Entidades.TipoPublicacion;
import com.ladoe.rocker.Patrones.PubFactory;

import java.util.ArrayList;
import java.util.List;


public class FiltroActivity extends AppCompatActivity {
    private ImageView imageVieBack;
    private Spinner spinnerDistanciaMaxima;
    private Spinner spinnerCategoria;
    private Spinner spinnerSubCategoria;
    private Spinner spinnerOrdenarPor;
    private EditText editTextpalabraClave;
    private View dividerOrdenarPor;
    private TextView textViewOrdenarPor;
    private TextView textViewBorrar;
    private TextView textViewContiuar;
    private List<String> distancias;
    private List<String> categorias;
    private List<String> ordenarPor;
    private List<String> subCategorias;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        //instancias
        spinnerDistanciaMaxima=(Spinner)findViewById(R.id.spinnerDistanciaMaxima);
        spinnerCategoria=(Spinner)findViewById(R.id.spinnerCategoria);
        spinnerSubCategoria=(Spinner)findViewById(R.id.spinnerSubCategoria);
        spinnerOrdenarPor=(Spinner)findViewById(R.id.spinnerOrdenarPor);
        editTextpalabraClave=(EditText)findViewById(R.id.editTextPalabraClave);
        imageVieBack=(ImageView) findViewById(R.id.imageViewBack);
        textViewBorrar=(TextView) findViewById(R.id.textViewBorrar);
        textViewContiuar=(TextView) findViewById(R.id.textViewContinuar);
        textViewOrdenarPor=(TextView)findViewById(R.id.textViewOrdenarPor);
        dividerOrdenarPor=(View)findViewById(R.id.dividerOrdenarPor) ;
        //listener
        imageVieBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textViewBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarSeleccion();
                startActivity(generarIntentOrigen());
            }
        });
        textViewContiuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarSeleccion();
                startActivity(generarIntentOrigen());

            }
        });

        //carga de datos
        cargarDistancias();
        cargarCategorias();
        cargarSubCategorias();
        cargarOrdenarPor();

        //ocultar campos segun origen
        if(obtenerOrigen()== CLAVES.MAPACTIVITY) {
            dividerOrdenarPor.setVisibility(View.GONE);
            textViewOrdenarPor.setVisibility(View.GONE);
            spinnerOrdenarPor.setVisibility(View.GONE);


        }

        //carga de filtro activo
        SharedPreferences sharedPref = getSharedPreferences(CLAVES.FILTRO, MODE_PRIVATE);
        if(sharedPref.getBoolean(CLAVES.FILTRAR,false)) {
            spinnerDistanciaMaxima.setSelection(sharedPref.getInt(CLAVES.DISTANCIA_MAXIMA, 0));
            spinnerCategoria.setSelection(sharedPref.getInt(CLAVES.CATEGORIA, 0));
            spinnerSubCategoria.setSelection(sharedPref.getInt(CLAVES.SUB_CATEGORIA, 0));
            spinnerOrdenarPor.setSelection(sharedPref.getInt(CLAVES.ORDEN, 0));
            editTextpalabraClave.setText(sharedPref.getString(CLAVES.PALABRA_CLAVE, ""));
        }

    }


    //CARGA DE SPINNERS
    private void cargarOrdenarPor() {
        ordenarPor=new ArrayList<>();
        ordenarPor.add("Elija una opción...");
        ordenarPor.add("Distancia mas cercana");
        ordenarPor.add("Nombre de la publicación");
        ordenarPor.add("Tipo de publicación");
        spinnerOrdenarPor.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ordenarPor){

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        });

        spinnerOrdenarPor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void cargarDistancias(){
        distancias=new ArrayList<>();
        distancias.add("Distancia máxima");
        distancias.add("1 km");
        distancias.add("2 km");
        distancias.add("5 km");
        distancias.add("10 km");
        distancias.add("15 km");
        spinnerDistanciaMaxima.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, distancias){

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        });
        spinnerDistanciaMaxima.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void cargarCategorias(){
        categorias=new ArrayList<>();
        categorias.add("Categoria");
        for (TipoPublicacion tipoPublicacion:
             PubFactory.getTipoPublicacionList()) {
            categorias.add(tipoPublicacion.getDescripcion());
        }
        spinnerCategoria.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categorias){

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        });
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==3)
                    spinnerSubCategoria.setVisibility(View.VISIBLE);
                else
                    spinnerSubCategoria.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void cargarSubCategorias(){
        subCategorias=new ArrayList<>();
        subCategorias.add("Sub-categoria");
        subCategorias.addAll(PubFactory.getEstilosVida());
        spinnerSubCategoria.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, subCategorias){

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);

                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        });
        spinnerSubCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //GUARDAR DATOS
    private int obtenerOrigen(){
        Bundle extras = getIntent().getExtras();
        return extras.getInt(CLAVES.ORIGEN);
    }

    private Intent generarIntentOrigen(){
        Intent intent=null;
        if(obtenerOrigen()==CLAVES.LISTACTIVITY)
            intent=new Intent(FiltroActivity.this,ListActivity.class );
        if(obtenerOrigen()== CLAVES.MAPACTIVITY)
            intent=new Intent(FiltroActivity.this,MapActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        return intent;
    }

    private void borrarSeleccion(){
        SharedPreferences sharedPref = getSharedPreferences(CLAVES.FILTRO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CLAVES.FILTRAR, false);
        editor.commit();
    }

    public void guardarSeleccion(){
        SharedPreferences sharedPref = getSharedPreferences(CLAVES.FILTRO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(spinnerOrdenarPor.getSelectedItemPosition()!=0 ||
                spinnerCategoria.getSelectedItemPosition()!=0 ||
                spinnerSubCategoria.getSelectedItemPosition()!=0 ||
                spinnerDistanciaMaxima.getSelectedItemPosition()!=0 ||
                !editTextpalabraClave.getText().toString().equals("")) {
            editor.putBoolean(CLAVES.FILTRAR, true);
            editor.putInt(CLAVES.DISTANCIA_MAXIMA, spinnerDistanciaMaxima.getSelectedItemPosition());
            editor.putInt(CLAVES.CATEGORIA, spinnerCategoria.getSelectedItemPosition());
            editor.putInt(CLAVES.SUB_CATEGORIA, spinnerSubCategoria.getSelectedItemPosition());
            editor.putString(CLAVES.PALABRA_CLAVE, editTextpalabraClave.getText().toString());
            editor.putInt(CLAVES.ORDEN, spinnerOrdenarPor.getSelectedItemPosition());
            Log.d("filtro", "a"+editTextpalabraClave.getText().toString()+"a");
            Log.d("filtro", "a"+spinnerCategoria.getSelectedItemPosition()+"a");
            Log.d("filtro", "a"+spinnerSubCategoria.getSelectedItemPosition()+"a");
            Log.d("filtro", "a"+spinnerDistanciaMaxima.getSelectedItemPosition()+"a");
            Log.d("filtro", "a"+spinnerOrdenarPor.getSelectedItemPosition()+"a");

        }else{
            editor.putBoolean(CLAVES.FILTRAR, false);

        }
        editor.commit();

    }

}
