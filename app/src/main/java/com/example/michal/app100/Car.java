package com.example.michal.app100;

import android.*;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Car extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final String LOG_TAG = "TestApp";
    private Spinner carSpinner; //event type
    private ImageButton LocationButton;
    private Button sendReport;
    private EditText LocationText; // menualliy or automatic loaction
    private EditText licenseNum;
    private EditText eventDetails;
    private ImageView pic1;
    private ImageView pic2;
    private ImageView pic3;
    private ImageButton picPickerFile;
    private ImageButton picPickerCamera;
    private ImageButton goBack;
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 0;
    private Connector connectorCar;
    private Button dateButton;
    private Button hourButton;
    private int choosen = 0;
    private int year_x, month_x, day_x, min_x, hour_x;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID2 = 1;
    private GoogleApiClient client;
    //fields from location class//////////////////////
    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLoCation;
    protected LocationRequest mLocationRequest;
    private Geocoder geocoder;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private boolean permissionIsGranted = true;
    private double Latutide = 0;
    private double Longitude = 0;
    // user auth
    private ProgressDialog mProgressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDataBaseUser;
    private Uri imageUri, imageUri2,imageUri3,downloadUrl,downloadUrl2 = null;
    public String imageUri1,imageUri22,imageUri33=null;
    private DatabaseReference reference;
    private Bitmap bitmap=null;
    private long numberReport;
    Gvariable typeEvent = Gvariable.getInstance();
    //////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        //mProgressDialog = new ProgressDialog(this);
        //things for location service/////////////////////////////////////////////////////
        buildGoogleApiClient();
        geocoder = new Geocoder(this, Locale.ENGLISH);
        connectorCar = new Connector();
        //fill car spinner with data
        carSpinner = (Spinner) findViewById(R.id.eventTypeSpinner);
        ArrayAdapter<String> carAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.carOptions));
        carSpinner.setAdapter(carAdapter);
        //report details title
        eventDetails = (EditText) findViewById(R.id.describeTheEventTextBox);
        ///////////////////////////////////////////////////////////////
        LocationButton = (ImageButton) findViewById(R.id.locationButton1); // G image
        LocationText = (EditText) findViewById(R.id.locationOfTheEventTextBox); //set location
        ///////////////////////////////////////////////////////////////
        licenseNum = (EditText) findViewById(R.id.licensePlateTextBox);
        ///////////////////////////////////////////////////////////////
        picPickerFile = (ImageButton) findViewById(R.id.openfromfile);
        picPickerCamera = (ImageButton) findViewById(R.id.openfromcamera);
        goBack = (ImageButton) findViewById(R.id.goBack);
        pic1 = (ImageView) findViewById(R.id.imageView1);
        pic2 = (ImageView) findViewById(R.id.imageView3);
        pic3 = (ImageView) findViewById(R.id.imageView4);
        dateButton = (Button) findViewById(R.id.chooseDateButton);
        hourButton = (Button) findViewById(R.id.chooseTimeButton);

        ///////////////////////////////////////////////////////////////
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Car.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        definedDateTime();
        //set current time to pickers
        showDialogOnButtonClick();
        //image picker button shows an image picker to upload a image from phone
        picPickerFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });
        //pic picker button open up camera to take photos
        picPickerCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });
        ///////////////////////////////////////////////////////////////
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else if (Latutide != 0 && Longitude != 0) {
                    findAdress(Latutide, Longitude);
                } else {
                    LocationText.setHint(Longitude + Latutide + "בעיית תקשורת");
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////
        connectorCar.databaseReferenceReport.child("Car").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberReport = dataSnapshot.getChildrenCount()+1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ///////////////////////////////////////////////////////////////////////////////
        //sending report to db
        sendReport = (Button) findViewById(R.id.send);
        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what if something missing...
                mProgressDialog = ProgressDialog.show(Car.this,"אנא המתן","שולח דוח..",true);
                //mProgressDialog.setMessage("שולח דוח.."); //start dialog progress
                //mProgressDialog.show();
                //take data from form
                final String sogEvent = carSpinner.getSelectedItem().toString();
                final String liceNum = licenseNum.getText().toString();
                final String tehorEvent = eventDetails.getText().toString();
                final String locationEvent = LocationText.getText().toString();
                final String uId = mCurrentUser.getUid();
                final String rDate = dateButton.getText().toString();
                final String rTime = hourButton.getText().toString();
                typeEvent.setEvent(sogEvent);

                //


                mDataBaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //adding report //
                        String idUser = (String) dataSnapshot.child("idUser").getValue();
                        reference=  connectorCar.databaseReferenceReport.child("Car").push();

                        Report rCar = new Report(Long.toString(numberReport),sogEvent,rDate,rTime, liceNum, tehorEvent, locationEvent,uId,idUser);

                        reference.setValue(rCar);

                        if(imageUri!=null) {
                            StorageReference filePath = connectorCar.mReportsPhotosStorageRef.child("Car").
                                    child(imageUri.getLastPathSegment());
                            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    imageUri1 = downloadUrl.toString();
                                    reference.child("image1").setValue(imageUri1);
                                }
                            });
                        }
                       if(imageUri2!=null) {
                            StorageReference filePath1 = connectorCar.mReportsPhotosStorageRef.child("Car").
                                    child(imageUri2.getLastPathSegment());
                            filePath1.putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                   Uri downloadUrl2 = taskSnapshot.getDownloadUrl();
                                    imageUri22 = downloadUrl2.toString();
                                    reference.child("image2").setValue(imageUri22);
                                }
                            });
                        }

                        if(bitmap!=null){
                            Uri pic3 = getImageUri(getApplicationContext(),bitmap);
                            StorageReference filePath2 = connectorCar.mReportsPhotosStorageRef.child("Car").
                                    child(pic3.getLastPathSegment());
                           filePath2.putFile(pic3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadUrl3 = taskSnapshot.getDownloadUrl();
                                    imageUri33 = downloadUrl3.toString();
                                    reference.child("image3").setValue(imageUri33);
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mProgressDialog.dismiss();
                //start new activity = semd report
                Intent i = new Intent(Car.this,Instructions.class);
                startActivity(i);
                finish();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        /////////////////////////////////////////////////////////////////////////////
        //user auth
        firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = firebaseAuth.getCurrentUser();
        mDataBaseUser = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mCurrentUser.getUid());
        //////////////////////////////////////////////////////////////////////////////////////

    }
    //////////////////////////////////////////////////////////////////////////////////////
    private Uri getImageUri(Context inContext,Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),bitmap,"pic3",null);
        return Uri.parse(path);

    }

    //////////////////////////////////////////////////////////////////////////////////////
    /*show dialog date*/
    public void showDialogOnButtonClick() {

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        hourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID2);
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////////////
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
        } else if (id == DIALOG_ID2) {
            return new TimePickerDialog(this, tpickerListner, hour_x, min_x, true);
        } else
            return null;
    }
    //////////////////////////////////////////////////////////////////////////////////////
    private DatePickerDialog.OnDateSetListener dpickerListner =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    year_x = year;
                    month_x = monthOfYear + 1;
                    day_x = dayOfMonth;
                    dateButton.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };
    //////////////////////////////////////////////////////////////////////////////////////
    private TimePickerDialog.OnTimeSetListener tpickerListner =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour_x = hourOfDay;
                    min_x = minute;
                    if (min_x < 10)
                        hourButton.setText(hour_x + ":" + "0" + min_x);
                    else
                        hourButton.setText(hour_x + ":" + min_x);
                }

            };

    //////////////////////////////////////////////////////////////////////////////////////
    //set date and hour to be on today date as default
    private void definedDateTime() {

        Calendar c = Calendar.getInstance();
        int amPm = c.get(Calendar.AM_PM);
        hour_x = c.get(Calendar.HOUR);
        if (hour_x < 12) {
            if (amPm == 1)
                hour_x = hour_x + 12;
        }
        min_x = c.get(Calendar.MINUTE);
        if (min_x < 10)
            hourButton.setText(hour_x + ":" + "0" + min_x);
        else
            hourButton.setText(hour_x + ":" + min_x);
        year_x = c.get(Calendar.YEAR);
        month_x = c.get(Calendar.MONTH)+1;
        day_x = c.get(Calendar.DAY_OF_MONTH);
        dateButton.setText(day_x + "/" + month_x + "/" + year_x);

    }
    ////////////////////////////////////////////////////////////////////////////
    //get the image in return from file or camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //back from file
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            if (choosen == 0) {
                imageUri = data.getData();
                pic1.setImageURI(imageUri);
                choosen = 1;
            } else if (choosen == 1) {
                imageUri2 = data.getData();
                pic3.setImageURI(imageUri2);
                Toast.makeText(Car.this, "שים לב , לא ניתן להוסיף יותר מ2 תמונות", Toast.LENGTH_LONG).show();
                choosen = 0;
            }

        }
        //back from camera
        if (requestCode == CAMERA_REQUEST && resultCode ==RESULT_OK) {
            bitmap = (Bitmap)data.getExtras().get("data");
            pic2.setImageBitmap(bitmap);


        }
    }

    ////////////////////////////////////////////////////////////////////////////
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Car Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    ////////////////////////////////////////////////////////////////////////////
    ////function from location services

    /////////////////////////////////////////////////////////////////////////////////////////
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    //run third if everything ok
    public void onConnected(Bundle connectionHint) {
        // requestLocation1();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(4000);

        requestLocation1();
        if (permissionIsGranted) {

            //noinspection MissingPermission
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////
    private void requestLocation1() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_FINE_LOCATION);
            }else{
                permissionIsGranted = true;
            }

        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////
    private void findAdress(double latutide, double longitude) {
        try{
            List<Address> addresses = geocoder.getFromLocation(latutide,longitude,1);
            if(addresses!=null){
                Address returnAddress= addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i=0; i<returnAddress.getMaxAddressLineIndex();i++){
                    if(i==0)
                        strReturnedAddress.append(returnAddress.getAddressLine(i)).append(",");
                    else
                        strReturnedAddress.append(returnAddress.getAddressLine(i)).append("");
                }
                LocationText.setText(strReturnedAddress.toString());
            }
            else{
                LocationText.setText("No Address returned!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            LocationText.setText("Cannot get Address!");
        }

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
        mGoogleApiClient.disconnect();
        super.onStop();
        //if(permissionIsGranted)
         //   if (mGoogleApiClient.isConnected())

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
                   // mLatitude.setText("permission problem");
                   // mLongitude.setText("permission problem");
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
    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG,location.toString());
        Latutide = location.getLatitude();
        Longitude =location.getLongitude();



    }
    /////////////////////////////////////////////////////////////////////////////////////////
}
