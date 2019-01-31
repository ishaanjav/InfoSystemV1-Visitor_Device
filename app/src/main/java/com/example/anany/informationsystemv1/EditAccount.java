package com.example.anany.informationsystemv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EditAccount extends AppCompatActivity {

    ListView listView;
    Button save;
    String username;
    String name, password, email, phone, description, relation;
    ArrayList<AccountStorer> accountStorer;
    TextView approvedtime, createdtime;
    EditText sphone, semail, sdescription, srelation, sname, spassword, susername;

    String rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);
        //   listView = findViewById(R.id.listView);
        Intent i = getIntent();
        username = i.getStringExtra("Username");
        makeToast(username);
        bindViews();
        readInfo();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeToast("Saving...");
                listeners();
            }
        });
    }

    private void listeners() {
        //Delete from Create Account. Add back to Create Account.
        // Write to Events Log


        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database2.getReference("Create Account");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String nametemp = child.child("Name").getValue(String.class);
                    String usernametemp = child.child("Username").getValue(String.class);
                    if (usernametemp.equals(username)) {
                     //   rowId = child.getKey();


                        //  DatabaseReference dbref = firebaseDatabase.getReference("Create Account");

                       /* String approvedTime = child.child("Approved").getValue(String.class);
                        String createdtime = child.child("Created").getValue(String.class);
                        makeToast(approvedTime + createdtime);*/






                        /*Approved:
                        "Oct 21, 2018 9:20:25 PM"
                        Created:
                        "Oct 21, 2018 9:19:19 PM"
                        Description:
                        "Imma yo friend from though Schopenhauer. N"
                        Email:
                        ""
                        Name:
                        "Newbue Hekk"
                        Password:
                        "hfjjjj"
                        Phone:
                        "5787875566"
                        Relation:
                        "Friend"
                        Username:
                        "fhfjfj"*/


                        break;
                    } else {
                        continue;
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbref = firebaseDatabase.getReference("Create Account");

        DatabaseReference databaseReference2 = firebaseDatabase.getReference("Events Log");

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        HashMap<String, String> values2 = new HashMap<>();
        values2.put("Name", sname.getText().toString());
        values2.put("Username", susername.getText().toString());
        values2.put("Password", spassword.getText().toString());
        values2.put("Phone", sphone.getText().toString());
        values2.put("Email", semail.getText().toString());
        values2.put("Relation", srelation.getText().toString());
        values2.put("Description", sdescription.getText().toString());
        values2.put("Created", createdtime.getText().toString());
        values2.put("Time", currentDateTimeString);
        values2.put("Classifier", "Updated");
        databaseReference2.push().setValue(values2);

        String path = "Create Account/" + rowId;
       // makeToast(path);
        DatabaseReference databaseReference1 = firebaseDatabase.getReference(path);
        databaseReference1.removeValue();


        HashMap<String, String> values = new HashMap<>();
        values.put("Name", sname.getText().toString());
        values.put("Username", susername.getText().toString());
        values.put("Password", spassword.getText().toString());
        values.put("Phone", sphone.getText().toString());
        values.put("Email", semail.getText().toString());
        values.put("Relation", srelation.getText().toString());
        values.put("Description", sdescription.getText().toString());
        values.put("Created", createdtime.getText().toString());
        values.put("Approved", approvedtime.getText().toString());

        dbref.push().setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                makeToast("IT WORKED");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast(e.getMessage());
            }
        });

        startActivity(new Intent(EditAccount.this, MainActivity.class));
    }


    private void bindViews() {
        spassword = findViewById(R.id.passwordedit);
        susername = findViewById(R.id.usernameedit);
        sphone = findViewById(R.id.phoneedit);
        semail = findViewById(R.id.emailedit);
        sdescription = findViewById(R.id.descriptionedit);
        srelation = findViewById(R.id.relationedit);
        sname = findViewById(R.id.nameedit);
        save = findViewById(R.id.save);
        approvedtime = findViewById(R.id.approvedtime);
        createdtime = findViewById(R.id.createdtime);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.save) {
            makeToast("Saving...");
            listeners();
        }

        return super.onOptionsItemSelected(item);
    }

    private void readInfo() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Create Account");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accountStorer = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String user = child.child("Username").getValue(String.class);
                    if (user.equals(username)) {
                        String password = child.child("Password").getValue(String.class);
                        String name = child.child("Name").getValue(String.class);
                        String phone = child.child("Phone").getValue(String.class);
                        String email = child.child("Email").getValue(String.class);
                        String relation = child.child("Relation").getValue(String.class);
                        String description = child.child("Description").getValue(String.class);
                        String atime = child.child("Approved").getValue(String.class);
                        String ctime = child.child("Created").getValue(String.class);
                        rowId = child.getKey();

                        //   makeToast("FOUND MATCH");
                        sname.setText(name);
                        spassword.setText(password);
                        semail.setText(email);
                        srelation.setText(relation);
                        susername.setText(user);
                        approvedtime.setText(atime);
                        createdtime.setText(ctime);
                        sdescription.setText(description);
                        sphone.setText(phone);

                        accountStorer.add(new AccountStorer("Hello", password, email, description, phone, relation, name));

                       /* ArrayAdapter mAdapter = new EditAdapter(EditAccount.this, getApplicationContext(), accountStorer);
                        listView.setAdapter(mAdapter);*/

                        break;
                    } else {
                        //makeToast("ERROR");
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);
    }

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

}
