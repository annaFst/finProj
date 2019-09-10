package com.example.bt.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v7.app.AppCompatActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt.R;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.app.LocalDateTimeConverter;
import com.example.bt.models.Event;
import com.example.bt.models.Item;

import java.util.ArrayList;
import java.util.List;

public class CurrentEventActivity extends AppCompatActivity {

    private TextView mEventName;
    private TextView mEventDate;
    private TextView mEventTime;
    private String eventId;
    private Event currEvent;
    private ListView items, takenItemsList, membersList;
    static private itemListAdapter adapter;
    static private takenItemListAdapter takenAdapter;
    static private CustomList membersAdapter;
    private ImageButton alarmBtn;
    private Button mDupEvent;
    private Button mDeleteEventBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_event);

        mEventName = findViewById(R.id.eventName);
        mEventDate = findViewById(R.id.currDate);
        mEventTime = findViewById(R.id.currTime);
        items = (ListView)findViewById(R.id.itemsList);
        takenItemsList = (ListView)findViewById(R.id.takenItemsList);
        mDupEvent = findViewById(R.id.dupEvent);
        mDeleteEventBtn = (Button)findViewById(R.id.btnDeleteEvent);
        membersList = findViewById(R.id.membersList);
        alarmBtn = findViewById(R.id.alarm);

        eventId = getIntent().getExtras().getString("eventId");
        mDupEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event temp = new Event();
                temp.copyEvent(currEvent);
                Toast.makeText(CurrentEventActivity.this, "The event has been duplicated", Toast.LENGTH_SHORT).show();

                Intent intent  = new Intent(CurrentEventActivity.this, EventsActivity.class);
                startActivity(intent);

            }
        });

        mDeleteEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUserAccount.getInstance().getCurrentUser().getEvents().remove(currEvent.getEventId());
                CurrentUserAccount.getInstance().GetEventRepository().remove(currEvent);
                CurrentUserAccount.getInstance().GetCurrentUserEventList().remove(currEvent);
                finish();
            }
        });

        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(CurrentEventActivity.this, SetAlarmActivity.class);
                startActivity(intent);
            }
        });

        //currEvent = DBdemo.eventArr.get(eventId);
        currEvent = CurrentUserAccount.getInstance().GetEventIfPresent(eventId);
        mEventName.setText(currEvent.getName());
        mEventDate.setText(LocalDateTimeConverter.
                        GetLocalDateFromEpochSeconds(currEvent.getEventDate()).toString());
        mEventTime.setText(LocalDateTimeConverter.
                        GetLocalTimeFromSeconds(currEvent.getEventTime()).toString());


        adapter  = new itemListAdapter(this,R.layout.items_list, currEvent.getItems());
        items.setAdapter(adapter);

        takenAdapter = new takenItemListAdapter(this,R.layout.taken_items, currEvent.getTakenItemsList());
        takenItemsList.setAdapter(takenAdapter);


        membersAdapter = new CustomList (this, R.layout.items_list,(ArrayList)currEvent.getParticipants());
        membersList.setAdapter(membersAdapter);

        items.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currEvent.takeItem(position);
                adapter.notifyDataSetChanged();
                takenAdapter.notifyDataSetChanged();
            }
        });

    }
    private class CustomList extends ArrayAdapter<String>{
        ArrayList<String> members;

        public CustomList(Context context, int resource, ArrayList<String> list) {
            super(context, resource, list);
            members = list;

        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = CurrentEventActivity.this.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.member_list, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.nameOfContact);
            txtTitle.setText(members.get(position));
            return rowView;
        }
    }

    private class itemListAdapter extends  ArrayAdapter<Item>{
        private int layout;

        public itemListAdapter(Context context, int resource, List<Item> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemHolder lItem = null;
            if (convertView == null ){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ItemHolder listItem = new ItemHolder();
                listItem.item = (TextView)convertView.findViewById(R.id.curr_item);
                listItem.item.setText(getItem(position).getName());
                convertView.setTag(listItem);
            }
            else{
                lItem = (ItemHolder)convertView.getTag();
                lItem.item.setText(getItem(position).getName());
            }
            return convertView;
        }

    }

    private class takenItemListAdapter extends  ArrayAdapter<Item>{
        private int layout;

        public takenItemListAdapter(Context context, int resource, List<Item> objects) {
            super(context, resource, objects);
            layout = resource;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemHolder lItem = null;
            if (convertView == null ){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ItemHolder listItem = new ItemHolder();
                listItem.item = (TextView)convertView.findViewById(R.id.taken_item);
                listItem.item.setText(getItem(position).getName());
                convertView.setTag(listItem);
            }
            else{
                lItem = (ItemHolder)convertView.getTag();
                lItem.item.setText(getItem(position).getName());
            }
            return convertView;
        }

    }
    public class ItemHolder{
        public TextView item;

    }
}
