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
    private List<Event> mUserEvents;

    private static CurrentUserAccount INSTANCE = null;

    private CurrentUserAccount()
    {
        mUserEvents = new ArrayList<>();
        mCurrentUser = new User();
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
        mCurrentUser.setId(userPhoneNum);

        DatabaseReference user = userRepository.getDataRef().child(userPhoneNum);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    // User exists - load user data
                    mCurrentUser = dataSnapshot.getValue(User.class);

                    // Initialize user events
                    initCurrentUserEventsMap();
                }
                else{
                    // New user - create a user object and init new events list
                    //mCurrentUser.setId(userPhoneNum);
                    mCurrentUser.setEvents(new ArrayList<String>());

                    // Add new user to user repository
                    userRepository.add(mCurrentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("CurrentUser", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    private void initCurrentUserEventsMap()
    {
        mEventRepository =
                (EventRepository) RepositoryFactory.GetRepositoryInstance(RepositoryFactory.RepositoryType.EventRepository);

        mEventRepository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Event>() {
            @Override
            public void onSuccess(List<Event> repositoryEvents)
            {
                List<Event> userEvents = new ArrayList<>();
                for (Event event : repositoryEvents)
                {
                    if (mCurrentUser.getEvents() != null && mCurrentUser.getEvents().contains(event)) {
                        userEvents.add(event);
                    }
                }

                mUserEvents.addAll(userEvents);
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

    public List<Event> GetCurrentUserEventList()
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
