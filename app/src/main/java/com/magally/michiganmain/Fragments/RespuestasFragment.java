package com.magally.michiganmain.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.magally.michiganmain.NewQuestion;
import com.magally.michiganmain.R;

/**
 * Created by Magally on 26-08-2015.
 */
public class RespuestasFragment extends android.support.v4.app.Fragment {

    View rootview;
    Button pruebaBtn, pruebaBtn2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.respuestas_layout,container,false);
        pruebaBtn = (Button) rootview.findViewById(R.id.pruebaBtn);
        pruebaBtn2 = (Button) rootview.findViewById(R.id.pruebaBtn2);

        pruebaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prueba = new Intent(getActivity(), NewQuestion.class);
                startActivity(prueba);
            }
        });

        pruebaBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prueba = new Intent(getActivity(), Answers.class);
                startActivity(prueba);
            }
        });

        return rootview;
    }
}
