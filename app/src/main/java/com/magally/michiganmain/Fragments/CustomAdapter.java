package com.magally.michiganmain.Fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.magally.michiganmain.R;
import com.squareup.picasso.Picasso;

class CustomAdapter extends ArrayAdapter<Pregunta> {
/*
TODO: colocar los datos de reputacion en el TextView Correspondiente
 */
    CustomAdapter(Context context, Pregunta[] preguntas) {
        super(context, R.layout.feed_row,preguntas);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater rowInflater =LayoutInflater.from(getContext());
        View customView = rowInflater.inflate(R.layout.feed_row, parent, false);
        ViewHolder v = new ViewHolder(customView);
        Pregunta singlePregunta = getItem(position);


        v.enunciadoTextView.setText(singlePregunta.getEnunciado());
        Log.d("GetFeedTask", singlePregunta.getFoto());

            Picasso.with(getContext())
                    .load(singlePregunta.getFoto())
                    .placeholder(R.drawable.silhouette)
                    .error(R.drawable.prueba)
                    .fit()
                    .centerCrop()
                    .into((ImageView)customView.findViewById(R.id.preguntaImg));

        v.usuarioTextView.setText(singlePregunta.getUsername());
        v.repTV.setText(singlePregunta.getReputacion());
        v.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"+1",Toast.LENGTH_SHORT).show();
            }
        });

        v.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "-1", Toast.LENGTH_SHORT).show();
            }
        });

        return customView;

    }


    public static class ViewHolder{
        public final ImageView preguntaImg;
        public final TextView  enunciadoTextView, usuarioTextView, numRespuestaTextView, repTV;
        public final Button plusButton, minusButton;

        public ViewHolder(View view){
            preguntaImg = (ImageView)view.findViewById(R.id.preguntaImg);
            enunciadoTextView = (TextView)view.findViewById(R.id.enunciadoFeed);
            usuarioTextView = (TextView)view.findViewById(R.id.qUserTV);
            numRespuestaTextView = (TextView)view.findViewById(R.id.numRespuestasTV);
            plusButton = (Button)view.findViewById(R.id.plusBtn);
            minusButton = (Button)view.findViewById(R.id.minusBtn);
            repTV = (TextView)view.findViewById(R.id.repTV);







        }

    }
}
