package com.example.bt.data.Repositories;

import com.example.bt.data.Mappers.EventMapper;
import com.example.bt.models.Event;

public class EventRepository extends FirebaseDatabaseRepository<Event> {


    public EventRepository() {
        super(new EventMapper());
    }

    @Override
    protected String getRootNode() {
        return "events";
    }

    @Override
    public String add(Event event) {
        return addEvent(event);
    }

    @Override
    public void update(Event event) {
        mDataRef.child(event.getEventId()).setValue(event);
    }

    @Override
    public void remove(Event event) {
        mDataRef.child(event.getEventId()).setValue(null);
    }

    private String addEvent(Event event)
    {
        String key = mDataRef.push().getKey();
        event.setEventId(key);
        mDataRef.child(key).setValue(event);

        return key;
    }
}
