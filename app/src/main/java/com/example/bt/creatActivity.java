package com.example.bt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class creatActivity extends AppCompatActivity {

    private Button mAddBtn;
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
    private Event myEvent;
    private String item = null;


   // ArrayList <String> inputEvents;
    //ListView showToScreen;

    Calendar myCalendar;
    DatePickerDialog myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat);
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


        myEvent = new Event();
        DBdemo.eventArr.add(myEvent);
        //adapter  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, myEvent.getItems());
       // itemListView.setAdapter(adapter);

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
                MainActivity.updateList(title);
                Intent intent  = new Intent(creatActivity.this,MainActivity.class);
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
                    Toast.makeText(creatActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mDateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int myDay;
                int myMonth;
                int myYear;

                myCalendar = Calendar.getInstance();
                myDay = myCalendar.get(Calendar.DAY_OF_MONTH);
                myMonth = myCalendar.get(Calendar.MONTH);
                myYear = myCalendar.get(Calendar.YEAR);


                myDate = new DatePickerDialog(creatActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String currDate = dayOfMonth + "/" + (month+1) + "/" + year;
                        date.setText(currDate);
                        myEvent.setDate(currDate);
                    }
                },myYear,myMonth,myDay);
                myDate.show();

            }
        });

        mTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                int hours= myCalendar.get(Calendar.HOUR);
                int minute = myCalendar.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(creatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String cuttTime = hourOfDay + ":" + minute;
                        mTime.setText(cuttTime);
                        myEvent.setTime(cuttTime);
                    }
                },hours,minute,true);
                tpd.show();
            }
        });
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
        Intent intent  = new Intent(this,contactsList.class);
        startActivity(intent);
    }
/*
    private Button mAddBtn;
    private Button mDoneBtn;
    private String mTitle;
    private EditText txt;
    private ImageButton mDateChoice;

    ArrayList <String> names = new ArrayList<String>();
    ListView showToScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat);
        mAddBtn = (Button)findViewById(R.id.addBt);
        mDoneBtn = (Button)findViewById(R.id.doneBtn);
        txt = (EditText)findViewById(R.id.titleBtn);
        mDateChoice = (ImageButton)findViewById(R.id.calendarButton);

        showToScreen = (ListView)findViewById(R.id.eventsList);

        EditText date  = (EditText)findViewById(R.id.dateView);
        String dateStr = getIntent().getStringExtra("date");
        if (dateStr != null){
            date.setText(dateStr);
        }


        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondScreen();
            }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = txt.getText().toString();
                Intent intent  = new Intent(creatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mDateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
    }

    public void openCalendar(){
        Intent intent  = new Intent(this,myCalendar.class);
        startActivity(intent);
    }

    public void openSecondScreen(){
        Intent intent  = new Intent(this,contactsList.class);
        startActivity(intent);
    }
    */
}
