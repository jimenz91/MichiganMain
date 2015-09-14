package com.magally.michiganmain.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.magally.michiganmain.R;
import com.magally.michiganmain.Tasks.GetFeedTask;

/**
 * Created by Magally on 26-08-2015.
 */
public class FeedFragment extends android.support.v4.app.Fragment {
    Pregunta[] preguntaArray;
    ListView preguntasList;
    View rootview;
    ListAdapter adaptador;
    GetFeedTask getFeedTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.feed_layout,container,false);
        preguntasList = (ListView) rootview.findViewById(R.id.feedList);
        getFeedTask = new GetFeedTask(getActivity(),new GetFeedTask.AsyncResponse() {
            @Override
            public void processFinish(Pregunta[] output) {
                Log.d("FeedFragment", "on processFinish Method ! "+  output.length);
                preguntaArray = output;
                adaptador = new CustomAdapter(getActivity(), preguntaArray);
                preguntasList.setAdapter(adaptador);

            }
        });
        getFeedTask.execute();
        //String[] preguntas ={"Uno","Dos","Tres","Cuatro","Cinco", "Seis"};


        //preguntasList.setAdapter(adaptador);



        return rootview;
    }
}
