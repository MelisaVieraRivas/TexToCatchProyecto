package com.melisaviera.textocatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;

public class TextToVoz2 extends AppCompatActivity {

    private ListView lv1;
    private ImageView iv1;
    private String[] archivos;
    private ArrayAdapter<String> adaptador1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//invalida la rotacion de la pantalla
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//Evita el modo oscuro de los telefonos
        setContentView(R.layout.activity_text_to_voz2);

        File dir = getExternalFilesDir(null);
        archivos = dir.list();
        adaptador1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, archivos);
        lv1 = (ListView) findViewById(R.id.listView1);
        lv1.setAdapter(adaptador1);

        iv1 = (ImageView) findViewById(R.id.imageView1);



        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Bitmap bitmap1 = BitmapFactory.decodeFile(getExternalFilesDir(null) + "/" + archivos[arg2]);
                iv1.setImageBitmap(bitmap1);
            }
        });

    }
}