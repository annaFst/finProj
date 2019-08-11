package com.example.bt.models;

public class Item {
    private String mName;
    private Boolean mIsTaken;
    private User mTakenBy;
    private int mQuantity;

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

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }
}
