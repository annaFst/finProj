package com.example.bt.activities;

import android.app.DatePickerDialog;
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

import com.example.bt.R;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.app.LocalDateTimeConverter;
import com.example.bt.data.Repositories.RepositoryFactory;
import com.example.bt.models.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    private static final int CONTACT_LIST_CODE = 0;

    private Button mAddBtn, setAlarmBtn, mDeleteEventBtn;
    private Button mDoneBtn;
    private ImageButton mDateChoice;
    private ImageButton mTimeBtn;
    private String title = null ;
    private EditText eventName;
    private EditText date;
    private EditText enterItem;
    private EditText mTime;
    private ImageButton addItem;
    private ListView itemListView;
    static private ArrayAdapter<String> adapter;
    private static Event myEvent;
    private String item = null;
    private Switch onOffAlert;
    private TextView setAlert;
    private int alarmDay=0, alarmMonth=0, alarmYear=0, alarmHour=0, alarmMin=0;
    private boolean selectedDate = false, selectedTime= false;

    public static Calendar myCalendar;
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


        myEvent = new Event();
        if (!CurrentUserAccount.getInstance().getCurrentUser().getId().isEmpty())
            myEvent.setEventCreatorId(CurrentUserAccount.getInstance().getCurrentUser().getId());

        eventName.addTextChangedListener(nameWatcher);
        enterItem.addTextChangedListener(itemWatcher);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondScreen();
            }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDb(myEvent);
                Intent intent  = new Intent(CreateEventActivity.this, EventsActivity.class);
                startActivity(intent);
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
                    //onOffAlert.setChecked(true);
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

    private void updateDb(Event myEvent) {
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
        intent.putEx
        startActivityForResult(intent, CONTACT_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if it is the contact list activity with an OK result
        if (requestCode == CONTACT_LIST_CODE) {
            if (resultCode == RESULT_OK) {

                ArrayList<String> names = data.getStringArrayListExtra("contacts names");
                ArrayList<String> phones = data.getStringArrayListExtra("contacts phones");

                myEvent.setParticipants(names);
                myEvent.setParticipantsPhoneNumbers(phones);
            }
        }
    }

    public static void setAlarmindex(int index){
        myEvent.setAlarmIndex(index);
    }

    public static int getEventIndex(){
        return myEvent.getEventAlarmIndex();
    }
}
