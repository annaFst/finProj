package com.example.bt.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bt.app.CurrentUserAccount;
import com.example.bt.data.Repositories.FirebaseDatabaseRepository;
import com.example.bt.models.Event;
import com.example.bt.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventsActivityViewModel extends ViewModel {

    private static final String TAG = "EventsActivityViewModel";

    private MutableLiveData<Set<Event>> mEvents;

    public LiveData<Set<Event>> getEvents()
    {
        if (mEvents == null) {
            mEvents = new MutableLiveData<>();
            loadCurrentUserEvents();
        }

        return mEvents;
    }

    @Override
    protected void onCleared() {
        CurrentUserAccount.getInstance().GetEventRepository().removeListener();
    }

    private void loadCurrentUserEvents() {
        CurrentUserAccount.getInstance().GetEventRepository().addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Event>() {
            @Override
            public void onSuccess(List<Event> result) {

                Set<Event> userEvents = new HashSet<>();
                User currentUser = CurrentUserAccount.getInstance().getCurrentUser();
                for (Event event : result)
                {
                    if (currentUser.getEvents() != null && currentUser.getEvents().contains(event.getEventId())) {
                        userEvents.add(event);
                    }
                }
                //mUserEventsSet.addAll(userEvents);

                mEvents.setValue(userEvents);
                CurrentUserAccount.getInstance().setUserEventsSet(userEvents);
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "loadCurrentUserEvents: Error");
            }
        });
    }
}
