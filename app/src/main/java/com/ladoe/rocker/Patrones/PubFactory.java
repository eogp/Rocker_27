package com.ladoe.rocker.Patrones;

import android.util.Log;

import com.ladoe.rocker.Entidades.EstiloDeVida;
import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.SalasYEstudios;
import com.ladoe.rocker.Entidades.ServiciosProfesionales;
import com.ladoe.rocker.Entidades.ShowsYEventos;
import com.ladoe.rocker.Entidades.SubTipos.DatosBasicos;
import com.ladoe.rocker.Entidades.SubTipos.Direccion;
import com.ladoe.rocker.Entidades.SubTipos.Equipos;
import com.ladoe.rocker.Entidades.SubTipos.Horarios;
import com.ladoe.rocker.Entidades.SubTipos.Imagen;
import com.ladoe.rocker.Entidades.SubTipos.Instrumento;
import com.ladoe.rocker.Entidades.SubTipos.Telefono;
import com.ladoe.rocker.Entidades.SubTipos.Video;
import com.ladoe.rocker.Entidades.TipoPublicacion;
import com.ladoe.rocker.Entidades.VentaDeInstrumentos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by enriquegomezpena on 28/3/18.
 */

public class PubFactory {

    private static List<EstiloDeVida> estiloDeVidaList=new ArrayList<>();;
    private static List<VentaDeInstrumentos> ventaDeInstrumentosList=new ArrayList<>();
    private static List<SalasYEstudios> salasYEstudiosList=new ArrayList<>();
    private static List<ServiciosProfesionales> serviciosProfesionalesList=new ArrayList<>();
    private static List<ShowsYEventos> showsYEventosList=new ArrayList<>();
    private static List<TipoPublicacion> tipoPublicacionList=new ArrayList<>();
    private static List<String> estilosVida =new ArrayList<>();

    public static List<String> getEstilosVida() {
        return estilosVida;
    }

    public static void setEstilosVida(List<String> estilosVidanew) {
        PubFactory.estilosVida = estilosVidanew;
    }

    public static List<? extends Publicacion> getEstiloDeVidaList() {
        return estiloDeVidaList;
    }

    public static void setEstiloDeVidaList(List<EstiloDeVida> estiloDeVidaList) {
        PubFactory.estiloDeVidaList = estiloDeVidaList;
    }

    public static List<? extends Publicacion> getVentaDeInstrumentosList() {
        return ventaDeInstrumentosList;
    }

    public static void setVentaDeInstrumentosList(List<VentaDeInstrumentos> ventaDeInstrumentosList) {
        PubFactory.ventaDeInstrumentosList = ventaDeInstrumentosList;
    }

    public static List<? extends Publicacion> getSalasYEstudiosList() {
        return salasYEstudiosList;
    }

    public static void setSalasYEstudiosList(List<SalasYEstudios> salasYEstudiosList) {
        PubFactory.salasYEstudiosList = salasYEstudiosList;
    }

    public static List<? extends Publicacion> getServiciosProfesionalesList() {
        return serviciosProfesionalesList;
    }

    public static void setServiciosProfesionalesList(List<ServiciosProfesionales> serviciosProfesionalesList) {
        PubFactory.serviciosProfesionalesList = serviciosProfesionalesList;
    }

    public static List<? extends Publicacion> getShowsYEventosList() {
        return showsYEventosList;
    }

    public static void setShowsYEventosList(List<ShowsYEventos> showsYEventosList) {
        PubFactory.showsYEventosList = showsYEventosList;
    }

    public static List<TipoPublicacion> getTipoPublicacionList() {
        return tipoPublicacionList;
    }

    public static void setTipoPublicacionList(List<TipoPublicacion> tipoPublicacionList) {
        PubFactory.tipoPublicacionList = tipoPublicacionList;
    }

    public static void generarListas(JSONObject jsonObject){

        try {

            //LISTA TIPOS DE SUBCATEGORIAS
            JSONArray jsonArrayEstilosVida = jsonObject.getJSONArray("estiloVida");
            //Log.d("factory", jsonArrayEstilosVida.toString());
            for (int i=0; i<jsonArrayEstilosVida.length(); i++){
                estilosVida.add(jsonArrayEstilosVida.getJSONObject(i).optString("descripcion"));
            }

            //LISTA TIPOS DE PUBLICACIONES
            JSONArray jsonArrayTiposPublicaciones = jsonObject.getJSONArray("tiposPub");
            //Log.d("factory", jsonArrayTiposPublicaciones.toString());
            for (int i=0; i<jsonArrayTiposPublicaciones.length(); i++){
                TipoPublicacion tipoPublicacion=new TipoPublicacion();
                tipoPublicacion.setId(jsonArrayTiposPublicaciones.getJSONObject(i).optInt("id"));
                tipoPublicacion.setDescripcion(jsonArrayTiposPublicaciones.getJSONObject(i).optString("descripcion"));
                tipoPublicacionList.add(tipoPublicacion);
            }

            //LISTA PUBLICACIONES
            JSONArray jsonArrayPublicaciones = jsonObject.getJSONArray("publicaciones");
            for (int i=0;i<jsonArrayPublicaciones.length();i++) {
                //datos generales
                JSONObject jsonObjectPublicacion=jsonArrayPublicaciones.getJSONObject(i).getJSONObject("publicacion");
                //Log.d("factory", jsonObjectPublicacion.toString());
                JSONObject jsonObjectDatosBasicos=jsonObjectPublicacion.getJSONObject("datosBasicos");
                DatosBasicos datosBasicos = new DatosBasicos();
                datosBasicos.setId(jsonObjectDatosBasicos.optInt("id"));
                datosBasicos.setNombre(jsonObjectDatosBasicos.optString("nombre"));
                datosBasicos.setDescripcion(jsonObjectDatosBasicos.optString("descripcion"));
                datosBasicos.setEmail(jsonObjectDatosBasicos.optString("email"));
                datosBasicos.setTipoPub(jsonObjectDatosBasicos.optInt("tipoPub_id"));
                datosBasicos.setWeb(jsonObjectDatosBasicos.optString("web"));

                JSONObject jsonObjectDireccion=jsonObjectPublicacion.getJSONObject("direccion");
                Direccion direccion=new Direccion();
                direccion.setAltura(jsonObjectDireccion.optInt("altura"));
                direccion.setCalle(jsonObjectDireccion.optString("calle"));
                direccion.setLocalidad(jsonObjectDireccion.optString("localidad"));
                direccion.setCiudad(jsonObjectDireccion.optString("ciudad"));
                direccion.setPartido(jsonObjectDireccion.optString("partido"));
                direccion.setCp(jsonObjectDireccion.optString("cp"));
                direccion.setPais(jsonObjectDireccion.optString("pais"));
                direccion.setProvincia(jsonObjectDireccion.optString("provincia"));
                direccion.setLatitud(jsonObjectDireccion.optDouble("latitud"));
                direccion.setLongitud(jsonObjectDireccion.optDouble("longitud"));

                JSONObject jsonObjectTelefono=jsonObjectPublicacion.getJSONObject("telefono");
                Telefono telefono=new Telefono();
                telefono.setNumero(jsonObjectTelefono.optInt("numero"));
                telefono.setCodArea(jsonObjectTelefono.optInt("codarea"));
                telefono.setCodPais(jsonObjectTelefono.optInt("codpais"));
                telefono.setCelular(jsonObjectTelefono.optBoolean("celular"));

                JSONArray jsonArrayImagenes=jsonObjectPublicacion.getJSONArray("imagenes");//recorrer
                List<Imagen> imagenList=new ArrayList<>();
                for (int a=0;a<jsonArrayImagenes.length();a++){
                    JSONObject jsonObjectImagen=jsonArrayImagenes.getJSONObject(a);
                    imagenList.add(new Imagen(jsonObjectImagen.optString("uri")));
                    //Log.d("factory", jsonArrayImagenes.getJSONObject(a).toString());
                }

                JSONObject jsonObjectVideo=jsonObjectPublicacion.getJSONObject("video");
                Video video=new Video(jsonObjectVideo.optString("uri"));

                switch (jsonObjectPublicacion.getInt("tipoPub")){
                    case 1:
                        //salas y estudios
                        //Log.d("factory", jsonObjectPublicacion.toString());
                        //datos generales
                        SalasYEstudios salasYEstudios=new SalasYEstudios();
                        salasYEstudios.setDatosBasicos(datosBasicos);
                        salasYEstudios.setDireccion(direccion);
                        salasYEstudios.setTelefono(telefono);
                        salasYEstudios.setImagenList(imagenList);
                        salasYEstudios.setVideo(video);

                        //datos especificos
                        JSONArray jsonArrayEquipos=jsonObjectPublicacion.getJSONArray("equipos");//recorrer
                        List<Equipos> equiposList=new ArrayList<>();
                        for (int e=0;e<jsonArrayEquipos.length();e++){
                            JSONObject equipo=jsonArrayEquipos.getJSONObject(e);
                            JSONObject tipo=equipo.getJSONObject("tipo");
                            JSONObject marca=equipo.getJSONObject("marca");
                            equiposList.add(new Equipos(tipo.optString("descripcion"),marca.optString("descripcion")));
                            //Log.d("factory", jsonArrayEquipos.getJSONObject(e).toString());
                        }

                        JSONArray jsonArrayServicios=jsonObjectPublicacion.getJSONArray("servicios");//recorrer
                        List<String> serviciosList= new ArrayList<>();
                        for (int o=0;o<jsonArrayServicios.length();o++){
                            JSONObject servicio=jsonArrayServicios.getJSONObject(o);
                            serviciosList.add(servicio.optString("descripcion"));
                            //Log.d("factory", jsonArrayServicios.getJSONObject(o).toString());
                        }
                        JSONObject jsonObjectSalas=jsonObjectPublicacion.getJSONObject("salas");
                        JSONObject jsonObjectPrecioHora=jsonObjectPublicacion.getJSONObject("precioHora");

                        salasYEstudios.setEquiposList(equiposList);
                        salasYEstudios.setServiciosList(serviciosList);
                        salasYEstudios.setSalas(jsonObjectSalas.optInt("cantidad"));
                        salasYEstudios.setPrecioHora(jsonObjectPrecioHora.optDouble("valor"));

                        salasYEstudiosList.add(salasYEstudios);

                        break;
                    case 2:
                        //venta de instrumentos
                        Log.d("factory", jsonObjectPublicacion.toString());
                        //datos generales
                        VentaDeInstrumentos ventaDeInstrumentos=new VentaDeInstrumentos();
                        ventaDeInstrumentos.setDatosBasicos(datosBasicos);
                        ventaDeInstrumentos.setDireccion(direccion);
                        ventaDeInstrumentos.setTelefono(telefono);
                        ventaDeInstrumentos.setImagenList(imagenList);
                        ventaDeInstrumentos.setVideo(video);

                        //datos especificos
                        JSONObject jsonObjectInstrumento=jsonObjectPublicacion.getJSONObject("instrumento");
                        JSONObject jsonObjectTipo=jsonObjectInstrumento.getJSONObject("tipo");
                        JSONObject jsonObjectInstrumentoDatos=jsonObjectPublicacion.getJSONObject("instrumento_datos");
                        JSONObject jsonObjectPais=jsonObjectPublicacion.getJSONObject("pais");
                        JSONObject jsonObjectValor=jsonObjectPublicacion.getJSONObject("valor");

                        Instrumento instrumento=new Instrumento();
                        instrumento.setTipo(jsonObjectTipo.optString("descripcion"));
                        if(!jsonObjectTipo.optString("descripcion").equals("Otros")){
                            JSONObject jsonObjectMarca=jsonObjectInstrumento.getJSONObject("marca");
                            instrumento.setMarca(jsonObjectMarca.optString("descripcion"));
                        }
                        instrumento.setAnio(jsonObjectInstrumentoDatos.optInt("anio"));
                        instrumento.setEstado(jsonObjectInstrumentoDatos.optString("estado"));
                        instrumento.setOtro(jsonObjectInstrumentoDatos.optString("otro"));
                        instrumento.setPais(jsonObjectPais.optString("nombre"));
                        instrumento.setValor(jsonObjectValor.optDouble("valor"));

                        ventaDeInstrumentos.setInstrumento(instrumento);

                        ventaDeInstrumentosList.add(ventaDeInstrumentos);

                        break;
                    case 3:
                        //estilo de vida
                        //datos generales
                        //Log.d("factory", jsonObjectPublicacion.toString());
                        EstiloDeVida estiloDeVida=new EstiloDeVida();
                        estiloDeVida.setDatosBasicos(datosBasicos);
                        estiloDeVida.setDireccion(direccion);
                        estiloDeVida.setTelefono(telefono);
                        estiloDeVida.setImagenList(imagenList);
                        estiloDeVida.setVideo(video);

                        //datos especificos
                        JSONObject jsonObjectEstiloVida=jsonObjectPublicacion.getJSONObject("estiloVida");
                        JSONArray jsonArrayProductos=jsonObjectPublicacion.getJSONArray("produtos");
                        List<String> productos=new ArrayList<>();
                        for (int e = 0; e < jsonArrayProductos.length(); e++){
                            productos.add(jsonArrayProductos.getJSONObject(e).optString("descripcion"));
                        }
                        JSONArray jsonArrayHorarios=jsonObjectPublicacion.getJSONArray("horarios");
                        List<Horarios> horarios=new ArrayList<>();
                        for (int e = 0; e < jsonArrayHorarios.length(); e++){
                            Horarios horario=new Horarios();
                            horario.setDesdeDia(jsonArrayHorarios.getJSONObject(e).optString("desdeDia"));
                            horario.setHastaDia(jsonArrayHorarios.getJSONObject(e).optString("hastaDia"));
                            horario.setDesdeHora(jsonArrayHorarios.getJSONObject(e).optString("desdeHora"));
                            horario.setHastaHora(jsonArrayHorarios.getJSONObject(e).optString("hastaHora"));
                            horarios.add(horario);
                        }
                        estiloDeVida.setEstiloVida(jsonObjectEstiloVida.optString("descripcion"));
                        estiloDeVida.setEstiloVidaId(jsonObjectEstiloVida.optInt("id"));
                        estiloDeVida.setProductos(productos);
                        estiloDeVida.setHorarios(horarios);

                        estiloDeVidaList.add(estiloDeVida);

                        break;
                    case 4:
                        //servicios profesionales
                        //datos generales
                        //Log.d("factory", jsonObjectPublicacion.toString());
                        ServiciosProfesionales serviciosProfesionales=new ServiciosProfesionales();
                        serviciosProfesionales.setDatosBasicos(datosBasicos);
                        serviciosProfesionales.setDireccion(direccion);
                        serviciosProfesionales.setTelefono(telefono);
                        serviciosProfesionales.setImagenList(imagenList);
                        serviciosProfesionales.setVideo(video);

                        //datos especificos
                        JSONObject jsonObjectExperiencia=jsonObjectPublicacion.getJSONObject("experiencia");
                        JSONObject jsonObjectServProf=jsonObjectPublicacion.getJSONObject("servProf");
                        JSONObject jsonObjectpreciHora=jsonObjectPublicacion.getJSONObject("precioHora");

                        serviciosProfesionales.setExperiencia(jsonObjectExperiencia.optString("experiencia"));
                        serviciosProfesionales.setServioPorfesional(jsonObjectServProf.optString("descripcion"));
                        serviciosProfesionales.setPrecioHora(jsonObjectpreciHora.optDouble("valor"));

                        serviciosProfesionalesList.add(serviciosProfesionales);

                        break;
                    case 5:
                        //shows y eventos
                        //datos generales
                        //Log.d("factory", jsonObjectPublicacion.toString());
                        ShowsYEventos showsYEventos=new ShowsYEventos();
                        showsYEventos.setDatosBasicos(datosBasicos);
                        showsYEventos.setDireccion(direccion);
                        showsYEventos.setTelefono(telefono);
                        showsYEventos.setImagenList(imagenList);
                        showsYEventos.setVideo(video);

                        //datos especificos
                        JSONObject jsonObjectShowEvento=jsonObjectPublicacion.getJSONObject("showEventos");
                        JSONArray jsonArrayFechas=jsonObjectPublicacion.getJSONArray("fechas");
                        List<String> fechas=new ArrayList<>();
                        for(int e=0;e<jsonArrayFechas.length();e++){
                            fechas.add(jsonArrayFechas.getJSONObject(e).optString("diaHora"));
                        }
                        JSONObject jsonObjectBandas=jsonObjectPublicacion.getJSONObject("bandas");
                        JSONObject jsonObjectvalor=jsonObjectPublicacion.getJSONObject("valor");

                        showsYEventos.setShowEvento(jsonObjectShowEvento.optString("descripcion"));
                        showsYEventos.setFechas(fechas);
                        showsYEventos.setBandas(jsonObjectBandas.optString("descripcion"));
                        showsYEventos.setValor(jsonObjectvalor.optDouble("valor"));

                        showsYEventosList.add(showsYEventos);

                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //verificacion por consola
        checkVentaInstrumentos();
    }


    private static void checkTiposPub(){

        for(TipoPublicacion item : tipoPublicacionList ){
            Log.d("tipoPub", "----------------------------------------------------");

            Log.d("tipoPub", "id: "+item.getId());
            Log.d("tipoPub", "descripcion: "+item.getDescripcion());


            Log.d("tipoPub", "----------------------------------------------------");

        }

    }

    private static void checkSalasYEstudios(){
        for(SalasYEstudios item : salasYEstudiosList ){
            checkDatosGenerales(item, "salas");
            //-----------------------------------------------------------------------
            Log.d("salas", "SERVICIOS");
            for (String item2: item.getServiciosList()) {
                Log.d("salas", "servicio: "+item2);

            }
            Log.d("salas", "EQUIPOS");
            for (Equipos item2:item.getEquiposList()) {
                Log.d("salas", "equipo: "+item2.getTipo() + "marca: "+ item2.getMarca());

            }
            Log.d("salas", "SALAS");
            Log.d("salas", "salas: "+item.getSalas());
            Log.d("salas", "PRECIO HORA");
            Log.d("salas", "precio hora: "+item.getPrecioHora());
            Log.d("salas", "----------------------------------------------------");

        }

    }

    private static void checkVentaInstrumentos(){
        for(VentaDeInstrumentos item : ventaDeInstrumentosList ){
            checkDatosGenerales(item, "instrumentos");
            //-----------------------------------------------------------------------
            Log.d("instrumentos", "INSTRUMENTO");
            Log.d("instrumentos", "estado: "+item.getInstrumento().getEstado());
            Log.d("instrumentos", "marca: "+item.getInstrumento().getMarca());
            Log.d("instrumentos", "otro: "+item.getInstrumento().getOtro());
            Log.d("instrumentos", "pais: "+item.getInstrumento().getPais());
            Log.d("instrumentos", "tipo: "+item.getInstrumento().getTipo());
            Log.d("instrumentos", "anio: "+item.getInstrumento().getAnio());
            Log.d("instrumentos", "valor: "+item.getInstrumento().getValor());
            Log.d("instrumentos", "----------------------------------------------------");


        }

    }

    private static void checkEstiloVida(){
        for(EstiloDeVida item : estiloDeVidaList ){
            checkDatosGenerales(item, "estiloVida");
            //-----------------------------------------------------------------------
            Log.d("estiloVida", "estiloVida: "+item.getEstiloVida());
            Log.d("estiloVida", "HORARIOS");
            for (Horarios horario:item.getHorarios()) {
                Log.d("estiloVida", "desde dia: "+horario.getDesdeDia());
                Log.d("estiloVida", "hasta dia: "+horario.getHastaDia());
                Log.d("estiloVida", "desde hora: "+horario.getDesdeHora());
                Log.d("estiloVida", "hasta hora: "+horario.getHastaHora());
            }
            Log.d("estiloVida", "PRODUCTOS");
            for (String producto:item.getProductos()) {
                Log.d("estiloVida", "producto: "+ producto);

            }

            Log.d("estiloVida", "----------------------------------------------------");

        }

    }

    private static void checkServProf(){
        for(ServiciosProfesionales item : serviciosProfesionalesList ){
            checkDatosGenerales(item, "servProf");
            //-----------------------------------------------------------------------
            Log.d("servProf", "servProf: "+item.getServioPorfesional());
            Log.d("servProf", "experiencia: "+item.getExperiencia());
            Log.d("servProf", "precio hora: "+item.getPrecioHora());

            Log.d("servProf", "----------------------------------------------------");

        }

    }

    private static void checkShowEventos(){
        for(ShowsYEventos item : showsYEventosList ){
            checkDatosGenerales(item, "showEventos");
            //-----------------------------------------------------------------------
            Log.d("showEventos", "showEvento: "+item.getShowEvento());
            Log.d("showEventos", "bandas: "+item.getBandas());
            Log.d("showEventos", "valor: "+item.getValor());

            for (String fecha:item.getFechas()) {
                Log.d("showEventos", "fecha: "+fecha);

            }

            Log.d("showEventos", "----------------------------------------------------");

        }

    }

    private static void checkDatosGenerales(Publicacion publicacion, String tag){

        Log.d(tag, "----------------------------------------------------");
        Log.d(tag, "DATOSBASICOS");
        Log.d(tag, "descripcion: " +publicacion.getDatosBasicos().getDescripcion());
        Log.d(tag, "email: " +publicacion.getDatosBasicos().getEmail());
        Log.d(tag, "nombre: " +publicacion.getDatosBasicos().getNombre());
        Log.d(tag, "web: " +publicacion.getDatosBasicos().getWeb());
        Log.d(tag, "id: "+publicacion.getDatosBasicos().getId());
        Log.d(tag, "tipoPub: "+publicacion.getDatosBasicos().getTipoPub());
        Log.d(tag, "DIRECCION");
        Log.d(tag, "calle: " +publicacion.getDireccion().getCalle());
        Log.d(tag, "ciudad: " +publicacion.getDireccion().getCiudad());
        Log.d(tag, "cp: " +publicacion.getDireccion().getCp());
        Log.d(tag, "localidad: " +publicacion.getDireccion().getLocalidad());
        Log.d(tag, "partido: " +publicacion.getDireccion().getPartido());
        Log.d(tag, "provincia: " +publicacion.getDireccion().getProvincia());
        Log.d(tag, "pais: " +publicacion.getDireccion().getPais());
        Log.d(tag, "altura: " +publicacion.getDireccion().getAltura());
        Log.d(tag, "latitud: " +publicacion.getDireccion().getLatitud());
        Log.d(tag, "longitud: " +publicacion.getDireccion().getLongitud());
        Log.d(tag, "TELEFONO");
        Log.d(tag, "codarea: "+publicacion.getTelefono().getCodArea());
        Log.d(tag, "codpais: "+publicacion.getTelefono().getCodPais());
        Log.d(tag, "numero: "+publicacion.getTelefono().getNumero());
        Log.d(tag, "es celular: "+publicacion.getTelefono().isCelular());
        Log.d(tag, "VIDEO");
        Log.d(tag, "video: " +publicacion.getVideo().getUri());
        Log.d(tag, "IMAGENES");
        for (Imagen item2:publicacion.getImagenList()) {
            Log.d(tag, "imagen: "+item2.getUri());
        }
    }

}
