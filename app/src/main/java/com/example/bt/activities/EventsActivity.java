package com.example.bt.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import android.support.v7.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt.DBdemo;
import com.example.bt.NotificationReceiver;
import com.example.bt.R;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.app.LocalDateTimeConverter;
import com.example.bt.models.Event;
import com.example.bt.ui.EventAdapter;
import com.example.bt.viewmodels.EventsActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;


public class EventsActivity extends AppCompatActivity implements EventAdapter.ClickListener<Event> {

    public static boolean updated = false;
    private static boolean reSetNotifi = false;

    private Button addEvent;
    private Button btnLogout;
    //static private ArrayAdapter<String> adapter;
    static private MainListAdapter adapter;
    private ListView eventsListView;
    static private String eventName;
    private RecyclerView mRecyclerView;

    EventsActivityViewModel mEventsActivityViewModel;

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEventsActivityViewModel = new ViewModelProvider(this).get(EventsActivityViewModel.class);

        boolean isLoggedIn = mEventsActivityViewModel.checkLoggedInUser();

        String eventTitle = getIntent().getStringExtra("Event");

        if (eventTitle!=null)
                //TODO reset item list of eventTitle event
        if (!isLoggedIn)
        {
            openLoginActivity();
        }

        // TODO: Fix this bug
        LayoutInflater inflater = LayoutInflater.from(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        btnLogout = findViewById(R.id.btnLogout);


        mEventsActivityViewModel.getEvents().observe(this, new Observer<Set<Event>>() {
            @Override
            public void onChanged(Set<Event> events) {
                EventAdapter eventAdapter = new EventAdapter(new ArrayList<>(events));
                if (!reSetNotifi) {
                    for (Event event : events) {
                        reSetNotifi = true;
                        reSetNotification(event);
                    }
                }
                eventAdapter.setOnItemClickListener(EventsActivity.this);
                mRecyclerView.setAdapter(eventAdapter);
            }
        });

        addEvent = (Button)findViewById(R.id.addEvent);
        //eventsListView = (ListView)findViewById(R.id.eventsList);

//        adapter  = new MainListAdapter
//                (this, R.layout.main_list_view, mEventsActivityViewModel.getEvents().getValue());
//        eventsListView.setAdapter(adapter);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondScreen();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                openLoginActivity();
            }
        });

    }

    private void openLoginActivity()
    {
        Intent intent  = new Intent(EventsActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public void openSecondScreen(){
        Intent intent  = new Intent(EventsActivity.this, CreateEventActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(String key, View v) {
        Intent intent  = new Intent(this, CurrentEventActivity.class);
        intent.putExtra("eventId", key);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(String key, View v) {
        Intent intent  = new Intent(this, CurrentEventActivity.class);
        intent.putExtra("eventId", key);
        startActivity(intent);
    }


    private class MainListAdapter extends  ArrayAdapter<Event>{
        private int layout;

        public MainListAdapter(Context context, int resource, List<Event> objects) {
            super(context, resource, objects);
            layout = resource;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            listItemHolder lItem = null;
            if (convertView == null ){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                listItemHolder listItem = new listItemHolder();
                listItem.item = (TextView)convertView.findViewById(R.id.text_list_item);
                listItem.item.setText(getItem(position).getName());
                convertView.setTag(listItem);
            }
            else{
                lItem = (listItemHolder)convertView.getTag();
                lItem.item.setText(getItem(position).getName());
            }
            return convertView;
        }

    }

    public class listItemHolder{
        public TextView item;

    }

    private void  reSetNotification( Event event) {
        int hour ;
        int minute ;
        int day ;
        int month ;
        int year ;
        String type ;
        int index ;
        // in the array :
        //alarmHour, alarmMin,  alarmDay, alarmMonth, alarmYear, RepeatType(), notificationIndex
        ArrayList<String> res = DBdemo.readFromFile(EventsActivity.this,event.getName());
        if (!res.isEmpty()) {
            hour = parseInt(res.get(0));
            minute = parseInt(res.get(1));
            day = parseInt(res.get(2));
            month = parseInt(res.get(3));
            year = parseInt(res.get(4));
            type = res.get(5);
            index = parseInt(res.get(6));
            //Log.d("DEBAG EVENTS ACTIVITY", "the string from file is " + hour + " " + minute + " " + day + " " + month + " " + year + " " + type + " " + index);


            Calendar alarmTime = Calendar.getInstance();
            alarmTime.set(Calendar.HOUR_OF_DAY, hour);
            alarmTime.set(Calendar.MINUTE, minute);
            alarmTime.set(Calendar.SECOND, 0);
            alarmTime.set(Calendar.DAY_OF_MONTH, day);
            alarmTime.set(Calendar.MONTH, month);
            alarmTime.set(Calendar.YEAR, year);

            int alarmIndex = index+ DBdemo.OFFSET;
            long startAlarm = alarmTime.getTimeInMillis();

            Intent intent = new Intent(EventsActivity.this, NotificationReceiver.class);
            intent.putExtra("index", alarmIndex);
            intent.putExtra("title", event.getName());

            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmIndex, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (alarmIndex < DBdemo.OFFSET + 1) {
                DBdemo.onTimeNatification = (AlarmManager) getSystemService(ALARM_SERVICE);
            }

            switch (type) {

                    case "None":
                        DBdemo.onTimeNatification.set(AlarmManager.RTC_WAKEUP, startAlarm, alarmIntent);
                        break;
                    case "Daily":
                        DBdemo.onTimeNatification.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
                        break;
                    case "Weekly":
                        DBdemo.onTimeNatification.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, alarmIntent);
                        break;
                    case "Monthly":
                        long interval;
                        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                            interval = AlarmManager.INTERVAL_DAY * 31;
                        } else if (month == 2) {
                            interval = AlarmManager.INTERVAL_DAY * 28;
                        } else {
                            interval = AlarmManager.INTERVAL_DAY * 30;
                        }
                        DBdemo.onTimeNatification.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), interval, alarmIntent);
                        break;
            }

            DBdemo.notificationArray.add(alarmIntent);
            CreateEventActivity.setAlarmindex(alarmIndex);
            event.setNotificationIndex(index);
            String saveNotification = hour + "\n" + minute + "\n" + day + "\n" + month + "\n" + year + "\n" + type + "\n" + index;
            Log.d("DEBAG ", saveNotification);
            DBdemo.writeToFile(this, saveNotification, event.getName());
            DBdemo.notificationIndex++;
        }

    }

}

