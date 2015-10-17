package com.magally.michiganmain.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.magally.michiganmain.Pregunta;
import com.magally.michiganmain.R;
import com.magally.michiganmain.Tasks.GetMyQuestTask;

/**
 * Created by Magally on 26-08-2015.
 */
public class QuestionsFragment extends android.support.v4.app.Fragment {
    Pregunta[] preguntaArray;
    ListView preguntasList;
    View rootview;
    ListAdapter adaptador;
    GetMyQuestTask getMyQuestTask;
    String username;
    SharedPreferences sharedUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.preguntas_layout,container,false);
        preguntasList = (ListView) rootview.findViewById(R.id.pregLV);
        sharedUsername = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        username = sharedUsername.getString("usuario","");
        getMyQuestTask = new GetMyQuestTask(getActivity(),new GetMyQuestTask.AsyncResponse() {
            @Override
            public void processFinish(Pregunta[] output) {
                Log.d("FeedFragment", "on processFinish Method ! " + output.length);
                preguntaArray = output;
                adaptador = new CustomAdapter(getActivity(), preguntaArray);
                preguntasList.setAdapter(adaptador);

            }
        }, username);
        getMyQuestTask.execute();


        return rootview;
    }
}