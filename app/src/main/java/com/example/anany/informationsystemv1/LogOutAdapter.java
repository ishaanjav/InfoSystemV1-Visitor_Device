package com.example.anany.informationsystemv1;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LogOutAdapter extends ArrayAdapter<WorkerLogOut> {
    private Context mContext;
    private List<WorkerLogOut> list;
    String rowid = "";
    Activity activity;
    CardView cardView;


    public LogOutAdapter(Activity a, Context context, ArrayList<WorkerLogOut> list) {
        super(context, 0, list);
        mContext = context;
        this.list = list;
        activity = a;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.log_out_list, parent, false);
        TextView name = listItem.findViewById(R.id.name);
        TextView time = listItem.findViewById(R.id.time);
        Button logout = listItem.findViewById(R.id.logout);
        final WorkerLogOut row = list.get(position);

        String[] indi = row.getTime().split(" ");
        name.setText(row.getName());
        time.setText("Entered at " + indi[3] + " " + indi[4]);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("To Log Out");
                final String currentName = row.getName();
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String temp = child.child("Name").getValue().toString();
                            if (temp.equals(currentName)) {
                                rowid = child.getKey();
                                String path = "To Log Out/" + rowid;
                                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Events Log");
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Time", currentDateTimeString);
                                hashMap.put("Name", currentName);
                                hashMap.put("Phone", row.getPhone());
                                hashMap.put("Email", row.getEmail());
                                hashMap.put("Username", row.getUsername());
                                hashMap.put("Relation", row.getRelation());
                                if (row.getEmail().contains(".") || row.getEmail().contains("@") || row.getEmail().isEmpty()) {
                                    hashMap.put("Classifier", "Visitor Logout");
                                } else {
                                    hashMap.put("Classifier", "Worker Logout");
                                }
                                dbref.push().setValue(hashMap);
                                DatabaseReference delete = FirebaseDatabase.getInstance().getReference(path);
                                delete.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        makeToast("You have been successfully logged out.");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        longToast("Error logging you out: " + e.getMessage() + " Cause: " + e.getCause());
                                    }
                                });

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
            }
        });


        return listItem;
    }


    private void makeToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    private void longToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

}
