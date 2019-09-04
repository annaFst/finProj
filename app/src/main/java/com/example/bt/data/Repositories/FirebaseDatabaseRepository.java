package com.example.bt.data.Repositories;

import androidx.annotation.NonNull;

import com.example.bt.data.Mappers.FirebaseMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public boolean exists(final String key)
    {
        DatabaseReference child = mDataRef.child(key);
        final boolean[] exists = {};
        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    exists[0] = true;
                }
                else
                {
                    exists[0] = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                exists[0] = false;
            }
        });

        if (exists[0])
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
