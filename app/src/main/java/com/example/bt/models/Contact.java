package com.example.bt.models;

public class Contact {

    public String contactName = "";
    public String phoneNumber = "";

    public Contact()
    {

    }

    public Contact(String newName, String newPhoneNumber) {
        contactName = newName;
        phoneNumber = newPhoneNumber;
    }
}
