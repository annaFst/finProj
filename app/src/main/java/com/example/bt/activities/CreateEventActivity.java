package com.example.bt.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;

import com.example.bt.DBdemo;
import com.example.bt.NotificationReceiver;
import com.example.bt.R;
import com.example.bt.RepeatDialog;
import com.example.bt.UpdateReceiver;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.app.LocalDateTimeConverter;
import com.example.bt.data.Repositories.RepositoryFactory;
import com.example.bt.models.Contact;
import com.example.bt.models.Event;
import com.example.bt.models.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity implements RepeatDialog.repeatDialogListener {

    private static final int CONTACT_LIST_CODE = 0;
    private static final int UPDATE_EVENT_OFFSET = 200;
    public static Calendar myCalendar;


    private boolean setNotifi = false;
    private Button mAddBtn, setAlarmBtn, mDeleteEventBtn, mDoneBtn;
    private ImageButton mDateChoice,mTimeBtn;
    private String title = null ;
    private EditText eventName,date,mTime;
    private EditText enterItem;
    private ImageButton addItem;
    private ListView itemListView;
    static private ArrayAdapter<String> adapter;
    private static Event myEvent;
    private String item = null;
    private Switch onOffAlert;
    private TextView setRepeat, setRepeatType;
    private int alarmDay=0, alarmMonth=0, alarmYear=0, alarmHour=0, alarmMin=0;
    private boolean selectedDate = false, selectedTime= false;
    private ArrayList<Contact> contacts;
    private long interval;

    private PendingIntent alarmIntent;
    private PendingIntent updateIntent;
    List<Event> inputEvents;
    //ListView showToScreen;

    DatePickerDialog myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mAddBtn = (Button)findViewById(R.id.addBt);
        mDoneBtn = (Button)findViewById(R.id.doneBtn);
        mDateChoice = (ImageButton)findViewById(R.id.calendarButton);
        eventName = (EditText)findViewById(R.id.titleBtn);
        date  = (EditText)findViewById(R.id.dateView);
        enterItem = (EditText)findViewById(R.id.enterItem);
        addItem = (ImageButton)findViewById(R.id.addItemBtn);
        mTimeBtn = (ImageButton)findViewById(R.id.timeBtn);
        itemListView = (ListView)findViewById(R.id.itemList);
        mTime = findViewById(R.id.timeStr);
        onOffAlert = findViewById(R.id.alertSwitch);
        setRepeat =findViewById(R.id.repeatBtn);
        setRepeatType = findViewById(R.id.repeatType);

        User currentUser = CurrentUserAccount.getInstance().getCurrentUser();
        myEvent = new Event();
        if (!currentUser.getId().isEmpty())
        {
            myEvent.setEventCreatorId(currentUser.getId());
            myEvent.getParticipants().add(new Contact(currentUser.getName(), currentUser.getId()));
        }


        eventName.addTextChangedListener(nameWatcher);
        enterItem.addTextChangedListener(itemWatcher);

        setRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate && selectedTime) {
                    openDialog();
                }
                else{
                    Toast.makeText(CreateEventActivity.this, "Please select date and time first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondScreen();
            }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTime && selectedDate){
                    updateDb(myEvent);
                    setNotification();
                    Intent intent  = new Intent(CreateEventActivity.this, EventsActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CreateEventActivity.this, "Please select date and time first.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item !=  null) {
                    myEvent.addToList(item);
                    //adapter.add(item);
                    enterItem.setText("");
                    Toast.makeText(CreateEventActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDateChoice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int myDay;
                int myMonth;
                int myYear;

                myCalendar = Calendar.getInstance();
                myDay = myCalendar.get(Calendar.DAY_OF_MONTH);
                myMonth = myCalendar.get(Calendar.MONTH);
                myYear = myCalendar.get(Calendar.YEAR);

                myDate = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String currDate = dayOfMonth + "/" + (month+1) + "/" + year;
                                alarmDay = dayOfMonth;
                                alarmMonth = month;
                                alarmYear = year;
                                date.setText(currDate);
                                LocalDate localDate = LocalDate.of(alarmYear, alarmMonth, alarmDay);
                                selectedDate = true;
                                myEvent.setEventDate(LocalDateTimeConverter.
                                        GetLocalDateInEpochSecond(localDate));

                            }
                },
                        myYear, myMonth, myDay);

                myDate.show();
            }
        });

        mTimeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                int hours = myCalendar.get(Calendar.HOUR);
                int minute = myCalendar.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String currTime = hourOfDay + ":" + minute;
                                alarmHour = hourOfDay;
                                alarmMin = minute;
                                mTime.setText(currTime);
                                LocalTime localTime1 = LocalTime.of(alarmHour,alarmMin);
                                myEvent.setEventTime(LocalDateTimeConverter.
                                        GetLocalTimeInSeconds(localTime1));
                                selectedTime = true;
                            }
                },
                        hours, minute,true);

                tpd.show();
            }
        });

        onOffAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onOffAlert.isChecked()){
                    Intent intent  = new Intent(CreateEventActivity.this, SetAlarmActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("date selected", selectedDate);
                    intent.putExtra("time selected", selectedTime);
                    intent.putExtra("day", alarmDay);
                    intent.putExtra("month", alarmMonth);
                    intent.putExtra("year", alarmYear);
                    intent.putExtra("hour", alarmHour);
                    intent.putExtra("minutes", alarmMin);
                    startActivity(intent);
                }
                else {

                    int alarmInd = myEvent.getEventAlarmIndex();
                    if (alarmInd != -1) {
                        SetAlarmActivity.myAlarm.cancel(SetAlarmActivity.alarmsArray.get(alarmInd));
                        Toast.makeText(CreateEventActivity.this, "Alarm deleted!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void openDialog(){
        RepeatDialog dialog = new RepeatDialog();
        dialog.show(getSupportFragmentManager(),"repeat dialog");
    }

    public void updateDb(Event myEvent) {
        // Add new event
        String eventKey = RepositoryFactory.
                GetRepositoryInstance(RepositoryFactory.RepositoryType.EventRepository)
                .add(myEvent);
        myEvent.setEventId(eventKey);
        CurrentUserAccount.getInstance().getCurrentUser().addEvent(myEvent);
        CurrentUserAccount.getInstance().GetCurrentUserEventList().add(myEvent);
        RepositoryFactory.
                GetRepositoryInstance(RepositoryFactory.RepositoryType.UserRepository)
                .update(CurrentUserAccount.getInstance().getCurrentUser());
    }

    private TextWatcher itemWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            item = enterItem.getText().toString().trim();
            if (item != null) {
                addItem.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            title = eventName.getText().toString().trim();
            myEvent.setName(title);
            if (title != null) {
                mDoneBtn.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void openSecondScreen(){
        Intent intent  = new Intent(this, ContactsListActivity.class);
        intent.putExtra("contacts", (Serializable) myEvent.getParticipants());
        startActivityForResult(intent, CONTACT_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if it is the contact list activity with an OK result
        if (requestCode == CONTACT_LIST_CODE) {
            if (resultCode == RESULT_OK)
            {
                ArrayList<Contact> contacts = (ArrayList)data.getSerializableExtra("contacts");
                myEvent.setParticipants(contacts);
            }
        }
    }

    public static void setAlarmindex(int index){
        myEvent.setAlarmIndex(index);
    }

    public static int getEventIndex(){
        return myEvent.getEventAlarmIndex();
    }

    public void setNotification(){
        Log.d("DEBAG check", "onClick: in set notification ");

        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, alarmHour);
        alarmTime.set(Calendar.MINUTE, alarmMin);
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.DAY_OF_MONTH,alarmDay);
        alarmTime.set(Calendar.MONTH,alarmMonth);
        alarmTime.set(Calendar.YEAR,alarmYear);

        int alarmIndex = DBdemo.notificationIndex + DBdemo.OFFSET;
        long startAlarm = alarmTime.getTimeInMillis();

        Intent intent = new Intent (CreateEventActivity.this, NotificationReceiver.class);
        Log.d("DEBAG", "send index number"+alarmIndex );
        intent.putExtra("index", alarmIndex);
        Log.d("DEBAG", "send event name"+myEvent.getName() );
        intent.putExtra("title", myEvent.getName());
        Log.d("DEBAG", "send event ID"+myEvent.getEventId() );
        intent.putExtra("eventId", myEvent.getEventId());


        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),alarmIndex,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmIndex  < DBdemo.OFFSET + 1 ) {
            DBdemo.onTimeNatification = (AlarmManager)getSystemService(ALARM_SERVICE);
        }
        if (myEvent.getIsRepeat()) {
            Log.d("DEBAG", "in if is a repeat");
            switch (myEvent.getRepeatType()) {

                    case "None":
                        break;
                    case "Daily":
                        DBdemo.onTimeNatification.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY,alarmIntent);
                        break;
                    case "Weekly":
                        DBdemo.onTimeNatification.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,alarmIntent);
                        break;
                    case "Monthly":
                        if (alarmMonth==1 || alarmMonth==3 || alarmMonth==5 || alarmMonth==7 || alarmMonth==8 || alarmMonth==10 || alarmMonth==12){
                            interval  = AlarmManager.INTERVAL_DAY*31;
                        }else if (alarmMonth == 2 ){
                            interval  = AlarmManager.INTERVAL_DAY*28;
                        }
                        else{
                            interval  = AlarmManager.INTERVAL_DAY*30;
                        }
                        DBdemo.onTimeNatification.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),interval,alarmIntent);
                        break;

            }
        }
        else{
            Log.d("DEBAG", "no enter the if -  is a repeat");
            DBdemo.onTimeNatification.set(AlarmManager.RTC_WAKEUP,startAlarm,alarmIntent);
        }

        DBdemo.notificationArray.add(alarmIntent);
        CreateEventActivity.setAlarmindex(alarmIndex);
        myEvent.setNotificationIndex(DBdemo.notificationIndex);
        String saveNotification = alarmHour+ "\n" + alarmMin+"\n" + alarmDay+"\n" + alarmMonth+"\n" + alarmYear + "\n"+ myEvent.getRepeatType()+"\n" + DBdemo.notificationIndex ;
        Log.d("DEBAG ", saveNotification);
        DBdemo.writeToFile(this,saveNotification,myEvent.getName());
        DBdemo.notificationIndex++;
    }

    @Override
    public void applyChoice(boolean none ,boolean daily, boolean weekly, boolean monyhly) {
        if (none){
            Log.d("return from dialog", "none is true");
            setRepeatType.setText("None");
            myEvent.setRepeat(false);
        }
        if(daily){
            Log.d("return from dialog", "daily is true");
            setRepeatType.setText("Daily");
            myEvent.setRepeat(true);
            myEvent.setRepeatType("Daily");
        }
        if(weekly){
            Log.d("return from dialog", "weekly is true");
            setRepeatType.setText("Weekly");
            myEvent.setRepeat(true);
            myEvent.setRepeatType("Weekly");
        }
        if(monyhly){
            Log.d("return from dialog", "monthly is true");
            setRepeatType.setText("Monthly");
            myEvent.setRepeat(true);
            myEvent.setRepeatType("Monthly");
        }
    }
/*
    public void setUpdateNotification(String type){


        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, alarmHour);
        alarmTime.set(Calendar.MINUTE, alarmMin);
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.DAY_OF_MONTH,alarmDay);
        alarmTime.set(Calendar.MONTH,alarmMonth);
        alarmTime.set(Calendar.YEAR,alarmYear);

        int alarmIndex = DBdemo.notificationIndex + OFFSET;
        long startAlarm = alarmTime.getTimeInMillis();

        Intent intent = new Intent (CreateEventActivity.this, UpdateReceiver.class);
        intent.putExtra("index", alarmIndex);
        intent.putExtra("title", myEvent.getName());

        updateIntent = PendingIntent.getBroadcast(getApplicationContext(),alarmIndex,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmIndex  < UPDATE_EVENT_OFFSET+1 ) {
            DBdemo.updateNatification = (AlarmManager)getSystemService(ALARM_SERVICE);
        }

        switch(type){
            case "Daily":
                //DBdemo.updateNatification.setRepeating(AlarmManager.RTC,alarmTime.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,updateIntent);
                break;
            case "Weekly":
                //myAlarm.setRepeating(AlarmManager.RTC,alarmTime.getTimeInMillis(),(AlarmManager.INTERVAL_FIFTEEN_MINUTES*2),alarmIn);
                break;
            case "Monthly":
                //myAlarm.setRepeating(AlarmManager.RTC,alarmTime.getTimeInMillis(),(AlarmManager.INTERVAL_FIFTEEN_MINUTES*2),alarmIn);
                break;
        }

        DBdemo.updateNotificationArray.add(updateIntent);
        CreateEventActivity.setAlarmindex(alarmIndex);
        DBdemo.updateNotificationIndex++;
    }*/
}
