package com.melisaviera.textocatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class VozToText extends AppCompatActivity {

    private TextView textView;
    private ImageButton imageButton;
    private final int VOZ = 1;
    private final int CAMARA = 2;
    private Button btnEscuchar;
    private Button button;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//invalida la rotacion de la pantalla
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//Evita el modo oscuro de los telefonos
        setContentView(R.layout.activity_voz_to_text);

        btnEscuchar=findViewById(R.id.btnEscuchar);
        textView = findViewById(R.id.textView);
        imageButton = findViewById(R.id.imageButton);

        // create an object textToSpeech and adding features into it
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });



        // Adding OnClickListener
        btnEscuchar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(textView.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });



        imageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                capturarVoz();
            }
        });

    }

    private void capturarVoz()
    {
        Intent intent = new Intent(RecognizerIntent
                .ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, VOZ);
        } else
        {
            Log.e("ERROR","Su dispositivo no admite entrada de voz");
        }
    }

    @Override
    protected  void onActivityResult(int requestCode,
                                     int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOZ && resultCode == RESULT_OK && data != null)
        {
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            textView.setText(result.get(0));
        }

    }



}