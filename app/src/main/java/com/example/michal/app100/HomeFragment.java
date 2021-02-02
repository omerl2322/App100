package com.example.michal.app100;





import android.location.Location;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.res.Configuration;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {
    //declaration
    private TextView userLogin;
    private ImageButton car;
    private ImageButton property;
    private FirebaseAuth firebaseAuth;
    private Connector connector;
    private DatabaseReference ref;
    private ImageButton phoneIcon;
    public HomeFragment() {
        // Required empty public constructor
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Title and  initiaizatlion
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        gpsAlert(v);
        explainAlert(v);
        userLogin = (TextView) v.findViewById(R.id.userName);
        phoneIcon = (ImageButton) v.findViewById(R.id.phoneIcon);
        firebaseAuth = FirebaseAuth.getInstance();
        String Uid = firebaseAuth.getCurrentUser().getUid();
        connector = new Connector();
        ref = connector.databaseReferenceUsers.child(Uid).child("firstName");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userLogin.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String  tel = "tel:" + "100";
                callIntent.setData(Uri.parse(tel));
                startActivity(callIntent);
            }
        });
        //userLogin.setText(getActivity().getIntent().getExtras().getString("Email"));
        return v;
    }

    private void gpsAlert(View v) {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(getActivity(), "שירותי מיקום לא פעילים,הפעל במידת הצורך", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), "שירותי מיקום פעילים", Toast.LENGTH_LONG).show();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    private void explainAlert(View v) {

        final ImageButton popUpButton = (ImageButton) v.findViewById(R.id.locationButton1);
        final ImageButton popUpButtonProperty = (ImageButton) v.findViewById(R.id.imageButton2);
        final ImageButton popUpButtonNoise = (ImageButton) v.findViewById(R.id.imageButton8);
        final ImageButton popUpButtonInf = (ImageButton) v.findViewById(R.id.imageButton7);
        final ImageButton popUpButtonOther = (ImageButton) v.findViewById(R.id.imageButton9);
        popUpButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder= new AlertDialog.Builder(getActivity());
                a_builder.setMessage(Html.fromHtml(getString(R.string.Other)))

                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getActivity(),Other.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert= a_builder.create();
                alert.setTitle("Other");
                alert.show();
            }
        });
        popUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder= new AlertDialog.Builder(getActivity());
                a_builder.setMessage(Html.fromHtml(getString(R.string.car)))

                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getActivity(),Car.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert= a_builder.create();
                alert.setTitle("Car");
                alert.show();
            }
        });

        //for proprty
        popUpButtonProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder= new AlertDialog.Builder(getActivity());
                a_builder.setMessage(Html.fromHtml(getString(R.string.propertyCrimes)))

                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getActivity(),Property.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert= a_builder.create();
                alert.setTitle("Property");
                alert.show();
            }
        });

        //for Noise
        popUpButtonNoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder= new AlertDialog.Builder(getActivity());
                a_builder.setMessage(Html.fromHtml(getString(R.string.NoiseCrimes)))

                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getActivity(),Noise.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert= a_builder.create();
                alert.setTitle("Noise");
                alert.show();
            }
        });
        //for Inf
        popUpButtonInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder= new AlertDialog.Builder(getActivity());
                a_builder.setMessage(Html.fromHtml(getString(R.string.InfCrimes)))

                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getActivity(),Infastructure.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert= a_builder.create();
                alert.setTitle("Infastructure");
                alert.show();
            }
        });
    }
////////////////////////////////////////////////////////////////////////////////////////////

}
