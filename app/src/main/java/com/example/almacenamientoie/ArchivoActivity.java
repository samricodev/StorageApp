package com.example.almacenamientoie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ArchivoActivity extends AppCompatActivity {

    EditText nombreArchivo;
    Button action;
    String contenido = "";
    int tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivo);

        nombreArchivo = findViewById(R.id.txtNombreArchivo);
        action = findViewById(R.id.btnAction);
        tipo = (int) getIntent().getIntExtra("tipo",1);
        switch (tipo){
            case 1: action.setText("Abrir"); break;
            case 2: action.setText("Guardar");
                contenido = (String) getIntent().getStringExtra("contenido");
                break;
        }
    }

    public void realizaAccion(View view){
        String nomArch = nombreArchivo.getText().toString();
        if(nomArch != ""){
            switch (tipo){
                case 1: abrirArchivoSD(nomArch); break;
                case 2: guardarArchivoSD(nomArch); break;
            }
            finish();
        } else {
            Toast.makeText(this, "Debes escribir un nombre", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirArchivoSD(String name){
        try {
            File tarjetaSD = Environment.getExternalStorageDirectory();
            File archivo = new File(tarjetaSD.getPath(), name);
            InputStreamReader abrirArchivo = new InputStreamReader(openFileInput(archivo.getName()));
            BufferedReader leerArchivo = new BufferedReader(abrirArchivo);
            String linea = leerArchivo.readLine();
            String textoLeido = "";
            while(linea != null){
                textoLeido += linea + '\n';
                linea = leerArchivo.readLine();
            }
            leerArchivo.close();
            abrirArchivo.close();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("contenido", textoLeido);
            startActivity(intent);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            Toast.makeText(this, "El archivo no se pudo abrir", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarArchivoSD(String name){
        try {
            File tarjetaSD = Environment.getExternalStorageDirectory();
            Toast.makeText(this, tarjetaSD.getPath(), Toast.LENGTH_SHORT).show();
            File archivo = new File(tarjetaSD.getPath(), name);
            OutputStreamWriter crearArchivo = new OutputStreamWriter(openFileOutput(archivo.getName(), Activity.MODE_PRIVATE));
            crearArchivo.write(contenido);
            crearArchivo.flush();
            crearArchivo.close();
            Toast.makeText(this, "Informacion almacenada!!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            Toast.makeText(this, "El archivo no se pudo leer", Toast.LENGTH_SHORT).show();
        }
    }
}