package com.example.isonidos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String> listaSonidos = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout principal = (LinearLayout) findViewById(R.id.botones);

        int numeroLinea = 0;
        LinearLayout auxiliar = creaLineaBotones(numeroLinea);
        principal.addView(auxiliar);

        Field[] listaCanciones = R.raw.class.getFields();

        int columnas = 5;
        for (int i = 0; i < listaCanciones.length; i++) {

            //creamos un botón por código y lo añadimos a la pantalla principal
            Button b = creaBoton(i, listaCanciones);

            //añadimos el botón al layout
            auxiliar.addView(b);
            if (i % columnas == columnas - 1) {
                auxiliar = creaLineaBotones(i);
                principal.addView(auxiliar);
            }
            listaSonidos.put(b.getTag().toString(), b.getText().toString());
            b.setText(acortaEtiquetaBoton((b.getText().toString())));
        }
    }

    private LinearLayout creaLineaBotones(int numeroLinea) {
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        parametros.weight = 1;
        LinearLayout linea = new LinearLayout(this);

        linea.setOrientation(LinearLayout.HORIZONTAL);
        linea.setLayoutParams(parametros);
        linea.setId(numeroLinea);
        return linea;
    }

    private Button creaBoton(int i, Field[] _listaCanciones) {
        LinearLayout.LayoutParams parametrosBotones = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        parametrosBotones.weight = 1;
        parametrosBotones.setMargins(8, 8, 8, 8);
        parametrosBotones.gravity = Gravity.CENTER_HORIZONTAL;
        Button b = new Button(this);
        b.setLayoutParams(parametrosBotones);
        b.setText(_listaCanciones[i].getName());
        b.setTextColor(Color.WHITE);
        b.setTextSize(8);
        b.setBackgroundColor(Color.rgb(73,182, 117));
        b.setAllCaps(true); //todas las letras del botón en mayúscula/minúscula
        int id = this.getResources().getIdentifier(_listaCanciones[i].getName(), "raw", this.getPackageName());
        String nombreLargo = _listaCanciones[i].getName();

        if (nombreLargo != null && nombreLargo.substring(0, 2).contains("v_")) {
            b.setBackgroundColor(Color.rgb(78, 59, 49));
        }
        b.setTag(id);
        b.setId(i + 50);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sonido(view);
            }
        });

        return b;
    }

    public void sonido(View view){
        Button b = (Button) findViewById(view.getId());
        String nombre = listaSonidos.get(view.getTag().toString());
        if (nombre.substring(0,2).contains("v_")) {
            VideoView videoview = (VideoView) findViewById(R.id.videoView);
            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+view.getTag());
            videoview.setVideoURI(uri);
            videoview.start();

            MediaController mediaController = new MediaController(this);
            videoview.setMediaController(mediaController);
            mediaController.setAnchorView(videoview);

        } else {
            MediaPlayer m = new MediaPlayer();
            m = MediaPlayer.create(this, (int) findViewById(view.getId()).getTag());
            m.start();
            m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                }
            });
        }
    }

    private String acortaEtiquetaBoton(String s){
        if (s.substring(0,2).contains("v_")) {
            s = s.substring(s.indexOf('_')+1);
        }
        if (s.contains("_")) {s = s.substring(s.indexOf('_'));}
        s = s.replace('_',' ');
        return s;
    }


}
