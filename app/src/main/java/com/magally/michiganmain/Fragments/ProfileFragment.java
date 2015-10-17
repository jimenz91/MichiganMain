package com.magally.michiganmain.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.magally.michiganmain.R;
import com.magally.michiganmain.Tasks.GetUserInfoTask;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Magally on 26-08-2015.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {

    private static final int SELECT_FILE = 2;
    private static final String UPDATE_PROFILE_PIC_URL = "http://192.168.1.113/michigan_server/update_profile_pic.php";
    View rootview;
    TextView username, nombreCompleto, carrera, reputacion;
    ImageView profileImgView;
    SharedPreferences sharedUserame;
    String usuario;
    private long uid;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private File fileToUpload = null;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.perfil_layout,container,false);

        username = (TextView) rootview.findViewById(R.id.profileUserTV);
        nombreCompleto = (TextView) rootview.findViewById(R.id.profileNameTxt);
        carrera = (TextView) rootview.findViewById(R.id.carreraTxtVw);
        reputacion = (TextView) rootview.findViewById(R.id.reputacionTxTView);
        profileImgView = (ImageView) rootview.findViewById(R.id.profileImgView);

        sharedUserame = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        usuario = sharedUserame.getString("usuario", "");
        uid = sharedUserame.getLong("uid",0);
        profileImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        new GetUserInfoTask(getActivity(),usuario).execute();

        return rootview;
    }
    private void selectImage() {
        final CharSequence[] items = { "Tomar Foto", "Usar imagen existente",
                "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Cambiar Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Tomar Foto")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = null;
                    try {
                        f = createImageFile();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        Toast.makeText(getContext(),"Error al crear archivo",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(f!=null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }else{
                        Toast.makeText(getContext(),"Error al crear archivo",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (items[item].equals("Usar imagen existente")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileToUpload = null;
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                Picasso.with(getContext())
                        .load(new File(mCurrentPhotoPath))
                        .placeholder(R.drawable.silhouette)
                        .error(R.drawable.prueba)
                        .fit()
                        .into(profileImgView);
                fileToUpload = new File(mCurrentPhotoPath);
            }else if(requestCode == SELECT_FILE){

                Uri selectedImageUri = data.getData();
                String imagePath = getPath(selectedImageUri);
                Picasso.with(getContext())
                        .load(new File(imagePath))
                        .placeholder(R.drawable.silhouette)
                        .error(R.drawable.prueba)
                        .fit()
                        .into(profileImgView);
                fileToUpload = new File(imagePath);
            }
            if(fileToUpload!=null) {
                uploadPhoto();
            }else{
                Toast.makeText(getContext(),"Error al cargar imagen",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadPhoto() {
        if(uid==0){
            Toast.makeText(getContext(),"Error al buscar usuario",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Cargando Imagen...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("usuario_id",String.valueOf(uid));
        requestParams.put("usuario",usuario);
        try {
            requestParams.put("uploaded_file",fileToUpload);
        } catch (FileNotFoundException e) {
            Toast.makeText(getContext(),"Error al cargar imagen",Toast.LENGTH_SHORT).show();
            return;
        }
        client.post(getContext(),UPDATE_PROFILE_PIC_URL,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               // super.onSuccess(statusCode, headers, response);
                try {
                    int success = response.getInt("success");
                    if(success==1){
                        Toast.makeText(getContext(),"Imagen Actualizada",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }else{
                        Toast.makeText(getContext(),"Error al cargar archivo",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Toast.makeText(getContext(),"Error al cargar archivo",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getContext(),"Error en la comunicación",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getContext(),"Error en la comunicación",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
