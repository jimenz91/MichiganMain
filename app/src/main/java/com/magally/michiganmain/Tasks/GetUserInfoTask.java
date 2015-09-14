package com.magally.michiganmain.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.magally.michiganmain.Carreras;
import com.magally.michiganmain.JSONParser;
import com.magally.michiganmain.R;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 06-09-2015.
 */
public class GetUserInfoTask extends AsyncTask <Void,Void,Void> {
    private static String url_create_user = "http://192.168.1.113/michigan_server/get_user_details.php";
    private static String LOG_TAG = "GetUserTask";
    JSONParser jsonParser = new JSONParser();
    String username, nombre, apellido, correo, carrera_id, reputacion, foto, nombreCompleto, carrera, errorMsg;
    Activity parentActivity;
    int success;
    private static final String TAG_SUCCESS = "success";
    ProgressDialog dialog;


    public GetUserInfoTask(Activity activity, String username) {
        parentActivity = activity;
        this.username = username;

    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("Cargando información de usuario...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    protected Void doInBackground(Void... args) {

        success=0;
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        Log.d(LOG_TAG, "Usuario: "+ username);
        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_user,"GET", params);

        // check log cat fro response
        if(json!=null)
            Log.d("Create Response", json.toString());
        else{
            Log.d("GetUserActivity: ","Json empty");
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
                username = jsonObject.getString("user");
                carrera_id = jsonObject.getString("carrera_id");
                reputacion = jsonObject.getString("reputacion");
                foto = jsonObject.getString("foto");

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
        //super.onPostExecute(aVoid);
        if (success==1){
            //Toast.makeText(parentActivity,"Usuario Obtenido", Toast.LENGTH_SHORT).show();
            nombreCompleto = nombre+" "+apellido;
            ((TextView)parentActivity.findViewById(R.id.profileNameTxt)).setText(nombreCompleto);
            ((TextView)parentActivity.findViewById(R.id.profileCorreoTxt)).setText(correo);

            carrera = Carreras.CARRERAS.get(carrera_id);

            ((TextView) parentActivity.findViewById(R.id.profileCarreraTxt)).setText(carrera);
            ((TextView)parentActivity.findViewById(R.id.reputacionTxTView)).setText(reputacion);
            ((TextView)parentActivity.findViewById(R.id.profileUserTV)).setText(username);
            if(!foto.equals(""))
                Picasso.with(parentActivity).load(foto).placeholder(R.id.profileImgView);


        } else{
            Toast.makeText(parentActivity, errorMsg,Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
}
