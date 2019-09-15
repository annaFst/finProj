package com.example.bt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.bt.activities.CreateEventActivity;
import com.example.bt.activities.SetAlarmActivity;
import com.example.bt.models.Event;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class DBdemo {
    public static final int OFFSET = 100;

    public static final String FILENAME = "eventsListInfo.dat";
    public static ArrayList<String> allEvents= new ArrayList<String>();
    public static ArrayList<Event> eventArr = new ArrayList<Event> ();

    public static ArrayList<PendingIntent> notificationArray= new ArrayList<PendingIntent>();
    public static  int  notificationIndex = 0;
    public static AlarmManager onTimeNatification;

    public static ArrayList<PendingIntent> updateNotificationArray= new ArrayList<PendingIntent>();
    public static  int  updateNotificationIndex = 0;
    public static AlarmManager updateNatification;


    public static  void addPedInt( PendingIntent intent ){
        notificationArray.add(intent);
    }

    public static int getNotifiIndex(){
        return notificationIndex;
    }

    public static void incIndex(){
        notificationIndex++;
    }

    public static AlarmManager getAlarmManager(){
        return onTimeNatification;
    }

    public static void writeToFile(Context context, String data, String fileName)
    {
        String file_name = fileName.concat(".txt");

        try {
            FileOutputStream outputStream = context.openFileOutput(file_name, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<String> readFromFile(Context context, String fileName)
    {
        String file_name = fileName.concat(".txt");
        ArrayList<String> res = new ArrayList<>();
        String curr;

        try{
            FileInputStream fileInputStream = context.openFileInput(file_name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            curr = bufferedReader.readLine();
            while (curr!=null) {
                res.add(curr);
                curr = bufferedReader.readLine();
            }
            fileInputStream.close();
            bufferedReader.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return res;
    }

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

