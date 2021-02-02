package com.example.michal.app100;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class Login extends Activity {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private Button newUserButton;
    private FirebaseAuth firebaseAuth;
    private Connector connectorLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        loginButton = (Button)findViewById(R.id.loginButton);
        newUserButton = (Button)findViewById(R.id.newUserButton);
        firebaseAuth = FirebaseAuth.getInstance();
        connectorLogin = new Connector();

    }
    //on click registration button -- go to reg form
    public void newUserRegistraion_Click(View v){
        Intent i = new Intent(Login.this , Registration.class);
        startActivity(i);
    }
    //on click sign in button -- chack if exists
    public void loginButton_Click (View v){
        checkLogIn();

    }

    private void checkLogIn() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            final ProgressDialog progressDialog = ProgressDialog.show(Login.this,"אנא המתן","מתחבר לשירות",true);
            (firebaseAuth.signInWithEmailAndPassword(loginEmail.getText().toString(),loginPassword.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()){
                                checkUserExist();

                            }
                            else {
                                Log.e("ERROR",task.getException().toString());
                                Toast.makeText(Login.this,task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void checkUserExist() {
        final String userId = firebaseAuth.getCurrentUser().getUid();
        connectorLogin.databaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userId)){
                    Toast.makeText(Login.this,"מחובר כעת" ,Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Login.this,MainActivity.class);
                    //TO DO - get user's name
                    i.putExtra("Email",firebaseAuth.getCurrentUser().getEmail());
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(Login.this,"אינך קיים במערכת, בצע הרשמה!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
