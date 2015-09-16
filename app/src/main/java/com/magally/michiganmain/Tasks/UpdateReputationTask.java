package com.magally.michiganmain.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.magally.michiganmain.JSONParser;
import com.magally.michiganmain.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 15-09-2015.
 */
public class UpdateReputationTask extends AsyncTask<Void,Void,Void> {

    private static String url_create_user = "http://192.168.1.113/michigan_server/update_reputation.php";
    private static String LOG_TAG = "UpdateReputationTask";
    JSONParser jsonParser = new JSONParser();
    String reputacion, username;
    ProgressDialog dialog;
    Activity parentActivity;
    int success;
    private static final String TAG_SUCCESS = "success";

    public UpdateReputationTask(Activity activity, String reputacion, String username){
        parentActivity = activity;
        this.reputacion = reputacion;
        this.username = username;
    }

    @Override
    protected Void doInBackground(Void... args) {
        success=0;
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reputacion", reputacion));
        params.add(new BasicNameValuePair("username", username));

        JSONObject json = jsonParser.makeHttpRequest(url_create_user,"POST", params);

        // check log cat fro response
        if(json!=null)
            Log.d("Create Response", json.toString());
        else{
            Log.d("UpdateReputation: ","Json empty");
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
                Log.d(LOG_TAG, "Failed creating answer, success = "+success);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("Actualizando reputación...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}


