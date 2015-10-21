package com.magally.michiganmain.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.magally.michiganmain.Respuesta;
import com.magally.michiganmain.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 16-09-2015.
 */
public class GetAnswersTask extends AsyncTask<Void,Void,Void> {

    public AsyncResponse delegate = null;


    private static String url_create_user = "http://192.168.1.113/michigan_server/get_answers.php";
    private static String LOG_TAG = "GetAnswerTask";
    JSONParser jsonParser = new JSONParser();
    String username, respuesta, foto,  errorMsg;
    Respuesta[] respuestaArray;
    int preguntaID;
    Activity parentActivity;
    int success;
    private static final String TAG_SUCCESS = "success";
    ProgressDialog dialog;
    private long reputacion;


    public GetAnswersTask(Activity activity, int preguntaID, AsyncResponse asyncResponse) {
        parentActivity = activity;
        delegate = asyncResponse;
        this.preguntaID = preguntaID;

    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("Cargando Respuestas...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    protected Void doInBackground(Void... args) {
        Log.d(LOG_TAG, "doInBackground");
        success=0;
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pregunta_id", Integer.toString(preguntaID)));

        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_user,"GET", params);

        // check log cat fro response
        if(json!=null)
            Log.d(LOG_TAG, "HTTTP Response: "+  json.toString());
        else{
            Log.d("GetAnswersActivity: ","Json empty");
            return null;
        }
        // check for success tag
        try {
            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                JSONArray jsonarray = json.getJSONArray("answers");
                respuestaArray = new Respuesta[jsonarray.length()];
                for(int i = 0; i<respuestaArray.length; i++){
                    JSONObject jsonObject = jsonarray.getJSONObject(i);
                    username = jsonObject.getString("username");
                    respuesta = jsonObject.getString("respuesta");
                    preguntaID = jsonObject.getInt("aid");
                    foto = jsonObject.getString("foto");
                    reputacion = jsonObject.getLong("reputacion");
                    respuestaArray[i]= new Respuesta(preguntaID, respuesta, foto, username, reputacion);
                    Log.d("GetAnswersTask", "Answers received: "+ i);
                }

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
        if (success!=1){
            Toast.makeText(parentActivity, errorMsg, Toast.LENGTH_SHORT).show();
        }else{
            delegate.processFinish(respuestaArray);
        }



        dialog.dismiss();
        //return preguntaArray;
    }

    public interface AsyncResponse{
        public void processFinish(Respuesta[] output);
    }

}