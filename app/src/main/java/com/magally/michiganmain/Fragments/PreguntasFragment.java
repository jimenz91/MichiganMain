package com.magally.michiganmain.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.magally.michiganmain.R;
import com.magally.michiganmain.Tasks.GetPregTask;

/**
 * Created by Magally on 26-08-2015.
 */
public class PreguntasFragment extends android.support.v4.app.Fragment {
    Pregunta[] preguntaArray;
    ListView preguntasList;
    View rootview;
    ListAdapter adaptador;
    GetPregTask getPregTask;
    String username;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.preguntas_layout,container,false);
        preguntasList = (ListView) rootview.findViewById(R.id.pregLV);
        username = sharedPref.getString("username","");
       /* getPregTask = new GetPregTask(getActivity(),new GetPregTask.AsyncResponse() {
            @Override
            public void processFinish(Pregunta[] output) {
                Log.d("FeedFragment", "on processFinish Method ! " + output.length);
                preguntaArray = output;
                adaptador = new CustomAdapter(getActivity(), preguntaArray);
                preguntasList.setAdapter(adaptador);

            }
        });
        getPregTask.execute();
*/

        return rootview;
    }
}