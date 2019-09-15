package com.example.bt.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.bt.app.CurrentUserAccount;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Event {

    private String mEventId;
    private String mEventName = "";
    private int mEventAlarmIndex = -1;
    private int mNotificationIndex = -1;
    private List<Item> mItemList = new ArrayList<>();
    private List<Contact> participants = new ArrayList<>();
    private long eventDate; // Epoch seconds
    private long eventTime; // Seconds of day
    private String mEventCreatorId;
    private boolean admin = false;
    private boolean isRepeat = false;
    private String repeatType ;

    public int getNotificationIndex() {
        return mNotificationIndex;
    }

    public void setNotificationIndex(int mNotificationIndex) {
        this.mNotificationIndex = mNotificationIndex;
    }

    public void setRepeat(boolean repeat){
        isRepeat = repeat;
    }

    public boolean getRepeat(){
        return isRepeat;
    }

    public void setRepeatType (String repeatTypeStr){
        repeatType =repeatTypeStr;
    }

    public String getRepeatType(){
        return repeatType;
    }

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

    public List<Item> getItems(){
        return mItemList;
    }


//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void copyEvent(Event other){
//        Event res = new Event();
//
//        res.setName(other.getName().concat("(copy)"));
//        res.setEventDate(other.getEventDate());
//        res.setEventTime(other.getEventTime());
//
//        int size  = other.mItemList.size();
//        int takenSize = other.mTakenItemsList.size();
//
//        for (Item item : mTakenItemsList){
//            res.addToList(item);
//        }
//
//        if (takenSize>0) {
//            for (int i=0; i<takenSize ;i++) {
//                Item takenItem = other.mTakenItemsList.get(i);
//                res.addToTakenList(takenItem);
//            }
//        }
//
//        //EventsActivity.updateList(res.getName());
//    }

    public void takeItem(int index)
    {
        User currUser = CurrentUserAccount.getInstance().getCurrentUser();
        Contact itemTaker =
                new Contact(currUser.getName(), currUser.getId());
        mItemList.get(index).setTaken(true);
        mItemList.get(index).setTakenBy(itemTaker);

        //mTakenItemsList.add(mItemList.get(index));
        //mItemList.remove(index);
    }

    public List<Contact> getParticipants()
    {
        return participants;
    }

    public void setParticipants(List<Contact> members)
    {
        for (Contact name : members){
            participants.add(name);
        }
    }
}
