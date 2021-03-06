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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A singleton class of the currently logged in user
 */
public class CurrentUserAccount {

    private static final String TAG = "CurrentUserAccount";

    private FirebaseUser mFirebaseUser;
    private User mCurrentUser;
    private EventRepository mEventRepository;
    private Set<Event> mUserEventsSet;

    private static CurrentUserAccount INSTANCE = null;

    private CurrentUserAccount()
    {
        mEventRepository = (EventRepository) RepositoryFactory.GetRepositoryInstance(RepositoryFactory.RepositoryType.EventRepository);
        mUserEventsSet = new HashSet<>();
        mCurrentUser = new User();
    }

    public static synchronized CurrentUserAccount getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CurrentUserAccount();
        }

        return(INSTANCE);
    }

    public void InitCurrentUser(FirebaseUser firebaseUser)
    {
        mFirebaseUser = firebaseUser;

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
                }
                else{
                    // New user - create a user object and init new events list
                    //mCurrentUser.setId(userPhoneNum);
                    mCurrentUser.setEvents(new ArrayList<String>());

                    // Add new user to user repository
                    userRepository.add(mCurrentUser);
                }

                // Initialize user events
                //initCurrentUserEventsMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    private void initCurrentUserEventsMap()
    {
        mEventRepository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Event>() {
            @Override
            public void onSuccess(List<Event> repositoryEvents)
            {
                Set<Event> userEvents = new HashSet<>();
                for (Event event : repositoryEvents)
                {
                    if (mCurrentUser.getEvents() != null && mCurrentUser.getEvents().contains(event.getEventId())) {
                        userEvents.add(event);
                    }
                }

                mUserEventsSet.addAll(userEvents);
                //mUserEvents.addAll(userEvents);
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
        return new ArrayList<>(mUserEventsSet);
    }

    public Event GetEventIfPresent(String source)
    {
        for (Event event : mUserEventsSet) {
            if (event.getEventId().equals(source))
                return event;
        }

        return null;
    }

    public void setUserEventsSet(Set<Event> userEventsSet) {
        mUserEventsSet = userEventsSet;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }
}
