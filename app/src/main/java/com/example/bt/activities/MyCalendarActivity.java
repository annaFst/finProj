package com.example.bt.activities;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt.R;


public class MyCalendarActivity extends AppCompatActivity {

    private String resDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);

        CalendarView clendar = findViewById(R.id.calendarView);

        clendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                resDate = dayOfMonth+"/"+month+"/"+year;

                Intent intent  = new Intent( MyCalendarActivity.this, CreateEventActivity.class);
                intent.putExtra("date",resDate);
                startActivity(intent);
            }
        });
    }
}
