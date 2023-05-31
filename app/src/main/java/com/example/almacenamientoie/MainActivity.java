package com.example.almacenamientoie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    public EditText editor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.txtEditor);
        editor.setText((String) getIntent().getStringExtra("contenido"));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_archivo, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.itemAbrir: abrirArchivo(); break;
            case R.id.itemGuardar: guardarArchivo(); break;
            case R.id.itemAbrirSD:
                intent = new Intent(this, ArchivoActivity.class);
                intent.putExtra("tipo", 1);
                startActivity(intent);
                break;
            case R.id.itemGuardarSD:
                intent = new Intent(this, ArchivoActivity.class);
                intent.putExtra("tipo", 2);
                intent.putExtra("contenido", editor.getText().toString());
                startActivity(intent);
                editor.setText("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirArchivo(){
        String archivos [] = fileList();
        if(existeArchivo(archivos, "appclase10.txt")){
            try{
                InputStreamReader archivoInterno = new InputStreamReader(openFileInput("appclase10.txt"));
                BufferedReader leerArchivo = new BufferedReader(archivoInterno);
                String linea = leerArchivo.readLine();
                String textoLeido = "";
                while( linea != null){
                    textoLeido += linea + '\n';
                    linea = leerArchivo.readLine();
                }
                leerArchivo.close();
                archivoInterno.close();
                editor.setText(textoLeido);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                Toast.makeText(this, "Error al leer el archivo.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "El archivo no existe.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean existeArchivo(String[] archivos, String file) {
        for (int i = 0; i < archivos.length; i++)
            if(file.equals(archivos[i]))
                return true;
        return false;
    }

    private void guardarArchivo(){
        try {
            OutputStreamWriter archivoInterno = new OutputStreamWriter(openFileOutput("appclase10.txt", Activity.MODE_PRIVATE));
            archivoInterno.write(editor.getText().toString());
            archivoInterno.flush();
            archivoInterno.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            Toast.makeText(this, "Error al escribir en el archivo", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Archivo Guardado", Toast.LENGTH_SHORT).show();
        editor.setText("");
    }
}