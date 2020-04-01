package com.example.isonidos;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String> listaSonidos=new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void reproduceSonido(View vista) {

        MediaPlayer m = new MediaPlayer();
        int idSonido = this.getResources().getIdentifier(vista.getTag().toString(), "raw", this.getPackageName());
        m = MediaPlayer.create(this, idSonido);
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

    public void reproduceVideo(View vista) {
       VideoView videoview = (VideoView) findViewById(R.id.videoView);
        int idVideo = this.getResources().getIdentifier(vista.getTag().toString(), "raw", this.getPackageName());
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + idVideo);
        videoview.setVideoURI(uri);
        videoview.start();
    }
}
