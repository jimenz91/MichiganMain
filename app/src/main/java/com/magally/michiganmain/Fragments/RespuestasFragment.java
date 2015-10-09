package com.magally.michiganmain.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.magally.michiganmain.Pregunta;
import com.magally.michiganmain.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Magally on 26-08-2015.
 */
public class RespuestasFragment extends android.support.v4.app.Fragment {

    View rootview;
    ListView preguntaList;
    long usuario_id;
    private static final String URL = "http://192.168.1.113/michigan_server/get_my_answers.php";
    Pregunta[] preguntaArray;
    CustomAdapter adaptador;
    ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.feed_layout,container,false);
        preguntaList = (ListView)rootview.findViewById(R.id.feedList);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        usuario_id = sharedPrefs.getLong("uid",0);
        getQuestions();
        return rootview;
    }

    private void getQuestions(){
        Log.d("RespuestasFragment", "In getQuestions method!");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("usuario_id", usuario_id);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Cargando preguntas...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
        client.get(URL,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if(response.getInt("success")==1) {
                        JSONArray jArray = response.getJSONArray("questions");
                        int jsonlength = jArray.length();
                        preguntaArray = new Pregunta[jsonlength];
                        for (int i = 0; i < jsonlength; i++) {
                            JSONObject jobj = jArray.getJSONObject(i);
                            preguntaArray[i] = new Pregunta(jobj.getInt("qid"),
                                    jobj.getString("enunciado"),
                                    jobj.getString("foto"),
                                    jobj.getString("username"),
                                    jobj.getString("reputacion"),
                                    jobj.getString("answer_count"));
                        }
                        adaptador = new CustomAdapter(getActivity(), preguntaArray);
                        preguntaList.setAdapter(adaptador);
                    }else{
                        Toast.makeText(getActivity(), "Sin resultados", Toast.LENGTH_LONG).show();
                        preguntaList.setAdapter(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialog.dismiss();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }
}
