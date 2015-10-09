package com.magally.michiganmain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OtherUser extends ActionBarActivity {

    private final String URL= "http://192.168.1.113/michigan_server/get_user_details.php";
    private TextView userRepTV;
    private ImageView userIV;
    private TextView userTV;
    private TextView userCTV, userCarTV;
    private String usuario;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);
        userIV = (ImageView)findViewById(R.id.otherUserIV);
        userTV = (TextView)findViewById(R.id.otherUserTV);
        userCTV = (TextView)findViewById(R.id.otherUserCorTV);
        userRepTV = (TextView)findViewById(R.id.otherUserRepTV);
        userCarTV = (TextView)findViewById(R.id.otherUserCTV);

        Intent intent = getIntent();
        if(intent.hasExtra("usuario")){
            usuario = intent.getStringExtra("usuario");
            getUserInfo();
        } else{
            Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_LONG).show();
            finish();
        }

    }
    private void getUserInfo(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", usuario);
        AsyncHttpClient client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
        client.get(URL,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("usuario");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    userTV.setText(jsonObject.getString("nombre"));
                    userCTV.setText(jsonObject.getString("correo"));
                    userRepTV.setText("Reputacion: "+jsonObject.getString("reputacion")+" pts.");
                    userCarTV.setText(jsonObject.getString("carrera_nombre"));
                    if(!jsonObject.getString("foto").equals("")){
                    Picasso.with(getApplicationContext())
                            .load(jsonObject.getString("foto"))
                            .placeholder(R.drawable.silhouette)
                            .error(R.drawable.dragonball)
                            .fit()
                            .centerCrop()
                            .into(userIV);}
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(),"Error: "+statusCode, Toast.LENGTH_LONG);

            }
        });
    }
}
