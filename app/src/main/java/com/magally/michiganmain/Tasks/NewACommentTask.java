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
public class NewACommentTask extends AsyncTask <Void,Void,Void> {
    private static String url_create_user = "http://192.168.1.113/michigan_server/new_answer_comment.php";
    private static String LOG_TAG = "NewACommentTask";
    JSONParser jsonParser = new JSONParser();
    String comentario, username;
    long respuestaId, usuarioId;
    Activity parentActivity;
    int success;
    private static final String TAG_SUCCESS = "success";
    ProgressDialog dialog;


    public NewACommentTask(Activity activity, String comentario, long respuestaId, long usuarioId, String username) {
        parentActivity = activity;
        this.comentario = comentario;
        this.respuestaId = respuestaId;
        this.usuarioId = usuarioId;
        this.username = username;

    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("Creando nueva respuesta...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    protected Void doInBackground(Void... args) {

        success=0;
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("comentario", comentario));
        params.add(new BasicNameValuePair("usuario_Id", Long.toString(usuarioId)));
        params.add(new BasicNameValuePair("pregunta_Id", Long.toString(respuestaId)));
        params.add(new BasicNameValuePair("username",username));

        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                "POST", params);

        // check log cat fro response
        if(json!=null)
            Log.d("Create Response", json.toString());
        else{
            Log.d("NewACommentActivity: ","Json empty");
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
                Log.d(LOG_TAG, "Failed creating comment, success = "+success);

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
            Toast.makeText(parentActivity,"Comentario creada", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(parentActivity, "Operacion Fallida, Intente nuevamente",Toast.LENGTH_SHORT)
                    .show();
        }
        dialog.dismiss();
    }
}
