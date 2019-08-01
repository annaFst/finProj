package com.example.bt.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBConnetionWrapper {

    FirebaseDatabase mDataBase = null;
    DatabaseReference mDBRoot = null;

    public void Init(){
        mDataBase = FirebaseDatabase.getInstance("https://finalproject-a6d0a.firebaseio.com/");
        mDBRoot = mDataBase.getReference();
    }

    DatabaseReference getDBRoot() { return mDBRoot; }

    DatabaseReference getChild(String child){
        return mDBRoot.child(child);
    }


}
