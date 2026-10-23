package com.example.appaudiovideo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button playAudioButton;
    Button playVideoButton;
    Button animateButton;
    VideoView videoView;

    MediaPlayer mediaPlayer;

    private static final int PERMISO_ALMACENAMIENTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conectar los elementos de la interfaz
        imageView = findViewById(R.id.imageView);
        playAudioButton = findViewById(R.id.playAudioButton);
        playVideoButton = findViewById(R.id.playVideoButton);
        animateButton = findViewById(R.id.animateButton);
        videoView = findViewById(R.id.myVideoView);

        // Botón para reproducir audio
        playAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Evitar que se acumulen reproductores
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                mediaPlayer = MediaPlayer.create(
                        MainActivity.this,
                        R.raw.mi_audio
                );

                if (mediaPlayer != null) {
                    mediaPlayer.setOnCompletionListener(
                            new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.release();
                                    mediaPlayer = null;
                                }
                            }
                    );

                    mediaPlayer.start();
                }
            }
        });

        // Botón para reproducir video
        playVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri videoUri = Uri.parse(
                        "android.resource://" + getPackageName()
                                + "/" + R.raw.mi_video
                );

                videoView.setVideoURI(videoUri);
                videoView.start();
            }
        });

        // Botón para aplicar animación
        animateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation fadeIn = AnimationUtils.loadAnimation(
                        MainActivity.this,
                        R.anim.fade_in
                );

                imageView.startAnimation(fadeIn);
            }
        });

        // Solicitar permiso de almacenamiento en Android antiguo
        solicitarPermisoAlmacenamiento();
    }

    private void solicitarPermisoAlmacenamiento() {

        // Para Android 12 o anterior (API 32 o inferior)
        if (Build.VERSION.SDK_INT <= 32) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        PERMISO_ALMACENAMIENTO
                );
            }
        }
    }

    // Recibir la respuesta del usuario al permiso
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == PERMISO_ALMACENAMIENTO) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // El permiso fue concedido
            } else {

                // El usuario rechazó el permiso
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
