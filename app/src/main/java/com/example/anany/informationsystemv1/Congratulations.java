package com.example.anany.informationsystemv1;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/*final KonfettiView konfettiView = findViewById(R.id.konfettiView);
        konfettiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.RECT, Shape.CIRCLE)
                        .addSizes(new Size(12, 5f))
                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                        .streamFor(300, 5000L);
            }
        });*/
public class Congratulations extends AppCompatActivity {
    TextView description, congratulations;
    CardView within, outside;
    Button ok;
    Boolean goback = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congratulations);

        bindViews();
        within.setBackgroundColor(getResources().getColor(R.color.lightorange));
        MediaPlayer ring = MediaPlayer.create(Congratulations.this, R.raw.createaccount);
        ring.start();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Thank you for your compliance.", Toast.LENGTH_SHORT).show();
                goback = false;
                goback();
            }
        });

        if(goback == true){
            Runnable rt = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Automatically going back to Login Page in 5 seconds.", Toast.LENGTH_SHORT).show();
                    slowgoback();
                }
            };

            Handler ht = new Handler();
            ht.postDelayed(rt, 30000);
        }else{

        }



    }

    private void slowgoback() {
        if (goback == true) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Congratulations.this, MainActivity.class));
                }
            };

            Handler h = new Handler();
            h.postDelayed(r, 5500);
        }
    }

    private void goback() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Congratulations.this, MainActivity.class));
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 1500);
    }

    private void bindViews() {
        description = findViewById(R.id.description);
        congratulations = findViewById(R.id.congratulationstitle);
        outside = findViewById(R.id.cardview5);
        within = findViewById(R.id.cardviewwithin);
        ok = findViewById(R.id.button);


    }
}