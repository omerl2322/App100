package com.example.michal.app100;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Connector {
    public FirebaseDatabase connector;
    //to connect to reports
    public DatabaseReference databaseReferenceReport;
    //to connect to users
    public DatabaseReference databaseReferenceUsers;
    // connect to storage
    public FirebaseStorage mFirebaseStorage;
    public StorageReference mReportsPhotosStorageRef;
    //consturctor
    public Connector(){
        //get a reference to the database service storage and data
        connector = FirebaseDatabase.getInstance();
        databaseReferenceReport = connector.getReference().child("Reports");
        databaseReferenceUsers = connector.getReference().child("Users");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mReportsPhotosStorageRef = mFirebaseStorage.getReference().child("Report_photos");
    }




}
