package com.example.michal.app100;



public class Gvariable {
    private static Gvariable instance;
    private static String event;

    private Gvariable(){}


    public static String getEvent() {
        return event;
    }

    public static void setEvent(String event) {
        Gvariable.event = event;
    }
    public static synchronized Gvariable getInstance(){
        if (instance==null){
            instance = new Gvariable();
        }
        return  instance;
    }
}
