package com.example.bt.data.Entities;

import com.example.bt.models.Contact;
import com.example.bt.models.Item;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class EventEntity {

    private String eventId;
    private String name;
    private long eventDate;
    private long eventTime;
    private List<Contact> participants;
    private List<Item> items;
    private String eventCreatorId;
   // private boolean active;
   // private boolean repeat;

   /* public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }*/

    public EventEntity() {
        participants = new ArrayList<>();
        items = new ArrayList<>();
        //repeat = false;
       // active = true;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String mEventId) {
        this.eventId = mEventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String mEventName) {
        this.name = mEventName;
    }

    public long getEventDate() {
        return eventDate;
    }

    public void setEventDate(long mEventDateTime) {
        this.eventDate = mEventDateTime;
    }

    public List<Contact> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Contact> mParticipants) {
        this.participants = mParticipants;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> mEventItems) {
        this.items = mEventItems;
    }

    public String getEventCreatorId() {
        return eventCreatorId;
    }

    public void setEventCreatorId(String creator) {
        this.eventCreatorId = creator;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }
}
