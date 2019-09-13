package com.example.bt.models;

import java.io.Serializable;

public class Contact implements Serializable
{
    private String contactName = "";
    private String phoneNumber = "";

    public Contact()
    {

    }

    public Contact(String newName, String newPhoneNumber) {
        contactName = newName;
        phoneNumber = newPhoneNumber;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
