package com.magally.michiganmain.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.magally.michiganmain.Carreras;
import com.magally.michiganmain.JSONParser;
import com.magally.michiganmain.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 12-09-2015.
 */
public class LoginTask extends AsyncTask<Void,Void,Void>{
    private static String url_create_user = "http://192.168.1.113/michigan_server/user_login.php";
    private static String LOG_TAG = "LoginTask";
    String usuario, contrasena, nombreCompleto, nombre, apellido, correo, carrera_id, reputacion, foto, errorMsg;
    JSONParser jsonParser = new JSONParser();
    Activity parentActivity;
    int success;
    private static final String TAG_SUCCESS = "success";
    ProgressDialog dialog;
    SharedPreferences sharedPref;

    public LoginTask (Activity activity,String usuario, String contrasena){
        parentActivity = activity;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }


    @Override
    protected Void doInBackground(Void... args) {
        success=0;
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("usuario", usuario));
        params.add(new BasicNameValuePair("contrasena",contrasena));
        Log.d(LOG_TAG, "Usuario: "+ usuario + " Contrasena: "+ contrasena);
        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_user,"POST", params);

        // check log cat fro response
        if(json!=null)
            Log.d("Create Response", json.toString());
        else{
            Log.d(LOG_TAG,"Json empty");
            return null;
        }
        // check for success tag
        try {
            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created product
                //Intent i = new Intent(parentActivity, MainActivity.class);
                //parentActivity.startActivity(i);
                JSONArray jsonarray = json.getJSONArray("usuario");
                JSONObject jsonObject = jsonarray.getJSONObject(0);
                nombre = jsonObject.getString("nombre");
                apellido = jsonObject.getString("apellido");
                correo = jsonObject.getString("correo");
                usuario = jsonObject.getString("user");
                carrera_id = jsonObject.getString("carrera_id");
                reputacion = jsonObject.getString("reputacion");
                foto = jsonObject.getString("foto");
                nombreCompleto = nombre+" "+apellido;

                // closing this screen
                //finish();
            } else {
                // failed to create product
                errorMsg = json.getString("message");
                Log.d(LOG_TAG, "Failed getting user info, success = "+success);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (success == 1) {
            sharedPref = parentActivity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("usuario", usuario);
            editor.putString("nombre", nombre);
            editor.putString("apellido", apellido);
            editor.putString("nombreCompleto", nombreCompleto);
            editor.putString("correo", correo);
            editor.putString("foto", foto);
            editor.putString("carrera", Carreras.CARRERAS.get(carrera_id));
            editor.putString("foto", foto);
            editor.apply();
            dialog.dismiss();
            Toast.makeText(parentActivity,"Sesion Iniciada" , Toast.LENGTH_SHORT).show();
            Intent intent =  new Intent(parentActivity, MainActivity.class);
            parentActivity.startActivity(intent);
        }else {
            dialog.dismiss();
            Toast.makeText(parentActivity,errorMsg , Toast.LENGTH_SHORT).show();
        }
        //super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("Iniciando Sesión...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
        //super.onPreExecute();
    }
}
