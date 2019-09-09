package com.example.bt.data.Entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class UserEntity {

    private String id;
    private String name;
    private List<String> events;

    public UserEntity(){}


    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }


}
