package com.example.bt.models;

public class Item {
    private String mName;
    private Boolean mIsTaken = false;
    private Contact mTakenBy;
    private int mQuantity;

    public Item(){}

    public Item(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Boolean getTaken() {
        return mIsTaken;
    }

    public void setTaken(Boolean mIsTaken) {
        this.mIsTaken = mIsTaken;
    }

    public Contact getTakenBy() {
        return mTakenBy;
    }

    public void setTakenBy(Contact mTakenBy) {
        this.mTakenBy = mTakenBy;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }
}
