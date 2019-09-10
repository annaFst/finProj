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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class SetAlarm extends AppCompatActivity {

    public static ArrayList<PendingIntent> alarmsArray = new ArrayList<PendingIntent>();
    public static  int  alarmIndex = 0;
    public static  AlarmManager myAlarm;

    private Button setAlarmBt;
    private Button delAlarmBt;
    private int notificationID = 100;
    private TimePicker alarmTimePicker;
    private DatePicker alarmDatePicker;
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
    String title;
    boolean isDaySelected, isTimeSelected;


    DatePickerDialog myDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        title = getIntent().getStringExtra("title");

        mTimeBtn = (ImageButton)findViewById(R.id.timeBtn);
        mDateChoice = (ImageButton)findViewById(R.id.calendarButton);
        date  = (EditText)findViewById(R.id.dateView);
        mTime = (EditText)findViewById(R.id.timeStr);
        setAlarmBt = findViewById(R.id.setBt);
        delAlarmBt = findViewById(R.id.cancelBt);

        //larmTimePicker = findViewById(R.id.timePicker);
        //alarmDatePicker = findViewById(R.id.datePicker);

        isDaySelected = getIntent().getBooleanExtra("date selected", false);
        isTimeSelected = getIntent().getBooleanExtra("time selected", false);
        if(isDaySelected) {
            alarmDay = getIntent().getIntExtra("day", 0);
            alarmMonth = getIntent().getIntExtra("month", 0);
            alarmYear = getIntent().getIntExtra("year", 0);
            String currDate = alarmDay + "/" + (alarmMonth+1) + "/" + alarmYear;
            date.setText(currDate);
        }
        if(isTimeSelected) {
            alarmHour = getIntent().getIntExtra("hour", 0);
            alarmMinute = getIntent().getIntExtra("minutes", 0);
            String currentTime = alarmHour + ":" + alarmMinute;
            mTime.setText(currentTime);
        }

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
                        date.setText(currDate);
                        alarmYear = year;
                        alarmMonth = month;
                        alarmDay = dayOfMonth;
                    }
                },myYear,myMonth,myDay);


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
                        String currentTime = hourOfDay + ":" + minute;
                        mTime.setText(currentTime);
                        alarmHour = hourOfDay;
                        alarmMinute = minute;
                    }
                },myHours,myMinute,true);
                tpd.show();
            }
        });


        setAlarmBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Calendar alarmTime = Calendar.getInstance();
                alarmTime.set(Calendar.HOUR_OF_DAY, alarmHour);
                alarmTime.set(Calendar.MINUTE, alarmMinute);
                alarmTime.set(Calendar.SECOND, 0);
                alarmTime.set(Calendar.DAY_OF_MONTH,alarmDay);
                alarmTime.set(Calendar.MONTH,alarmMonth);
                alarmTime.set(Calendar.YEAR,alarmYear);

                long startAlarm = alarmTime.getTimeInMillis();

                Intent intent = new Intent (SetAlarm.this,AlarmReceiver.class);
                intent.putExtra("index", alarmIndex);
                intent.putExtra("title", title);

                alarmIn = PendingIntent.getBroadcast(getApplicationContext(),alarmIndex,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                if (alarmIndex  < 1 ) {
                    myAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                }
                myAlarm.set(AlarmManager.RTC_WAKEUP,startAlarm,alarmIn);
                //myAlarm.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),(AlarmManager.INTERVAL_FIFTEEN_MINUTES*2),alarmIn);


                alarmsArray.add(alarmIn);
                CreateActivity.setAlarmindex(alarmIndex);
                alarmIndex++;

                Toast.makeText(SetAlarm.this, "Alarm set!", Toast.LENGTH_SHORT).show();
                finish();


            }

        });

        delAlarmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmIndex > 0) {
                    int alarmInd = getIntent().getIntExtra("eventIndex", -1);
                    if (alarmInd != -1) {
                        myAlarm.cancel(alarmsArray.get(alarmInd));
                        Toast.makeText(SetAlarm.this, "Alarm deleted!", Toast.LENGTH_SHORT).show();
                    }
                }
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
