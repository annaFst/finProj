package com.example.bt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class creatActivity extends AppCompatActivity {


    private Button addBt;
    private Button dBtn;
    private String title;
    private String date;
    private EditText txt;
    private ImageButton dateChoice;

    ArrayList <String> names = new ArrayList<String>();
    ListView showToScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat);
        addBt = (Button)findViewById(R.id.addBt);
        dBtn = (Button)findViewById(R.id.doneBtn);
        txt = (EditText)findViewById(R.id.titelBtn);
        dateChoice = (ImageButton)findViewById(R.id.calendarButton);

        showToScreen = (ListView)findViewById(R.id.eventsList);

        EditText date  = (EditText)findViewById(R.id.dateView);
        String dateStr = getIntent().getStringExtra("date");
        if (dateStr != null){
            date.setText(dateStr);
        }


        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondScreen();
            }
        });

        dBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = txt.getText().toString();
                Intent intent  = new Intent(creatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        dateChoice.setOnClickListener(new View.OnClickListener() {
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
}
