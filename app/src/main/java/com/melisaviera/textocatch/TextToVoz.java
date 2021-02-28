package com.melisaviera.textocatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class TextToVoz extends AppCompatActivity {
    Button btnCamara;
    ImageView imgView;
    Button btnLista;
    ImageButton btnLeer;
    Button btnGaleria;
    ImageButton btnWhats;
    EditText textView;
    ImageButton btnStop;
    TextToSpeech textToSpeech;
    //Codigos para los activitysresoult
    private final int CAMARA = 1;
    private final int VOZ = 2;
    private final int PICK_IMAGE=3;
    //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//invalida la rotacion de la pantalla
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//Evita el modo oscuro de los telefonos
        setContentView(R.layout.activity_text_to_voz);
        btnStop=findViewById(R.id.btnStop);
        btnGaleria=findViewById(R.id.btbGaleria);
        btnWhats=findViewById(R.id.btnWhats);
        btnLeer=findViewById(R.id.btnLeer);
        btnCamara = findViewById(R.id.btnCamara);
        imgView = findViewById(R.id.imageView);
        btnLista = findViewById(R.id.btnlista);
        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());//Permite el scroll en el text view

        // create an object textToSpeech and adding features into it
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.stop();
            }
        });
        btnWhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textView.getText().toString());
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);

            }
        });
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento1 = new Intent(TextToVoz.this, TextToVoz2.class);
                startActivity(intento1);
            }
        });
        btnLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(textView.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();

            }
        });
        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
    }


    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMARA);
        }

    }


    private void capturarVoz() {
        Intent intent = new Intent(RecognizerIntent
                .ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, VOZ);
        } else {
            Log.e("ERROR", "Su dispositivo no admite entrada de voz");
        }
    }




   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMARA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imgBitmap);//Muestra la imagen tomada en el textview
            //Comienzo del alert dialog
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Nombre de la foto:");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String nombre = input.getText().toString();
                    String filename = nombre + ".png";
                    File sd = getExternalFilesDir(null);
                    File dest = new File(sd, filename);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    try {
                        FileOutputStream out = new FileOutputStream(dest);//Guarda la imagen en la ruta de la variable dest
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();//Reconoce el texto de la imagen del imageview
                        if (!txtRecognizer.isOperational()) {
                            textView.setText("Detector dependencies are not yet available");
                        } else {
                            Frame frame = new Frame.Builder().setBitmap(imgBitmap).build();
                            SparseArray items = txtRecognizer.detect(frame);
                            StringBuilder strBuilder = new StringBuilder();
                            for (int i = 0; i < items.size(); i++) {
                                TextBlock item = (TextBlock) items.valueAt(i);
                                strBuilder.append(item.getValue());
                                strBuilder.append("/");
                                for (Text line : item.getComponents()) {
                                    //extract scanned text lines here
                                    Log.v("lines", line.getValue());
                                    for (Text element : line.getComponents()) {
                                        //extract scanned text words here
                                        Log.v("element", element.getValue());
                                    }
                                }
                            }
                            textView.setText(strBuilder.toString().substring(0, strBuilder.toString().length() - 1));//Una vez reconocido el texto lo muestra en el textview
                        }
                    }catch (Exception e){
                        textView.setText("No hay texto reconocible");
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();//Si oprimimos cancel no guarda la foto ni reconoce el texto
                }
            });
            builder.show();
            //fin alert dialog
        } else if (requestCode == VOZ && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            textView.setText(result.get(0));
        }else if(resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            imgView.setImageURI(imageUri);
            try {
                Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();//Reconoce el texto de la imagen del imageview
                if (!txtRecognizer.isOperational()) {
                    textView.setText("Detector dependencies are not yet available");
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray items = txtRecognizer.detect(frame);
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = (TextBlock) items.valueAt(i);
                        strBuilder.append(item.getValue());
                        strBuilder.append("/");
                        for (Text line : item.getComponents()) {
                            //extract scanned text lines here
                            Log.v("lines", line.getValue());
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                Log.v("element", element.getValue());
                            }
                        }
                    }
                    textView.setText(strBuilder.toString().substring(0, strBuilder.toString().length() - 1));//Una vez reconocido el texto lo muestra en el textview
                }
            }catch (Exception e){
                textView.setText("No hay texto reconocible");

            }
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        textToSpeech.stop();

    }
    @Override
    protected void onStop() {
        super.onStop();

        textToSpeech.stop();

    }





}