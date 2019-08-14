package com.example.bt.data.Repositories;

import com.example.bt.data.Mappers.FirebaseMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public abstract class FirebaseDatabaseRepository<Model> {

    protected DatabaseReference mDataRef;
    protected FirebaseDatabaseRepositoryCallback<Model> firebaseCallback;
    private BaseValueEventListener listener;
    private FirebaseMapper mapper;

    protected abstract String getRootNode();

    public FirebaseDatabaseRepository(FirebaseMapper mapper){
        mDataRef = FirebaseDatabase.getInstance("https://androidapps-4049e.firebaseio.com/").getReference(getRootNode());
        this.mapper = mapper;
    }

    public DatabaseReference getDataRef() {
        return mDataRef;
    }

    public void addListener(FirebaseDatabaseRepositoryCallback<Model> firebaseCallback) {
        this.firebaseCallback = firebaseCallback;
        listener = new BaseValueEventListener(mapper, firebaseCallback);
        mDataRef.addValueEventListener(listener);
    }

    public boolean exists(String key)
    {
        DatabaseReference child = mDataRef.child(key);
        if (child != null)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public void removeListener() {
        mDataRef.removeEventListener(listener);
    }

    public interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }
}
