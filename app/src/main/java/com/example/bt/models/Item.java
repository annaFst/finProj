package com.example.bt.models;

public class Item {
    String mName;
    Boolean mIsTaken;
    User mTakenBy;

    public Item(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

//    public void setName(String mName) {
//        this.mName = mName;
//    }

    public Boolean getIsTaken() {
        return mIsTaken;
    }

    public void setIsTaken(Boolean mIsTaken) {
        this.mIsTaken = mIsTaken;
    }

    public User getTakenBy() {
        return mTakenBy;
    }

    public void setTakenBy(User mTakenBy) {
        this.mTakenBy = mTakenBy;
    }
}
