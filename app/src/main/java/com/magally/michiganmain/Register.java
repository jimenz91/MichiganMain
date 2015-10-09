package com.magally.michiganmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.magally.michiganmain.Tasks.NewUserTask;

import java.util.ArrayList;
import java.util.List;


public class Register extends ActionBarActivity {

    EditText nombreTxt, apellidoTxT, correoTxt, usuarioTxt, contrasenaTxt;
    Button regButton;
    RadioButton estRadio, profRadio;
    String nombre, apellido, correo, usuario, contrasena, nombreCompleto;
    long carrera;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        final List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Telecomunicaciones");
        spinnerArray.add("Informatica");
        spinnerArray.add("Civil");
        spinnerArray.add("Industrial");
        spinnerArray.add("Administración");
        spinnerArray.add("Contaduría");
        spinnerArray.add("Economía");
        spinnerArray.add("Comunicación Social");
        spinnerArray.add("Psicología");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sCarreras = (Spinner) findViewById(R.id.spinner);
        sCarreras.setAdapter(adapter);

        nombreTxt = (EditText) findViewById(R.id.editText3);
        apellidoTxT = (EditText) findViewById(R.id.editText4);
        correoTxt = (EditText) findViewById(R.id.editText5);
        usuarioTxt = (EditText) findViewById(R.id.editText6);
        contrasenaTxt = (EditText) findViewById(R.id.editText7);

        spinner = (Spinner) findViewById(R.id.spinner);

        regButton = (Button) findViewById(R.id.registrarButton);

        profRadio = (RadioButton) findViewById(R.id.radioButton);
        estRadio = (RadioButton) findViewById(R.id.radioButton2);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                nombre = nombreTxt.getText().toString().trim();

                if (nombre.equals("")){
                    Toast.makeText(getApplication(),"Por favor ingresar nombre",Toast.LENGTH_SHORT).show();
                    return;
                }

                apellido = apellidoTxT.getText().toString().trim();

                if (apellido.equals("")){
                    Toast.makeText(getApplication(),"Por favor ingresar apellido",Toast.LENGTH_SHORT).show();
                    return;
                }
                String mail = correoTxt.getText().toString();
                if(!mail.equals("")) {

                    if (profRadio.isChecked()) {
                        correo = correoTxt.getText().toString().trim() + "@ucab.edu.ve";
                    } else if (estRadio.isChecked()) {
                        correo = correoTxt.getText().toString().trim() + "@est.ucab.edu.ve";
                    } else {
                        Toast.makeText(getApplication(), "Por favor seleccionar tipo de correo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else{
                    Toast.makeText(getApplication(),"Por favor ingresar correo", Toast.LENGTH_SHORT).show();
                    return;
                }

                usuario =usuarioTxt.getText().toString().trim();
                if (usuario.equals("")){
                    Toast.makeText(getApplication(),"Por favor ingresar nombre de usuario",Toast.LENGTH_SHORT).show();
                    return;
                }

                contrasena = contrasenaTxt.getText().toString().trim();
                if (contrasena.equals("")){
                    Toast.makeText(getApplication(),"Por favor ingresar contraseña",Toast.LENGTH_SHORT).show();
                    return;
                }

                nombreCompleto = nombre+" "+apellido;

                carrera = spinner.getSelectedItemId() + 1;
                new NewUserTask(Register.this, nombre, apellido, correo, usuario, contrasena, carrera).execute();
                Intent iReg = new Intent(getApplication(), LoginActivity.class);
                iReg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(iReg);




            }

        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_registrar, menu);
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
