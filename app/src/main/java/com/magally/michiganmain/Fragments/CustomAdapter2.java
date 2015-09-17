package com.magally.michiganmain.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magally.michiganmain.R;
import com.magally.michiganmain.Respuesta;
import com.squareup.picasso.Picasso;


class CustomAdapter2 extends ArrayAdapter<Respuesta> {


    CustomAdapter2(Context context, Respuesta[] respuestas) {
        super(context, R.layout.answer_row, respuestas);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater rowInflater = LayoutInflater.from(getContext());
        View customView = rowInflater.inflate(R.layout.answer_row, parent, false);
        ViewHolder v = new ViewHolder(customView);
        Respuesta singleRespuesta = getItem(position);
        v.respuestaTextView.setText(singleRespuesta.getRespuesta());
        v.usuarioTextView.setText(singleRespuesta.getUsername());
        Picasso.with(getContext())
                .load(singleRespuesta.getFoto())
                .placeholder(R.drawable.silhouette)
                .error(R.drawable.prueba)
                .fit()
                .centerCrop()
                .into((ImageView) customView.findViewById(R.id.respuestaImg));

        return customView;

    }


    public static class ViewHolder {
        public final ImageView respuestaImg;
        public final TextView respuestaTextView, usuarioTextView, reputacionTextView;


        public ViewHolder(View view) {
            respuestaImg = (ImageView) view.findViewById(R.id.respuestaImg);
            respuestaTextView = (TextView) view.findViewById(R.id.respuestaFeed);
            usuarioTextView = (TextView) view.findViewById(R.id.aUserTV);
            reputacionTextView = (TextView) view.findViewById(R.id.repTV2);


        }

    }
}
