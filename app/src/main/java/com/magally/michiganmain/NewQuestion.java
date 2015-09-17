package com.magally.michiganmain;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class NewQuestion extends ActionBarActivity {
    private static String url_create_user = "http://192.168.1.113/michigan_server/new_question.php";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText enunPregET;
    String foto = "hola", usuario, enunciado;
    Spinner matSpinner, temSpinner;
    Button tomarBtn, pregBtn;
    ImageView tomarPregIV;
    SharedPreferences sharedPref;
    long tema_id = 3, usuario_id = 2;
    Uri mPictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_question);

        enunPregET = (EditText) findViewById(R.id.enunPregET);
        matSpinner = (Spinner) findViewById(R.id.matSpinner);
        temSpinner = (Spinner) findViewById(R.id.temSpinner);
        tomarBtn = (Button) findViewById(R.id.tomarBtn);
        pregBtn = (Button) findViewById(R.id.pregBtn);
        tomarPregIV = (ImageView) findViewById(R.id.tomarPregIV);
        String mat = Materias.MATERIAS.get("2");

        final List<String> matSpinArray = new ArrayList<String>();
        matSpinArray.add(mat);
        matSpinArray.add(Materias.MATERIAS.get("4"));
        matSpinArray.add(Materias.MATERIAS.get("5"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, matSpinArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        matSpinner.setAdapter(adapter);

        final List<String> temSpinArray = new ArrayList<String>();
        temSpinArray.add(Temas.TEMAS.get("3"));
        temSpinArray.add(Temas.TEMAS.get("4"));
        temSpinArray.add(Temas.TEMAS.get("5"));
        temSpinArray.add(Temas.TEMAS.get("6"));
        temSpinArray.add(Temas.TEMAS.get("7"));

        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, temSpinArray);

        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temSpinner.setAdapter(adapters);

        sharedPref = getApplication().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", "");

        if (!hasCamera())
            tomarBtn.setEnabled(false);

        tomarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camI = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //camI.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);
                startActivityForResult(camI, REQUEST_IMAGE_CAPTURE);
            }
        });


        pregBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enunciado = enunPregET.getText().toString();
                //new NewQuestionTask(NewQuestion.this, enunciado, foto, tema_id, usuario_id, usuario);
                File file = new File(getPath(mPictureUri));
                if(file.exists()) {
                    upload(file);
                }else{
                    Log.d("NewQuestion","File not found");
                }
                Intent qMain = new Intent(getApplication(), MainActivity.class);
                startActivity(qMain);
            }
        });

    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            String result = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "", "");
            mPictureUri = Uri.parse(result);
            tomarPregIV.setImageBitmap(photo);
            Log.d("New Question: ", "Image Path: " + result);
            Toast.makeText(getApplicationContext(), "Path: " + result, Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(File file) {
        Log.d("NewQuestion", "In upload method");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("enunciado", enunciado);
        requestParams.put("tema_id", Long.toString(tema_id));
        requestParams.put("usuario_id", Long.toString(usuario_id));
        requestParams.put("username", usuario);

        try {
            requestParams.put("uploaded_file", file);
        } catch (FileNotFoundException e) {
           // e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File not Found", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("NewQuestion","Upload Method: Request Params created. Post");

        client.post(url_create_user, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("NewQuestion", "client.post Success");
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(getApplicationContext(),"Fail. StatusCode: "+statusCode, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("NewQuestion", "client.post Failure SC: "+statusCode);
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
