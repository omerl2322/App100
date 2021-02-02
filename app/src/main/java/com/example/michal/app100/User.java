package com.example.michal.app100;

public class User {
    private String idUser;
    private  String firstName;
    private  String lastName;
    private long phoneNumber;
    private String email;
    private String password;

    public User ( String idUser,String firstName,String lastName,long phoneNumber,String email,String password){
        this.idUser=idUser;
        this.firstName=firstName;
        this.lastName= lastName;
        this.phoneNumber =phoneNumber;
        this.email =email;
        this.password=password;
    }

    //////////////////////////////////////////////////
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    //////////////////////////////////////////////////
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    //////////////////////////////////////////////////
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    //////////////////////////////////////////////////
    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //////////////////////////////////////////////////
}
