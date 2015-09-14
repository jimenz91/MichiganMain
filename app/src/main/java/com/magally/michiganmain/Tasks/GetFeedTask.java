package com.magally.michiganmain.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.magally.michiganmain.Fragments.Pregunta;
import com.magally.michiganmain.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 13-09-2015.
 */
public class GetFeedTask extends AsyncTask<Void,Void,Void> {

    public AsyncResponse delegate = null;


    private static String url_create_user = "http://192.168.1.113/michigan_server/get_questions.php";
    private static String LOG_TAG = "GetFeedTask";
    JSONParser jsonParser = new JSONParser();
    String username, enunciado, foto,  errorMsg, reputacion;
    Pregunta[] preguntaArray;
    int preguntaID;
    Activity parentActivity;
    int success;
    private static final String TAG_SUCCESS = "success";
    ProgressDialog dialog;


    public GetFeedTask(Activity activity, AsyncResponse asyncResponse) {
        parentActivity = activity;
        delegate = asyncResponse;
        this.username = "empty";

    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("Cargando preguntas...");
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
                JSONArray jsonarray = json.getJSONArray("questions");
                preguntaArray = new Pregunta[jsonarray.length()];
                for(int i = 0; i<preguntaArray.length; i++){
                    JSONObject jsonObject = jsonarray.getJSONObject(i);
                    username = jsonObject.getString("username");
                    enunciado = jsonObject.getString("enunciado");
                    preguntaID = jsonObject.getInt("qid");
                    foto = jsonObject.getString("foto");
                    reputacion = jsonObject.getString("reputacion");
                    preguntaArray[i]= new Pregunta(preguntaID,
                            enunciado,
                            foto,
                            username,
                            reputacion);
                    Log.d("GetFeedTask", "Questions received: "+ i);
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
            delegate.processFinish(preguntaArray);
        }

        dialog.dismiss();
        //return preguntaArray;
    }

    public interface AsyncResponse{
        public void processFinish(Pregunta[] output);
    }

}
