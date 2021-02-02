package com.example.michal.app100;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Registration extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //set the logo and the tool bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //initialized connector
        final Connector connectorR = new Connector();
        firebaseAuth = FirebaseAuth.getInstance();
        // create and set virables to User
        final EditText userId = (EditText) findViewById(R.id.IDtext);
        final EditText userFname = (EditText) findViewById(R.id.Fname);
        final EditText userLname = (EditText) findViewById(R.id.Lname);
        final EditText userPnumber = (EditText) findViewById(R.id.Pnumber);
        final EditText emailR = (EditText) findViewById(R.id.emailR);
        final EditText passwordR = (EditText) findViewById(R.id.passwordR);
        final Button button = (Button) findViewById(R.id.bRegister);

            // on click we create User instance and push him to database
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(userId.getText().toString()) || TextUtils.isEmpty(userFname.getText().toString()) || TextUtils.isEmpty(userLname.getText().toString())
                            || TextUtils.isEmpty(userPnumber.getText().toString()) || TextUtils.isEmpty(emailR.getText().toString()) || TextUtils.isEmpty(passwordR.getText().toString())) {
                        Toast.makeText(Registration.this, "נא למלא את כל שדות הרישום", Toast.LENGTH_LONG).show();
                    }else{
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Registration.this);
                    a_builder.setMessage(R.string.alertMessage).
                            setCancelable(false).setPositiveButton("מאשר/ת", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //create the user and send him to DB
                            final String userId1 = userId.getText().toString().trim();
                            final String userFname1 = userFname.getText().toString().trim();
                            final String userLname1 = userLname.getText().toString().trim();
                            final long userPnumber1 = Long.parseLong(userPnumber.getText().toString().trim());
                            final String userMail = emailR.getText().toString().trim();
                            final String userPassword = passwordR.getText().toString().trim();

                            if (!TextUtils.isEmpty(userMail) && !TextUtils.isEmpty(userPassword) && !TextUtils.isEmpty(userId1)) {

                                //create new user
                                // what if he is exsist
                                //
                                //write to db
                                // connectorR.databaseReferenceUsers.push().setValue(user1);
                                final ProgressDialog progressDialog = ProgressDialog.show(Registration.this, "אנא המתן", "מבצע הרשמה", true);
                                firebaseAuth.createUserWithEmailAndPassword(userMail, userPassword)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    String user_id = firebaseAuth.getCurrentUser().getUid();
                                                    DatabaseReference current_user_db = connectorR.databaseReferenceUsers.child(user_id);
                                                    User user1 = new User(userId1, userFname1, userLname1, userPnumber1, userMail, userPassword);
                                                    //current_user_db.push().setValue(user1);
                                                    current_user_db.setValue(user1);

                                                    Toast.makeText(Registration.this, "הרישום בוצע בהצלחה", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(Registration.this, MainActivity.class);
                                                    i.putExtra("Email", userFname1);

                                                    startActivity(i);
                                                } else {
                                                    Log.e("ERROR", task.getException().toString());
                                                    Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        }
                    })
                            .setNegativeButton("ביטול/תיקון", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert = a_builder.create();
                    alert.setTitle("משטרת ישראל - אימות נתונים");
                    alert.show();
                }}
            });


        }
    }


