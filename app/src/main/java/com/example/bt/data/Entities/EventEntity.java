package com.example.bt.data.Entities;

import com.example.bt.models.Item;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class EventEntity {

    private String id;
    private String name;
    private long eventDate;
    private long eventTime;
    private List<String> participants;
    private List<Item> items;
    private String creator;

    public EventEntity() {
        participants = new ArrayList<>();
        items = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String mEventId) {
        this.id = mEventId;
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

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> mParticipants) {
        this.participants = mParticipants;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> mEventItems) {
        this.items = mEventItems;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }
}
