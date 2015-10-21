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
import com.magally.michiganmain.R;
import com.magally.michiganmain.Respuesta;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


class CustomAdapter2 extends ArrayAdapter<Respuesta> {

    private Context mContext;
    private static final String URL_REPUTATION_ANSWER = "http://192.168.1.113/michigan_server/update_rep_answer";
    CustomAdapter2(Context context, Respuesta[] respuestas) {

        super(context, R.layout.feed_row, respuestas);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater rowInflater = LayoutInflater.from(getContext());
        View customView = rowInflater.inflate(R.layout.answer_row, parent, false);
        ViewHolder v = new ViewHolder(customView);
        final Respuesta singleRespuesta = getItem(position);
        v.respuestaTextView.setText(singleRespuesta.getRespuesta());
        v.usuarioTextView.setText(singleRespuesta.getUsername());
        Picasso.with(getContext())
                .load(singleRespuesta.getFoto())
                .placeholder(R.drawable.silhouette)
                .error(R.drawable.prueba)
                .fit()
                .centerCrop()
                .into((ImageView) customView.findViewById(R.id.respuestaImg));
        v.usuarioTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Asociar intent con el usuario que es
                Intent otherUI = new Intent(getContext(), OtherUser.class);
                otherUI.putExtra("usuario",singleRespuesta.getUsername());
                mContext.startActivity(otherUI);
                Toast.makeText(getContext(), "usuario", Toast.LENGTH_LONG).show();

            }
        });

        v.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),"+1",Toast.LENGTH_SHORT).show();
                updateRep("up",singleRespuesta);
            }
        });

        v.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRep("down",singleRespuesta);
                //Toast.makeText(getContext(), "-1", Toast.LENGTH_SHORT).show();
            }
        });


        return customView;

    }


    public static class ViewHolder {
        public final ImageView respuestaImg;
        public final TextView respuestaTextView, usuarioTextView, reputacionTextView;
        public final Button plusButton, minusButton;


        public ViewHolder(View view) {
            respuestaImg = (ImageView) view.findViewById(R.id.respuestaImg);
            respuestaTextView = (TextView) view.findViewById(R.id.respuestaFeed);
            usuarioTextView = (TextView) view.findViewById(R.id.aUserTV);
            reputacionTextView = (TextView) view.findViewById(R.id.repTV2);
            plusButton = (Button) view.findViewById(R.id.answerPlusButton);
            minusButton = (Button) view.findViewById(R.id.answerMinusButton);


        }

    }

    private void updateRep(final String rep, final Respuesta answer){
        Log.d("CustomAdapter", "in updateRep Method");


        RequestParams params = new RequestParams();
        params.put("uid", answer.getUsername());
        params.put("aid",answer.getRespuestaID());
        if (rep.equals("up")){
            params.put("reputation","up");
        } else{
            params.put("reputation","down");
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL_REPUTATION_ANSWER,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    if(response.getInt("success")==1){
                        long reputacion = Long.valueOf(answer.getReputacion());
                        if(rep.equals("up")) {

                            answer.setReputacion(++reputacion);
                            notifyDataSetChanged();
                        }else{
                            answer.setReputacion(--reputacion);
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
