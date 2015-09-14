package com.magally.michiganmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        iniSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = userET.getText().toString();
                contrasena = passET.getText().toString();
                new LoginTask(LoginActivity.this,usuario,contrasena).execute();
                //Intent iSes = new Intent(getApplication(), MainActivity.class);
                //startActivity(iSes);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iReg = new Intent(getApplication(), Registrar.class);
                startActivity(iReg);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
