package com.example.bt;

import java.util.ArrayList;

public class Event {

    private String eventName= "";
    private ArrayList<String> myList= new ArrayList<String>();
    private ArrayList<String> takenItems= new ArrayList<String>();
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
        myList.add(str);
    }

    public void addToTakenList(String str){
        takenItems.add(str);
    }
    public String getFromList(int i){
        return myList.get(i);
    }

    public int sizeOfList(){
        return myList.size();
    }

    public ArrayList<String> getItems(){
        return myList;
    }

    public ArrayList<String> getTakenItems(){
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

        for (int i=0; i<size ;i++){
            String item = other.myList.get(i);
            res.addToList(item);
        }

        if (takenSize>0) {
            for (int i=0; i<takenSize ;i++) {
                String takenItem = other.takenItems.get(i);
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
