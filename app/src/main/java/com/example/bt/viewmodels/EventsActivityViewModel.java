package com.example.bt.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bt.app.CurrentUserAccount;
import com.example.bt.models.Event;

import java.util.List;

public class EventsActivityViewModel extends ViewModel {

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
        mEvents.setValue(CurrentUserAccount.getInstance().GetCurrentUserEventList());
    }
}
