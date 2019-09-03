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

}
