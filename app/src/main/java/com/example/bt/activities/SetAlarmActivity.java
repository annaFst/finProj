package com.example.bt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bt.AlarmReceiver;
import com.example.bt.R;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.models.Event;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class SetAlarmActivity extends AppCompatActivity {

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
    private Event currEvent;
    private boolean first ;


    DatePickerDialog myDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        title = getIntent().getStringExtra("title");
        String eventId = getIntent().getStringExtra("eventId");
        isDaySelected = getIntent().getBooleanExtra("date selected", false);
        isTimeSelected = getIntent().getBooleanExtra("time selected", false);
        if (!isDaySelected && !isTimeSelected) {
            currEvent = CurrentUserAccount.getInstance().GetEventIfPresent(eventId);
            currEvent.setAlarm(true);
            CurrentUserAccount.getInstance().GetEventRepository().update(currEvent);
            //checkIfSet();
        }
        else{
            first = true;
        }

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

                myDate = new DatePickerDialog(SetAlarmActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                TimePickerDialog tpd = new TimePickerDialog(SetAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

                Intent intent = new Intent (SetAlarmActivity.this, AlarmReceiver.class);
                intent.putExtra("index", alarmIndex);
                intent.putExtra("title", title);

                alarmIn = PendingIntent.getBroadcast(getApplicationContext(),alarmIndex,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                if (alarmIndex  < 1 ) {
                    myAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                }
                myAlarm.set(AlarmManager.RTC_WAKEUP,startAlarm,alarmIn);
                //myAlarm.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),(AlarmManager.INTERVAL_FIFTEEN_MINUTES*2),alarmIn);


                alarmsArray.add(alarmIn);
                if (first){
                    CreateEventActivity.setAlarmindex(alarmIndex);
                }
                else {
                    currEvent.setAlarmIndex(alarmIndex);
                }

                //String saveNotification = alarmHour + "\n" + alarmMinute + "\n" + alarmDay + "\n" + alarmMonth + "\n" + alarmYear + "\n" + currEvent.getRepeatType() + "\n" + alarmIndex;
                //writeToFile(SetAlarmActivity.this, saveNotification, currEvent.getName());
                alarmIndex++;

                Toast.makeText(SetAlarmActivity.this, "Alarm set!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SetAlarmActivity.this, "Alarm deleted!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static void writeToFile(Context context, String data, String fileName)
    {
        String alarmStr = "Alarm";
        String temp = alarmStr.concat(fileName);
        String file_name = temp.concat(".txt");

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
        String alarmStr = "Alarm";
        String temp = alarmStr.concat(fileName);
        String file_name = temp.concat(".txt");
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

    public void checkIfSet() {
        int hour;
        int minute;
        int day;
        int month;
        int year;
        String type;
        int index;
        // in the array :
        //alarmHour, alarmMin,  alarmDay, alarmMonth, alarmYear, RepeatType(), notificationIndex
        ArrayList<String> res = readFromFile(SetAlarmActivity.this, currEvent.getName());
        if (!res.isEmpty()) {
            hour = parseInt(res.get(0));
            minute = parseInt(res.get(1));
            day = parseInt(res.get(2));
            month = parseInt(res.get(3));
            year = parseInt(res.get(4));
            type = res.get(5);
            index = parseInt(res.get(6));

            String currDate = day + "/" + (month+1) + "/" + year;
            date.setText(currDate);
            String currentTime = hour + ":" + minute;
            mTime.setText(currentTime);

        }
    }
}
