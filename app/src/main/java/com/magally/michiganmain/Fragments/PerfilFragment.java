package com.magally.michiganmain.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magally.michiganmain.R;
import com.magally.michiganmain.Tasks.GetUserInfoTask;

/**
 * Created by Magally on 26-08-2015.
 */
public class PerfilFragment extends android.support.v4.app.Fragment {

    View rootview;
    TextView username, nombreCompleto, carrera, reputacion;
    ImageView profileImgView;
    SharedPreferences sharedUserame;
    String usuario;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.perfil_layout,container,false);

        username = (TextView) rootview.findViewById(R.id.profileUserTV);
        nombreCompleto = (TextView) rootview.findViewById(R.id.profileNameTxt);
        carrera = (TextView) rootview.findViewById(R.id.carreraTxtVw);
        reputacion = (TextView) rootview.findViewById(R.id.reputacionTxTView);
        profileImgView = (ImageView) rootview.findViewById(R.id.profileImgView);

        sharedUserame = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        usuario = sharedUserame.getString("usuario","");

        new GetUserInfoTask(getActivity(),usuario).execute();

        return rootview;
    }
}
