package com.sunirban.musicplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    static int oneTimeOnly = 0;
    // Widgets
    Button playButton, pauseButton, forwardButton, backwardButton;
    TextView timeTextView, titleTextView;
    SeekBar seekBar;
    // Media Player
    MediaPlayer mediaPlayer;
    // Handlers
    Handler handler = new Handler();
    // Variables
    double startTime = 0;
    // Creating the Runnable
    private final Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            timeTextView.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            seekBar.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }
    };
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        forwardButton = findViewById(R.id.forward_button);
        backwardButton = findViewById(R.id.backward_button);

        timeTextView = findViewById(R.id.time_left_text);
        titleTextView = findViewById(R.id.song_title);

        seekBar = findViewById(R.id.seekBar);

        // Media Player
        mediaPlayer = MediaPlayer.create(this, R.raw.trial);
        titleTextView.setText(getResources().getIdentifier("trial", "raw", getPackageName()));
        seekBar.setClickable(false);

        // Adding Functionalities for Button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusic();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp + forwardTime) <= finalTime) {
                    startTime += forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't go forward anymore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp - backwardTime) > 0) {
                    startTime -= backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't go backward anymore", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void playMusic() {
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        timeTextView.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }
}