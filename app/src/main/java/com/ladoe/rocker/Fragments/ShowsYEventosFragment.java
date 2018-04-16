package com.ladoe.rocker.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.ShowsYEventos;
import com.ladoe.rocker.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowsYEventosFragment extends Fragment {

    private Publicacion publicacion;

    public ShowsYEventosFragment() {
        // Required empty public constructor
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shows_yeventos, container, false);

        //carga de datos
        cargaDescripcion(view);
        cargarArtistas(view);

        return view;

    }

    private void cargaDescripcion(View view) {
        TextView textViewDescripcion=view.findViewById(R.id.textViewDescripcion);
        TextView textViewfaltan=view.findViewById(R.id.textViewFaltan);
        String descripcion="";
        if(publicacion!=null){
            ShowsYEventos showsYEventos=(ShowsYEventos)publicacion;
            //CALCULA Y VISUALIZA EL TIEMPO RESTANTE PARA LA PROXIMA FECHA DEL EVENTO
            for(String fechaYHora: showsYEventos.getFechas()){
                if(tiempoParaElEvento(fechaYHora)!=null){
                    textViewfaltan.setText(tiempoParaElEvento(fechaYHora));
                    textViewfaltan.setVisibility(View.VISIBLE);
                    break;
                }
            }
            //OBTINENE Y VISUALIZA LAS FECHAS DEL EVENTO Y EL VALOR DE LA ENTRADA
            descripcion+=showsYEventos.getShowEvento() +"<br />";
            for(String fechaYHora: showsYEventos.getFechas()){
                descripcion+=obtenerFecha(fechaYHora)+"<br />";
                descripcion+="A las "+obtenerHora(fechaYHora)+" hs.<br />";
            }
            descripcion+="Entradas desde $"+ String.format("%.2f", showsYEventos.getValor());
            textViewDescripcion.setText(Html.fromHtml(descripcion));
        }
    }

    //RETORNA LA HORA CON FORMATO
    private String obtenerHora(String fechaYHora) {
        String arrayFechaYHora[]=fechaYHora.split(" ");
        String hora=arrayFechaYHora[1].substring(0,5);
        return hora;
    }

    //RETORNA LA FECHA CON FORMATO
    private String obtenerFecha(String fechaYHora) {
        String arrayFechaYHora[]=fechaYHora.split(" ");
        String arrayFecha[]=arrayFechaYHora[0].split("-");
        Log.d("fecha",arrayFecha[0]+" "+arrayFecha[1]+" "+arrayFecha[2]);
        Integer intAnio=Integer.parseInt(arrayFecha[0]);
        Integer intMes=Integer.parseInt(arrayFecha[1]);
        Integer intDia=Integer.parseInt(arrayFecha[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(intAnio,intMes,intDia);

        String[] strDays = new String[]{
                "Domingo",
                "Lunes",
                "Martes",
                "Miercoles",
                "Jueves",
                "Viernes",
                "Sabado"};

        String[] strMonths = new String[]{
                "Enero",
                "Febebro",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Septiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"};

        String dia=strDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        String mes=strMonths[calendar.get(Calendar.MONTH) - 1];

        return dia+" "+intDia+" de "+mes+" de "+intAnio;
    }

    //RETRONA EL TIEMPO RESTANTE PARA LA PROXIMA FECHA DE UN EVENTO
    //SI LA FECHA YA PASO RETORNA NULL
    private String tiempoParaElEvento(String fechaYHora){
        String arrayFechaYHora[]=fechaYHora.split(" ");
        String arrayFecha[]=arrayFechaYHora[0].split("-");
        String arrayHora[]=arrayFechaYHora[1].split(":");
        Log.d("fecha",arrayFecha[0]+" "+arrayFecha[1]+" "+arrayFecha[2]);
        Integer intAnio=Integer.parseInt(arrayFecha[0]);
        Integer intMes=Integer.parseInt(arrayFecha[1]);
        Integer intDia=Integer.parseInt(arrayFecha[2]);
        Log.d("hora",arrayHora[0]);
        Integer intHora=Integer.parseInt(arrayHora[0]);
        Integer intMin=Integer.parseInt(arrayHora[1]);
        //FECHA ACTUAL
        Calendar hoy = Calendar.getInstance();
        //FECHA DEL EVENTO
        Calendar evento = new GregorianCalendar();
        evento.set(intAnio, intMes-1, intDia, intHora, intMin);
        //IDEM EVENTO PARA CALCULAR EL TIEMPO RESTANTE
        Calendar diferencia= new GregorianCalendar();
        diferencia.set(intAnio, intMes-1, intDia, intHora, intMin);

        diferencia.add(Calendar.MINUTE, -hoy.get(Calendar.MINUTE));
        diferencia.add(Calendar.HOUR_OF_DAY, -hoy.get(Calendar.HOUR_OF_DAY));
        diferencia.add(Calendar.DAY_OF_MONTH, -hoy.get(Calendar.DAY_OF_MONTH));
        diferencia.add(Calendar.MONTH, -hoy.get(Calendar.MONTH));
        diferencia.add(Calendar.YEAR, -hoy.get(Calendar.YEAR));

        Log.d("evento", "año "+evento.get(Calendar.YEAR)+
                ", mes "+evento.get(Calendar.MONTH)+
                ", dia "+evento.get(Calendar.DAY_OF_MONTH)+
                ", hora "+evento.get(Calendar.HOUR_OF_DAY)+
                ", minuto "+evento.get(Calendar.MINUTE));
        Log.d("hoy", "año "+hoy.get(Calendar.YEAR)+
                ", mes "+hoy.get(Calendar.MONTH)+
                ", dia "+hoy.get(Calendar.DAY_OF_MONTH)+
                ", hora "+hoy.get(Calendar.HOUR_OF_DAY)+
                ", minuto "+hoy.get(Calendar.MINUTE));
        Log.d("faltan", "faltan "+diferencia.get(Calendar.MONTH)+" meses, "
                + diferencia.get(Calendar.DAY_OF_MONTH)+" días, "
                + diferencia.get(Calendar.HOUR_OF_DAY)+" horas, "
                + "y " + diferencia.get(Calendar.MINUTE)+" minutos");

        String retorno=null;

        //SI LA FECHA ACTUAL ES MENOR AL EVENTO CARGA LA DIFERENCIA CON FORMATO
        if(hoy.compareTo(evento)<1) {
            retorno = "Faltan ";
            if (diferencia.get(Calendar.MONTH) > 0)
                retorno += diferencia.get(Calendar.MONTH) + " meses ";
            if (diferencia.get(Calendar.DAY_OF_MONTH) > 0)
                retorno += diferencia.get(Calendar.DAY_OF_MONTH) + " días ";
            if (diferencia.get(Calendar.HOUR_OF_DAY) > 0)
                retorno += diferencia.get(Calendar.HOUR_OF_DAY) + " horas ";
            if (diferencia.get(Calendar.MINUTE) > 0)
                retorno += " y " + diferencia.get(Calendar.MINUTE) + " minutos";
        }

        return retorno;
    }

    //VISUALIZA LOS ARTISTAS PARA EL EVENTO
    private void cargarArtistas(View view) {
        TextView textViewArtistas=view.findViewById(R.id.textViewArtistas);

        if(publicacion!=null){
            ShowsYEventos showsYEventos=(ShowsYEventos)publicacion;
            textViewArtistas.setText(showsYEventos.getBandas());
        }


    }

}
