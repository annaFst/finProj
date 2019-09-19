package com.example.bt.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bt.app.CurrentUserAccount;
import com.example.bt.models.Event;


public class CurrentEventActivityViewModel extends ViewModel
{
    private static final String TAG = "CurrentEventViewModel";

    MutableLiveData<Event> mCurrentEvent;

    public LiveData<Event> getEvent(String eventId)
    {
        Log.d(TAG, String.format("getEvent:", eventId));

        if (mCurrentEvent == null) {
            mCurrentEvent = new MutableLiveData<>();
            loadEvent(eventId);
        }
        else if (!mCurrentEvent.getValue().getEventId().equals(eventId))
        {
            loadEvent(eventId);
        }

        return mCurrentEvent;
    }

    private void loadEvent(final String eventId)
    {
        mCurrentEvent.setValue(CurrentUserAccount.getInstance().GetEventIfPresent(eventId));
    }
}
