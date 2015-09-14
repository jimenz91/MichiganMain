package com.magally.michiganmain.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.magally.michiganmain.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magally on 26-08-2015.
 */
public class BuscarFragment extends android.support.v4.app.Fragment {

    View rootview;
    Spinner spinner;
    long carrera;
    Button buscarBtn;
    EditText buscarET;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.buscar_layout,container,false);

        buscarBtn = (Button) rootview.findViewById(R.id.buscarBtn);
        buscarET = (EditText) rootview.findViewById(R.id.buscarET);

        final List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Telecomunicaciones");
        spinnerArray.add("Informatica");
        spinnerArray.add("Civil");
        spinnerArray.add("Industrial");
        spinnerArray.add("Administración");
        spinnerArray.add("Contaduría");
        spinnerArray.add("Economía");
        spinnerArray.add("Comunicación Social");
        spinnerArray.add("Psicología");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sCarreras = (Spinner) rootview.findViewById(R.id.buscarPregSpinner);
        sCarreras.setAdapter(adapter);

        //carrera = spinner.getSelectedItemId() + 1;

        return rootview;
    }
}
