package com.example.anany.informationsystemv1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AboutApp extends AppCompatActivity {

    TextView appdescription;
    CardView holder;
    Button nextpage;
    TextView welcome;
    RelativeLayout rlt;
    Boolean shouldgo = false;
    CardView colorchange, buttonholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);
        bindViews();
        colorchange.setBackgroundColor(getResources().getColor(R.color.lightred2));
        rlt.setVisibility(View.INVISIBLE);
        welcome.setVisibility(View.INVISIBLE);
        holder.setVisibility(View.INVISIBLE);
        appdescription.setVisibility(View.INVISIBLE);
        buttonholder.setVisibility(View.INVISIBLE);
        nextpage.setVisibility(View.INVISIBLE);

        startActivity(new Intent(AboutApp.this, CreateAccount.class));

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setStartOffset(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);

        rlt.setVisibility(View.VISIBLE);
        rlt.setAnimation(animation);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setStartOffset(1000);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(3000);
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                AnimationSet animation = new AnimationSet(false); //change to false
                animation.addAnimation(fadeIn);
                welcome.setVisibility(View.VISIBLE);

                welcome.setAnimation(animation);
                displayimage();

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1500);
        listeners();


    }

    private void displayimage() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);
                Animation nim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.realfadein);
                AnimationSet dd2 = new AnimationSet(false);
                dd2.addAnimation(nim2);
                holder.setVisibility(View.VISIBLE);
                appdescription.setVisibility(View.VISIBLE);
                holder.setAnimation(nim);
                appdescription.setAnimation(nim2);
                fadein();
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 2500);

    }

    private void fadein() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);
                Animation nim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.realfadein);
                AnimationSet dd2 = new AnimationSet(false);
                dd2.addAnimation(nim2);
                appdescription.setVisibility(View.VISIBLE);
                appdescription.setAnimation(nim2);
                buttonappear();

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1000);
    }

    private void buttonappear() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoominspecial);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);
                Animation nim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.realfadein);
                AnimationSet dd2 = new AnimationSet(false);
                dd2.addAnimation(nim2);
                buttonholder.setVisibility(View.VISIBLE);
                nextpage.setVisibility(View.VISIBLE);
                appdescription.setAnimation(nim2);
                nextpage.setAnimation(nim);
                listeners();

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1500);

    }


    private void listeners() {
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutApp.this, CreateAccount.class));
                shouldgo = false;
            }
        });
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (shouldgo) {
                    Toast.makeText(getApplicationContext(), "Automatically going to next page in 5 seconds", Toast.LENGTH_SHORT).show();
                    shouldgo = false;
                    gotonext();
                } else {

                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 34000);


    }

    private void gotonext() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Going to next page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AboutApp.this, CreateAccount.class));
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 6000);
    }

    private void bindViews() {

        appdescription = findViewById(R.id.appdescription);
        holder = findViewById(R.id.holder);
        nextpage = findViewById(R.id.gotomainactivity);
        welcome = findViewById(R.id.welcome);
        rlt = findViewById(R.id.card);
        colorchange = findViewById(R.id.changeback);
        buttonholder = findViewById(R.id.cardView);


    }

}
