package com.ladoe.rocker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RecoveyPassActivity extends AppCompatActivity {

    private EditText email;
    private ImageButton imageButton;
    private ProgressDialog progressDialog;
    private AlertDialog alert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovey_pass);

        //instancias
        email= findViewById(R.id.editTextEmail);
        imageButton= findViewById(R.id.imageButtonRegister);
        progressDialog=new ProgressDialog(this);
        //listener
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VALIDA SI ESTA CONETADO A INTERNET
                if(estaConectado()){
                    //VALIDA EL EMAIL
                    if(validarEmail(email.getText().toString())){
                        //INICIA RECUPERO DE CONTRASEÑA
                        try {
                            recuperoAsync(email.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else {
                        dialogoErrorEmail();
                    }
                }else {
                    dialogoNoIntenet();
                }
            }
        });

    }


    //VALIDACIONES----------------------------------------------------------------------------------
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    //VALIDACION DE CONEXION A INTERNET-------------------------------------------------------------
    protected Boolean estaConectado() {
        return conectadoRedMovil() || conectadoWifi();

    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {

                return info.isConnected();

            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {

                return info.isConnected();

            }
        }
        return false;
    }

    //DIALOGOS--------------------------------------------------------------------------------------
    public void dialogoNoIntenet(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Debe estar conectado a Internet para continuar.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void dialogoErrorEmail(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Debes ingresar un email válido.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void dialogoErrorRecupero(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" Ocurrió un error durante el proceso. Verifica tu conexión a Internet y reinténtalo.")
                .setCancelable(false)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void dialogoErrorNoExisteEmail(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Este email no esta registrado, verifícalo o regístrate.")
                .setCancelable(false)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                }).setNegativeButton("Registrarme", new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                startActivity(new Intent(RecoveyPassActivity.this, RegisterActivity.class));
            }
        });
        alert = builder.create();
        alert.show();
    }

    public void dialogoExitoRecupero(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Recibirás un email con tu nueva contraseña.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(RecoveyPassActivity.this, LoginActivity.class));
                        finish();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    //RECUPERO DE CONTRASEÑA------------------------------------------------------------------------

    private void recuperoAsync(String _email) throws JSONException, UnsupportedEncodingException
    {
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("user",_email);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        AsyncHttpClient oHttpClient = new AsyncHttpClient();

        RequestHandle requestHandle = oHttpClient.post(getApplicationContext(),"https://www.rockerapp.com/recoveryPassApp.php",(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
                progressDialog.setMessage("Recuperando contraseña...");
                progressDialog.show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                System.out.println(statusCode);
                try {
                    String content = new String(responseBody, "UTF-8");
                    Log.d("recovery", content);
                    JSONObject obj = new JSONObject(content);


                    //VERIFICAMOS QUE SE HAYA RECUPERADO LA CONTRASEÑA EXIOSAMENTE
                    if(obj.getString("status").equals("1")) {

                        dialogoExitoRecupero();

                        //Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_LONG).show();
                    }
                    //VERIFICAMOS QUE EL EMAIL ESTE REGISTRADO
                    if(obj.getString("status").equals("0")){
                        progressDialog.dismiss();
                        dialogoErrorNoExisteEmail();
                        return;
                    }
                    //REPORTE DE ERROR
                    if(obj.getString("status").equals("2")){
                        progressDialog.dismiss();
                        dialogoErrorRecupero();
                        return;
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    dialogoErrorRecupero();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    dialogoErrorRecupero();
                    return;
                }
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                //Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_LONG).show();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                dialogoErrorRecupero();
                switch (statusCode){
                    case 404:
                        Log.d("recovery", "Error 404 !");
                        break;
                    case 500:
                        Log.d("recovery", "Error 500 !");
                        break;
                    case 403:
                        Log.d("recovery", "Error 403 !");
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
}
