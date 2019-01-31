package com.example.anany.informationsystemv1;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EditAdapter extends ArrayAdapter<AccountStorer> {

    private Context mContext;
    Activity activity;
    private List<AccountStorer> accountsList = new ArrayList<>();
    String rowid = "";
    StorageReference mImageRef;

    public EditAdapter(Activity a, Context context, ArrayList<AccountStorer> list) {
        super(context, 0, list);
        mContext = context;
        activity = a;
        accountsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.listviewitem, parent, false);





        return listItem;
    }

    private void makeToast(String S) {
        Toast.makeText(activity.getApplicationContext(), S, Toast.LENGTH_SHORT).show();
    }

}
