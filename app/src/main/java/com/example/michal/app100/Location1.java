package com.example.michal.app100;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//this class helps us find user location address
public class Location1 extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    //add tag to the code
    private final String LOG_TAG = "TestApp";
    protected TextView mLatitude;
    protected TextView mLongitude;
    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLoCation;
    protected TextView mAddress;
    private Button b;
    private RequestQueue requestQueue;
    private Geocoder geocoder;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION=101;
    private boolean permissionIsGranted = false;
    public static String returnAdd;
    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    //run first
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location1);
        mLatitude = (TextView) findViewById(R.id.txtOutput1);
        mLongitude = (TextView) findViewById(R.id.txtOutput2);
        mAddress = (TextView) findViewById(R.id.txtOutput3);
        //class that handling adress Recognition

        buildGoogleApiClient();
        geocoder = new Geocoder(this, Locale.ENGLISH);

        mLatitude.setText("omer");
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    //run third if everything ok
    public void onConnected(Bundle connectionHint) {
        requestLocation1();
    }

    private void requestLocation1() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_FINE_LOCATION);
            }else{
                permissionIsGranted = true;
            }
            return ;

        }

        mLastLoCation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLoCation!= null){
            mLatitude.setText(String.valueOf(mLastLoCation.getLatitude()));
            mLongitude.setText(String.valueOf(mLastLoCation.getLongitude()));
            double Latutide = mLastLoCation.getLatitude();
            double Longitude = mLastLoCation.getLongitude();
            findAdress(Latutide,Longitude);
        }
        else{
            mLatitude.setText("cant find location");
            mLongitude.setText("cant find location");
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    private void findAdress(double latutide, double longitude) {
        try{
            List<Address> addresses = geocoder.getFromLocation(latutide,longitude,1);
            if(addresses!=null){
                Address returnAddress= addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for (int i=0; i<returnAddress.getMaxAddressLineIndex();i++){
                    strReturnedAddress.append(returnAddress.getAddressLine(i)).append("\n");
                }
                mAddress.setText(strReturnedAddress.toString());
            }
            else{
                mAddress.setText("No Address returned!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mAddress.setText("Cannot get Address!");
        }
        returnAdd= (String) mAddress.getText();
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    //comes after onCreate... run second
    protected void onStart() {
        super.onStart();
        //connect to location services
        mGoogleApiClient.connect();

    }
    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStop() {
        super.onStop();
        if(permissionIsGranted)
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    //if something goes worng with the connection = connect again
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG,"Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    /////////////////////////////////////////////////////////////////////////////////////////
    //runs if there is a problem with the connection to the location services
    //resopnse to second method = on start
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG,"Connection failed:"+ connectionResult.getErrorCode());
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission granted
                    permissionIsGranted =true;
                }else{
                    //permiision denied
                    permissionIsGranted =false;
                    Toast.makeText(getApplicationContext(),"this app requires permission to ne granted",Toast.LENGTH_SHORT)
                            .show();
                    mLatitude.setText("permission problem");
                    mLongitude.setText("permission problem");
                }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionIsGranted){
            if(mGoogleApiClient.isConnected()){
                requestLocation1();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onPause() {
        super.onPause();

    }
}
