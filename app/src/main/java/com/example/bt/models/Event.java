package com.example.bt.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.bt.DBdemo;
import com.example.bt.EventsActivity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private String mEventId;
    private String mEventName = "";
    private int mEventAlarmIndex = -1;
    private List<Item> mItemList = new ArrayList<>();
    private List<Item> mTakenItemsList = new ArrayList<>();
    private List<String> participants = new ArrayList<>();
    private long eventDate; // Epoch seconds
    private long eventTime; // Seconds of day
    private String mEventCreatorId;
    private boolean admin = false;

    public void setAlarmIndex(int i){
        mEventAlarmIndex = i;
    }

    public int getEventAlarmIndex() {
        return mEventAlarmIndex;
    }

    public boolean isAdmin () {
        return admin;
    }

    public void setEventId(String eventId) {
        mEventId = eventId;
    }

    public String getEventId(){
        return mEventId;
    }

    public String getEventCreatorId() {
        return mEventCreatorId;
    }

    public void setEventCreatorId(String eventCreatorId) {
        mEventCreatorId = eventCreatorId;
    }

    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public long getEventDate() {
        return this.eventDate;
    }

    public long getEventTime() {
        return this.eventTime;
    }

    public void setName (String str){
        mEventName = str;
    }

    public String getName (){
        return mEventName;
    }


    public void addToList(String str){
        mItemList.add(new Item(str));
    }

    public void addToList(Item item){
        mItemList.add(item);
    }

    public void addToList(List<Item> items){
        mItemList.addAll(items);
    }

//    public void addToTakenList(String str){
//        mTakenItemsList.add(new Item(str));
//    }

    public void addToTakenList(Item item){
        mTakenItemsList.add(item);
    }

    public Item getFromList(int i){
        return mItemList.get(i);
    }

    public int sizeOfList(){
        return mItemList.size();
    }

    public List<Item> getItems(){
        return mItemList;
    }

    public List<Item> getTakenItemsList(){
        return mTakenItemsList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void copyEvent(Event other){
        Event res = new Event();

        res.setName(other.getName().concat("(copy)"));
        res.setEventDate(other.getEventDate());
        res.setEventTime(other.getEventTime());

        int size  = other.mItemList.size();
        int takenSize = other.mTakenItemsList.size();

        for (Item item : mTakenItemsList){
            res.addToList(item);
        }

        if (takenSize>0) {
            for (int i=0; i<takenSize ;i++) {
                Item takenItem = other.mTakenItemsList.get(i);
                res.addToTakenList(takenItem);
            }
        }

        DBdemo.eventArr.add(res);
        //EventsActivity.updateList(res.getName());
    }

    public void takeItem(int index){
        mTakenItemsList.add(mItemList.get(index));
        mItemList.remove(index);
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }


}
