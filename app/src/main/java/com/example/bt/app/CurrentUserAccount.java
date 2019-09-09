package com.example.bt.app;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bt.data.Repositories.EventRepository;
import com.example.bt.data.Repositories.FirebaseDatabaseRepository;
import com.example.bt.data.Repositories.RepositoryFactory;
import com.example.bt.data.Repositories.UserRepository;
import com.example.bt.models.Event;
import com.example.bt.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserAccount {

    private FirebaseUser mFirebaseUser;
    private User mCurrentUser;
    private EventRepository mEventRepository;
    private MutableLiveData<List<Event>> mUserEvents;

    private static CurrentUserAccount INSTANCE = null;

    private CurrentUserAccount()
    {
        mUserEvents = new MutableLiveData<>();
    }

    public static synchronized CurrentUserAccount getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CurrentUserAccount();
        }

        return(INSTANCE);
    }

    public void InitCurrentUser(FirebaseUser firebaseUser) {
        final UserRepository userRepository =
                (UserRepository) RepositoryFactory.GetRepositoryInstance(RepositoryFactory.RepositoryType.UserRepository);

        final String userPhoneNum = firebaseUser.getPhoneNumber();
        DatabaseReference user = userRepository.getDataRef().child(userPhoneNum);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    mCurrentUser = dataSnapshot.getValue(User.class);
                }
                else{
                    userRepository.addNewUser(userPhoneNum, "");
                    mCurrentUser = new User(userPhoneNum, "");
                }

                initCurrentUserEventsMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("CurrentUser", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    private void initCurrentUserEventsMap() {
        mEventRepository =
                (EventRepository) RepositoryFactory.GetRepositoryInstance(RepositoryFactory.RepositoryType.EventRepository);
        mEventRepository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Event>() {
            @Override
            public void onSuccess(List<Event> result) {
                List<Event> userEvents = new ArrayList<>();
                for (Event event : result) {
                    if (mCurrentUser.getEvents().contains(event)) {
                        userEvents.add(event);
                    }
                }

                mUserEvents.setValue(userEvents);
            }

            @Override
            public void onError(Exception e) {
                Log.w("CurrentUserEvents", "loadPost:onCancelled", e);

            }
        });
    }

    public FirebaseDatabaseRepository GetEventRepository()
    {
        return mEventRepository;
    }

    public MutableLiveData<List<Event>> GetCurrentUserEventList()
    {
        return mUserEvents;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser)
    {
        mFirebaseUser = firebaseUser;
    }

    public FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }
}