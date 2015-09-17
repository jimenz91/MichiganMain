package com.magally.michiganmain.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.magally.michiganmain.Materias;
import com.magally.michiganmain.Pregunta;
import com.magally.michiganmain.R;
import com.magally.michiganmain.Tasks.QuestionSearchTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 26-08-2015.
 */
public class BuscarFragment extends android.support.v4.app.Fragment {

    private final String SEARCH_URL = "http://192.168.1.113/michigan_server/search.php";
    View rootview;
    Spinner mateSpinner;
    String materia, clave1, clave2, clave3;
    long carrera;
    Button buscarBtn;
    EditText buscarET1, buscarET2, buscarET3;
    QuestionSearchTask questionSearchTask;
    ListView preguntaList;
    ProgressDialog dialog;
    ListAdapter adaptador;
    Pregunta[] preguntaArray;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.buscar_layout,container,false);

        buscarBtn = (Button) rootview.findViewById(R.id.buscarBtn);
        buscarET1 = (EditText) rootview.findViewById(R.id.buscarET1);

        mateSpinner = (Spinner) rootview.findViewById(R.id.buscarPregSpinner);
        preguntaList = (ListView) rootview.findViewById(R.id.buscarLV);

        final List<String> SpinnerArray = new ArrayList<String>();
        SpinnerArray.add(Materias.MATERIAS.get("2"));
        SpinnerArray.add(Materias.MATERIAS.get("4"));
        SpinnerArray.add(Materias.MATERIAS.get("5"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mateSpinner.setAdapter(adapter);

        buscarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clave1 = buscarET1.getText().toString();
                materia = mateSpinner.getSelectedItem().toString();
                Toast.makeText(getActivity(), materia, Toast.LENGTH_SHORT).show();

                searchQuestion(clave1, getMateriaID(materia));
                //questionSearchTask.execute();



            }
        });

        return rootview;
    }

    private void searchQuestion(String keywords, long materiaID){
        RequestParams requestParams = new RequestParams();
        requestParams.put("keywords", keywords);
        requestParams.put("materia_id", materiaID);
        AsyncHttpClient client = new AsyncHttpClient();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Cargando preguntas...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
        client.get(SEARCH_URL, requestParams, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);

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
                        Toast.makeText(getActivity(),"Sin resultados", Toast.LENGTH_LONG).show();
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
    private long getMateriaID(String materia){
        switch (materia){
            case "Física I": return 2;
            case "Cálculo I": return 4;
            case "Calculo II": return 5;
            default: return 2;
        }
    }
}
