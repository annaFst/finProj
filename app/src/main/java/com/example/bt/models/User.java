package com.example.bt.models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String mId;
    private String mName;
    private List<String> mEvents;

    public User() {
        mEvents = new ArrayList<>();
    }

    public User(String mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<String> getEvents() {
        return mEvents;
    }

    public void setEvents(List<String> events) {
        mEvents = events;
    }
}
