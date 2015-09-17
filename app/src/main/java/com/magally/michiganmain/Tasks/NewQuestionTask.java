package com.magally.michiganmain.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.magally.michiganmain.JSONParser;
import com.magally.michiganmain.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 06-09-2015.
 */
public class NewQuestionTask extends AsyncTask <Void,Void,Void> {
    private static String url_create_user = "http://192.168.1.113/michigan_server/new_question.php";
    private static String LOG_TAG = "NewQuestionTask";
    JSONParser jsonParser = new JSONParser();
    String enunciado, foto, username;
    long temaId, usuarioId;
    Activity parentActivity;
    int success;
    private static final String TAG_SUCCESS = "success";
    ProgressDialog dialog;


    public NewQuestionTask(Activity activity, String enunciado, String foto, long temaId, long usuarioId, String username) {
        parentActivity = activity;
        this.enunciado = enunciado;
        this.foto = foto;
        this.temaId = temaId;
        this.usuarioId = usuarioId;
        this.username = username;

    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("Creando nueva pregunta...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    protected Void doInBackground(Void... args) {

        success=0;
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("enunciado", enunciado));
        params.add(new BasicNameValuePair("foto", foto));
        params.add(new BasicNameValuePair("tema_id", Long.toString(temaId)));
        params.add(new BasicNameValuePair("usuario_id",Long.toString(usuarioId)));
        params.add(new BasicNameValuePair("username",username));

        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                "POST", params);

        // check log cat fro response
        if(json!=null)
            Log.d("Create Response", json.toString());
        else{
            Log.d("NewQuestionActivity: ","Json empty");
            return null;
        }
        // check for success tag
        try {
            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created product
                Intent i = new Intent(parentActivity, MainActivity.class);
                parentActivity.startActivity(i);

                // closing this screen
                //finish();
            } else {
                // failed to create product
                Log.d(LOG_TAG, "Failed creating question, success = "+success);

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
            Toast.makeText(parentActivity,"Pregunta creada", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(parentActivity, "Operacion Fallida, Intente nuevamente",Toast.LENGTH_SHORT)
                    .show();
        }
        dialog.dismiss();
    }

//    private String upload(File file, List<NameValuePair> params){
//        JSONObject result;
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("enunciado",enunciado);
//        requestParams.put("tema_id",Long.toString(temaId));
//        requestParams.put("usuario_id",Long.toString(usuarioId));
//        requestParams.put("username",username);
//        client.post(url_create_user,requestParams, new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                //super.onSuccess(statusCode, headers, response);
//
//            }
//        });
//        try {
//            requestParams.put("uploaded_file", file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//    }

}
