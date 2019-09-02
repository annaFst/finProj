package com.example.bt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import android.support.v7.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.bt.models.Event;
import com.example.bt.viewmodels.EventsActivityViewModel;

import java.util.List;


public class EventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    public static boolean updated = false;

    private Button addEvent;
    //static private ArrayAdapter<String> adapter;
    static private MainListAdapter adapter;
    private ListView eventsListView;
    static private String eventName;

    EventsActivityViewModel mEventsActivityViewModel;

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Remove this - local test
//        try {
//            CurrentUserAccount.getInstance().InitCurrentUser();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // TODO: Fix this bug
        mEventsActivityViewModel = new ViewModelProvider(this).get(EventsActivityViewModel.class);
//        mEventsActivityViewModel.getEvents().observe(this, new Observer<List<Event>>() {
//            @Override
//            public void onChanged(List<Event> events) {
//                adapter  = new MainListAdapter(getParent().getApplicationContext(), R.layout.main_list_view, events);
//
//                eventsListView.setAdapter(adapter);
//            }
//        });

        addEvent = (Button)findViewById(R.id.addEvent);
        eventsListView = (ListView)findViewById(R.id.eventsList);

        adapter  = new MainListAdapter(getParent().getApplicationContext(), R.layout.main_list_view, mEventsActivityViewModel.getEvents().getValue());
        eventsListView.setAdapter(adapter);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondScreen();
            }
        });

        eventsListView.setOnItemClickListener(this);


    }
    static public void updateList (String str){
        eventName = str;
        Event event = new Event();
        event.setName(str);
        adapter.add(event);
    }

    public void openSecondScreen(){
        Intent intent  = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent  = new Intent(this, CurrentEventActivity.class);
        intent.putExtra("index",position);
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


}

