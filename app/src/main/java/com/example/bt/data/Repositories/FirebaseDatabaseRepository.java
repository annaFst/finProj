package com.example.bt.data.Repositories;

import com.example.bt.data.Mappers.FirebaseMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * A base class for every database entity table
 * @param <Model> The entity type
 */
public abstract class FirebaseDatabaseRepository<Model> {

    protected DatabaseReference mDataRef;
    protected FirebaseDatabaseRepositoryCallback<Model> firebaseCallback;
    private BaseValueEventListener listener;
    private FirebaseMapper mapper;

    public FirebaseDatabaseRepository(FirebaseMapper mapper)
    {
        mDataRef = FirebaseDatabase.getInstance("https://androidapps-4049e.firebaseio.com/").getReference(getRootNode());
        this.mapper = mapper;
    }

    protected abstract String getRootNode();

    public DatabaseReference getDataRef()
    {
        return mDataRef;
    }

    public void addListener(FirebaseDatabaseRepositoryCallback<Model> firebaseCallback)
    {
        this.firebaseCallback = firebaseCallback;
        listener = new BaseValueEventListener(mapper, firebaseCallback);
        mDataRef.addValueEventListener(listener);
    }

    // CRUD operations

    /**
     * @param model The model to be added
     * @return The inserted model key
     */
    public abstract String add(Model model);

    /**
     * @param model The model to be updated
     */
    public abstract void update(Model model);

    /**
     * @param model The model to be removed
     */
    public abstract void remove(Model model);

    public void removeListener()
    {
        mDataRef.removeEventListener(listener);
    }


    public interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }
}
