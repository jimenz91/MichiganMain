package com.magally.michiganmain.Fragments;

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

import com.magally.michiganmain.Materias;
import com.magally.michiganmain.R;
import com.magally.michiganmain.Respuesta;
import com.magally.michiganmain.Tasks.QuestionSearchTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 26-08-2015.
 */
public class BuscarFragment extends android.support.v4.app.Fragment {

    View rootview;
    Spinner mateSpinner;
    String materia, clave1, clave2, clave3;
    long carrera;
    Button buscarBtn;
    EditText buscarET1, buscarET2, buscarET3;
    QuestionSearchTask questionSearchTask;
    ListView respuestaList;
    ListAdapter adaptador;
    Respuesta[] respuestaArray;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.buscar_layout,container,false);

        buscarBtn = (Button) rootview.findViewById(R.id.buscarBtn);
        buscarET1 = (EditText) rootview.findViewById(R.id.buscarET1);
        buscarET2 = (EditText) rootview.findViewById(R.id.buscarET2);
        buscarET3 = (EditText) rootview.findViewById(R.id.buscarET3);
        mateSpinner = (Spinner) rootview.findViewById(R.id.buscarPregSpinner);
        respuestaList = (ListView) rootview.findViewById(R.id.answersLV);

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
                clave2 = buscarET2.getText().toString();
                clave3 = buscarET3.getText().toString();
                materia = mateSpinner.getSelectedItem().toString();
                Toast.makeText(getActivity(), materia, Toast.LENGTH_SHORT).show();


                questionSearchTask.execute();



            }
        });

        return rootview;
    }
}
