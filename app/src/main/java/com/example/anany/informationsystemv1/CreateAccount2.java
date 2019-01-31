package com.example.anany.informationsystemv1;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.MimeTypeFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.gui.Main;

public class CreateAccount2 extends AppCompatActivity {

    EditText address, paragraph;
    ImageView descriptionhelp;
    Button button;
    Button createaccount, previouspage;
    String s = "";
    TextView display;
    ImageView addresshelp, photohelp, photo, facehelp;
    Boolean sendmail, sendphone;
    String phonenumber, email, password, fullname, username, confirm, baddress, bparagrpah, brelat;
    RelativeLayout takeimage, deleteimage;
    Button picturer, register;
    ImageView relationhelp;
    CardView cardView;
    ProgressBar progressBar;
    String age = "";
    String gender = "";
    CheckBox useFace;
    TextView wordcount;
    Uri picUri;
    Boolean goodimage = false;

    TextToSpeech t1;

    ArrayList<HashMap<String, String>> data;
    private FaceServiceClient faceServiceClient;
    boolean doneFacial = false;
    int numoftimes = 0;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;

    private Uri filePath, extra;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private StorageReference picturereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account2);
        bindViews();
        useFace.setChecked(true);
        register = findViewById(R.id.register);
        register.setVisibility(View.VISIBLE);
        cardView = findViewById(R.id.card3);
        cardView.setVisibility(View.VISIBLE);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        storageReference = FirebaseStorage.getInstance().getReference("Pictures");
        databaseReference = FirebaseDatabase.getInstance().getReference("Pictures");
        faceServiceClient = new FaceServiceRestClient("<YOUR API ENDPOINT HERE>", "<YOUR API KEY HERE>");
        data = new ArrayList<>();

        firebaseStorage = FirebaseStorage.getInstance();

        picturereference = firebaseStorage.getReference();
        photo.setBackgroundColor(getResources().getColor(R.color.menubar));
        //UNCOMMENT BELOW //////////////////////////////////////////////////////////////////////////
        receiveIntents();
        //UNCOMMENT ABOVE //////////////////////////////////////////////////////////////////////////
        listeners();

        checkCheckBox();
        registerListener();

        chooseRelation();

    }


    private void checkCheckBox() {
        useFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useFace.isChecked()) {
                    register.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.VISIBLE);
                    register.setText("Register your face: 4 more times");
                    registerListener();
                    numoftimes = 0;
                    data = new ArrayList<>();
                    if (data != null)
                        data.clear();
                } else {
                    register.setVisibility(View.INVISIBLE);
                    cardView.setVisibility(View.INVISIBLE);
                    doneFacial = true;
                    if (data != null)
                        data.clear();
                }
            }
        });
    }

    private void registerListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (numoftimes == 0) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (numoftimes == 1) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (numoftimes == 2) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (numoftimes == 3) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                }*/
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateAccount2.this, new String[]{android.Manifest.permission.CAMERA}, 1000);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                    //   makeToast("Make sure that name is not null!");
                }
            }
        });
    }

    private void receiveIntents() {
         /*     i.putExtra("Boolean_Send_Email", sendemail);
        i.putExtra("Boolean_Send_Phone", sendphone);
        i.putExtra("Phone_Number", phonenumber);
        i.putExtra("Email", email);
        i.putExtra("Name", name);
        i.putExtra("Username", username);
        i.putExtra("Password", password);
        i.putExtra("Confirm", confirmlimit);
        i.putExtra("Paragraph", paragraphs);
        i.putExtra("Address", addres);
        i.putExtra("Relation", relations);*/

        Intent intent = getIntent();
        sendmail = intent.getBooleanExtra("Boolean_Send_Email", true);
        sendphone = intent.getBooleanExtra("Boolean_Send_Phone", true);
        phonenumber = intent.getStringExtra("Phone_Number");
        email = intent.getStringExtra("Email");
        password = intent.getStringExtra("Password");
        fullname = intent.getStringExtra("Name");
        username = intent.getStringExtra("Username");
        baddress = intent.getStringExtra("Address");
        bparagrpah = intent.getStringExtra("Paragraph");
        brelat = intent.getStringExtra("Relation");
       /* CreateAccount ca = new CreateAccount();
         baddress = ca.addres;
         bparagrpah = ca.paragraphs;
         brelat = ca.relations;*/

      /*  address.setText(baddress);
        paragraph.setText(bparagrpah);*/
        //  display.setText(brelat);

    }

    private void listeners() {
        facehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longestToast("Your images will not be saved at all. Facial data will remain on this device only and will not be sent anywhere else.");
                makeToast("Your images will not be saved at all. Facial data will remain on this device only and will not be sent anywhere else.");
            }
        });
        previouspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (display.getText().toString().isEmpty() && paragraph.getText().toString().isEmpty() && display.getText().toString().isEmpty()) {
                    Intent i = new Intent(CreateAccount2.this, CreateAccount.class);
                    i.putExtra("Boolean_Send_Email", sendmail);
                    i.putExtra("Boolean_Send_Phone", sendphone);
                    i.putExtra("Phone_Number", phonenumber);
                    i.putExtra("Email", email);
                    i.putExtra("Name", fullname);
                    i.putExtra("Username", username);
                    i.putExtra("Password", password);

                    String textad = address.getText().toString();
                    i.putExtra("Address", textad);
                    String textrelation = display.getText().toString();
                    i.putExtra("Relation", textrelation);
                    String textdescript = paragraph.getText().toString();
                    i.putExtra("Description", textdescript);

                    startActivity(i);
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateAccount2.this);
                    builder1.setMessage("Your info on this page will not be saved. Are you sure you want to go back?");
                    builder1.setCancelable(true);

                    builder1.setNegativeButton(
                            "Yes, go back.",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(CreateAccount2.this, CreateAccount.class);
                                    i.putExtra("Boolean_Send_Email", sendmail);
                                    i.putExtra("Boolean_Send_Phone", sendphone);
                                    i.putExtra("Phone_Number", phonenumber);
                                    i.putExtra("Email", email);
                                    i.putExtra("Name", fullname);
                                    i.putExtra("Username", username);
                                    i.putExtra("Password", password);

                                    String textad = address.getText().toString();
                                    i.putExtra("Address", textad);
                                    String textrelation = display.getText().toString();
                                    i.putExtra("Relation", textrelation);
                                    String textdescript = paragraph.getText().toString();
                                    i.putExtra("Description", textdescript);

                                    startActivity(i);
                                }
                            });

                    builder1.setPositiveButton(
                            "No, stay here",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        descriptionhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 3; i++) {
                    makeToast("This is the information that the resident will receive about you each time you sign in. This can be edited later, but it is mandatory");
                }
            }
        });

        photohelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 3; i++) {
                    longestToast("This is not optional. The picture is used to help the resident identify you when you visit and provide them with a picture..");
                }
            }
        });

        paragraph.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String st = paragraph.getText().toString();
                int count = 0;
                for (int mt = 0; mt < st.length(); mt++) {
                    char ut = st.charAt(mt);
                    if (ut == ' ') {
                        count++;
                    } else {
                        continue;
                    }
                }

                if (count == 59) {
                    specialToast("Reached limit");
                } else if (count == 30) {
                    specialToast("Halfway to limit.");
                } else if (count == 50) {
                    specialToast("*APPROACHING WORD LIMIT*");
                } else if (count == 60) {
                    int space = 0;
                    int adjust = 0;
                    for (int mt = 0; mt < st.length(); mt++) {
                        char ut = st.charAt(mt);
                        if (ut == ' ') {
                            adjust++;
                        } else {
                            continue;
                        }

                        if (adjust == 59) {
                            space = mt;
                            break;
                        }
                    }
                    paragraph.setText(st.substring(0, space));
                    paragraph.setSelection(paragraph.getText().toString().length());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String st = paragraph.getText().toString();
                int count = 0;
                for (int mt = 0; mt < st.length(); mt++) {
                    char ut = st.charAt(mt);
                    if (ut == ' ') {
                        count++;
                    } else {
                        continue;
                    }
                }
                String total = Integer.toString(count + 1);
                wordcount.setText("Word Count: " + total + "/60");

            }
        });

        addresshelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 2; i++) {
                    makeToast("This is optional but it is recommended that you fill it in.");
                }
            }
        });

        relationhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 1; i++) {
                    makeToast("This is necessary information.");
                }
            }
        });

        deleteimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount2.this);
                builder.setTitle("Are you sure you want to delete the picture");
                builder.setNegativeButton("Yes, delete the picture.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeToast("Picture deleted.");
                        photo = findViewById(R.id.photoholder);
                        photo.setImageResource(R.drawable.holder);
                        goodimage = false;
                        picturer.setText("Take\nPicture");
                    }
                });
                builder.setPositiveButton("No, keep the photo.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allgood = checkConditions();
                if (allgood) {
                    paragraph = findViewById(R.id.paragraph);
                    address = findViewById(R.id.addressedit);
                    String des = paragraph.getText().toString();
                    String add = address.getText().toString();
                    String rel = display.getText().toString();
                    if (des.length() < 25 || display.getText().toString().isEmpty()) {
                        for (int i = 0; i < 2; i++) {
                            makeToast("Enter more about yourself including your relation and desciption.");
                        }
                    } else if (bitmap2 == null) {
                        longToast("Take a picture of yourself for the resident to view.");
                    } else {
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dbref = database.getReference("Pending Requests");
                        DatabaseReference databaseReference = database.getReference("Events Log");
                        Map<String, String> values = new HashMap<>();
                        values.put("Description", des);
                        values.put("Relation", rel);
                        values.put("Address", add);
                        values.put("Email", email);
                        values.put("Phone", phonenumber);
                        values.put("Password", password);
                        values.put("Username", username);
                        values.put("Name", fullname);
                        values.put("Time", currentDateTimeString);
                        if (sendphone) {
                            try {
                                Intent smsIntent = new Intent(Intent.ACTION_VIEW);

                                smsIntent.putExtra("address", "8482482353");
                                smsIntent.putExtra("sms_body", "default content");
                                smsIntent.setType("vnd.android-dir/mms-sms");

                                startActivity(smsIntent);
                            } catch (Exception e) {
                                for (int i = 0; i < 2; i++) {
                                    makeToast("Unfortunately, this device does not support SMS Messaging and so your account info could not be sent to your phonenumber.");
                                }
                            }
                        }

                        //uploadFile();
                        doUpload(bitmap2, picturereference);

                        Map<String, String> values2 = new HashMap<>();
                        values2.put("Description", des);
                        values2.put("Relation", rel);
                        values2.put("Address", add);
                        values2.put("Email", email);
                        values2.put("Phone", phonenumber);
                        values2.put("Password", password);
                        values2.put("Username", username);
                        values2.put("Name", fullname);
                        values2.put("Time", currentDateTimeString);
                        values2.put("Classifier", "Pending");
                        databaseReference.push().setValue(values2);

                        final FirebaseDatabase firebaseDatabase10 = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseReference5 = firebaseDatabase10.getReference("Latest Create");
                        final DatabaseReference databaseReference62 = firebaseDatabase10.getReference("RLatest Create");

                        databaseReference5.removeValue();
                        databaseReference62.removeValue();
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Time", currentDateTimeString);
                        databaseReference5.push().setValue(hashMap);
                        databaseReference62.push().setValue(hashMap);

                        saveData(data);


                        dbref.push().setValue(values, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    makeToast("Creating Account...");
                                    if (sendmail) {
                                        String[] TO = {"ishaanjav@gmail.com"};
                                        String[] CC = {""};
                                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                                        emailIntent.setData(Uri.parse("mailto:"));
                                        emailIntent.setType("text/plain");
                                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Information System App");
                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello " + fullname + "!\nThis is an autonomous message generated by the Information System App through which you have just created an account. Here is your account information: \n\nUsername: " + username + "\nPassword: " + password + "\n\nThank you for using this app!\n:");
                                        try {
                                            startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), 300);
                                            startActivity(new Intent(CreateAccount2.this, Congratulations.class));

                                        } catch (android.content.ActivityNotFoundException ex) {
                                            Toast.makeText(CreateAccount2.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        startActivity(new Intent(CreateAccount2.this, Congratulations.class));
                                    }
                                } else {
                                    makeToast("Error with creating account");
                                }
                            }
                        });

                    }
                } else {

                }
            }
        });

        takeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateAccount2.this, new String[]{Manifest.permission.CAMERA}, 1034);

                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, extra);
                        startActivityForResult(cameraIntent, 100);
                    }
                } catch (ActivityNotFoundException anfe) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    makeToast(errorMessage);

                }
            }
        });

    }

    private boolean checkConditions() {
        if (!useFace.isChecked()) {
            ScrollView scrollView = findViewById(R.id.scroller);
            Snackbar snackbar = Snackbar
                    .make(scrollView, "Are you sure that you don't want to use facial recognition?", Snackbar.LENGTH_LONG).setDuration(5000);
            snackbar.show();
            makeToast("Not using facial recognition.");
            return true;
        } else if (useFace.isChecked() && data.size() >= 4) {
            makeToast("You are using facial recognition.");
//            saveData(data);
            startActivity(new Intent(CreateAccount2.this, MainActivity.class));
            return true;
        } else {
            longToast("You haven't registered your face enough times.");
            return false;
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void uploadFile() {
        if (filePath != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filePath));
            fileReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                        }
                    }, 4000);
                    //  makeToast("Image upload successful");
                    Upload upload = new Upload(fullname, taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                    //              String uploadId = databaseReference.push().getKey();
                    //            databaseReference.child(uploadId).setValue(upload);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    makeToast(e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });

        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                //Get the facial data from Bitmap: Firebase.
                //Get the Emotion Attributes from Face API.
                bitmap = (Bitmap) data.getExtras().get("data");
                getFaceAttributes(bitmap);
                //recognizeandFrame(bitmap, 0);
            } else if (requestCode == 100) {
                picUri = data.getData();
                extra = data.getData();

                bitmap2 = (Bitmap) data.getExtras().get("data");

                photo.setImageBitmap(bitmap2);
                goodimage = true;
                //filePath = getImageUri(getApplicationContext(), bitmap);


            } else if (requestCode == 300) {
                finish();
                makeToast("Finished emailing your account info...");
                startActivity(new Intent(CreateAccount2.this, Congratulations.class));

            } else if (requestCode == 200) {
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                photo = findViewById(R.id.photoholder);
                photo.setImageBitmap(thePic);
            }
        }

    }

    private void saveData(ArrayList<HashMap<String, String>> mydata) {
        int counter = 0;
        int secondcount = 0;
        for (HashMap<String, String> hashMap : mydata) {
            BufferedReader inputReader = null;
            try {
                // makeToast(hashMap.toString());
                String yourFilePath = getApplicationContext().getFilesDir() + "/" + "faceData";
                File yourFile = new File(yourFilePath);
                inputReader = new BufferedReader(new FileReader(yourFile));

                String yourPath = getApplicationContext().getFilesDir() + "/" + "names";
                File file = new File(yourPath);
                BufferedReader namesReader = new BufferedReader(new FileReader(file));

                String myLine = "";
                String line1 = "";
                String line2 = "";
                String tline;
                int mycount = 0;
                while ((tline = namesReader.readLine()) != null) {
                    if (mycount == 0) {
                        myLine += tline + ",";
                        //Try taking off the comma. Add it only if the person is new with "," + name.
                        line1 += tline;
                        mycount++;
                    } else {
                        line2 += tline;
                    }
                }
                //         makeToast("TIME: " + counter);
                //         makeToast("Lines in names: " + line1 + "\n" + line2);
                //String[] namesarray = myLine.split(" ");
                String[] namesarray = line1.split(",");
                String[] numarray = line2.split(",");


                String st = "";
                String usethis = "";
                for (String m : namesarray) {
                    st += m + " next: ";
                    usethis += m + ",";
                }
                //  makeToast("Line1: " + line1);
                //     makeToast("NamesArray: " + st);
                HashMap<String, String> faceData = hashMap;

                StringBuilder sb = new StringBuilder();
                String line;
                String lines = "";
                float cheek = Float.parseFloat(faceData.get("Cheek diff"));
                float eye = Float.parseFloat(faceData.get("Eye diff"));
                float mouth = Float.parseFloat(faceData.get("Mouth diff"));
                float nose = Float.parseFloat(faceData.get("Nose diff"));
                String name = (faceData.get("Name"));
                String sgender = faceData.get("Gender");
                int gender = 0;
                if (sgender.contains("fem") || sgender.contains("Fem")) {
                    gender = 1;
                }
              /*  String oldname = (faceData.get("Name"));
                String[] splitnames = oldname.split(" ");
                String name = "";
                for (String indi : splitnames) {
                    name += indi;
                }*/
                double age = Float.parseFloat(faceData.get("Age"));

                int matchnum = 0;
                boolean contains = false;
                for (int i = 0; i < namesarray.length; i++) {
                    if (namesarray[i].equals(name)) {
                        contains = true;
                        matchnum = i;
                        //                    makeToast("Matching Number: " + matchnum);
                        break;
                    } else {
                        contains = false;
                        continue;
                    }
                }
                //    makeToast("Contains: " + contains);
                int lastnum = Integer.parseInt(numarray[numarray.length - 1]);

                int count = 1;
                if (contains == false) {
                    myLine += name;
                    usethis += name;
                    line1 += "," + name;
                    line2 += "," + Integer.toString(lastnum + 1);
                }
                //   makeToast("Contains: " + Boolean.toString(contains));
                //     makeToast("Usethis: " + usethis + "\nLine1: " + line1);
//Error below. Check to make sure no spaces in array.
                // String[] d = myLine.split("[ ,  ]");
                //  String[] d = line2.split("[ ,  ]");
                String[] d = line2.split(" |,  |,");
                String tidy = "";
                for (int i = 1; i < d.length; i++) {
                    tidy += d[i] + " ";
                }
                String allnames = "" + d[0] + "";
                for (int i = 1; i < d.length; i++) {
                    allnames += "," + d[i];
                    //      makeToast("Names: " + d[i]);
                }
                //            makeToast("Name: " + name);
                //    address.setText(name);
                //  makeToast("Your name: " + fullname);
                //          makeToast("Allnames: " + allnames);
                while ((line = inputReader.readLine()) != null) {
                    if (count == 8) {
                        lines += "@attribute class {" + allnames + "}\n";
                        // lines += "@data\n";
                    } else if (line.contains("0, 0, 0, 0, 0, 100")) {
                        //Don't append this.
                    } else {
                        sb.append(line);
                        lines += line + "\n";
                    }
                    count++;

                }

                if (contains == false) {
                    //They are a new person
                    lines += cheek + ", " + eye + ", " + mouth + ", " + nose + ", " + age + ", " + gender + ", " + (lastnum + 1) + "";
                } else {
                    lines += cheek + ", " + eye + ", " + mouth + ", " + nose + ", " + age + ", " + gender + ", " + (matchnum) + "";
                }

                FileOutputStream outputStream;
                FileOutputStream writenames;
                //    makeToast("Line: " + lines);

                try {
                    outputStream = openFileOutput("faceData", Context.MODE_PRIVATE);
                    outputStream.write(lines.getBytes());
                    outputStream.close();

                    writenames = openFileOutput("names", Context.MODE_PRIVATE);
                    writenames.write((line1 + "\n" + line2).getBytes());
                    writenames.close();
                    if (counter == data.size() - 1) {
                        //             makeToast("Data saved!");
                        //     makeToast(lines);
                    }
                    counter++;
                } catch (Exception e) {
                    makeToast("Message: " + e.getMessage() + " Cause: " + e.getCause());
                    paragraph.setText("Message: " + e.getMessage() + " Cause: " + e.getCause());
                }
            } catch (Exception e) {
                //Write the code to create the new file with all the tags for the first time.
                HashMap<String, String> faceData = hashMap;

                float cheek = Float.parseFloat(faceData.get("Cheek diff"));
                float eye = Float.parseFloat(faceData.get("Eye diff"));
                float mouth = Float.parseFloat(faceData.get("Mouth diff"));
                float nose = Float.parseFloat(faceData.get("Nose diff"));

                String name = (faceData.get("Name"));
               /* String oldname = (faceData.get("Name"));
                String[] splitnames = oldname.split(" ");
                String name = "";
                for (String indi : splitnames) {
                    name += indi;
                }*/
                float age = Float.parseFloat(faceData.get("Age"));
                String sgender = faceData.get("Gender");
                int gender = 0;
                if (sgender.contains("fem") || sgender.contains("Fem")) {
                    gender = 1;
                }

                String relation = "@relation practice";
                String attributes = "\n@attribute cheek numeric\n" +
                        "@attribute eye numeric\n" +
                        "@attribute mouth numeric\n" +
                        "@attribute nose numeric\n" +
                        "@attribute age numeric\n" +
                        "@attribute gender {0,1}\n" +
                        "@attribute class {0}";

                String lines = "\n" + cheek + ", " + eye + ", " + mouth + ", " + nose + ", " + age + ", " + gender + ", 0";
              /*  if (secondcount == data.size() - 1) {
                    lines += "\n0, 0, 0, 0, 0, 100";
                }*/
                secondcount++;

                //    lines += "\n0,0,0,0,0,0,1000";

                String data = "\n\n@data" + lines + "\n";
                FileOutputStream outputStream;
                FileOutputStream stream;
                String line = name + "\n" + "0";
                //    makeToast("Line: " + line);

                try {
                    stream = openFileOutput("names", Context.MODE_PRIVATE);
                    stream.write((line).getBytes());
                    stream.close();
                    //   makeToast("Made the file with names!");
                } catch (Exception ex) {
                    longToast("Message: " + ex.getMessage() + " Cause: " + ex.getCause());
                    paragraph.setText("Message: " + ex.getMessage() + " Cause: " + ex.getCause());
                }

                try {
                    outputStream = openFileOutput("faceData", Context.MODE_PRIVATE);
                    outputStream.write((relation + attributes + data).getBytes());
                    outputStream.close();
                    // makeToast("Made the file!");
                } catch (Exception ex) {
                    longToast("Message: " + ex.getMessage() + " Cause: " + ex.getCause());
                    longestToast("Message: " + ex.getMessage() + " Cause: " + ex.getCause());
                }

            }
        }
    }

    private String detectandFrame(final Bitmap mBitmap, final HashMap<String, String> hashMap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream((outputStream.toByteArray()));
        //  makeToast("IN DETECTANDFRAME");
        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {
            ProgressDialog pd = new ProgressDialog(CreateAccount2.this);

            @Override
            protected Face[] doInBackground(InputStream... inputStreams) {

                publishProgress("Processing...");
                FaceServiceClient.FaceAttributeType[] faceAttr = new FaceServiceClient.FaceAttributeType[]{
                        FaceServiceClient.FaceAttributeType.HeadPose,
                        FaceServiceClient.FaceAttributeType.Age,
                        FaceServiceClient.FaceAttributeType.Gender,
                        FaceServiceClient.FaceAttributeType.Emotion,
                        FaceServiceClient.FaceAttributeType.FacialHair,
                        FaceServiceClient.FaceAttributeType.Hair
                };


                try {
                    Face[] result = faceServiceClient.detect(inputStreams[0],
                            true,
                            false,
                            faceAttr);

                    if (result == null) {
                        publishProgress("Detection failed. Nothing detected.");
                    }

                    publishProgress(String.format("Detection Finished. %d face(s) detected", result.length));
                    return result;
                } catch (Exception e) {
                    publishProgress("Detection Failed: " + e.getMessage());
                    return null;
                }
            }


            @Override
            protected void onPreExecute() {
                pd.show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                pd.setMessage(values[0]);
            }

            @Override
            protected void onPostExecute(Face[] faces) {
                pd.dismiss();
                if (faces == null || faces.length == 0) {
                    makeToast("No face detected. Please retake the picture.");
                } else {
                    age = Double.toString(faces[0].faceAttributes.age);
                    gender = faces[0].faceAttributes.gender;
                    HashMap<String, String> temp = hashMap;
                    temp.put("Age", age);
                    temp.put("Gender", gender);
                    //  longToast(temp.toString());
                    numoftimes++;
                    //    makeToast(temp.get("Name"));
                    try {
                        double x = faces[0].faceAttributes.headPose.roll;
                        double y = faces[0].faceAttributes.headPose.pitch;
                        double z = faces[0].faceAttributes.headPose.yaw;
                        // makeToast("X: " + x + " Y: " + y + " Z: " + z);

                        if ((x > -10 && x < 10) && (z < 9 && z > -9)) {
                            age = Double.toString(faces[0].faceAttributes.age);
                            data.add(temp);
                            if (data.size() >= 0 && data.size() < 3) {
                                register.setText("Register your face: " + (4 - data.size()) + " More times");
                            } else if (data.size() == 3) {
                                register.setText("Register your face: 1 More time");
                            } else {
                                register.setText("Register your face: For improved Accuracy");
                            }
                        } else {
                            Speak("Please retake the picture at a better angle.", 1.27f);
                            longToast("Please retake the picture at a better angle.");
                        }
                    } catch (Exception e) {
                        longToast("LINE: 973: " + e.toString());
                        longToast("LINE: 973: " + e.toString());
                    }

                }
            }
        };
        detectTask.execute(inputStream);
        return age;
    }

    private void Speak(final String s, final float rate) {
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                    t1.setSpeechRate(rate);
                    t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }


    public class MyTask extends AsyncTask<InputStream, String, Face[]> {
        public CreateAccount2 activity;
        public Bitmap bitmap;
        ProgressDialog pd;
        String age;
        Button button;

        public MyTask(CreateAccount2 a, Bitmap bitmap) {
            this.activity = a;
            this.bitmap = bitmap;
            button = a.findViewById(R.id.choose);
        }

        @Override
        protected Face[] doInBackground(InputStream... inputStreams) {

            publishProgress("Processing...");
            FaceServiceClient.FaceAttributeType[] faceAttr = new FaceServiceClient.FaceAttributeType[]{
                    FaceServiceClient.FaceAttributeType.HeadPose,
                    FaceServiceClient.FaceAttributeType.Age,
                    FaceServiceClient.FaceAttributeType.Gender,
                    FaceServiceClient.FaceAttributeType.Emotion,
                    FaceServiceClient.FaceAttributeType.FacialHair
            };


            try {
                Face[] result = faceServiceClient.detect(inputStreams[0],
                        true,
                        false,
                        faceAttr);

                if (result == null) {
                    publishProgress("Detection failed. Nothing detected.");
                }

                publishProgress(String.format("Detection Finished. %d face(s) detected", result.length));
                return result;
            } catch (Exception e) {
                publishProgress("Detection Failed: " + e.getMessage());
                return null;
            }
        }


        @Override
        protected void onPreExecute() {
         /*   pd = new ProgressDialog(activity.getApplicationContext());
            pd.show();*/
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //  pd.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(Face[] faces) {
            //    pd.dismiss();
            if (faces == null || faces.length == 0) {
                makeToast("No face detected. Please retake the picture.");
                age = "No age";
            } else {
                age = Double.toString(faces[0].faceAttributes.age);
            }
            button.setText("Age: " + age);
        }

        public String getAge() {
            return age;
        }
    }

    private void getFaceAttributes(final Bitmap bitmap) {
        //Write the code to use Firebase to get the facial positions.
        //If successful, check if numoftimes = 4. If it does, set cardView and button invisible. Say they can now move on to next steps.
        //Then, set doneFacial = true;
        //When createAccount button is pressed, check for conditions: if doneFacial == true let them move on.

        try {
            FirebaseVisionFaceDetectorOptions options =
                    new FirebaseVisionFaceDetectorOptions.Builder().
                            setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                            .setMinFaceSize(0.15f)
                            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                            .build();
            FirebaseVisionFaceDetector detector;

            detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(options);
            final FirebaseVisionImage image2 = FirebaseVisionImage.fromBitmap(bitmap);
            Task<List<FirebaseVisionFace>> result =
                    detector.detectInImage(image2)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<FirebaseVisionFace>>() {
                                        @Override
                                        public void onSuccess(List<FirebaseVisionFace> faces) {
                                            for (FirebaseVisionFace face : faces) {

                                                Rect bounds = face.getBoundingBox();
                                                int bot = bounds.bottom;
                                                int top = bounds.top;
                                                int right = bounds.right;
                                                int left = bounds.left;
                                                //makeToast(Float.toString(bot) + " " + Float.toString(top) + " " + Float.toString(right) + " " + Float.toString(left));


                                                float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                                float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                                                int id = 0;
                                                float smileProb = 0, rightEyeOpenProb = 0, leftEyeOpenProb = 0;
                                                FirebaseVisionPoint leftEarPos, rightEarPos, moutPos, leftEyePos, rightEyePos;

                                                int heightDif = Math.round(Math.abs(bot - top));
                                                int lengthDif = Math.round(Math.abs(left - right));

                                                HashMap<String, String> hashMap = new HashMap<>();
                                              /*  hashMap.put("Bottom", bot);
                                                hashMap.put("Top", top);
                                                hashMap.put("Right", right);
                                                hashMap.put("Left", left);*/

                                                float leftcheekpos = 0;
                                                float rightcheekpos = 0;
                                                FirebaseVisionFaceLandmark leftcheek = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
                                                FirebaseVisionFaceLandmark rightcheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);

                                                if (leftcheek != null) {
                                                    leftcheekpos = leftcheek.getPosition().getX();
                                                }

                                                if (rightcheek != null) {
                                                    rightcheekpos = rightcheek.getPosition().getX();
                                                }

                                                int nosex = 0;
                                                int nosey = 0;
                                                FirebaseVisionFaceLandmark nosebase = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);

                                                if (nosebase != null) {
                                                    nosex = Math.round(nosebase.getPosition().getX());
                                                    nosey = Math.round(nosebase.getPosition().getY());
                                                }
                                                FirebaseVisionFaceLandmark mouthleft = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT);
                                                FirebaseVisionFaceLandmark mouthright = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT);
                                                float mouthleftx = 0;
                                                float mouthrightx = 0;
                                                if (mouthleft != null) {
                                                    mouthleftx = mouthleft.getPosition().getX();
                                                }

                                                if (mouthright != null) {
                                                    mouthrightx = mouthright.getPosition().getX();
                                                }

                                                FirebaseVisionFaceLandmark mouth = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM);
                                                int mouthX = 0;
                                                int mouthY = 0;
                                                int eyeY = 0;
                                                if (mouth != null) {
                                                    moutPos = mouth.getPosition();
                                                    mouthX = Math.round(moutPos.getX());
                                                    mouthY = Math.round(moutPos.getY());
                                                    //makeToast("Mouth - " + moutPos.getX().toString());
                                                } else {
                                                    // makeToast("No mouth.");
                                                }
                                                float leftEyep = 0;
                                                FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                                                if (leftEye != null) {
                                                    leftEyePos = leftEye.getPosition();
                                                    //position.setText(position.getText().toString() + "\nLeft Eye Pos - " + leftEyePos.getX().toString());
                                                    leftEyep = leftEyePos.getX();
                                                    eyeY = Math.round(leftEyePos.getY());
                                                }

                                                FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
                                                float rightEyep = 0;
                                                if (rightEye != null) {
                                                    rightEyePos = rightEye.getPosition();
                                                    //position.setText(position.getText().toString() + "\nRight Eye Pos - " + rightEyePos.toString());
                                                    rightEyep = rightEyePos.getX();
                                                    eyeY = Math.round(Math.abs(Math.round(rightEyePos.getY()) - eyeY) / 2);

                                                }

                                                FirebaseVisionFaceLandmark nbase = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);
                                                FirebaseVisionFaceLandmark mbase = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM);
                                                double leftdis, rightdis;
                                                float leftx = nbase.getPosition().getX() - mouthleftx;
                                                float lefty = nbase.getPosition().getY() - mouthleft.getPosition().getY();
                                                float righty = nbase.getPosition().getY() - mouthright.getPosition().getY();
                                                float rightx = nbase.getPosition().getX() - mouthrightx;


                                                leftdis = Math.sqrt(Math.pow(leftx, 2) + Math.pow(lefty, 2));
                                                rightdis = Math.sqrt(Math.pow(rightx, 2) + Math.pow(righty, 2));

                                                leftdis = (100 * leftdis) / heightDif;
                                                rightdis = (100 * rightdis) / heightDif;

                                                double eyedisl, eyedisr;
                                                float rightx2 = rightEye.getPosition().getX() - mouthrightx;
                                                float rightxy = rightEye.getPosition().getY() - mouthright.getPosition().getY();
                                                float lefttxy = leftEye.getPosition().getY() - mouthleft.getPosition().getY();
                                                float leftx2 = leftEye.getPosition().getX() - mouthleftx;

                                                eyedisl = Math.sqrt(Math.pow(lefttxy, 2) + Math.pow(leftx2, 2));
                                                eyedisr = Math.sqrt(Math.pow(rightx2, 2) + Math.pow(rightxy, 2));

                                                eyedisl = (100 * eyedisl) / heightDif;
                                                eyedisr = (100 * eyedisr) / heightDif;
                                                //  display.setText(leftdis + " " + rightdis + " " + eyedisl + " " + eyedisr + " " + Double.toString((leftdis + rightdis) / 2).substring(0, 5) + " " + Double.toString((eyedisl + eyedisr) / 2).substring(0, 5));

                                              /*  hashMap.put("Left Eye-Mouth", Double.toString(eyedisl));
                                                hashMap.put("Right Eye-Mouth", Double.toString(eyedisr));
                                                hashMap.put("Left Nose-Mouth", Double.toString(leftdis));
                                                hashMap.put("Right Nose-Mouth", Double.toString(rightdis));*/
                                                // hashMap.put("AVG Eye-Mouth", Double.toString((eyedisl + eyedisr) / 2));

                                                //databaseReference.push().setValue(hashMap);

                                                long a = Math.round(Math.sqrt(Math.pow(nosex, 2) + Math.pow(nosey, 2)));
                                                float scale = 100;
                                                float eyeDif = (100 * (rightEyep - leftEyep)) / ((heightDif + lengthDif) / 2);
                                                float mouthDif = (100 * (mouthrightx - mouthleftx)) / ((heightDif + lengthDif) / 2);
                                                float cheekDif = (100 * (rightcheekpos - leftcheekpos)) / ((heightDif + lengthDif) / 2);

                                                hashMap.put("Eye diff", Float.toString(eyeDif));
                                                hashMap.put("Height diff", Integer.toString(heightDif) + ".0");
                                                hashMap.put("Cheek diff", Float.toString(cheekDif));

                                                hashMap.put("Length diff", Integer.toString(lengthDif) + ".0");
                                                hashMap.put("Mouth diff", Float.toString(mouthDif));
                                                hashMap.put("Nose diff", Double.toString((leftdis + rightdis) / 2).substring(0, 9));

                                                address = findViewById(R.id.addressedit);
                                              /*  String addlocation = address.getText().toString();
                                                if (addlocation == null || address.getText().toString().isEmpty()) {
                                                    addlocation = "Ishaan";
                                                }*/
                                                String addlocation = fullname;
                                                hashMap.put("Name", addlocation);
                                                detectandFrame(bitmap, hashMap);

                                                //  saveData2(hashMap);

                                                // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                                // nose available):


                                             /*   display.setText("ID: " + Integer.toString(id) + "     Smiling: " + smileProbability + "\nLeft Eye Open: " + lefteye + "\nRight Eye Open: " + righteye
                                                        + "\nTilts:   Right/Left:  " + sidetilt + "  Sideways:  " + sidewaytilt);*/

                                            }
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            makeToast("IT FAILED!");
                                            longestToast(e.getMessage());
                                            longToast(e.getMessage());
                                        }
                                    });
        } catch (Exception e) {
            longestToast(e.toString());
            longestToast(e.toString());
        }
    }

    private void doUpload(Bitmap bitmap, StorageReference storageReference2) {
        String s = "Visitors/" + fullname + ".jpg";
        storageReference2 = FirebaseStorage.getInstance().getReference(s);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data2 = baos.toByteArray();

        UploadTask uploadTask = storageReference2.putBytes(data2);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                makeToast("Exception " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              //  makeToast("Picture saved.");
            }
        });
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    private void performCrop(Uri picUri) {
        try {


            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 200);
        } catch (ActivityNotFoundException anve) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    public void specialToast(String s) {
        Toast t = Toast.makeText(this, s, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 100);
        t.show();
    }

    public void makeToast(String s) {
        Toast t = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        t.show();
    }

    public void longToast(String s) {
        Toast t = Toast.makeText(this, s, Toast.LENGTH_LONG);
        t.show();
    }

    public void longestToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void chooseRelation() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeBuilder();
            }
        });

    }


    private void bindViews() {
        facehelp = findViewById(R.id.facehelp);
        useFace = findViewById(R.id.useface);
        address = findViewById(R.id.emailedit);
        descriptionhelp = findViewById(R.id.descriptionhelp);
        paragraph = findViewById(R.id.paragraph);
        display = findViewById(R.id.choice);
        addresshelp = findViewById(R.id.helpaddress);
        createaccount = findViewById(R.id.createanaccount);
        previouspage = findViewById(R.id.goback);
        photo = findViewById(R.id.photoholder);
        takeimage = findViewById(R.id.taking);
        relationhelp = findViewById(R.id.relationhelp);
        wordcount = findViewById(R.id.wordcount);
        picturer = findViewById(R.id.takepicture);
        deleteimage = findViewById(R.id.deleting);
        photohelp = findViewById(R.id.photohelp);
        button = findViewById(R.id.choose);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("8482482353", null, "Hi", null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    public void makeBuilder() {
        final String[] colors = {"Family", "Friend", "Business", "Other"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount2.this);
        builder.setTitle("Pick one of the below");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s = colors[which];
                display.setText(s);
                button.setText("Change");
            }
        });
        builder.show();
    }

}
