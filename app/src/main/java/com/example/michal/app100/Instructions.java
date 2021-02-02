package com.example.michal.app100;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Instructions extends Activity {

    private TextView reportNum,test;
    private ImageView insrImage;
    private Connector connectorInst;
    private Button finishButton;
    private long counter;
    Gvariable typeEvent = Gvariable.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        //test = (TextView) findViewById(R.id.textView7);
        //test.setText();

        reportNum = (TextView) findViewById(R.id.textView2);
        insrImage = (ImageView)findViewById(R.id.imageView);
        finishButton = (Button)findViewById(R.id.finishButton);
        connectorInst = new Connector();
        //change to all categories
        connectorInst.databaseReferenceReport.child("Car").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = dataSnapshot.getChildrenCount();
                long numberReport= counter;
                reportNum.setText(Long.toString(numberReport));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Instructions.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        setImageInst();
    }

    private void setImageInst() {
        String typeInst= typeEvent.getEvent();
        if(typeInst.equals("רכב חונה באיסור")){
            insrImage.setImageResource(R.drawable.honebeisor);
        }
        else if(typeInst.equals("רכב חוסם")){
            insrImage.setImageResource(R.drawable.hosem1);
        }
        else if(typeInst.equals("רכב נטוש / שרוף")){
            insrImage.setImageResource(R.drawable.nisraf);
        }
        else if(typeInst.equals("רכב מפריע לתנועה")){
            insrImage.setImageResource(R.drawable.mafria);
        }
        else{
            insrImage.setImageResource(R.drawable.other1);
        }
    }
}
