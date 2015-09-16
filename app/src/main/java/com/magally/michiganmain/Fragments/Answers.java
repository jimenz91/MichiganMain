package com.magally.michiganmain.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.magally.michiganmain.R;
import com.magally.michiganmain.Respuesta;
import com.magally.michiganmain.Tasks.GetAnswersTask;
import com.magally.michiganmain.Tasks.NewAnswerTask;
import com.magally.michiganmain.Tasks.QuestionSearchTask;


public class Answers extends ActionBarActivity {
    static final int REQUEST_IMAGE_CAPTURE =1;
    ImageView answerIV;
    EditText answerET;
    ListView answersLV;
    Button answerBtn;
    SharedPreferences sharedPref;
    String respuesta,  username, foto = "1";
    long preguntaId, usuarioId = 2;
    Respuesta[] respuestaArray;
    ListView respuestasList;
    ListAdapter adaptador;
    GetAnswersTask getAnswersTask;
    TextView usuarioTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        getAnswersTask = new QuestionSearchTask(this,new GetAnswersTask().AsyncResponse() {
            @Override
            public void processFinish2(Respuesta[] output) {
                Log.d("BuscarFragment", "on processFinish Method ! " + output.length);
                respuestaArray = output;
                adaptador = new CustomAdapter2(this, respuestaArray);
                respuestasList.setAdapter(adaptador);

            }
        }, preguntaId);

        sharedPref = getApplication().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        username = sharedPref.getString("usuario","");


        answerET = (EditText) findViewById(R.id.answerET);
        answerBtn = (Button) findViewById(R.id.answerBtn);
        answersLV = (ListView) findViewById(R.id.answersLV);
        answerIV = (ImageView) findViewById(R.id.answerIV);

        if(!hasCamera())
            answerIV.setEnabled(false);

        answerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camI = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camI,REQUEST_IMAGE_CAPTURE);
            }
        });

        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                respuesta = answerET.getText().toString();

                if (respuesta.equals("")){
                    Toast.makeText(getApplication(), "Por favor ingresar respuesta", Toast.LENGTH_SHORT).show();
                    return;
                }

                new NewAnswerTask(Answers.this,respuesta,foto,preguntaId, usuarioId,username).execute();
                finish();



            }
        });

    }

    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            answerIV.setImageBitmap(photo);
        }
    }

}
