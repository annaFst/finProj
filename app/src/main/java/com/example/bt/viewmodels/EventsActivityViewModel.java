package com.example.bt.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bt.app.CurrentUserAccount;
import com.example.bt.data.Repositories.FirebaseDatabaseRepository;
import com.example.bt.models.Event;

import java.util.List;

public class EventsActivityViewModel extends ViewModel {

    private static final String TAG = "EventsActivityViewModel";

    private MutableLiveData<List<Event>> mEvents;

    public LiveData<List<Event>> getEvents()
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
        CurrentUserAccount.getInstance().GetEventRepository().addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback() {
            @Override
            public void onSuccess(List result) {
                mEvents.setValue(CurrentUserAccount.getInstance().GetCurrentUserEventList());
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "loadCurrentUserEvents: Error");
            }
        });
    }
}
