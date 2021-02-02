package com.example.michal.app100;

import android.provider.Settings;

import java.sql.Date;
import java.sql.Time;

public class Report {
    public String idReport;
    public   String reportDescription;
    public String reportDate; // check if working
    public String reportTime;
    public String reportLocation;
    public String picture; // handle this later
    public String reportOn; // car, noise...
    public String idUser; // how gives the report
    public String userZehot;
    //for car
    public String eventType;
    public String licenseCar;
    public String status;

    ////////////////////////////////////////////////////////////////////////////////////////
    //constractors
    // for car
    public Report(String idReport,String eventType,String reportDate,String reportTime,String licenseCar,String reportDescription,
                  String reportLocation,String idUser ,String userZehot){
        this.idReport = idReport;
        this.reportOn ="Car";
        this.eventType = eventType;
        this.reportDate =reportDate;
        this.reportTime = reportTime;
        this.licenseCar =licenseCar;
        this.reportDescription=reportDescription;
        this.reportLocation=reportLocation;
        this.idUser = idUser;
        this.userZehot = userZehot;
        this.status="בהמתנה";


    }
    //property
    public Report(String idReport,String eventType,String reportDate,String reportTime,String reportDescription,
                  String reportLocation,String idUser ,String userZehot){
        this.idReport = idReport;
        this.reportOn ="Property";
        this.eventType = eventType;
        this.reportDate =reportDate;
        this.reportTime = reportTime;
        this.reportDescription=reportDescription;
        this.reportLocation=reportLocation;
        this.idUser = idUser;
        this.userZehot = userZehot;
        this.status="בהמתנה";


    }

    //FOR OTHER
    public Report(String idReport,String reportDate,String reportTime,String reportDescription,
                  String reportLocation,String idUser ,String userZehot){
        this.idReport = idReport;
        this.reportOn ="Other";
        this.reportDate =reportDate;
        this.reportTime = reportTime;
        this.reportDescription=reportDescription;
        this.reportLocation=reportLocation;
        this.idUser = idUser;
        this.userZehot = userZehot;
        this.status="בהמתנה";
    }


    public Report(){

    }


    ///////////////////////////////////////////////////////////////////////////////////////////



}
