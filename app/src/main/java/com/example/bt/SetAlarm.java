package com.example.bt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Set;

public class SetAlarm extends AppCompatActivity {

    private Button setAlermBt;
    private Button delAlermBt;
    private int notificationID = 100;
    private TimePicker alarmTimePicker;
    private DatePicker alarmDatePicker;
    public static  AlarmManager myAlarm;
    private PendingIntent alarmIn;
    public static Calendar myCalendar;
    private ImageButton mDateChoice;
    private ImageButton mTimeBtn;
    private EditText mTime;
    private EditText date;
    int myDay,alarmDay;
    int myMonth,alarmMonth;
    int myYear,alarmYear;
    int myHours,alarmHour;
    int myMinute,alarmMinute;

    private EditText Dcheck;
    private EditText Tcheck;

    DatePickerDialog myDate;

   /*
    public static AlarmManager getMyAlarm(){
        return myAlarm;
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        mTimeBtn = (ImageButton)findViewById(R.id.timeBtn);
        mDateChoice = (ImageButton)findViewById(R.id.calendarButton);
        date  = (EditText)findViewById(R.id.dateView);
        mTime = (EditText)findViewById(R.id.timeStr);
        setAlermBt = findViewById(R.id.setBt);
        delAlermBt = findViewById(R.id.cancelBt);

        Dcheck = (EditText)findViewById(R.id.dateChack);
        Tcheck = (EditText)findViewById(R.id.timeCheck);

        //larmTimePicker = findViewById(R.id.timePicker);
        //alarmDatePicker = findViewById(R.id.datePicker);

        mDateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myCalendar = Calendar.getInstance();
                myDay = myCalendar.get(Calendar.DAY_OF_MONTH);
                myMonth = myCalendar.get(Calendar.MONTH);
                myYear = myCalendar.get(Calendar.YEAR);


                myDate = new DatePickerDialog(SetAlarm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String currDate = dayOfMonth + "/" + (month+1) + "/" + year;
                        alarmYear = year;
                        alarmMonth = month+1;
                        alarmDay = dayOfMonth;
                        date.setText(currDate);
                    }
                },myYear,myMonth,myDay);

                //String temp = myDay + "/" + (myMonth+1) + "/" + myYear;
                //Dcheck.setText(temp);

                myDate.show();




            }
        });

        mTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                myHours= myCalendar.get(Calendar.HOUR);
                myMinute = myCalendar.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(SetAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String cuttTime = hourOfDay + ":" + minute;
                        alarmHour = hourOfDay;
                        alarmMinute = minute;
                        mTime.setText(cuttTime);
                    }
                },myHours,myMinute,false);

               // String temp = myHours + "/" + myMinute;
                //Tcheck.setText(temp);

                tpd.show();
            }
        });


        setAlermBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               /* int day = alarmDatePicker.getDayOfMonth();
                int month = alarmDatePicker.getMonth();
                int year = alarmDatePicker.getYear();
                int hour = alarmTimePicker.getCurrentHour();
                int minute = alarmTimePicker.getCurrentMinute();*/

                Calendar alarmTime = Calendar.getInstance();

                alarmTime.set(Calendar.HOUR_OF_DAY, alarmHour);
                alarmTime.set(Calendar.MINUTE, alarmMinute);
                alarmTime.set(Calendar.SECOND, 0);
                alarmTime.set(alarmYear,alarmMonth,alarmDay);
                long startAlarm = alarmTime.getTimeInMillis();

                Intent intent = new Intent (SetAlarm.this,AlarmReceiver.class);

                alarmIn = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                myAlarm = (AlarmManager)getSystemService(ALARM_SERVICE);
                myAlarm.set(AlarmManager.RTC_WAKEUP,startAlarm,alarmIn);
                //myAlarm.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY,alarmIn);

                Toast.makeText(SetAlarm.this, "Alarm set!", Toast.LENGTH_SHORT).show();

            }

        });

        delAlermBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlarm.cancel(alarmIn);
            }
        });
    }


/*
    @Override
    public void onClick(View view){
        final String myActivityTitle = getIntent().getStringExtra("title");

        Intent intent = new Intent(SetAlarm.this, AlarmReceiver.class);
        intent.putExtra("notificationId",notificationID);
        intent.putExtra("title", myActivityTitle);

        PendingIntent alarmIn = PendingIntent.getBroadcast(SetAlarm.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager myAlarm = (AlarmManager)getSystemService(ALARM_SERVICE);

        switch(view.getId()) {
            case R.id.setBt:
                alarmTime = (TimePicker)findViewById(R.id.timePicker);

                int hours = alarmTime.getCurrentHour();
                int minutes = alarmTime.getCurrentMinute();

                Calendar startT = Calendar.getInstance();
                startT.set(Calendar.HOUR_OF_DAY, hours);
                startT.set(Calendar.MINUTE, minutes);
                startT.set(Calendar.SECOND, 0);

                long alarmStart = startT.getTimeInMillis();

                myAlarm.set(AlarmManager.RTC_WAKEUP, alarmStart, alarmIn);

                Toast.makeText(SetAlarm.this, "Alarm set!", Toast.LENGTH_SHORT).show();
                break;
        }

    }*/
}
