package com.magally.michiganmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.magally.michiganmain.Tasks.LoginTask;


public class LoginActivity extends ActionBarActivity {

    EditText userET, passET;
    String usuario,contrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button iniSesionButton = (Button) findViewById(R.id.loginButton);
        Button regButton = (Button) findViewById(R.id.signUpButton);

        userET = (EditText) findViewById(R.id.loginUsET);
        passET = (EditText) findViewById(R.id.loginPasET);
        
        ImageView logoIV = (ImageView) findViewById(R.id.logoIV);


        iniSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = userET.getText().toString();
                contrasena = passET.getText().toString();
                new LoginTask(LoginActivity.this,usuario,contrasena).execute();
                Intent iSes = new Intent(getApplication(), MainActivity.class);
                iSes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(iSes);
                finish();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iReg = new Intent(getApplication(), Register.class);
                startActivity(iReg);
            }
        });

    }




}
