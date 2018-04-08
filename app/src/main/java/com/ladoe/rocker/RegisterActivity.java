package com.ladoe.rocker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText retryEmail;
    private EditText pass;
    private EditText retryPass;
    private ImageButton imageButton;
    private ProgressDialog progressDialog;
    private AlertDialog alert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //instancias
        email= findViewById(R.id.editTextEmail);
        retryEmail= findViewById(R.id.editTextRetryEmail);
        pass= findViewById(R.id.editTextPassword);
        retryPass=findViewById(R.id.editTextRetryPassword);
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
                        //VALIDA QUE COINCIDADN LOS EMAIL
                        if(email.getText().toString().equals(retryEmail.getText().toString())){
                            //VALIDA LA CONTRASEÑA
                            if(validarPass(pass.getText().toString())){
                                //VALIDA QUE COINCIDAN LAS CONTRASEÑAS
                                if(pass.getText().toString().equals(retryPass.getText().toString())){
                                    //EJECUTA EL REGISTRO ASINCRONICO
                                    try {
                                        registroAsync(email.getText().toString(), pass.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    dialogoPassDiferentes();
                                }
                            }else {
                                dialogoErrorPass();
                            }
                        }else {
                            dialogoEmailDiferentes();
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

    private boolean validarPass(String pass){
        return  pass != null && !pass.equals("") && pass.length()>7;
    }

    //VALIDACION DE CONEXION A INTERNET-------------------------------------------------------------
    protected Boolean estaConectado() {
         return conectadoRedMovil() ||  conectadoWifi();

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

    public void dialogoEmailDiferentes(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Los email deben coincidir.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void dialogoErrorPass(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tu contraseña debe tener en 8 y 20 caracteres.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void dialogoPassDiferentes(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Las contraseñas deben coincidir.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void dialogoErrorExisteEmail(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Este email ya está registrado, verifícalo o recupera tu contraseña.")
                .setCancelable(false)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                }).setNegativeButton("Recuperar", new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                startActivity(new Intent(RegisterActivity.this, RecoveyPassActivity.class));
            }
        });
        alert = builder.create();
        alert.show();
    }

    public void dialogoErrorRegistro(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ocurrió un error durante el registro. Verifica tu conexión a Internet y reinténtalo.")
                .setCancelable(false)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    //REGISTRO USUSARIO-----------------------------------------------------------------------------

    private void registroAsync(String _email,String _password) throws JSONException, UnsupportedEncodingException
    {
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("user",_email);
        oJSONObject.put("pass",_password);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        AsyncHttpClient oHttpClient = new AsyncHttpClient();

        RequestHandle requestHandle = oHttpClient.post(getApplicationContext(),"https://www.rockerapp.com/registroUserApp.php",(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
                progressDialog.setMessage("Registrando...");
                progressDialog.show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                System.out.println(statusCode);
                try {
                    String content = new String(responseBody, "UTF-8");
                    JSONObject obj = new JSONObject(content);

                    Log.d("registro", new String(responseBody, "UTF-8"));
                    //VERIFICAMOS QUE SE HAYA REGISTRADO EXITOSAMENTE
                    if(obj.getString("status").equals("1")) {
                        saveLogin(obj.getJSONObject("usuario"),1);
                        startActivity(new Intent(RegisterActivity.this, ListActivity.class));
                        finish();

                        //Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_LONG).show();
                    }
                    //VERIFICAMOS QUE SE EL EMAIL NO ESTE REGISTRADO
                    if(obj.getString("status").equals("0")){
                        progressDialog.dismiss();
                        dialogoErrorExisteEmail();
                        return;
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    dialogoErrorRegistro();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    dialogoErrorRegistro();
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
                dialogoErrorRegistro();
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


    //SAVE USERDATA
    public void saveLogin(JSONObject usuario, int tipoUsuario){
        SharedPreferences sharedPref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("autologin", tipoUsuario);
        try {
            editor.putString("id",usuario.getString("id"));
            editor.putString("email",usuario.getString("email"));
            editor.putString("nombre","");
            editor.putString("faceid","");
            editor.putString("imageURI","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();
    }
}
