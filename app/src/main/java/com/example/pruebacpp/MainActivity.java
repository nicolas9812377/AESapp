package com.example.pruebacpp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnTexto = findViewById(R.id.btnTexto);
        EditText txtpersona = findViewById(R.id.txtpersona);
        txtpersona.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Metricas.nombre = txtpersona.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Texto.class));
            }
        });

        Button btnarchivos = findViewById(R.id.btnArchivos);
        btnarchivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Archivos.class));
            }
        });

        Button btnGraficos = findViewById(R.id.btnGraficos);
        btnGraficos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Graficos.class));
            }
        });

    }



}