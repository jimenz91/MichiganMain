package com.magally.michiganmain.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.magally.michiganmain.R;
import com.magally.michiganmain.Respuesta;
import com.magally.michiganmain.Tasks.GetAnswersTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;


public class Answers extends ActionBarActivity {
    static final int REQUEST_IMAGE_CAPTURE =1;
    static final String LOG_TAG = Answers.class.getSimpleName();
    private static String url_create_user = "http://192.168.1.113/michigan_server/new_answer.php";
    boolean mNewImage = false;
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
    int preguntaID = 2;
    private Uri mPictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
       Intent intent = getIntent();
       if(intent.hasExtra("preguntaID")) {
           preguntaID = intent.getIntExtra("preguntaID", 0);
       }
        Log.d("Answers Activity", "onCreate Method");

        sharedPref = getApplication().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        username = sharedPref.getString("usuario","");
        usuarioId = sharedPref.getLong("uid",0);
       // usuarioId = sharedPref.getLong("")

        answerET = (EditText) findViewById(R.id.answerET);
        answerBtn = (Button) findViewById(R.id.answerBtn);
        answersLV = (ListView) findViewById(R.id.answersLV);
        answerIV = (ImageView) findViewById(R.id.answerIV);

        // if(preguntaID==0){
            //Toast.makeText(getApplicationContext(), "Error al obtener ID de pregunta",Toast.LENGTH_SHORT).show();
        //}else {
        updateAnswerList();
        // }

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
                if (mNewImage){
                    File file = new File(getPath(mPictureUri));
                    if(file.exists()) {
                        upload(file);
                    }else{
                        Log.d("NewQuestion","File not found");
                        Toast.makeText(getApplication(), "Error: No se encontro la imagen", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else{
                    Toast.makeText(getApplication(), "Por favor ingresar foto", Toast.LENGTH_SHORT).show();
                    return;
                }

//                new NewAnswerTask(Answers.this,respuesta,foto,preguntaId, usuarioId,username).execute();
//                finish();



            }
        });

    }

    private void updateAnswerList() {
        getAnswersTask = new GetAnswersTask(this, 2, new GetAnswersTask.AsyncResponse() {
            @Override
            public void processFinish(Respuesta[] output) {
                Log.d("BuscarFragment", "on processFinish Method ! " + output.length);
                respuestaArray = output;

                adaptador = new CustomAdapter2(getApplicationContext(), respuestaArray);
                answersLV.setAdapter(adaptador);

            }
        });
        getAnswersTask.execute();
    }

    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            String result = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "", "");
            mPictureUri = Uri.parse(result);
            answerIV.setImageBitmap(photo);
            Log.d("New Question: ", "Image Path: " + result);
            Toast.makeText(getApplicationContext(), "Path: " + result, Toast.LENGTH_SHORT).show();
            mNewImage = true;
        }
    }

    private void upload(File file) {
        Log.d(LOG_TAG, "In upload method");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("respuesta", respuesta);
        requestParams.put("pregunta_id", preguntaID);
        requestParams.put("usuario_id", Long.toString(usuarioId));
        requestParams.put("username", username);

        try {
            requestParams.put("uploaded_file", file);
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File not Found", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(LOG_TAG,"Upload Method: Request Params created. Post");

        client.post(url_create_user, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d(LOG_TAG, "client.post Success");
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        answerET.setText("");
                        answerIV.setImageResource(R.drawable.prueba);
                        updateAnswerList();
                    } else{
                        Toast.makeText(getApplicationContext(),"Fail. Success: "+statusCode, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(LOG_TAG, "client.post Failure SC: "+statusCode);
                Toast.makeText(getApplicationContext(),"Fail. Status Code: "+statusCode, Toast.LENGTH_LONG).show();
            }
        });


    }

    private String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

}
