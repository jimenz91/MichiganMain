package com.magally.michiganmain.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magally.michiganmain.R;

/**
 * Created by Magally on 26-08-2015.
 */
public class MateriasFragment extends android.support.v4.app.Fragment {

    View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.materias_layout,container,false);

        return rootview;
    }
}
