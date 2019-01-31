package com.example.anany.informationsystemv1;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

public class MainActivity extends AppCompatActivity {

    TextView createnew;
    Button login, workersignin;
    EditText username;
    EditText password;
    private FirebaseAnalytics mFirebaseAnalytics;
    DatabaseReference databaseReference;
    DatabaseReference getDatabase;
    ImageView loginhelp, workerhelp, facehelp1, facehelp2;
    Boolean passwordshowing = false;
    int numofpresses = 0;
    int numoftries = 0;
    TextView forgotpassword;
    Boolean dismiss = false;
    int seconds = 10;
    TextView mainerror;
    RelativeLayout bar, container;
    Spinner spinner;
    TextView error;
    EditText workername, workercompany;
    ListView listView;
    TextView invisible;

    boolean didBad = false;
    int faceFail = 0;
    int numofPics = 0;

    TextToSpeech t1;
    MultilayerPerceptron mlp;
    SMO smo;
    Classifier ibk;
    J48 j48;
    ScrollView scrollView;
    private FaceServiceClient faceServiceClient;
    String age;
    String gender;
    Button useface;

    boolean doit = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*WHEN VERIFYING USER LOGGING IN, GET THEIR NAME AND ALL THEIR ACCOUNT INFO AND PASS IT IN INTENT
     * TO EditAccount. THEN, IN EditAccount, DISPLAY IT IN A LISTVIEW AND ALLOW THEM TO EDIT THEIR INFORMATION.*/
////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Intent i = new Intent(MainActivity.this, EditAccount.class);
        i.putExtra("Username", "helllf");
        startActivity(i);*/
        scrollView = findViewById(R.id.bigScroll);
        faceServiceClient = new FaceServiceRestClient("<YOUR API ENDPOINT HERE>", "<YOUR API KEY HERE>");
        bindViews();
        //  startActivity(new Intent(MainActivity.this, Congratulations.class));

        // username.setText(FirebaseInstanceId.getInstance().getId());
        bar.setBackgroundColor(getResources().getColor(R.color.mainbar));
        container.setBackgroundColor(getResources().getColor(R.color.container));
        setupspinner();
        listeners();

        readlogOuts();

     /*   new Thread(new Runnable() {
            public void run() {
                try {
                    getFaceDataInstances();
                } catch (Exception e) {
                  *//*  longestToast("MLP LINE 197: " + e.toString());
                    longestToast("MLP LINE 197: " + e.toString());
               *//*
                }
            }
        }).start();*/
        Instances instances = getFaceDataInstances();
        new TrainNeuralNetworkTask().execute(instances);
    }

    private class TrainNeuralNetworkTask extends AsyncTask<Instances, String, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);

        @Override
        protected String doInBackground(Instances... instances) {
            publishProgress("Training Neural Network...");

            smo = new SMO();
            j48 = new J48();
            ibk = new IBk(3);

            mlp = new MultilayerPerceptron();
            mlp.setLearningRate(0.1);
            mlp.setMomentum(0.2);
            mlp.setTrainingTime(3500);
            mlp.setHiddenLayers("11");
            try {
                mlp.buildClassifier(instances[0]);
                smo.buildClassifier(instances[0]);
                j48.buildClassifier(instances[0]);
                ibk.buildClassifier(instances[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Done Training Neural Network!";
        }

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            pd.setMessage(values[0]);
        }

        protected void onPostExecute(String result) {
            Snackbar snackbar = Snackbar
                    .make(scrollView, result, Snackbar.LENGTH_LONG).setDuration(3000);
            snackbar.show();
            pd.dismiss();
        }
    }


    private Instances getFaceDataInstances() {
        String yourFilePath = getApplicationContext().getFilesDir() + "/" + "faceData";
        File yourFile = new File(yourFilePath);
        Instances combined = null;
        try {
            BufferedReader all = new BufferedReader(new FileReader(yourFile));
            combined = new Instances(all);
            combined.setClassIndex(6);
            combined.randomize(new java.util.Random(0));

            if (combined == null) {
                // longToast("Combined is NULL. LINE 197.");
            } else {
               /* mlp = new MultilayerPerceptron();
                mlp.setLearningRate(0.1);
                mlp.setMomentum(0.2);
                mlp.setTrainingTime(3500);
                mlp.setHiddenLayers("11");
                mlp.buildClassifier(combined);
                Toast.makeText(MainActivity.this, "Done Training", Toast.LENGTH_SHORT).show();*/
                //   makeToast("Done training.");
            }
        } catch (Exception e) {
        /*    longToast("LINE 200: " + e.toString());
            longToast(e.toString());
       */
        }
        // makeToast("It worked!");
        return combined;

    }

    private void readlogOuts() {
        listView = findViewById(R.id.list);
        invisible = findViewById(R.id.invisible);
        invisible.setVisibility(View.INVISIBLE);
        final ImageView multi = findViewById(R.id.nousers);
        DatabaseReference logouts = FirebaseDatabase.getInstance().getReference("To Log Out");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> time = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> phone = new ArrayList<>();
                ArrayList<String> email = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();
                ArrayList<String> relation = new ArrayList<>();
                ArrayList<WorkerLogOut> workerLogOut = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    time.add(child.child("Time").getValue().toString());
                    name.add(child.child("Name").getValue().toString());
                    phone.add(child.child("Phone").getValue().toString());
                    email.add(child.child("Email").getValue().toString());
                    username.add(child.child("Username").getValue().toString());
                    relation.add(child.child("Relation").getValue().toString());
                }
                if (time.isEmpty()) {
                    invisible.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    multi.setVisibility(View.VISIBLE);
                } else {
                    invisible.setVisibility(View.INVISIBLE);
                    multi.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < time.size(); i++) {
                        workerLogOut.add(new WorkerLogOut(time.get(i), name.get(i), phone.get(i), email.get(i), username.get(i), relation.get(i)));
                    }
                    ArrayAdapter<WorkerLogOut> arrayAdapter = new LogOutAdapter(MainActivity.this, getApplicationContext(), workerLogOut);
                    listView.setAdapter(arrayAdapter);
                    interceptTouchEvents(listView);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        logouts.addValueEventListener(valueEventListener);


    }

    public static void interceptTouchEvents(ListView listView) {
        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (numofpresses < 3) {
            numofpresses++;
        } else if (numofpresses == 3) {
            makeToast("Press one more time to go back to setup.");
            numofpresses++;
        } else {
            startActivity(new Intent(MainActivity.this, InitialSetup.class));
        }
    }

    private void setupspinner() {
        ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.servicetype, R.layout.service_item);
        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.service_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new ServiceItemSelected());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");

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
            FirebaseVisionImage image2 = FirebaseVisionImage.fromBitmap(bitmap);
            Task<List<FirebaseVisionFace>> result =
                    detector.detectInImage(image2)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<FirebaseVisionFace>>() {
                                        @Override
                                        public void onSuccess(List<FirebaseVisionFace> faces) {
                                            for (FirebaseVisionFace face : faces) {

                                                List<FirebaseVisionPoint> leftEyeContour =
                                                        face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
                                                List<FirebaseVisionPoint> upperLipBottomContour =
                                                        face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints();
                                                // information.setText(leftEyeContour.toString());
                                                // makeToast(leftEyeContour.toString() + " " + upperLipBottomContour.toString());

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
                                                FirebaseVisionPoint leftEyePos, rightEyePos;

                                                int heightDif = Math.round(Math.abs(bot - top));
                                                int lengthDif = Math.round(Math.abs(left - right));

                                                HashMap<String, String> hashMap = new HashMap<>();


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


                                                float leftEyep = 0;
                                                FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                                                if (leftEye != null) {
                                                    leftEyePos = leftEye.getPosition();
                                                    //position.setText(position.getText().toString() + "\nLeft Eye Pos - " + leftEyePos.getX().toString());
                                                    leftEyep = leftEyePos.getX();
                                                    //  eyeY = Math.round(leftEyePos.getY());
                                                }

                                                FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
                                                float rightEyep = 0;
                                                if (rightEye != null) {
                                                    rightEyePos = rightEye.getPosition();
                                                    //position.setText(position.getText().toString() + "\nRight Eye Pos - " + rightEyePos.toString());
                                                    rightEyep = rightEyePos.getX();
                                                    //  eyeY = Math.round(Math.abs(Math.round(rightEyePos.getY()) - eyeY) / 2);

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

                                                long a = Math.round(Math.sqrt(Math.pow(nosex, 2) + Math.pow(nosey, 2)));
                                                float scale = 100;
                                                float eyeDif = (100 * (rightEyep - leftEyep)) / ((heightDif + lengthDif) / 2);
                                                float mouthDif = (100 * (mouthrightx - mouthleftx)) / ((heightDif + lengthDif) / 2);
                                                float cheekDif = (100 * (rightcheekpos - leftcheekpos)) / ((heightDif + lengthDif) / 2);

                                                hashMap.put("Eye diff", Float.toString(eyeDif));
                                                hashMap.put("Height diff", Integer.toString(heightDif) + ".0");
                                                hashMap.put("Length diff", Integer.toString(lengthDif) + ".0");
                                                hashMap.put("Mouth diff", Float.toString(mouthDif));
                                                hashMap.put("Cheek diff", Float.toString(cheekDif));
                                                hashMap.put("Nose diff", Double.toString((leftdis + rightdis) / 2).substring(0, 9));
                                                recognizeandFrame(bitmap, hashMap);


                                                //makeToast(hashMap.toString());

                                            }
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            makeToast("IT FAILED!");
                                            makeToast(e.getMessage());
                                        }
                                    });
        }

    }

    private String recognizeandFrame(final Bitmap mBitmap, final HashMap<String, String> hashMap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream((outputStream.toByteArray()));

        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {
            ProgressDialog pd = new ProgressDialog(MainActivity.this);

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
                pd.show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                pd.setMessage(values[0]);
            }

            @Override
            protected void onPostExecute(Face[] faces) {
                pd.dismiss();
                if (faces == null || faces.length == 0 || faces[0] == null) {
                    makeToast("No face detected. Please retake the picture.");
                } else {
                    age = Double.toString(faces[0].faceAttributes.age);
                    //  longToast("AGE: " + age);
                    gender = faces[0].faceAttributes.gender;

                    double x = faces[0].faceAttributes.headPose.roll;
                    double y = faces[0].faceAttributes.headPose.pitch;
                    double z = faces[0].faceAttributes.headPose.yaw;
                    //  makeToast("X: " + x + " Y: " + y + " Z: " + z);

                    if ((x > -10 && x < 10) && (z < 9.5 && z > -9.5) && (y < 8 && y > -8)) {
                        HashMap<String, String> temp = hashMap;
                        temp.put("Age", age);
                        temp.put("Gender", gender);
                        makePrediction(temp);
                    } else {
                        Speak("Please retake the picture at a better angle.", 1.27f);
                        longToast("Please retake the picture at a better angle.");
                    }

                    //progressDialog(temp);

                }
            }
        };
        detectTask.execute(inputStream);
        return age;
    }

    private Object makePrediction(HashMap<String, String> hashMap) {
        float cheek = Float.parseFloat(hashMap.get("Cheek diff"));
        float eye = Float.parseFloat(hashMap.get("Eye diff"));
        float mouth = Float.parseFloat(hashMap.get("Mouth diff"));
        float nose = Float.parseFloat(hashMap.get("Nose diff"));
        double age = Double.parseDouble(hashMap.get("Age"));
        String sgender = hashMap.get("Gender");
        int gender = 0;
        if (sgender.contains("fem") || sgender.contains("Fem")) {
            gender = 1;
        }

        String yourFilePath = getApplicationContext().getFilesDir() + "/" + "faceData";
        File yourFile = new File(yourFilePath);
        try {
            BufferedReader all = new BufferedReader(new FileReader(yourFile));
            Instances combined = new Instances(all);
            if (combined == null) {
                longToast("Combined is NULL. LINE 584.");
            } else {
                //   longToast("Combined is not null " + combined.numInstances());
            }
            // makeToast("It worked!");
            combined.setClassIndex(6);

            String yourPath = getApplicationContext().getFilesDir() + "/" + "names";
            File file = new File(yourPath);
            BufferedReader namesReader = new BufferedReader(new FileReader(file));

            String line1 = "";
            String line2 = "";
            String tline;
            int mycount = 0;
            while ((tline = namesReader.readLine()) != null) {
                if (mycount == 0) {
                    line1 += tline;
                    mycount++;
                } else {
                    line2 += tline;
                }
            }
            String[] numarray = line2.split(",");
            String[] names = line1.split(",");
            String name = numarray[0];
            for (int i = 1; i < numarray.length; i++) {
                name += ", " + numarray[i];
            }

            combined.randomize(new java.util.Random(0));

            String relation = "@relation testdata";
            String attributes = "\n@attribute cheek numeric\n" +
                    "@attribute eye numeric\n" +
                    "@attribute mouth numeric\n" +
                    "@attribute nose numeric\n" +
                    "@attribute age numeric\n" +
                    "@attribute gender {0,1}\n" +
                    "@attribute class {" + name + "}";

            String lines = "\n" + cheek + ", " + eye + ", " + mouth + ", " + nose + ", " + age + ", " + gender + ", 0";
            String datat = "\n\n@data" + lines + "\n";
            //  longToast("This is the data being put in testData: " + lines);
            FileOutputStream stream;
            //longToast("Attributes: " + attributes);
            try {
                stream = openFileOutput("TestData", Context.MODE_PRIVATE);
                stream.write((relation + attributes + datat).getBytes());
                stream.close();
                // makeToast("Made the file with new data!");
            } catch (Exception ex) {
                longestToast("Message: " + ex.getMessage() + " Cause: " + ex.getCause());
                longestToast("Message: " + ex.getMessage() + " Cause: " + ex.getCause());
            }

            String yourFilePath2 = getApplicationContext().getFilesDir() + "/" + "TestData";
            File yourFile2 = new File(yourFilePath2);
            BufferedReader inputReader = new BufferedReader(new FileReader(yourFile2));
            BufferedReader inputReader2 = new BufferedReader(new FileReader(yourFile2));

            Instances test = new Instances(inputReader2);
            test.setClassIndex(6);
            Instance[] instances = new Instance[1];
            instances[0] = test.instance(0);
            int count = 0;

            String newdata = "";
            while ((tline = inputReader.readLine()) != null) {
                newdata += tline;
                // makeToast("Lines in file: " + tline);
            }
            //  makeToast("This is data of current person: " + newdata);
            //   makeToast(newdata);

            double nnpredictions = mlp.classifyInstance(instances[0]);
            double predictions = ibk.classifyInstance(instances[0]);
            double svcprediction = smo.classifyInstance(instances[0]);
            double jprediction = j48.classifyInstance(instances[0]);
            //   makeToast("Double prediction: " + predictions);
            int knum = (int) predictions;
            int snum = (int) svcprediction;
            int nnum = (int) nnpredictions;
            int jnum = (int) jprediction;
            String knnmatch = "";
            for (int i = 0; i < numarray.length; i++) {
                if (Integer.toString(knum) == numarray[i]) {
                    knnmatch = names[i];
                    break;
                } else {
                    continue;
                }
            }
            Evaluation jeval = new Evaluation(combined);
            jeval.evaluateModel(j48, combined);
            //     longToast("Decision Tree: " + jeval.toSummaryString());

            Evaluation seval = new Evaluation(combined);
            seval.evaluateModel(smo, combined);
            //       longToast("SVC: " + seval.toSummaryString());

            Evaluation keval = new Evaluation(combined);
            keval.evaluateModel(ibk, combined);
            //      longToast("KNN: " + keval.toSummaryString());

            Evaluation neval = new Evaluation(combined);
            neval.evaluateModel(mlp, combined);
            //  longToast("NN: " + neval.toSummaryString());

            knnmatch = names[knum];
            String svcmatch = names[snum];
            String nnmatch = names[nnum];
            String jmatch = names[jnum];

            ArrayList<String> matches = new ArrayList<>();
            matches.add(knnmatch);
            matches.add(svcmatch);
            matches.add(nnmatch);
            matches.add(jmatch);

            String allname = "";
            for (String s : matches) {
                allname += s + " ";
            }

            Set<String> unique = new HashSet<>();
            unique.add(svcmatch);
            unique.add(svcmatch);
            unique.add(nnmatch);
            unique.add(jmatch);

            List<String> myList = new ArrayList<>();
            myList.addAll(unique);

            String[] occurences = new String[unique.size()];
            for (int i = 0; i < unique.size(); i++) {
                String word = myList.get(i);
                if (word.equals(nnmatch)) {
                    occurences[i] = word + " " + (countOccurences(allname, word) + 1);
                } else {
                    occurences[i] = word + " " + (countOccurences(allname, word));
                }
            }
            String result = "";
            for (String t : occurences) {
                result += t + "\n";
            }

            TreeMap<String, Integer> treeMap = new TreeMap<>();
            ArrayList<String> strings = new ArrayList<>();
            strings.add(svcmatch);
            strings.add(nnmatch);
            strings.add(knnmatch);
            for (String w : strings) {
                Integer i = treeMap.get(w);
                if (i == null) {
                    treeMap.put(w, 1);
                } else {
                    treeMap.put(w, i + 1);
                }
            }

            ArrayList<String> allnames = new ArrayList<>();
            for (String namer : names) {
                allnames.add(namer);
            }


            //   makeToast(cheek + " " + eye + " " + mouth + " " + nose);
            double[] nnconfidencea = mlp.distributionForInstance(test.instance(0));
            double[] dtconfidencea = j48.distributionForInstance(test.instance(0));
            double[] knnconfidencea = ibk.distributionForInstance(test.instance(0));
            double[] svcconfidencea = smo.distributionForInstance(test.instance(0));

            double nncofidence = nnconfidencea[nnum];
            double dtcofidence = dtconfidencea[jnum];
            double knncofidence = knnconfidencea[knum];
            double svccofidence = svcconfidencea[snum];

            int badness = 0;
            //if neural network less than 92, say please take again. If below 70 say unrecognized visitor at the door.
            //if knn below 45 say unrecognized visitor. If below 70 say please take again.
            String vals = getBestCandidate(occurences);
            numofPics += 1;

            longToast("NN Confidence: " + nnconfidencea[nnum] + "\nKNN Confidence: " + knnconfidencea[knum] + "\nDT Confidence: " + dtconfidencea[jnum] + "\nSVC Confidence: " + svcconfidencea[snum]);

            if ((nncofidence < 0.65 && knncofidence < 0.60)) {
                //Say unrecognized visitor. Write to Failed Login. Disable button and sign in.
                Speak("Unrecognized Visitor!", 1.15f);
                Snackbar snackbar = Snackbar
                        .make(scrollView, "Unrecognized visitor! Notifying caretaker of your arrival.", Snackbar.LENGTH_LONG).setDuration(5000);
                snackbar.show();
                String namer = vals;
                makeFailedLogin(namer);
                username.setEnabled(false);
                password.setEnabled(false);
                login.setEnabled(false);
                useface.setEnabled(false);
                disableLogin();
                //Disable logins
            } else if ((nncofidence < 0.85 && knncofidence < 0.7)) {
                faceFail += 2;
                //Tell them they have only one try left.
                if (numofPics >= 2) {
                    Speak("Unrecognized Visitor! The caretaker has been notified of your arrival.", 1.15f);
                    Snackbar snackbar = Snackbar
                            .make(scrollView, "Unable to identify you. The caretaker has been notified of your arrival", Snackbar.LENGTH_LONG).setDuration(5000);
                    snackbar.show();
                    username.setEnabled(false);
                    password.setEnabled(false);
                    login.setEnabled(false);
                    useface.setEnabled(false);
                    disableLogin();
                    makeFailedLogin(name);
                } else {
                    Speak("Unable to identify you. Please try again.", 1.2f);
                    Snackbar snackbar = Snackbar
                            .make(scrollView, "Unable to identify you across all existing users. You have one try left.", Snackbar.LENGTH_LONG).setDuration(5000);
                    snackbar.show();
                }
                didBad = true;
                badness++;
            } else if (knncofidence < 0.7) {
                faceFail += 1;
                Speak("Unable to identify you.", 1.2f);
                Snackbar snackbar = Snackbar
                        .make(scrollView, "Unable to identify you. You have one less try available.", Snackbar.LENGTH_LONG).setDuration(5000);
                snackbar.show();
            } else if (vals.contains("OR")) {
                faceFail += 1;
                Speak("Unable to identify you.", 1.2f);
                Snackbar snackbar = Snackbar
                        .make(scrollView, "Unable to identify you. Please try again.", Snackbar.LENGTH_LONG).setDuration(5000);
                snackbar.show();
                badness += 1;
            } else if ((knncofidence > 0.92 && knncofidence < 0.965) && (nncofidence > 0.86 && nncofidence < 0.91)) {
                //They signed in. However, show them the username and tell them to fill in the password.
                //Ask them to fill in the password.
                //Ask for password.
                Speak("Identified as: " + getBestCandidate(occurences) + ". Please enter your password to sign in.", 1.25f);
                Snackbar snackbar = Snackbar
                        .make(scrollView, "Partially logged in as " + getBestCandidate(occurences) + "! Please enter your password as well.", Snackbar.LENGTH_LONG).setDuration(5000);
                snackbar.show();
                didBad = false;
                numofPics = 0;
                faceFail = 0;
                passwordResult(getBestCandidate(occurences));
            } else if ((nncofidence < 0.905) && knncofidence < 0.86) {
                faceFail += 1;
                Speak("Unable to identify you.", 1.2f);
                Snackbar snackbar = Snackbar
                        .make(scrollView, "Unable to identify you across all existing users. You have one try less.", Snackbar.LENGTH_LONG).setDuration(5000);
                snackbar.show();
                //Tell them they have one try less.
                badness += 1;
            } else if (nncofidence < 0.89 && knncofidence > nncofidence) {
                //They signed in. However, show them the username and tell them to fill in the password.
                //Ask them to fill in the password.
                //Ask for password.
                Speak("Identified as: " + getBestCandidate(occurences) + ". Please enter your password to sign in.", 1.25f);
                Snackbar snackbar = Snackbar
                        .make(scrollView, "Partially logged in as " + getBestCandidate(occurences) + "! Please enter your password as well.", Snackbar.LENGTH_LONG).setDuration(5000);
                snackbar.show();
                didBad = false;
                numofPics = 0;
                faceFail = 0;
                passwordResult(getBestCandidate(occurences));
            } else {
                if (nncofidence > .94 && knncofidence > .89) {
                    //saveData(getBestCandidate(occurences), cheek, eye, mouth, nose, age, gender, hashMap);
                    //SAVE THEIR DATA
                    //Since it is high confidence and they were able to sign in, save their data.
                }

                if (numofPics >= 3 || didBad || (knncofidence < 0.87 && nncofidence < .925)) {
                    //Ask for password.
                    //Ask them to fill in password. Show the username and tell them to fill in the password.
                    //They have two tries for password by clicking submit button. If they fail, contact caretaker through Failed Login.
                    //Disable login and tell visitor login disabled.
                    //If they click no button, then contact caretaker of failed login and tell visitor caretaker is on their way.
                    Speak("Identified as: " + getBestCandidate(occurences) + ". However, it is not completely certain. Please enter your password to sign in.", 1.25f);
                    Snackbar snackbar = Snackbar
                            .make(scrollView, "Partially logged in as " + getBestCandidate(occurences) + "! Please enter your password as well.", Snackbar.LENGTH_LONG).setDuration(5000);
                    snackbar.show();
                    didBad = false;
                    numofPics = 0;
                    faceFail = 0;
                    passwordResult(getBestCandidate(occurences));
                } else {
                    didBad = false;
                    numofPics = 0;
                    faceFail = 0;
                    Speak("Identified as: " + getBestCandidate(occurences) + "!", 1.20f);
                    Snackbar snackbar = Snackbar
                            .make(scrollView, "Successfully logged in as " + getBestCandidate(occurences) + "!", Snackbar.LENGTH_LONG).setDuration(5000);
                    snackbar.show();
                    displayResult(cheek, mouth, nose, eye, age, gender, knncofidence, nncofidence, getBestCandidate(occurences));
                    //Successful sign in.
                    //Show them username. Underneath show password, but instead of characters, show ....... for how many ever characters.
                    //Have a Yes button or no. For yes save in Firebase under Visitor Login and reset all variables.
                    //For no, do failedlogin in Firebase with their guessed name. Tell the person that the caretaker has been
                    //contacted of their arrival and is on their way..
                }
            }

            if (faceFail >= 3) {
                //They failed. Say unrecognized visitor. Write to Failed Login. Disable button and sign in.
                //Tell them that caretaker has been contacted.
                didBad = false;
                numofPics = 0;
                faceFail = 0;
                Speak("You have failed to login. The login has been disabled and the caretaker has been notified", 1.25f);
                Snackbar snackbar = Snackbar
                        .make(scrollView, "You have failed to login. The caretaker has been notified of your arrival.", Snackbar.LENGTH_LONG).setDuration(5000);
                snackbar.show();
                makeFailedLogin(vals);
                username.setEnabled(false);
                password.setEnabled(false);
                login.setEnabled(false);
                useface.setEnabled(false);
                disableLogin();
                //Disable login.
            }
            //      longToast("# of Fails: " + faceFail + "\n# of Pics: " + numofPics);


            //       makeToast("NN: " + nnum + " KNN: " + knum + " SVC: " + snum + " DT: " + jnum);
            //       longToast("DT: " + jmatch + " " + jnum + "\nKNN: " + knnmatch + " " + knum + "\nSVC: " + svcmatch + " " + snum + "\nNeural Network: " + nnmatch + " " + nnum + "\nFalse Value: " + instances[0].classValue());

            //     longToast("YOU ARE: " + getBestCandidate(occurences));
            // makeAlertDialog(match, eye, mouth, cheek, nose, allnames);

        } catch (Exception ex) {
            longestToast("LINE 963 Message: " + ex.getMessage() + " Cause: " + ex.getCause());
            longestToast("LINE 963 Message: " + ex.getMessage() + " Cause: " + ex.getCause());
            longestToast("LINE 963 Message: " + ex.getMessage() + " Cause: " + ex.getCause());
        }

        return "hi";

    }

    private void saveData(String propername, float cheek, float eye, float mouth, float nose, double age, int gender, HashMap<String, String> hashMap) {
        String yourPath = getApplicationContext().getFilesDir() + "/" + "names";
        File file = new File(yourPath);
        BufferedReader namesReader = null;
        try {
            namesReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            longestToast("LINE 979: " + e.toString());
            longToast("LINE 979: " + e.toString());
        }

        String[] splitter = propername.split(" ");
        String name = splitter[0];
        for (int i = 1; i < splitter.length; i++) {
            name += " " + splitter[i];
        }
        try {
            String line1 = "";
            String line2 = "";
            String tline;
            int mycount = 0;
            while ((tline = namesReader.readLine()) != null) {
                if (mycount == 0) {
                    line1 += tline;
                    mycount++;
                } else {
                    line2 += tline;
                }
            }
            String[] numarray = line2.split(",");
            String[] names = line1.split(",");
            String allnames = numarray[0];
            int position = 0;
            for (int i = 0; i < names.length; i++) {
                if (name.equals(names[i])) {
                    position = i;
                    break;
                } else {
                    continue;
                }
            }

            for (int i = 1; i < numarray.length; i++) {
                allnames += ", " + numarray[i];
            }

            int matchingNum = Integer.parseInt(numarray[position]);
            longToast("Matching Number: " + matchingNum);

            String yourFilePath = getApplicationContext().getFilesDir() + "/" + "faceData";
            File yourFile = new File(yourFilePath);
            BufferedReader reader = new BufferedReader(new FileReader(yourFile));
            String lines = "";
            String line = "";
            int count = 0;
            while ((line = reader.readLine()) != null) {
                lines += line + "\n";
            }
            lines += cheek + ", " + eye + ", " + mouth + ", " + nose + ", " + age + ", " + gender + ", " + (matchingNum) + "";

            try {
                FileOutputStream outputStream = openFileOutput("faceData", Context.MODE_PRIVATE);
                outputStream.write(lines.getBytes());
                outputStream.close();

                Instances instances = getFaceDataInstances();
                new TrainNeuralNetworkTask().execute(instances);
            } catch (Exception e) {
                longToast("Error saving facial data - " + e.toString());
            }


            //Write the code to find the number match and write to FaceData with new data of user.
        } catch (Exception e) {
            longestToast(e.toString());
            longToast(e.toString());
        }
    }

    Boolean passclicked = false;

    private void passwordResult(final String propername) {
        //Read from Firebase. Get the person's username and password.
        //Make the Alertdialog with their name, username, and password asking if it is them.
        //Have yes and no button. YES - Save in Visitor Logins and Events Log. Notify Caretaker.
        //NO - call disableFace() and tell them to sign in using username and password. Contact Caretaker about failed login.
        //IMPORTANT - use the passedclicked boolean to see if dialog was dismissed. First, when this is created call a function
        //that goes through a loop for 15 seconds. If user clicks no or yes set clicked to true. Then, in the runnable, if clicked is false
        //Dismiss the dialog and save the data. Otherwise don't dismiss and don't save..

        makeToast("In password result");
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.passwordface);

        TextView name = dialog.findViewById(R.id.name);
        final TextView usernamet = dialog.findViewById(R.id.username);
        TextView confirm = dialog.findViewById(R.id.info);
        TextView passwordt = dialog.findViewById(R.id.password);
        final EditText epassword = dialog.findViewById(R.id.epassword);
        final Button yes = dialog.findViewById(R.id.dialog_cancel);
        final Button no = dialog.findViewById(R.id.dialog_ok);

        final String[] splitter = propername.split(" ");
        final String extra = propername;
        String names = splitter[0];
        for (int i = 1; i < splitter.length; i++) {
            names += " " + splitter[i];
        }
        name.setText(names);
        confirm.setText("It is not completely certain if you are " + names + ". Please confirm your password below to sign in.");

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Create Account");
        final String finalNames = names;
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String tempname = " ";
                    String username = "";
                    String password = "";
                    String relation = "";
                    String description = "";
                    String email = "";

                    String phone = "";
                    Boolean found = false;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        tempname = child.child("Name").getValue(String.class);
                        username = child.child("Username").getValue(String.class);
                        password = child.child("Password").getValue(String.class);
                        relation = child.child("Relation").getValue(String.class);
                        description = child.child("Description").getValue(String.class);
                        String temail = child.child("Email").getValue(String.class);
                        if (temail == null) {
                            email = " ";
                        } else {
                            email = temail;
                        }
                        String tphone = child.child("Phone").getValue(String.class);
                        if (tphone == null) {
                            phone = " ";
                        } else {
                            phone = tphone;
                        }

                        if (tempname.equals(finalNames)) {
                            found = true;
                            break;
                        } else {
                            continue;
                        }
                    }
                    //   makeToast("Found match: " + finalNames + " Username: " + username);
                    //      longToast("testname INSIDE: " + testname);

                    //  longToast("Found: " + found);
                    if (found) {
                        usernamet.setText("Your Username: " + username);
                        processPasswordButtons(tempname, username, password, relation, description, email, phone, no, yes, dialog, epassword);
                    } else {
                        dialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(scrollView, "Uh oh! It looks like your account hasn't been approved yet. The caretaker has been notified of your arrival.", Snackbar.LENGTH_LONG).setDuration(5000);
                        snackbar.show();
                        makeFailedLogin(extra);
                    }
                } catch (Exception e) {
                    longToast(e.toString());
                    longToast(e.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbref.addValueEventListener(valueEventListener);


        dialog.show();

    }

    int numofclicks = 0;

    private void processPasswordButtons(final String name, final String susername, final String spassword, final String relation, final String description, final String email, final String phone, Button no, Button yes, final Dialog dialog, final EditText epassword) {
        //closeDialog(dialog, name);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
                username.setEnabled(false);
                login.setEnabled(false);
                password.setEnabled(false);
                useface.setEnabled(false);
                disableLogin();
                longestToast("You have failed to login. The caretaker has been notified of your arrival.");
                Speak("You have failed to login. The login has been disabled and the caretaker has been notified", 1.25f);
                Snackbar snackbar = Snackbar
                        .make(scrollView, "You have failed to login. The caretaker has been notified of your arrival.", Snackbar.LENGTH_LONG).setDuration(5000);
                snackbar.show();
                numofclicks = 0;
                dialog.dismiss();
                makeFailedLogin(name);
            }
        });


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numofclicks++;
                if (epassword.getText().toString().isEmpty() || epassword.getText().toString() == null) {
                    epassword.setText(" ");
                }

                if (numofclicks <= 2) {
                    if (spassword.equals(epassword.getText().toString())) {
                        clicked = true;
                        dialog.dismiss();
                        //save Data in ToLogOut.
                        //Write to EventsLog about Visitor Sign in.
                        //Write to Login/Visitor Login
                        //Write to notification paths
                        numofclicks = 0;

                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Classifier", "Visitor");
                        hashMap.put("Email", email);
                        hashMap.put("Name", name);
                        hashMap.put("Phone", phone);
                        hashMap.put("Relation", relation);
                        hashMap.put("Time", currentDateTimeString);
                        hashMap.put("Username", susername);
                        hashMap.put("Description", description);
                        hashMap.put("Device", "Visitor Device");
                        hashMap.put("Password", spassword);
                        DatabaseReference eventsLog = FirebaseDatabase.getInstance().getReference("Events Log");
                        eventsLog.push().setValue(hashMap);

                        DatabaseReference login = FirebaseDatabase.getInstance().getReference("Login/Visitor Login");
                        HashMap<String, String> hashMap2 = new HashMap<>();
                        hashMap2.put("Email", email);
                        hashMap2.put("Name", name);
                        hashMap2.put("Phone", phone);
                        hashMap2.put("Relation", relation);
                        hashMap2.put("Time", currentDateTimeString);
                        hashMap2.put("Username", susername);
                        hashMap2.put("Password", spassword);
                        hashMap2.put("Device", "Visitor Device");
                        hashMap2.put("Description", description);
                        login.push().setValue(hashMap2);

                        DatabaseReference logouts = FirebaseDatabase.getInstance().getReference("To Log Out");
                        HashMap<String, String> logoutvalues = new HashMap<>();
                        logoutvalues.put("Time", currentDateTimeString);
                        logoutvalues.put("Name", name);
                        logoutvalues.put("Phone", phone);
                        logoutvalues.put("Email", email);
                        logoutvalues.put("Username", susername);
                        logoutvalues.put("Relation", relation);
                        logouts.push().setValue(logoutvalues);

                        writeNotifications(currentDateTimeString, name, relation);
                        readlogOuts();
                    } else {
                        Speak("Wrong password!", 1.25f);
                        Snackbar snackbar = Snackbar
                                .make(scrollView, "You have entered the wrong password.", Snackbar.LENGTH_LONG).setDuration(4000);
                        snackbar.show();
                    }
                } else {
                    Speak("The login has been disabled and the caretaker has been notified of your arrival.", 1.25f);
                    Snackbar snackbar = Snackbar
                            .make(scrollView, "You have run out of tries. The caretaker has been notified of your arrival", Snackbar.LENGTH_LONG).setDuration(4000);
                    snackbar.show();
                    clicked = true;
                    username.setEnabled(false);
                    login.setEnabled(false);
                    password.setEnabled(false);
                    useface.setEnabled(false);
                    disableLogin();
                    dialog.dismiss();
                    numofclicks = 0;
                    makeFailedLogin(name);
                }

            }
        });

    }

    Boolean clicked = false;
    String testname = "";

    private void displayResult(final float cheek, final float mouth, final float nose, final float eye, final double age, final int gender, final double knncofidence, final double nncofidence, final String propername) {
        //Read from Firebase. Get the person's username and password.
        //Make the Alertdialog with their name, username, and password asking if it is them.
        //Have yes and no button. YES - Save in Visitor Logins and Events Log. Notify Caretaker.
        //NO - call disableFace() and tell them to sign in using username and password. Contact Caretaker about failed login.
        //IMPORTANT - use the clicked boolean to see if dialog was dismissed. First, when this is created call a function
        //that goes through a loop for 15 seconds. If user clicks no or yes set clicked to true. Then, in the runnable, if clicked is false
        //Dismiss the dialog and save the data. Otherwise don't dismiss and don't save.
        try {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.successface);
            String[] splitter = propername.split(" ");
            String names = splitter[0];
            for (int i = 1; i < splitter.length; i++) {
                names += " " + splitter[i];
            }

            TextView name = dialog.findViewById(R.id.name);
            final TextView tusername = dialog.findViewById(R.id.username);
            final TextView tpassword = dialog.findViewById(R.id.password);
            final Button yes = dialog.findViewById(R.id.dialog_cancel);
            final Button no = dialog.findViewById(R.id.dialog_ok);

            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Create Account");
            final String finalNames = names;
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        String tempname = " ";
                        String username = "";
                        String password = "";
                        String relation = "";
                        String description = "";
                        String email = "";
                        String phone = "";
                        Boolean found = false;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            tempname = child.child("Name").getValue(String.class);
                            testname = tempname;
                            username = child.child("Username").getValue(String.class);
                            password = child.child("Password").getValue(String.class);
                            relation = child.child("Relation").getValue(String.class);
                            description = child.child("Description").getValue(String.class);
                            String temail = child.child("Email").getValue(String.class);
                            if (temail == null) {
                                email = " ";
                            } else {
                                email = temail;
                            }
                            String tphone = child.child("Phone").getValue(String.class);
                            if (tphone == null) {
                                phone = " ";
                            } else {
                                phone = tphone;
                            }

                            if (tempname.equals(finalNames)) {
                                found = true;
                                break;
                            } else {
                                continue;
                            }
                        }
                        //   makeToast("Found match: " + finalNames + " Username: " + username);
                        //      longToast("testname INSIDE: " + testname);
                        // longToast("Found: " + found);
                        if (found) {
                            for (Character s : password.toCharArray()) {
                                if (tpassword.getText().toString().isEmpty() || tpassword.getText().toString() == null) {
                                    tpassword.setText("Your Password: .");
                                } else {
                                    tpassword.setText(tpassword.getText() + " .");
                                }
                            }
                            tusername.setText("Your Username: " + username);
                            processButtons(eye, nose, mouth, cheek, gender, age, knncofidence, nncofidence, tempname, username, password, relation, description, email, phone, no, yes, dialog);
                        } else {
                            dialog.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(scrollView, "Uh oh! It looks like your account hasn't been approved yet. The caretaker has been notified of your arrival.", Snackbar.LENGTH_LONG).setDuration(5000);
                            snackbar.show();
                            makeFailedLogin(propername);
                        }
                    } catch (Exception e) {
                        longToast(e.toString());
                        longToast(e.toString());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dbref.addValueEventListener(valueEventListener);

            //    longToast("TestNAME OUTSIDE: " + testname);

            name.setText(names);
            dialog.show();
        } catch (Exception e) {
            longestToast(e.toString());
        }

    }

    private void processButtons(final float eye, final float nose, final float mouth, final float cheek, final int gender, final double age, final double knncofidence, final double nncofidence, final String name, final String username, final String password, final String relation, final String description, final String email, final String phone, Button no, Button yes, final Dialog dialog) {
        //closeDialog(dialog, name);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
                useface.setEnabled(false);
                disableFace();
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                longestToast("Sign in using your username and password. The caretaker has been notified of your arrival.");
                dialog.dismiss();
                makeFailedLogin(name);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
                dialog.dismiss();
                //save Data in ToLogOut.
                //Write to EventsLog about Visitor Sign in.
                //Write to Login/Visitor Login
                //Write to notification paths
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Classifier", "Visitor");
                hashMap.put("Email", email);
                hashMap.put("Name", name);
                hashMap.put("Phone", phone);
                hashMap.put("Relation", relation);
                hashMap.put("Time", currentDateTimeString);
                hashMap.put("Username", username);
                hashMap.put("Description", description);
                hashMap.put("Device", "Visitor Device");
                hashMap.put("Password", password);
                DatabaseReference eventsLog = FirebaseDatabase.getInstance().getReference("Events Log");
                eventsLog.push().setValue(hashMap);

                if (nncofidence > .94 && knncofidence > .89) {
                    saveData(name, cheek, eye, mouth, nose, age, gender, hashMap);

                }

                DatabaseReference login = FirebaseDatabase.getInstance().getReference("Login/Visitor Login");
                HashMap<String, String> hashMap2 = new HashMap<>();
                hashMap2.put("Email", email);
                hashMap2.put("Name", name);
                hashMap2.put("Phone", phone);
                hashMap2.put("Relation", relation);
                hashMap2.put("Time", currentDateTimeString);
                hashMap2.put("Username", username);
                hashMap2.put("Password", password);
                hashMap2.put("Device", "Visitor Device");
                hashMap2.put("Description", description);
                login.push().setValue(hashMap2);

                DatabaseReference logouts = FirebaseDatabase.getInstance().getReference("To Log Out");
                HashMap<String, String> logoutvalues = new HashMap<>();
                logoutvalues.put("Time", currentDateTimeString);
                logoutvalues.put("Name", name);
                logoutvalues.put("Phone", phone);
                logoutvalues.put("Email", email);
                logoutvalues.put("Username", username);
                logoutvalues.put("Relation", relation);
                logouts.push().setValue(logoutvalues);

                writeNotifications(currentDateTimeString, name, relation);
                readlogOuts();
            }
        });

    }

    private void writeNotifications(String currentDateTimeString, String name, String relation) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference5 = firebaseDatabase.getReference("Latest Login");
        final DatabaseReference databaseReference8 = firebaseDatabase.getReference("RLatest Login");
        final DatabaseReference databaseReference2 = firebaseDatabase.getReference("Notify Login");
        final DatabaseReference databaseReference3 = firebaseDatabase.getReference("RNotify Login");
        databaseReference5.removeValue();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Time", currentDateTimeString);
        databaseReference5.push().setValue(hashMap);
        databaseReference8.removeValue();
        databaseReference8.push().setValue(hashMap);

        //databaseReference2.removeValue();
        databaseReference3.removeValue();
        HashMap<String, String> namers = new HashMap<>();
        namers.put("Name", name);
        namers.put("Relation", relation);
        databaseReference2.push().setValue(namers);
        databaseReference3.push().setValue(namers);

    }

    private void closeDialog(final Dialog dialog, final String name) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (clicked) {

                } else {
                    dialog.dismiss();
                    makeFailedLogin(name);
                    //Notify caretaker of failed login.
                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 17000);
    }

    private void makeFailedLogin(String name) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Events Log");
        HashMap<String, String> tt = new HashMap<>();
        tt.put("Classifier", "Failed");
        tt.put("Name", "Looked Similar to " + name);
        tt.put("Username", " ");
        tt.put("Phone", "Used face recognition");
        tt.put("Relation", " ");
        tt.put("Email", " ");
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        tt.put("Time", currentDateTimeString);
        db.push().setValue(tt);

        final DatabaseReference databaseReference101 = FirebaseDatabase.getInstance().getReference("NWrong Login");
        final DatabaseReference databaseReference102 = FirebaseDatabase.getInstance().getReference("NRWrong Login");
        final DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference("Wrong Login");
        final DatabaseReference databaseReference11 = FirebaseDatabase.getInstance().getReference("RWrong Login");

        databaseReference102.removeValue();
        databaseReference101.removeValue();
        databaseReference5.removeValue();
        databaseReference11.removeValue();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Time", currentDateTimeString);
        databaseReference101.push().setValue(hashMap);
        databaseReference102.push().setValue(hashMap);
        databaseReference11.push().setValue(hashMap);
        databaseReference5.push().setValue(hashMap);
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

    private String getBestCandidate(String[] occurences) {
        int max = Integer.MIN_VALUE;
        int position = 0;
        String possible = "";
        ArrayList<Integer> arrayList = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < occurences.length; i++) {
            String temp = occurences[i];
            String[] split = temp.split(" ");
            int size = split.length - 1;
            String name = "";
            name += split[0];
            for (int m = 1; m < size; m++) {
                name += " " + split[m];
            }
            int num = Integer.parseInt(temp.split(" ")[size]);
            if (num >= max) {
                max = num;
                position = i;
                arrayList.add(position);

                counter++;
            } else {
                continue;
            }
        }
        String t = occurences[arrayList.get(0)];
        String[] splitter = t.split(" ");
        for (int i = 0; i < splitter.length - 1; i++) {
            possible += splitter[i] + " ";
        }
        for (int i = 1; i < arrayList.size(); i++) {
            String[] temp = occurences[arrayList.get(i)].split(" ");
            String together = " OR ";
            for (int j = 0; j < temp.length - 1; j++) {
                together += temp[j] + " ";
            }
            possible += together;
        }
        return possible;
    }

    public int countOccurences(String str, String word) {
        String a[] = str.split(" ");
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            // if match found increase count
            if (word.equals(a[i]))
                count++;
        }

        return count;
    }

    private void listeners() {
        useface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, 1000);
                } else {
                    String yourPath = getApplicationContext().getFilesDir() + "/" + "names";
                    File file = new File(yourPath);
                    BufferedReader namesReader = null;
                    try {
                        namesReader = new BufferedReader(new FileReader(file));
                        String line1 = "";
                        String line2 = "";
                        String tline;
                        int mycount = 0;
                        while ((tline = namesReader.readLine()) != null) {
                            if (mycount == 0) {
                                line1 += tline;
                                mycount++;
                            } else {
                                line2 += tline;
                            }
                        }
                        String[] numarray = line2.split(",");
                        String[] names = line1.split(",");
                        if (names.length <= 1) {
                            longestToast("Unable to use facial recognition right now. Please use your username and password.");
                        } else {
                            if (numofPics == 0 || numofPics == 1) {
                                t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int status) {
                                        if (status != TextToSpeech.ERROR) {
                                            t1.setLanguage(Locale.US);
                                            t1.setSpeechRate(1.3f);
                                            t1.speak("Please make sure that you are looking directly at the camera with minimal head tilt for maximum accuracy.", TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                });
                            }
                            longestToast("Please make sure that you are looking directly at the camera with minimal head tilt for maximum accuracy.");
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, 1);
                        }
                    } catch (Exception e) {
                        longestToast(e.toString());
                    }
                }
            }
        });
        workerhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 3; i++) {
                    makeToast("This system does not store personal information about you. This is in place to ensure the safety of the resident by logging who entered the house at what time and for what reason.");
                }
            }
        });

        workersignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String worker = workername.getText().toString();
                String company = workercompany.getText().toString();
                String service = (String) spinner.getSelectedItem();
                Boolean goodworker = false;
                Boolean goodcompany = false;


                if (service.equals("What is Your Service?")) {
                    goodworker = false;
                    error.setText("Please choose the reason that best describes why you are here today.");
                } else {
                    goodworker = true;
                }
                String realcompany = "";

                int numofspacesend = 0;
                int numofspacesbegin = 0;
                for (int i = company.length() - 1; i > -1; i--) {
                    if (Character.isSpaceChar(company.charAt(i))) {
                        numofspacesend++;
                    } else {
                        break;
                    }
                }
                for (int i = 0; i < company.length() - 1; i++) {
                    if (Character.isSpaceChar(company.charAt(i))) {
                        numofspacesbegin++;
                    } else {
                        break;
                    }
                }
                realcompany = company.substring(numofspacesbegin, company.length() - numofspacesend);
                workercompany.setText(realcompany);
                workercompany.setSelection(realcompany.length());
                if (realcompany.isEmpty() || realcompany.length() < 5) {
                    error.setText("Enter your company's entire name, not an abbreviation.");
                    goodcompany = false;
                } else {
                    goodcompany = true;
                }


                if (goodworker && goodcompany) {
                    error.setText("");
                    FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
                    DatabaseReference getDatabase = databaseReference.getReference("Login/Worker Login");
                    DatabaseReference databaseReference1 = databaseReference.getReference("Events Log");
                    DatabaseReference loggingout = databaseReference.getReference("To Log Out");

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    Map<String, String> values = new HashMap<>();
                    values.put("Time", currentDateTimeString);
                    values.put("Name", worker);
                    values.put("Phone", realcompany);
                    values.put("Email", service);

                    Map<String, String> values2 = new HashMap<>();
                    values2.put("Time", currentDateTimeString);
                    values2.put("Name", worker);
                    values2.put("Phone", realcompany);
                    values2.put("Email", service);
                    values2.put("Classifier", "Worker");

                    databaseReference1.push().setValue(values2);

                    HashMap<String, String> logoutvalues = new HashMap<>();
                    logoutvalues.put("Time", currentDateTimeString);
                    logoutvalues.put("Name", worker);
                    logoutvalues.put("Phone", realcompany);
                    logoutvalues.put("Email", service);
                    logoutvalues.put("Username", " ");
                    logoutvalues.put("Relation", " ");
                    loggingout.push().setValue(logoutvalues);


                    getDatabase.push().setValue(values, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                makeToast("Successfully signed In. Thank you for your cooperation.");
                                workercompany.setText("");
                                workername.setText("");
                                spinner.setSelection(0);
                                error.setText("");
                            } else {
                                makeToast("Error with logging in.");
                            }
                        }
                    });
                }


            }
        });

        facehelp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yourFilePath = getApplicationContext().getFilesDir() + "/" + "names";
                File yourFile = new File(yourFilePath);
                try {
                    BufferedReader all = new BufferedReader(new FileReader(yourFile));
                    String tline;
                    String lines = "";
                    while ((tline = all.readLine()) != null) {
                        lines += tline + "\n";
                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage(lines);
                    builder1.setCancelable(true);

                    builder1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } catch (Exception e) {
                    longestToast("LINE 792: " + e.toString());
                    longToast("LINE 792: " + e.toString());
                }

              /*  longestToast("If you have not signed up for facial recognition when creating an account, you cannot use this service. You will have to request the caretaker to delete your account, then you have to recreate it with that option.");
                makeToast("If you have not signed up for facial recognition when creating an account, you cannot use this service. You will have to request the caretaker to delete your account, then you have to recreate it with that option.");
          */
            }
        });
        //    facehelp2.setVisibility(View.INVISIBLE);

        facehelp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yourFilePath = getApplicationContext().getFilesDir() + "/" + "faceData";
                File yourFile = new File(yourFilePath);
                try {
                    BufferedReader all = new BufferedReader(new FileReader(yourFile));
                    String tline;
                    String lines = "";
                    while ((tline = all.readLine()) != null) {
                        lines += tline + "\n";
                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage(lines);
                    builder1.setCancelable(true);

                    builder1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } catch (Exception e) {
                    longestToast("LINE 792: " + e.toString());
                    longToast("LINE 792: " + e.toString());
                }
             /*   longestToast("If you have not signed up for facial recognition when creating an account, you cannot use this service. You will have to request the caretaker to delete your account, then you have to recreate it with that option.");
                makeToast("If you have not signed up for facial recognition when creating an account, you cannot use this service. You will have to request the caretaker to delete your account, then you have to recreate it with that option.");
           */
            }
        });

       /* username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (numoftries > 2) {
                    makeToast("Login has been disabled.");
                    username.setText("");
                } else {

                }
            }
        });*/

        /*password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (numoftries > 2) {
                    makeToast("Login has been disabled.");
                    password.setText("");
                } else {

                }
            }
        });*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("/Create Account");
                final DatabaseReference databaseReference1 = firebaseDatabase.getReference("Events Log");
                final DatabaseReference databaseReference5 = firebaseDatabase.getReference("Latest Login");
                final DatabaseReference databaseReference8 = firebaseDatabase.getReference("RLatest Login");
                final DatabaseReference databaseReference2 = firebaseDatabase.getReference("Notify Login");
                final DatabaseReference databaseReference3 = firebaseDatabase.getReference("RNotify Login");

                ValueEventListener changeListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList<String> usernames;
                        final ArrayList<String> passwords;
                        final ArrayList<String> relation = new ArrayList<>();
                        final ArrayList<String> description = new ArrayList<>();
                        final ArrayList<String> phone = new ArrayList<>();
                        final ArrayList<String> email = new ArrayList<>();
                        usernames = new ArrayList<>();
                        passwords = new ArrayList<>();
                        final ArrayList<String> names = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String getusername = child.child("Username").getValue(String.class);
                            usernames.add(getusername);
                            String getpassword = child.child("Password").getValue(String.class);
                            passwords.add(getpassword);
                            names.add(child.child("Name").getValue(String.class));
                            description.add(child.child("Description").getValue(String.class));
                            relation.add(child.child("Relation").getValue(String.class));
                            phone.add(child.child("Phone").getValue(String.class));
                            email.add(child.child("Email").getValue(String.class));


                        }
                        String username1 = username.getText().toString();
                        String password1 = password.getText().toString();
                        if (numoftries < 3) {
                            if (username1.isEmpty() || password1.isEmpty()) {
                                mainerror.setText("You have not filled in both the username and password.");
                                numoftries = numoftries;
                            } else {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference dbref = database.getReference("Login/Visitor Login");

                                int j = 0;
                                String[] array = new String[usernames.size()];
                                for (int i = 0; i < usernames.size(); i++) {
                                    array[i] = usernames.get(i);
                                }

                                Boolean b = false;
                                for (int i = 0; i < usernames.size(); i++) {
                                    // makeToast(Integer.toString(j));
                                    if (array[i].equals(username1)) {
                                        b = true;
                                        break;
                                    } else {
                                        j++;
                                        b = false;
                                    }
                                }
                                if (b == false) {
                                    numoftries++;
                                    if ((3 - numoftries) == 1) {
                                        mainerror.setText("You have entered the wrong username and password. You have " + (3 - numoftries) + " try left.");
                                        reset();
                                        doit = true;
                                    } else if ((3 - numoftries) == 0) {
                                        mainerror.setText("Login has been disabled for 20 seconds due to 3 unsuccessful attempts.");
                                        numoftries++;
                                        doit = false;
                                        DatabaseReference db = firebaseDatabase.getReference("Events Log");
                                        HashMap<String, String> tt = new HashMap<>();
                                        tt.put("Classifier", "Failed");
                                        tt.put("Name", username1);
                                        tt.put("Username", " ");
                                        tt.put("Phone", " ");
                                        tt.put("Relation", " ");
                                        tt.put("Email", " ");
                                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                                        tt.put("Time", currentDateTimeString);

                                        final FirebaseDatabase firebaseDatabase10 = FirebaseDatabase.getInstance();
                                        final DatabaseReference databaseReference5 = firebaseDatabase10.getReference("Wrong Login");
                                        final DatabaseReference databaseReference101 = firebaseDatabase10.getReference("NWrong Login");
                                        final DatabaseReference databaseReference102 = firebaseDatabase10.getReference("NRWrong Login");

                                        final DatabaseReference databaseReference11 = firebaseDatabase10.getReference("RWrong Login");

                                        databaseReference5.removeValue();
                                        databaseReference102.removeValue();
                                        databaseReference101.removeValue();
                                        databaseReference11.removeValue();
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("Time", currentDateTimeString);
                                        databaseReference5.push().setValue(hashMap);
                                        databaseReference101.push().setValue(hashMap);
                                        databaseReference11.push().setValue(hashMap);
                                        databaseReference102.push().setValue(hashMap);

                                        db.push().setValue(tt);
                                        username.setEnabled(false);
                                        password.setEnabled(false);
                                        login.setEnabled(false);
                                        useface.setEnabled(false);
                                        disableLogin();
                                    } else {
                                        mainerror.setText("You have entered the wrong username and password. You have " + (3 - numoftries) + " tries left.");
                                    }
                                } else {
                                    if (!passwords.get(j).equals(password1)) {
                                        numoftries++;
                                        if ((3 - numoftries) == 1) {
                                            mainerror.setText("You have entered the wrong username and password. You have " + (3 - numoftries) + " try left.");

                                        } else if ((3 - numoftries) == 0) {
                                            mainerror.setText("Login has been disabled for 20 seconds due to 3 unsuccessful attempts.");
                                            numoftries++;
                                            username.setEnabled(false);
                                            password.setEnabled(false);
                                            login.setEnabled(false);
                                            useface.setEnabled(false);
                                            disableLogin();
                                        } else {
                                            mainerror.setText("You have entered the wrong username and password. You have " + (3 - numoftries) + " tries left.");
                                        }
                                    } else {
                                        mainerror.setText("");
                                        numoftries = 0;
                                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                                        Map<String, String> values = new HashMap<>();
                                        values.put("Time", currentDateTimeString);
                                        values.put("Password", password1);
                                        values.put("Username", username1);
                                        values.put("Name", names.get(j));
                                        values.put("Device", "Visitor Device");
                                        values.put("Description", description.get(j));
                                        values.put("Relation", relation.get(j));
                                        values.put("Phone", phone.get(j));
                                        values.put("Email", email.get(j));

                                        DatabaseReference d4 = FirebaseDatabase.getInstance().getReference("To Log Out");
                                        d4.push().setValue(values);


                                        Map<String, String> values2 = new HashMap<>();
                                        values2.put("Time", currentDateTimeString);
                                        values2.put("Password", password1);
                                        values2.put("Username", username1);
                                        values2.put("Name", names.get(j));
                                        values2.put("Device", "Visitor Device");
                                        values2.put("Description", description.get(j));
                                        values2.put("Relation", relation.get(j));
                                        values2.put("Phone", phone.get(j));
                                        values2.put("Email", email.get(j));
                                        values2.put("Classifier", "Visitor");
                                        databaseReference1.push().setValue(values2);

                                        databaseReference5.removeValue();
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("Time", currentDateTimeString);
                                        databaseReference5.push().setValue(hashMap);
                                        databaseReference8.removeValue();
                                        databaseReference8.push().setValue(hashMap);

                                        //databaseReference2.removeValue();
                                        databaseReference3.removeValue();
                                        HashMap<String, String> namers = new HashMap<>();
                                        namers.put("Name", names.get(j));
                                        namers.put("Relation", relation.get(j));
                                        databaseReference2.push().setValue(namers);
                                        databaseReference3.push().setValue(namers);

                                        dbref.push().setValue(values, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    makeToast("Logged In...");
                                                    makeAlertBuilder();
                                                } else {
                                                    makeToast("Error with logging in.");
                                                }
                                            }
                                        });
                                    }
                                }
                            }


                      /*  Query query = FirebaseDatabase.getInstance().getReference("/Login/Visitor Login").orderByChild("username");
                        query.addListenerForSingleValueEvent(valueEventListener);*/
                    /*    for (DataSnapshot child : dataSnapshot.getChildren()) {
                            makeToast(child.getKey());
                            makeToast(child.getValue(String.class));
                        }*/
                        } else {
                            makeToast("Login has been disabled.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        makeToast("Error. Possible error due to WiFi fluctuation.");
                    }
                };
                databaseReference.addValueEventListener(changeListener);




               /* Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "buttonclick");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/


                //  makeToast(Integer.toString(i));
       /*         Map<String, String> values = new HashMap<>();
                values.put("Time", currentDateTimeString);
                values.put("password", password1);
                values.put("username", username1);

                dbref.push().setValue(values, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            makeToast("Logging In...");
                            makeAlertBuilder();
                        } else {
                            makeToast("Error with logging in.");
                        }
                    }
                });
*/

                //DO DATABASE CHECKING HERE
            }
        });

        loginhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spassword = password.getText().toString();

                if (passwordshowing == false) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    loginhelp.setImageResource(R.drawable.hidepassword);
                    passwordshowing = true;
                    password.setSelection(password.getText().toString().length());
                } else if (passwordshowing == true) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    loginhelp.setImageResource(R.drawable.showpassword);
                    passwordshowing = false;
                    password.setSelection(password.getText().toString().length());
                }
            }
        });

        forgotpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                makebuilder2();
                return true;
            }
        });

        createnew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(MainActivity.this, AboutApp.class));
                return false;
            }
        });

    }

    private void reset() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (doit) {
                    numoftries = 0;
                    doit = false;
                } else {

                }
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 20000);


    }

    private void disableFace() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                faceFail = 0;
                numofPics = 0;
                didBad = false;
                useface.setEnabled(true);
                listeners();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 15000);

    }

    private void disableLogin() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                numoftries = 0;
                faceFail = 0;
                numofPics = 0;
                didBad = false;
                mainerror.setText("");
                username.setText("");
                password.setText("");
                username.setEnabled(true);
                login.setEnabled(true);
                password.setEnabled(true);
                useface.setEnabled(true);
                listeners();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 15000);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.phonemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.call) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:8482482353"));

            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        100);
            } else {
                startActivity(callIntent);
            }

        } else if (id == R.id.about) {
            makeInfoAlert();
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeInfoAlert() {
        final android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder1.setTitle("About This App!");
        builder1.setMessage("         The purpose of this app is to serve as an information system for senior citizens with disabilities and diseases, particularly Alzheimer's. This app functions by requiring visitors to create an account by entering information about their relation to the resident as well as a description of themselves in addition to other details. Once the account has been approved by the caretaker of the resident, visitors can use their account to sign in to the Visitor Device positioned near the front door of the house.\n\n          When a visitor signs in, the resident and caretaker will get notified of their arrival and they will also get the information about the visitor, helping the resident with Alzheimer's remember and know who who is visiting them. Furthermore, the caretaker can use this app to monitor the visits as well as ensure that the resident has not left the house. Essentially, this app can ensure the safety of the resident as well as inform them about who is visiting. It also gives the caretaker the ability to make sure that the visitors entering the house are safe.");
        builder1.setCancelable(true);

        builder1.setNeutralButton(
                "Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        final android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
        //  android:text="


    }

    private void makebuilder2() {
        final Dialog alert = new Dialog(this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.alertdialog_w_edittext);
        alert.setCancelable(true);
        final EditText username = alert.findViewById(R.id.dialogEditText);
        Button enter = alert.findViewById(R.id.Enter);
        Button cancel = alert.findViewById(R.id.cancel_action);
        final TextView errorhere = alert.findViewById(R.id.errorhere);

        enter.setClickable(true);
        cancel.setClickable(true);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                alert.dismiss();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String given = username.getText().toString();
                if (given.isEmpty() || given.length() < 6) {
                    errorhere.setText("**You have not entered a valid username.**");
                } else {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("/Create Account");
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> usernamelist = new ArrayList<>();
                            ArrayList<String> passwordlist = new ArrayList<>();
                            ArrayList<String> name = new ArrayList<>();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String getusername = child.child("Username").getValue(String.class);
                                usernamelist.add(getusername);
                                String getpassword = child.child("Password").getValue(String.class);
                                passwordlist.add(getpassword);
                                String getname = child.child("Name").getValue(String.class);
                                name.add(getname);
                            }
                            int val = 0;
                            boolean achieved = false;
                            for (String a : usernamelist) {
                                if (a.equals(given)) {
                                    achieved = true;
                                    break;
                                } else {
                                    val++;
                                    achieved = false;
                                }
                            }
                            if (achieved) {
                                errorhere.setText("You entered a valid username!");
                                errorhere.setTextColor(getResources().getColor(R.color.mainbar));
                                String[] TO = {"ishaanjav@gmail.com"};
                                String[] CC = {""};
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                                emailIntent.setData(Uri.parse("mailto:"));
                                emailIntent.setType("text/plain");
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Information System App: Password");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello " + name.get(val) + "!\nThis is an autonomous message generated by the Information System App through which you have just entered your username.\n\nHere is your account information: \nUsername: " + usernamelist.get(val) + "\nPassword: " + passwordlist.get(val) + "\n\nThank you for using this app!");
                                try {
                                    startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), 3);
                                    alert.dismiss();
                                    alert.hide();

                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                errorhere.setText("The username that you have entered does not exist");
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };

                    databaseReference.addValueEventListener(valueEventListener);

                }
            }
        });


        alert.show();
    }


    private void useAnalytics() {

    }

    public void makeAlertBuilder() {
        final android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder1.setTitle("Successful Sign In!");
        builder1.setMessage("Do you want to edit your account info?\n(Automatically closes in 10 seconds)");
        builder1.setCancelable(false);
        MediaPlayer ring = MediaPlayer.create(MainActivity.this, R.raw.doorbelltone);
        ring.start();
        dismiss = false;

        builder1.setPositiveButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        username.setText("");
                        password.setText("");
                        dialog.dismiss();
                        hideKeyboardFrom(getApplicationContext(), password);
                        dismiss = true;
                    }
                });

        builder1.setNegativeButton(
                "Yes, edit info.",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss = true;
                        Intent i = new Intent(MainActivity.this, EditAccount.class);
                        i.putExtra("Username", username.getText().toString());
                        startActivity(i);
                    }
                });

        final android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
        runit(alert11);


    }

    private void runit(final AlertDialog a) {
        new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                a.setMessage("Do you want to edit your account info?\n(Automatically closes in " + (millisUntilFinished / 1000) + " seconds)");
            }

            @Override
            public void onFinish() {
                if (dismiss == true) {

                } else {
                    a.cancel();
                    username.setText("");
                    hideKeyboardFrom(getApplicationContext(), password);
                    password.setText("");
                }
            }
        }.start();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void bindViews() {
        createnew = findViewById(R.id.createnew);
        forgotpassword = findViewById(R.id.forgotpassword);
        facehelp2 = findViewById(R.id.facehelp2);
        useface = findViewById(R.id.useface);
        facehelp1 = findViewById(R.id.facehelp1);

        SpannableString mySpannableString = new SpannableString(createnew.getText());
        mySpannableString.setSpan(new UnderlineSpan(), 12, mySpannableString.length(), 0);

        mySpannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 12, mySpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableString = new SpannableString(forgotpassword.getText());
        spannableString.setSpan(new UnderlineSpan(), 21, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 21, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotpassword.setText(spannableString);

        createnew.setText(mySpannableString);
        login = findViewById(R.id.signin);
        username = findViewById(R.id.useredit);
        password = findViewById(R.id.passedit);
        container = findViewById(R.id.container);
        workersignin = findViewById(R.id.workersignin);
        workerhelp = findViewById(R.id.workerhelp);
        loginhelp = findViewById(R.id.loginhelp);
        workername = findViewById(R.id.workername);
        error = findViewById(R.id.errotext2);
        mainerror = findViewById(R.id.errortext);
        workercompany = findViewById(R.id.workercompany);
        spinner = findViewById(R.id.service);
        bar = findViewById(R.id.bar);

    }

    public void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void longestToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

}
