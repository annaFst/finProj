package com.example.bt;

import android.content.Intent;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    private Button moveBtn;

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveBtn = (Button)findViewById(R.id.moveBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondScreen();
            }
        });

        ListView events = (ListView)findViewById(R.id.eventsList);
        String evetnName = getIntent().getStringExtra("name");
        String eventDate = getIntent().getStringExtra("date");
    }

    public void openSecondScreen(){
        Intent intent  = new Intent(this,creatActivity.class);
        startActivity(intent);
    }
}
