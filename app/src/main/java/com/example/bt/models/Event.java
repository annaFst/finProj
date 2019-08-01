package com.example.bt.models;

import com.example.bt.DBdemo;
import com.example.bt.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String eventName= "";
    private List<Item> myList= new ArrayList<>();
    private List<Item> takenItems= new ArrayList<>();
    private String date = "";
    private String eventTime = "";

    public void setName (String str){
        eventName = str;
    }

    public String getName (){
        return eventName;
    }
    public void setTime (String str){
        eventTime = str;
    }

    public String getTime (){
        return eventTime;
    }

    public void addToList(String str){
        myList.add(new Item(str));
    }

    public void addToList(Item item){
        myList.add(item);
    }

    public void addToTakenList(String str){
        takenItems.add(new Item(str));
    }

    public void addToTakenList(Item item){
        takenItems.add(item);
    }

    public Item getFromList(int i){
        return myList.get(i);
    }

    public int sizeOfList(){
        return myList.size();
    }

    public List<Item> getItems(){
        return myList;
    }

    public List<Item> getTakenItems(){
        return takenItems;
    }

    public void setDate(String dStr){
        date = dStr;
    }

    public String getDate(){
        return date;
    }

    public void copyEvent(Event other){
        Event res = new Event();

        res.setName(other.getName().concat("(copy)"));
        res.setDate(other.getDate());
        res.setTime(other.getTime());

        int size  = other.myList.size();
        int takenSize = other.takenItems.size();

        for (Item item : takenItems){
            res.addToList(item);
        }

        if (takenSize>0) {
            for (int i=0; i<takenSize ;i++) {
                Item takenItem = other.takenItems.get(i);
                res.addToTakenList(takenItem);
            }
        }

        DBdemo.eventArr.add(res);
        MainActivity.updateList(res.getName());
    }

    public void takeItem(int index){
        takenItems.add(myList.get(index));
        myList.remove(index);
    }
}
