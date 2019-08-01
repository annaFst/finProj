package com.example.bt.models;

import java.util.List;

public class User {
    String mId;
    String mName;
    List<Event> mEvents;

    public User(String mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }
}
