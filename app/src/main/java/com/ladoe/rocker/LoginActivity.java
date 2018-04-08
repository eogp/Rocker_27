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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ImageButton login;
    private TextView recoveryPass;
    private TextView facebook;
    private TextView registrate;
    private TextView invitado;
    private ProgressDialog progressDialog;
    private AlertDialog alert = null;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //facebook
        iniciarFacebookSDK();

        //instancias
        email= findViewById(R.id.editTextEmail);
        password= findViewById(R.id.editTextPassword);
        login=  findViewById(R.id.imageButtonLogin);
        recoveryPass= findViewById(R.id.textViewRecoveryPass);
        facebook=  findViewById(R.id.textViewFacebook);
        registrate= findViewById(R.id.textViewRegistrate);
        invitado= findViewById(R.id.textViewInvitado);
        progressDialog=new ProgressDialog(this);

        //listeners
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(estaConectado()){
                    try {
                        loginAsync(email.getText().toString(), password.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else{
                    dialogoNoIntenet();
                }
            }
        });
        recoveryPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RecoveyPassActivity.class));

            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFacebook();

            }
        });
        registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
        invitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ListActivity.class));

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //LOGIN-----------------------------------------------------------------------------------------
    private void loginAsync(String _email,String _pass) throws JSONException, UnsupportedEncodingException
    {
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("user",_email);
        oJSONObject.put("pass",_pass);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        AsyncHttpClient oHttpClient = new AsyncHttpClient();

        RequestHandle requestHandle = oHttpClient.post(getApplicationContext(),"https://www.rockerapp.com/loginUserApp.php",(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
                progressDialog.setMessage("Validando...");
                progressDialog.show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                System.out.println(statusCode);
                try {
                    String content = new String(responseBody, "UTF-8");
                    JSONObject obj = new JSONObject(content);

                    Log.d("login", new String(responseBody, "UTF-8"));

                    if(obj.getString("status").equals("1")) {
                        saveLogin(obj.getJSONObject("usuario"),1);
                        startActivity(new Intent(LoginActivity.this, ListActivity.class));
                        finish();
                        //Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_LONG).show();
                    }else {
                        dialogoErrorLogin();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_LONG).show();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
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
    private void loginFacebookAsync(String _faceid, String _email, String _first_name, String _last_name, String _imageURi) throws JSONException, UnsupportedEncodingException
    {
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("faceid", _faceid);
        oJSONObject.put("email", _email);
        oJSONObject.put("first_name", _first_name);
        oJSONObject.put("last_name", _last_name);
        oJSONObject.put("imageURi", _imageURi);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        AsyncHttpClient oHttpClient = new AsyncHttpClient();

        RequestHandle requestHandle = oHttpClient.post(getApplicationContext(),"https://www.rockerapp.com/loginUserAppFacebook.php",(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
                progressDialog.setMessage("Validando...");
                progressDialog.show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                System.out.println(statusCode);
                try {
                    String content = new String(responseBody, "UTF-8");
                    JSONObject obj = new JSONObject(content);
                    Log.d("login", "ResponseBody");
                    Log.d("login", new String(responseBody, "UTF-8"));

                    if(obj.getString("status").equals("1")) {
                        saveLogin(obj.getJSONObject("usuario"),2);
                        startActivity(new Intent(LoginActivity.this, ListActivity.class));
                        finish();
                        //Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_LONG).show();
                    }else {
                        dialogoErrorFaceLogin();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_LONG).show();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                switch (statusCode){
                    case 404:
                        Log.d("login", "Error 404 !");
                        break;
                    case 500:
                        Log.d("login", "Error 500 !");
                        break;
                    case 403:
                        Log.d("login","Error 403 !");
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
    //facebook--------------------------------------------------------------------------------------
    private void iniciarFacebookSDK(){
        if(FacebookSdk.isInitialized()) {
            //FacebookSdk.sdkInitialize(this.getApplicationContext());
            Log.d("login", "FacebooSDK inicio correctamente");
            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d("login", "onSuccess");
                            Log.d("login", "AccesToken");
                            Log.d("login", loginResult.getAccessToken().toString());
                            if (loginResult.getAccessToken()!=null){
                                obtenerDatosUsuarioFacebook(loginResult.getAccessToken());
                            }else{
                                dialogoErrorFaceLogin();
                            }
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(LoginActivity.this, "Inicio Cancelado", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            Log.d("login", "FacebooSDK NO inicio correctamente");

        }
    }
    private void loginFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }
    private void obtenerDatosUsuarioFacebook(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            //VERIFICA SI RECIBIMOS IMAGEN DE USSUARIO
                            String ImageURI="";
                            if(object.getJSONObject("picture")!=null && object.getJSONObject("picture").getJSONObject("data")!=null){
                                ImageURI=object.getJSONObject("picture").getJSONObject("data").optString("url");
                            }
                            //CONTROL DE DATOS RECIBIDOS
                            Log.d("login", "Graph");
                            Log.d("login", response.toString());
                            Log.d("login", "JSONObject");
                            Log.d("login", object.toString());
                            Log.d("login", "Imagen");
                            Log.d("login", ImageURI);
                            //SINCRONIZACION CON BACKEND DE DATOS RECIBIDOS
                            loginFacebookAsync(object.getString("id"),
                                    object.optString("email"),
                                    object.optString("first_name"),
                                    object.optString("last_name"),
                                    ImageURI);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
    //----------------------------------------------------------------------------------------------

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

    //DIALOGOS------------------------------------------------------------------------------------------
    public void dialogoErrorLogin(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Usuario y/o contraseña incorrectos. Recuerda que debes estar registrado para acceder al contenido completo de Rocker.")
                .setCancelable(false)
                .setPositiveButton("Registrame", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        finish();
                    }
                }).setNegativeButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                }
        );
        alert = builder.create();
        alert.show();

    }
    public void dialogoErrorFaceLogin(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Algo salio mal, recuerda aceptar los permisos para poder ingresar con Facebook.")
                .setCancelable(false)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();

    }
    public void dialogoNoIntenet(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Debes estar conectado a Internet para continuar.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    //SAVE USERDATA
    public void saveLogin(JSONObject usuario, int tipoUsuario){
        SharedPreferences sharedPref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("autologin", tipoUsuario);
        try {
            editor.putString("id",usuario.getString("id"));
            editor.putString("email",usuario.getString("email"));
            editor.putString("nombre",usuario.getString("nombre"));
            editor.putString("faceid",usuario.getString("faceid"));
            editor.putString("imageURI",usuario.getString("imageURI"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();
        Log.d("login", "autologin: "+tipoUsuario);
    }
}
