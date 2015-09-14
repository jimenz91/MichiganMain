package com.magally.michiganmain.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.magally.michiganmain.JSONParser;
import com.magally.michiganmain.LoginActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 06-09-2015.
 */
public class NewUserTask extends AsyncTask <Void,Void,Void> {
    private static String url_create_user = "http://192.168.1.113/michigan_server/new_user.php";
    private static String LOG_TAG = "NewUserTask";
    JSONParser jsonParser = new JSONParser();
    String nombre, apellido, correo, usuario, password;
    long carreraId;
    Activity parentActivity;
    int success;
    private static final String TAG_SUCCESS = "success";
    ProgressDialog dialog;


    public NewUserTask(Activity activity, String nombre, String apellido, String correo, String usuario, String password, long carreraId) {
        parentActivity = activity;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.usuario = usuario;
        this.password = password;
        this.carreraId = carreraId;

    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("Creando nuevo usuario...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    protected Void doInBackground(Void... args) {

        success=0;
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nombre", nombre));
        params.add(new BasicNameValuePair("apellido", apellido));
        params.add(new BasicNameValuePair("correo", correo));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("user", usuario));
        params.add(new BasicNameValuePair("carrera_id", Long.toString(carreraId)));

        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                "POST", params);

        // check log cat fro response
        if(json!=null)
            Log.d("Create Response", json.toString());
        else{
            Log.d("NewUsertActivity: ","Json empty");
            return null;
        }
        // check for success tag
        try {
            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created product
                Intent i = new Intent(parentActivity, LoginActivity.class);
                parentActivity.startActivity(i);

                // closing this screen
                //finish();
            } else {
                // failed to create product
               Log.d(LOG_TAG, "Failed creating user, success = "+success);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //super.onPostExecute(aVoid);
        if (success==1){
            Toast.makeText(parentActivity,"Usuario Creado", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(parentActivity, "Operacion Fallida, Intente nuevamente",Toast.LENGTH_SHORT)
                    .show();
        }
        dialog.dismiss();
    }
}
