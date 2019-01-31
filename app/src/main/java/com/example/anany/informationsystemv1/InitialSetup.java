package com.example.anany.informationsystemv1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.icu.lang.UProperty.INT_START;

public class InitialSetup extends AppCompatActivity {

    TextView welcome, aboutapp, appdescription, initialsetuptitle, spinnerdescription;
    Button gotosetup, clicktoconfirm;
    RelativeLayout title;
    int buttonclicked = 0;
    CardView cd, infoholder, cmt, image, colorhcnage, confirmholder;
    Spinner spinner;
    String firstcheck = "";
    CardView realholder;
    String secondcheck = "";
    Boolean informationfilled = false;
    Boolean displayingstuff = false;
    Boolean grantedpermissions = false;
    TextView choosetitle;
    RelativeLayout inflaterholder;
    String firsttime;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    Boolean camerapermission = false;
    Boolean smspermission = false;
    Boolean emailpermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_setup);
        bindViews();
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        //COMMENT OUT THE BELOw. IT WAS DONE TO MAKE TESTING STUFF FASTER!!!
//////////////////////////////////////////////////////////////////////////////
        choosetitle.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        initialsetuptitle.setVisibility(View.INVISIBLE);
        SpannableStringBuilder str = new SpannableStringBuilder("Welcome to the Information\n               System App");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        welcome.setText(str);
        invisibility();

       // startActivity(new Intent(this, MainActivity.class));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/Initial Setup Complete");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> values = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    values.add(child.child("State").getValue(String.class));
                }
                if (values.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "EMPTY", Toast.LENGTH_SHORT).show();
                    addAnimations();
                } else {
                    startActivity(new Intent(InitialSetup.this, MainActivity.class));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);


    }

    @Override
    protected void onStart() {
        super.onStart();
        bindViews();
        buttonclicked = 0;
        initialsetuptitle.setVisibility(View.INVISIBLE);


        gotosetup.setText("Continue to\n  Setup");
        SpannableStringBuilder str = new SpannableStringBuilder("Welcome to the Information\n               System App");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        welcome.setText(str);

        // addAnimations();

    }

    private void invisibility() {
        title.setVisibility(View.INVISIBLE);
        welcome.setVisibility(View.INVISIBLE);
        aboutapp.setVisibility(View.INVISIBLE);
        appdescription.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        spinnerdescription.setVisibility(View.INVISIBLE);

        confirmholder.setVisibility(View.INVISIBLE);
        colorhcnage.setVisibility(View.INVISIBLE);
        image.setBackgroundResource(R.color.lightorange);
        cd.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
        cmt = findViewById(R.id.changeback);
        choosetitle.setVisibility(View.INVISIBLE);

        cmt.setBackgroundResource(R.drawable.bettergradient);

        infoholder.setVisibility(View.INVISIBLE);
    }

    private void addAnimations() {
        title.setVisibility(View.VISIBLE);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setStartOffset(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(4000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        AnimationSet welcomeani = new AnimationSet(false);
        welcomeani.addAnimation(fadeOut);

        title.setAnimation(animation);


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
        h.postDelayed(r, 2500);


    }

    private void displayimage() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);
                image.setVisibility(View.VISIBLE);
                colorhcnage.setVisibility(View.VISIBLE);
                image.setAnimation(nim);
                aboutappanimation();
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1500);
    }

    private void aboutappanimation() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidefromright);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);

                infoholder.setVisibility(View.VISIBLE);
                aboutapp.setVisibility(View.VISIBLE);
                aboutapp.startAnimation(nim);
                descriptionAnimation();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 3200);

    }

    private void descriptionAnimation() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);

                String text2 = "      The purpose of this app is to serve as an information system for senior citizens with disabilities and diseases, particularly Alzheimer's. This app functions by requiring visitors to create an account by entering information about their relation to the resident as well as a description of themselves in addition to other details. READ MORE";
                Spannable spannable = new SpannableString(text2);

                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.READMORE)), 337, 346, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableStringBuilder str = new SpannableStringBuilder(text2);
                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 337, 346, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                Spannable spannable1 = new SpannableString(text2);
                spannable.setSpan(new UnderlineSpan(), 337, 346, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                appdescription.setText(spannable, TextView.BufferType.SPANNABLE);
                appdescription.setVisibility(View.VISIBLE);
                appdescription.startAnimation(nim);
                buttonAnimation();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 3000);

    }

    private void buttonAnimation() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoominspecial);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);

                gotosetup.setVisibility(View.VISIBLE);
                cd.setVisibility(View.VISIBLE);
                gotosetup.startAnimation(nim);
                listeners();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1700);
    }

    private void zoominanimation(final View v, int i) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);


                v.startAnimation(dd);

                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, i);
    }

    private void fadeinanimation(final View v, int i) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.realfadein);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);


                v.startAnimation(dd);

                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, i);
    }

    private void fadeoutanimation(final View v, int i) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);


                v.startAnimation(dd);

                listeners();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, i);
    }

    private void zoomoutanimation(final View v, int i) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);


                v.startAnimation(dd);
                v.setVisibility(View.INVISIBLE);
                listeners();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, i);
    }

    private void secondpageanimations() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);
                Animation nim4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidefromright);
                AnimationSet xd = new AnimationSet(false);
                dd.addAnimation(nim);
                Animation nim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.realfadein);
                AnimationSet dm = new AnimationSet(false);
                dm.addAnimation(nim);

                initialsetuptitle.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                spinnerdescription.setVisibility(View.VISIBLE);
                choosetitle.setVisibility(View.VISIBLE);

                confirmholder.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "This is the Initial Setup where you will setup your device.", Toast.LENGTH_LONG).show();
                initialsetuptitle.startAnimation(nim4);
                spinner.startAnimation(nim);
                choosetitle.startAnimation(nim);
                spinnerdescription.startAnimation(nim);
                confirmholder.startAnimation(nim4);
                gotosetup.setText("Make Changes");
                setupspinner();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 3000);
    }

    private void setupspinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("Visitor Device");
        categories.add("Caretaker Device");
        categories.add("Resident Device");
        ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.device, R.layout.spinner_item);
        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }


    private void caretakerdevice() {
        buttonclicked = 1;
        View inflatedView = View.inflate(getApplicationContext(), R.layout.caretakersetup, realholder);

    }

    private void residentdevice() {
        buttonclicked = 1;

        View inflatedview = View.inflate(getApplicationContext(), R.layout.residentsetup, realholder);
    }

    private void visitordevice() {
        buttonclicked = 1;
        View inflatedView = View.inflate(getApplicationContext(), R.layout.visitorsetup, realholder);

        //  checkPermissions();
    }

   /* private void checkPermissions() {
        if (spinner.getSelectedItem().toString().equals("Visitor Device")) {
            Button permissions = findViewById(R.id.requestpermissions);

            permissions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PackageManager mPackageManager = getApplication().getPackageManager();
                    int hasPermStorage = mPackageManager.checkPermission(android.Manifest.permission.CAMERA, getApplication().getPackageName());

                    if (hasPermStorage != PackageManager.PERMISSION_GRANTED) {
                        // do stuff
                        Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_LONG).show();
                    } else if (hasPermStorage == PackageManager.PERMISSION_GRANTED) {
                        // do stuff
                        camerapermission = true;
                    }



                    //CHECK TO SEE THAT EMAIL AND CAMERA HAVE TO BE ALLOWED!

                    //Set granted permissions to true.
                    //Check to see which oermissions are allowed already.
                    //This may be beacause they allow first permission but then exit. Then come back and press again.
                }
            });
        }
    }*/

    private void remove() {
        String text2 = spinner.getSelectedItem().toString();
        Toast.makeText(getApplicationContext(), firstcheck + " " + secondcheck, Toast.LENGTH_SHORT).show();
        if (secondcheck.equals("Visitor Device")) {
            RelativeLayout rlt = findViewById(R.id.visitorrelative1);
            rlt.setVisibility(View.INVISIBLE);

        } else if (secondcheck.equals("Caretaker Device")) {
            RelativeLayout rlt = findViewById(R.id.visitorrelative2);
            rlt.setVisibility(View.INVISIBLE);

        } else {
            RelativeLayout rlt = findViewById(R.id.visitorrelative3);
            rlt.setVisibility(View.INVISIBLE);

        }
        //Clear the Relative Layout variable that is initialized all the way at the top.
    }

    private void listeners() {
        clicktoconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstcheck = spinner.getSelectedItem().toString();
                if (secondcheck.isEmpty()) {
                    secondcheck = firstcheck;
                    if (firstcheck.equals("Visitor Device")) {
                        Toast.makeText(getApplicationContext(), "This device is the visitor device", Toast.LENGTH_SHORT).show();

                        visitordevice();
                        displayingstuff = true;
                    } else if (firstcheck.equals("Resident Device")) {
                        Toast.makeText(getApplicationContext(), "This device is the resident device", Toast.LENGTH_SHORT).show();

                        residentdevice();
                        displayingstuff = true;

                    } else {
                        Toast.makeText(getApplicationContext(), "This device is the caretaker device", Toast.LENGTH_SHORT).show();

                        caretakerdevice();
                        displayingstuff = true;

                    }
                    displayingstuff = true;
                } else {
                    if (secondcheck.equals(firstcheck)) {

                        displayingstuff = true;

                    } else {
                        if (firstcheck.equals("Visitor Device")) {
                            remove();
                            Toast.makeText(getApplicationContext(), "This device is the visitor device", Toast.LENGTH_SHORT).show();

                            visitordevice();
                            displayingstuff = true;

                        } else if (firstcheck.equals("Resident Device")) {

                            remove();
                            Toast.makeText(getApplicationContext(), "This device is the resident device", Toast.LENGTH_SHORT).show();

                            residentdevice();
                            displayingstuff = true;

                        } else {

                            remove();
                            Toast.makeText(getApplicationContext(), "This device is the caretaker device", Toast.LENGTH_SHORT).show();

                            caretakerdevice();
                            displayingstuff = true;
                        }

                        //This is for if secondcheck does not equal firstcheck meaning that they have changed their choice.
                        //based on firstcheck call a method TO CLEAR THE PREVIOUS STUFF. THEN CALL A METHOD BASED ON SPINNER

                        //Way to do REMOVE method would be to make RelativeLayout rl all the way at top. Don't assign it to anything.
                        //Then in each method for creating views create them in that relative layout.
                        //So remove method will basically clear content of the relative layout or whatever.
                        secondcheck = firstcheck;
                        displayingstuff = true;
                    }


                    secondcheck = spinner.getSelectedItem().toString();
                }
            }
        });


        gotosetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonclicked == 0) {
                    fadeoutanimation(title, 0);

                    zoomoutanimation(image, 100);
                    zoomoutanimation(welcome, 300);
                    fadeoutanimation(infoholder, 500);
                    title.setVisibility(View.INVISIBLE);
                    infoholder.setVisibility(View.INVISIBLE);

                    secondpageanimations();
                    buttonclicked++;
                } else {
                    if (buttonclicked < 3) {
                        if(buttonclicked == 1) {
                            for (int i = 0; i < 2; i++) {
                                Toast.makeText(getApplicationContext(), "If you are certain with these settings, click the button 2 more times. These settings can be changed later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        buttonclicked++;
                    } else {
                        if (!displayingstuff) {
                            Toast.makeText(getApplicationContext(), "You have not entered any information", Toast.LENGTH_SHORT).show();
                        } else {


                            String text = spinner.getSelectedItem().toString();
                            if (!text.equals("Visitor Device")) {
                                startActivity(new Intent(InitialSetup.this, MainActivity.class));
                            } else {
                                // checkPermissions();
                               /* if (camerapermission && emailpermission) {
                                    grantedpermissions = true;
                                } else {
                                    grantedpermissions = false;
                                }*/
                                /*   if (grantedpermissions) {*/
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InitialSetup.this);
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.setMessage("You have chosen that this device will serve as the Visitor Device in your system.");
                                alertDialog.setNegativeButton("Yes, Continue.", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference dbref = database.getReference("Initial Setup Complete");
                                        HashMap<String, String> values = new HashMap<>();
                                        values.put("State", "Complete");
                                        dbref.push().setValue(values, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    startActivity(new Intent(InitialSetup.this, MainActivity.class));

                                                } else {
                                                    Toast.makeText(InitialSetup.this, "Possible error with saving changes.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }

                                });
                                alertDialog.setPositiveButton("No, Stay Here.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                alertDialog.show();
                               /* } else {
                                    Toast.makeText(getApplicationContext(), "You have not granted camera and email permission which are mandatory!", Toast.LENGTH_SHORT).show();
                                }*/

                                //check if permissions are given. Look at Checkbox. If checked, change disableexit.
                                //check grantedpermissions boolean. Should be true; if true then check disablexit boolean and do stuff based on that.
                            }
                            //Check stuff in edittexts based on spinner text. If error in what they have enterd, set errortext

                            //if correct, then do the for loop above checking buttonclicked value. But first, set buttonclick to 1 if info is wrong.

                            /*MAKE AN ALERT DIALOG HERE ASKING IF THEY ARE SURE.*/
                        }

                    }
                    /*Boolean b = setupspinner();
                     * if(b){
                     * }else{
                     * Toast.makeText("You have not entered all the necessary info
                     * }*/
                }
            }
        });

        appdescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                /*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext())
                        .setTitle("My title")
                        .setMessage("Enter password");
                final FrameLayout frameView = new FrameLayout(getApplicationContext());
                builder.setView(frameView);

                final AlertDialog alertDialog = builder.create();
                LayoutInflater inflater = alertDialog.getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_demo, frameView);
                alertDialog.show();*/
            /*    LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_demo, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setView(dialoglayout);
                builder.show();*/


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InitialSetup.this, R.style.AlertDialogStyle);

                alertDialog.setTitle("About This App");
                alertDialog.setMessage("          The purpose of this app is to serve as an information system for senior citizens with disabilities and diseases, particularly Alzheimer's. This app functions by requiring visitors to create an account by entering information about their relation to the resident as well as a description of themselves in addition to other details. Once the account has been approved by the caretaker of the resident, visitors can use their account to sign in to the Visitor Device positioned near the front door of the house.\n\n          When a visitor signs in, the resident and caretaker will get notified of their arrival and they will also get the information about the visitor, helping the resident with Alzheimer's remember and know who who is visiting them. Furthermore, the caretaker can use this app to monitor the visits as well as ensure that the resident has not left the house. Essentially, this app can ensure the safety of the resident as well as inform them about who is visiting. It also gives the caretaker the ability to make sure that the visitors entering the house are safe.");

                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
//CONVER THIS TO ALERTDIALOF OR SOMETHING TO SETCONTENTVIEW TO XML FILE. FOLLOW STACK OVERFLOW ARTICLE.
                alertDialog.show();


                return false;
            }
        });

    }

    private void bindViews() {
        title = findViewById(R.id.card);
        welcome = findViewById(R.id.welcome);
        aboutapp = findViewById(R.id.aboutapp);
        appdescription = findViewById(R.id.appdescription);
        gotosetup = findViewById(R.id.InitialSetup);
        cd = findViewById(R.id.cardView);
        colorhcnage = findViewById(R.id.colorchange);
        infoholder = findViewById(R.id.holder);
        image = findViewById(R.id.image);
        choosetitle = findViewById(R.id.choosetitile);
        spinner = findViewById(R.id.spinner);
        confirmholder = findViewById(R.id.confirming);
        realholder = findViewById(R.id.realholder);
        clicktoconfirm = findViewById(R.id.clicktoconfirm);
        spinnerdescription = findViewById(R.id.choosetitile);
        initialsetuptitle = findViewById(R.id.initialsetuptitle);
        inflaterholder = findViewById(R.id.inflaterholder);

    }

}
