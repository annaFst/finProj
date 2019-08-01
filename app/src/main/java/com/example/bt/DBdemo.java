package com.example.bt;

import android.content.Context;

import com.example.bt.models.Event;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DBdemo {

    public static final String FILENAME = "eventsListInfo.dat";
    public static ArrayList<String> allEvents= new ArrayList<String>();
    public static ArrayList<Event> eventArr = new ArrayList<Event> ();


//
//    public static void  writeToFile (ArrayList<String> events, Context con){
//        try {
//            FileOutputStream outputFile = con.openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            ObjectOutputStream obj = new ObjectOutputStream(outputFile);
//            obj.writeObject(events);
//            obj.close();
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    public static ArrayList<String> readFromFile(Context con){
//        ArrayList<String> eventsL  = null;
//
//        try{
//            FileInputStream inputFile = con.openFileInput(FILENAME);
//            ObjectInputStream obj  = new ObjectInputStream(inputFile);
//            eventsL = (ArrayList<String>) obj.readObject();
//        }catch(FileNotFoundException e){
//            eventsL = new ArrayList<>();
//            e.printStackTrace();
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch(ClassNotFoundException e){
//            e.printStackTrace();
//        }
//
//        return eventsL;
//    }

}

