package com.example.anany.informationsystemv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

class CreateAccount extends AppCompatActivity {

    Button nextpage;
    EditText fullname;
    EditText username;
    EditText password;
    EditText passwordconfirm;
    EditText phonenumber;
    EditText email;
    TextView error;
    ImageView phonehelp;
    ImageView emailhelp;
    int boolcheck = 0;
    ImageView strengthhelp;
    Button previouspage;
    Boolean allgood = false;
    Boolean secondcheck;

    CheckBox mesemail;
    String phonestate;
    CheckBox mesphone;
    RelativeLayout rl;
    ImageView passwordhelp, usernamehelp;

    boolean sendemail = false;
    Boolean represent;
    ImageView showpassword;
    ImageView showconfirm;
    String userpassword = "";
    String usernamelimit = "";
    String phonelimit = "";
    String confirmlimit = "";
    Boolean passwordshowing = false;
    Boolean confirmshowing = false;
    String usernamespace = "";
    boolean sendphone = false;
    Intent receivIntent;
    public String addres, relations, paragraphs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        //COMMENT OUT THE BELOw. IT WAS DONE TO MAKE TESTING STUFF FASTER!!!
      //  startActivity(new Intent(CreateAccount.this, CreateAccount2.class));
        /////////////////////////////////////////////////////////////////////////////
        bindViews();
        mesphone.setChecked(false);
        mesemail.setChecked(false);
        receiveIntent();
        listeners();

    }

    public void sendingIntent(String ad, String re, String de, boolean sendemail, boolean sendphone, String phonenumber, String email, String name, String username, String password) {
        Intent i = new Intent(CreateAccount.this, CreateAccount2.class);
        receivIntent = getIntent();
        i.putExtra("Boolean_Send_Email", sendemail);
        i.putExtra("Boolean_Send_Phone", sendphone);
        i.putExtra("Phone_Number", phonenumber);
        i.putExtra("Email", email);
        i.putExtra("Name", name);
        i.putExtra("Username", username);
        i.putExtra("Password", password);
        i.putExtra("Address", ad);
        i.putExtra("Relation", re);
        i.putExtra("Description", de);

        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateAccount.this, MainActivity.class));
    }

    private void receiveIntent() {
        receivIntent = getIntent();
        String phonenumbers = receivIntent.getStringExtra("Phone_Number");
        String emails = receivIntent.getStringExtra("Email");
        String passwords = receivIntent.getStringExtra("Password");
        String fullnames = receivIntent.getStringExtra("Name");
        String usernames = receivIntent.getStringExtra("Username");
        Boolean sendphones = receivIntent.getBooleanExtra("Boolean_Send_Phone", false);
        Boolean sendemails = receivIntent.getBooleanExtra("Boolean_Send_Email", false);

        String pt = receivIntent.getStringExtra("Address");
        String mt = receivIntent.getStringExtra("Relation");
        String dt = receivIntent.getStringExtra("Description");

        addres = pt;
        relations = mt;
        paragraphs = dt;
        phonenumber.setText(phonenumbers);
        email.setText(emails);
        password.setText(passwords);
        fullname.setText(fullnames);
        username.setText(usernames);
        mesphone.setChecked(sendphones);
        mesemail.setChecked(sendemails);
        passwordconfirm.setText(passwords);


    }

    private void listeners() {
        strengthhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 4; i++) {
                    makeToast("The strength of your password is calculated based on the number of lowercase and upercase letters, digits, and other special characters in your password.");
                }
            }
        });

        mesemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox) view).isChecked()) {
                    sendemail = true;
                    makeToast("Your info will be sent to this email.");
                } else {
                    sendemail = false;
                }

            }
        });

        mesphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    sendphone = true;
                    makeToast("Your info will be sent to this phone number.");

                } else {
                    sendphone = false;
                }
            }
        });

        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spassword = password.getText().toString();

                if (passwordshowing == false) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showpassword.setImageResource(R.drawable.hidepassword);
                    passwordshowing = true;
                    password.setSelection(password.getText().toString().length());
                } else if (passwordshowing == true) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showpassword.setImageResource(R.drawable.showpassword);
                    passwordshowing = false;
                    password.setSelection(password.getText().toString().length());
                }

            }
        });

        showconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmshowing == false) {
                    passwordconfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showconfirm.setImageResource(R.drawable.hidepassword);
                    confirmshowing = true;
                    passwordconfirm.setSelection(passwordconfirm.getText().toString().length());
                } else if (confirmshowing == true) {
                    passwordconfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showconfirm.setImageResource(R.drawable.showpassword);
                    confirmshowing = false;
                    passwordconfirm.setSelection(passwordconfirm.getText().toString().length());
                }
            }
        });

        phonenumber.addTextChangedListener(new TextWatcher() {
            private int previousLength;
            private boolean backSpace;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousLength = phonenumber.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (phonenumber.getText().length() > 18) {
                    String s = phonenumber.getText().toString();
                    phonenumber.setText(s.substring(0, 18));
                    phonenumber.setSelection(phonenumber.getText().length());
                    makeToast("Phone number can't be more than 10 digits long");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int s = phonenumber.getText().length();
                backSpace = previousLength > s;

                if (backSpace) {
                    int currentLengt = phonenumber.getText().length();
                    String current = phonenumber.getText().toString();
                    if (currentLengt != 7 && currentLengt != 8 && currentLengt != 14 && currentLengt != 13 && currentLengt != 12 && currentLengt != 1) {

                    } else if (currentLengt == 1) {
                        phonenumber.setText("");
                    } else if (currentLengt == 8 || currentLengt == 7) {
                        phonenumber.setText(current.substring(0, 5));
                        phonenumber.setSelection(phonenumber.getText().length());
                    } else if (currentLengt == 14 || currentLengt == 13 || currentLengt == 12) {
                        String hello = phonenumber.getText().toString();
                        String hi = hello.substring(0, 11);

                        phonenumber.setText(hi);
                        phonenumber.setSelection(phonenumber.getText().length());
                    }
                } else {
                    int currentLength = phonenumber.getText().length();
                    String t = phonenumber.getText().toString();
                    if (currentLength == 1) {
                        phonenumber.setText("(" + phonenumber.getText());
                        phonenumber.setSelection(phonenumber.getText().length());

                    } else if (currentLength == 4) {
                        phonenumber.setText(phonenumber.getText() + ") - ");
                        phonenumber.setSelection(phonenumber.getText().length());

                    } else if (currentLength == 5) {
                        phonenumber.setText(t.substring(0, 4) + ") - " + t.substring(4, 5));
                        phonenumber.setSelection(phonenumber.getText().length());
                    } else if (currentLength == 6) {
                        phonenumber.setText(t.substring(0, 5) + " - " + t.substring(5, 6));
                        phonenumber.setSelection(phonenumber.getText().length());
                    } else if (currentLength == 11) {
                        phonenumber.setText(phonenumber.getText() + " - ");
                        phonenumber.setSelection(phonenumber.getText().length());
                    } else if (currentLength == 12) {
                        phonenumber.setText(t.substring(0, 11) + " - " + t.substring(11, 12));
                        phonenumber.setSelection(phonenumber.getText().length());
                    }

                }

            }
        });


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String susername = username.getText().toString();

                if (susername.contains(" ")) {
                    usernamespace = susername.substring(0, susername.length() - 1);
                    error.setText("No spaces allowed in username.");
                    username.setText(usernamespace);
                    username.setSelection(username.getText().length());
                }

                if (susername.length() == 15) {
                    usernamelimit = susername;
                }

                if (susername.length() > 15) {
                    for (int mb = 0; mb < 2; mb++) {
                        makeToast("Your username cannot be more than 15 characters long.");
                    }

                    username.setText(usernamelimit);
                    username.setSelection(username.getText().length());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordconfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String confirm = passwordconfirm.getText().toString();
                if (confirm.length() == 15) {
                    confirmlimit = confirm;
                }

                if (confirm.length() > 15) {
                    for (int mb = 0; mb < 2; mb++) {
                        makeToast("Your password can't be this long. Must be less than 15 characters.");
                    }

                    passwordconfirm.setText(confirmlimit);
                    passwordconfirm.setSelection(passwordconfirm.getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String spassword = password.getText().toString();
                int[] charactercount = new int[3];
                charactercount[0] = 0;
                charactercount[2] = 0;
                charactercount[1] = 0;
                int others = 0;
                //regular letter, capital letter, number, other
                if (spassword.length() == 15) {
                    userpassword = spassword;
                }

                if (spassword.length() > 15) {
                    for (int mb = 0; mb < 2; mb++) {
                        makeToast("Your password can't be this long. Must be less than 15 characters.");
                    }

                    password.setText(userpassword);
                    password.setSelection(password.getText().length());
                }

                for (int t = 0; t < spassword.length(); t++) {
                    char a = spassword.charAt(t);
                    if (Character.isLowerCase(a)) {
                        charactercount[0]++;
                    } else if (Character.isLetter(a)) {
                        charactercount[1]++;
                    } else if (Character.isDigit(a)) {
                        charactercount[2]++;
                    } else {
                        others++;
                    }
                }

                int let = 0;
                int cap = 0;
                int num = 0;
                int ot = 0;
                if (charactercount[0] == 0 || charactercount[0] == 1) {
                    let = 0;
                } else if (charactercount[0] == 2) {
                    let = 2;
                } else if (charactercount[0] == 3) {
                    let = 3;
                } else {
                    let = 4;
                }

                if (charactercount[1] == 0) {
                    cap = 0;
                } else if (charactercount[1] == 1) {
                    cap = 2;
                } else {
                    cap = 3;
                }

                if (others == 0) {
                    ot = 0;
                } else if (others == 1) {
                    ot = 2;
                } else if (others == 2) {
                    ot = 4;
                } else {
                    ot = 5;
                }

                if (charactercount[2] == 0) {
                    num = 0;
                } else if (charactercount[2] == 1) {
                    num = 1;
                } else if (charactercount[2] == 2) {
                    num = 2;
                } else {
                    num = 3;
                }

                int sum = num + ot + cap + let;
                String s = Integer.toString(sum);
                if (spassword.length() == 0) {
                    rl.setBackgroundColor(getResources().getColor(R.color.fade));
                } else if (sum < 4) {
                    rl.setBackgroundColor(getResources().getColor(R.color.darkred));
                } else if (sum == 4 || sum == 5) {
                    rl.setBackgroundColor(getResources().getColor(R.color.low1));
                } else if (sum == 6 || sum == 7) {
                    rl.setBackgroundColor(getResources().getColor(R.color.low2));
                } else if (sum == 8 || sum == 9) {
                    rl.setBackgroundColor(getResources().getColor(R.color.low3));

                } else if (sum == 10 || sum == 11) {
                    rl.setBackgroundColor(getResources().getColor(R.color.low4));

                } else {
                    rl.setBackgroundColor(getResources().getColor(R.color.lo25));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nextpage.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                //DO THE CHECKING CRITERION. AND ALL THE CONDITIONS
                final String name = fullname.getText().toString();
                final String susernames = username.getText().toString();
                password = findViewById(R.id.passwordedit);
                passwordconfirm = findViewById(R.id.confirmedit);
                final String spassword = password.getText().toString();
                String sconfirm = passwordconfirm.getText().toString();
                boolean emailclear = false;
                boolean phoneclear = false;
                boolean nameclear = false;
                boolean passwordclear = false;
                boolean sendingclear = false;
                boolean usernameclear = false;
                String phoneno = "";
                final String semail = email.getText().toString();
                final String phone = phonenumber.getText().toString();
                String[] realphone = phone.split("[() -]");
                for (String s : realphone) {
                    phoneno += s;
                }
                final String finalphone = phoneno;

                boolean passwordsmatch = false;


                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("/Create Account");

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> userlist = new ArrayList<>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String getusername = child.child("Username").getValue(String.class);
                            userlist.add(getusername);

                        }
                        for (String r : userlist) {
                            if (susernames.equals(r)) {
                                error.setText("An account with this username already exists. Choose another username.");
                                allgood = false;
                                //     setRepresent(false);
                                boolcheck++;
                                break;
                            } else {
                                allgood = true;
                                boolcheck++;


                                continue;
                            }
                        }
                        if (allgood) {
                            error.setText("");
                            FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference2 = firebaseDatabase2.getReference("/Create Account");

                            final String realname = fullname.getText().toString();

                            ValueEventListener valueEventListener1 = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<String> namelist = new ArrayList<>();
                                    Boolean allgood2 = false;
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        String getusername = child.child("Name").getValue(String.class);
                                        namelist.add(getusername);

                                    }
                                    for (String r : namelist) {
                                        if (realname.equals(r)) {
                                            error.setText("An account with this name already exists. If you already have an account and have forgotten your password, go back to the Login Page and tap on \"Forgot my password\". Or, just try reentering your name here.");
                                            allgood2 = false;
                                            break;
                                        } else {
                                            allgood2 = true;
                                            continue;
                                        }
                                    }
                                    if (allgood2) {
                                        error.setText("");
                                        final String name = fullname.getText().toString();
                                        final String susername = username.getText().toString();
                                        password = findViewById(R.id.passwordedit);
                                        passwordconfirm = findViewById(R.id.confirmedit);
                                        final String spassword = password.getText().toString();
                                        String sconfirm = passwordconfirm.getText().toString();
                                        boolean emailclear = false;
                                        boolean phoneclear = false;
                                        boolean nameclear = false;
                                        boolean passwordclear = false;
                                        boolean sendingclear = false;
                                        boolean usernameclear = false;
                                        String phoneno = "";
                                        final String semail = email.getText().toString();
                                        final String phone = phonenumber.getText().toString();
                                        String[] realphone = phone.split("[() -]");
                                        for (String s : realphone) {
                                            phoneno += s;
                                        }
                                        final String finalphone = phoneno;

                                        boolean passwordsmatch = false;

                                        if (susername == null) {
                                            error.setText("You have not entered a username.");
                                        } else if (susername.contains(" ")) {
                                            error.setText("You cannot have spaces in your username.");
                                        } else if (susername.length() < 6) {
                                            error.setText("Your username is too short");
                                        } else if (susername.length() > 15) {
                                            error.setText("Your username is too long");
                                        } else {
                                            usernameclear = true;
                                        }


                                        String[] split = spassword.split(" ");
                                        boolean space = false;
                                        if (split.length > 1) {
                                            space = true;
                                        } else {
                                            space = false;
                                        }
                                        if (spassword == null || sconfirm == null) {
                                            error.setText("One of your passwords is empty.");
                                        } else if (!spassword.equals(sconfirm)) {
                                            error.setText("Your passwords do not match.");
                                        } else if (spassword.length() < 6 || sconfirm.length() < 6) {
                                            error.setText("Your passwords must be at least 6 characters long.");
                                        } else if ((spassword.length() > 15 || sconfirm.length() > 15)) {
                                            error.setText("Your passwords must be at most 15 characters long.");
                                        } else if (space == true) {
                                            error.setText("Your password contains more than one word. No spaces allowed.");
                                        } else {
                                            passwordclear = true;
                                        }

                                        String[] namesplit = name.split(" ");
                                        if (name.isEmpty()) {
                                            error.setText("You have not given your name!");
                                        } else if (namesplit.length == 1) {
                                            error.setText("You have not entered your full name (First and Last name).");
                                        } else {
                                            nameclear = true;
                                        }

                                        int mt = 0;

                                        if (phone.isEmpty() && semail.isEmpty()) {
                                            error.setText("You must enter your email address or your phone number");
                                            mt = 0;
                                        } else if (phone.isEmpty() && !semail.isEmpty()) {
                                            mt = 1;
                                        } else if (!phone.isEmpty() && semail.isEmpty()) {
                                            mt = 2;
                                        } else {
                                            mt = 4;
                                        }

                                        if (sendemail == true && sendphone == true && (phone.isEmpty() || semail.isEmpty())) {
                                            error.setText("You have chosen to send your account info to your phone number and email, but you have not provided them.");
                                            sendingclear = false;
                                        } else if (sendemail == false && sendphone == true && phone.isEmpty()) {
                                            error.setText("Cannot send your info to your phone number if you don't provide it.");
                                            sendingclear = false;
                                        } else if (sendphone == false && sendemail == true && semail.isEmpty()) {
                                            error.setText("Cannot send your info to your email if you don't provide it.");
                                            sendingclear = false;
                                        } else {
                                            sendingclear = true;
                                        }

                                        if (mt == 4 || mt == 1) {
                                            if (semail.contains("@") && semail.contains(".com") && semail.length() > 5 && (semail.indexOf("@") < semail.indexOf(".com")) && ((semail.indexOf(".com") - semail.indexOf("@")) > 3)) {
                                                emailclear = true;
                                            } else {
                                                emailclear = false;
                                                error.setText("The email that you have entered is invalid. Make sure that you have '@' and '.com' in it.");
                                            }
                                        } else if (mt == 2) {
                                            emailclear = true;
                                        }


                                        if (mt == 4 || mt == 2) {

                                            if (phoneno.length() != 10) {
                                                phoneclear = false;
                                                error.setText("Your phone number is not 10 digits long.");
                                            } else {
                                                phoneclear = true;
                                            }
                                        } else if (mt == 1) {
                                            phoneclear = true;
                                            setPhone(phoneno);
                                        }

                                        //Boolean ed = getRepresent();
                                        if (emailclear && nameclear && passwordclear && phoneclear && usernameclear && sendingclear) {
                                            error.setText("");

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateAccount.this);
                                            builder1.setMessage("Are you sure everything you have entered is accurate?");
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "No, I want to check.",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                            boolcheck = 0;
                                                            /*   allgood = true;*/
                                                        }
                                                    });

                                            builder1.setNegativeButton(
                                                    "Yes, I am sure.",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            sendingIntent(addres, relations, paragraphs, sendemail, sendphone, finalphone, semail, name, susername, spassword);
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();
                                        }

                                    } else {

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            databaseReference2.addValueEventListener(valueEventListener1);


                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        error.setText("Unknown error. Please try reentering your username as it may help.");
                    }
                };
                databaseReference.addValueEventListener(valueEventListener);
            }
        });

        usernamehelp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 2; i++) {
                    makeToast("Your username should be at least 6 characters long.");
                }
            }
        });

        passwordhelp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 3; i++) {
                    makeToast("Your password should be at least 6 characters long. Refer to the Password Strength Bar to view your password's uniqueness.");
                }
            }
        });

        previouspage.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                String name = fullname.getText().toString();
                String susername = username.getText().toString();
                password = findViewById(R.id.passwordedit);
                passwordconfirm = findViewById(R.id.confirmedit);
                String spassword = password.getText().toString();
                String sconfirm = passwordconfirm.getText().toString();
                String semail = email.getText().toString();
                String phone = phonenumber.getText().toString();

                if (name.isEmpty() && susername.isEmpty() && spassword.isEmpty() && sconfirm.isEmpty() && semail.isEmpty() && phone.isEmpty()) {
                    startActivity(new Intent(CreateAccount.this, MainActivity.class));
                } else {
                    makeAlertBuilder();

                }
            }

        });

        phonehelp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 2; i++) {
                    makeToast("Make sure that you enter either your email address, phone number, or both. You must enter at least one of them.");
                }
            }
        });

        emailhelp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 2; i++) {
                    makeToast("Make sure that you enter either your email address, phone number, or both. You must enter at least one of them.");
                }
            }
        });


    }


    private void bindViews() {
        nextpage = findViewById(R.id.nextpage);
        phonehelp = findViewById(R.id.phonehelp);
        emailhelp = findViewById(R.id.emailhelp);
        fullname = findViewById(R.id.nameedit);
        username = findViewById(R.id.usernameedit);
        password = findViewById(R.id.passwordedit);
        passwordconfirm = findViewById(R.id.confirmedit);
        phonenumber = findViewById(R.id.phoneedit);
        rl = findViewById(R.id.rl);
        email = findViewById(R.id.emailedit);
        error = findViewById(R.id.errortext);
        previouspage = findViewById(R.id.previouspage);
        strengthhelp = findViewById(R.id.strengthhelp);
        mesemail = findViewById(R.id.emailcheckbox);
        showconfirm = findViewById(R.id.showconfirm);
        showpassword = findViewById(R.id.showpassword);
        passwordhelp = findViewById(R.id.passwordhelp);
        usernamehelp = findViewById(R.id.usernamehelp);
        mesphone = findViewById(R.id.phonecheck);
    }

    /*public void waitandgoback() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Going to previous page due to inactivity.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateAccount.this, MainActivity.class));
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 25000);
    }

    public void notemptywaiting() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Going to Login Page in 7 seconds due to inactivity.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateAccount.this, MainActivity.class));

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 25000);
    }*/


    public void makeToast(String s) {
        Toast t = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        t.show();
    }

    public String getPhone() {
        return phonestate;
    }

    public void setPhone(String a) {
        phonestate = a;
    }

    public void checkusername() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("/Create Account");
        final String s = username.getText().toString();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> userlist = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String getusername = child.child("Username").getValue(String.class);
                    userlist.add(getusername);

                }
                for (String r : userlist) {
                    if (s.equals(r)) {
                        error.setText("An account with this username already exists. Choose another username.");
                        allgood = false;
                        represent = false;
                        break;
                    } else {
                        allgood = true;
                        continue;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                error.setText("Unknown error. Please try reentering your username as it may help.");
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
        makeToast(Boolean.toString(allgood));
        if (allgood) {
            represent = true;
        } else {
            represent = false;
        }

    }

    public void makeAlertBuilder() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateAccount.this);
        builder1.setMessage("Are you sure you want to go back? Your info will not be saved.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Stay here",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder1.setNegativeButton(
                "Go Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(CreateAccount.this, MainActivity.class));
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public Boolean getRepresent() {
        return represent;
    }

    public void setRepresent(Boolean b) {
        represent = b;
    }

}
