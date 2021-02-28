package com.melisaviera.textocatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Seleccion extends AppCompatActivity {

    ImageButton btnVozTexto;
    ImageButton btnTextVoz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//invalida la rotacion de la pantalla
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//Evita el modo oscuro de los telefonos
        setContentView(R.layout.activity_seleccion);

        btnTextVoz=findViewById(R.id.btnTextoVoz);
        btnVozTexto=findViewById(R.id.btnVozTexto);

        btnVozTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento1 = new Intent(Seleccion.this, VozToText.class);
                startActivity(intento1);

            }
        });
        btnTextVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento1 = new Intent(Seleccion.this, TextToVoz.class);
                startActivity(intento1);

            }
        });



    }
}