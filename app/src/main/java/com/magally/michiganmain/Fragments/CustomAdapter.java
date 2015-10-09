package com.magally.michiganmain.Fragments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.magally.michiganmain.OtherUser;
import com.magally.michiganmain.Pregunta;
import com.magally.michiganmain.R;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


class CustomAdapter extends ArrayAdapter<Pregunta> {



    private Context mContext;
    private Pregunta pregunta;
    private static final String URL_REPUTATION = "http://192.168.1.113/michigan_server/question_rep.php";
    CustomAdapter(Context context, Pregunta[] preguntas) {
        super(context, R.layout.feed_row,preguntas);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater rowInflater =LayoutInflater.from(getContext());
        View customView = rowInflater.inflate(R.layout.feed_row, parent, false);
        ViewHolder v = new ViewHolder(customView);
        final Pregunta singlePregunta = getItem(position);


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
        v.numRespuestaTextView.setText(singlePregunta.getRespuestaCount()+ " Respuestas");
        v.repTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Asociar intent con la pregunta para sacar las respuestas que son
                Intent answersI = new Intent(getContext(), Answers.class);

                answersI.putExtra("pregunta_id",singlePregunta.getPreguntaID());
                mContext.startActivity(answersI);
            }
        });

        v.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),"+1",Toast.LENGTH_SHORT).show();
                updateRep("up",singlePregunta);
            }
        });

        v.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRep("down",singlePregunta);
                //Toast.makeText(getContext(), "-1", Toast.LENGTH_SHORT).show();
            }
        });

        v.usuarioTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Asociar intent con el usuario que es
                Intent otherUI = new Intent(getContext(), OtherUser.class);
                otherUI.putExtra("usuario",singlePregunta.getUsername());
                mContext.startActivity(otherUI);
                Toast.makeText(getContext(),"usuario",Toast.LENGTH_LONG).show();

            }
        });
        v.numRespuestaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Answers.class);
                intent.putExtra("pregunta_id", singlePregunta.getPreguntaID());
                mContext.startActivity(intent);
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

    private void updateRep(final String rep, Pregunta question){
        Log.d("CustomAdapter","in updateRep Method");
        this.pregunta = question;

        RequestParams params = new RequestParams();

        params.put("qid",pregunta.getPreguntaID());
        if (rep.equals("up")){
            params.put("reputation","up");
        } else{
            params.put("reputation","down");
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL_REPUTATION,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    if(response.getInt("success")==1){
                        long reputacion = Long.valueOf(pregunta.getReputacion());
                        if(rep.equals("up")) {

                            pregunta.setReputacion(String.valueOf(++reputacion));
                            notifyDataSetChanged();
                        }else{
                            pregunta.setReputacion(String.valueOf(--reputacion));
                            notifyDataSetChanged();
                        }
                        //Toast.makeText(getContext(), "Reputacion actualizada", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "No se pudo actualizar en estos momentos", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
