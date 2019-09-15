package com.example.bt.activities;

import android.content.Context;
import android.content.DialogInterface;
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
//import android.support.v7.app.AppCompatActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt.R;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.app.LocalDateTimeConverter;
import com.example.bt.data.Repositories.RepositoryFactory;
import com.example.bt.models.Contact;
import com.example.bt.models.Event;
import com.example.bt.models.Item;
import com.example.bt.models.User;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CurrentEventActivity extends AppCompatActivity {

    private TextView mEventName, mEventDate, mEventTime;
    private String eventId;
    private Event currEvent;
    private ListView itemsListView, takenItemsListView, membersList;
    static private itemListAdapter adapter;
    static private takenItemListAdapter takenAdapter;
    static private CustomList membersAdapter;
    private ImageButton alarmBtn;
    private Button mParticipants;
    private Button mDeleteEventBtn;
    private List<Item> takenItems = new ArrayList<>();
    private List<Item> nonTakenItems = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_event);

        mEventName = findViewById(R.id.eventName);
        mEventDate = findViewById(R.id.currDate);
        mEventTime = findViewById(R.id.currTime);
        itemsListView = (ListView)findViewById(R.id.itemsList);
        takenItemsListView = (ListView)findViewById(R.id.takenItemsList);
        mParticipants = findViewById(R.id.participants);
        mDeleteEventBtn = (Button)findViewById(R.id.btnDeleteEvent);
        alarmBtn = findViewById(R.id.alarm);

        eventId = getIntent().getExtras().getString("eventId");

        mParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(CurrentEventActivity.this, ContactsListActivity.class);
                intent.putExtra("contacts", (Serializable) currEvent.getParticipants());
                startActivity(intent);
            }
        });

        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(CurrentEventActivity.this, SetAlarmActivity.class);
                startActivity(intent);
            }
        });

        currEvent = CurrentUserAccount.getInstance().GetEventIfPresent(eventId);
        populateItemLists();
        populateTextView();
        setAdapters();

        takenItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                Item curr = takenItems.get(position);
                builder.setMessage(curr.getTakenBy().getContactName())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

            }
        });

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item currItem = nonTakenItems.get(position);
                // Update item state
                User currUser = CurrentUserAccount.getInstance().getCurrentUser();
                Contact itemTaker =
                        new Contact(currUser.getName(), currUser.getId());
                currItem.setTaken(true);
                currItem.setTakenBy(itemTaker);

                // Update local lists and adapters
                takenItems.add(currItem);
                nonTakenItems.remove(currItem);
                adapter.notifyDataSetChanged();
                takenAdapter.notifyDataSetChanged();

            }
        });

        mDeleteEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
            }
        });
    }

    private void populateItemLists()
    {
        for (Item item : currEvent.getItems())
        {
            if (item.getTaken()){
                takenItems.add(item);
            }
            else{
                nonTakenItems.add(item);
            }
        }
    }

    private void setAdapters()
    {
        adapter  = new itemListAdapter(this,R.layout.items_list, nonTakenItems);
        itemsListView.setAdapter(adapter);

        takenAdapter = new takenItemListAdapter(this,R.layout.taken_items, takenItems);
        takenItemsListView.setAdapter(takenAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void populateTextView()
    {
        mEventName.setText(currEvent.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        mEventDate.setText(LocalDateTimeConverter.
                GetLocalDateFromEpochSeconds(currEvent.getEventDate()).format(formatter));
        mEventTime.setText(LocalDateTimeConverter.
                GetLocalTimeFromSeconds(currEvent.getEventTime()).toString());
    }

    @Override
    public void onBackPressed() {
        CurrentUserAccount.getInstance().GetEventRepository().update(currEvent);
        finish();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    CurrentUserAccount.getInstance().getCurrentUser().getEvents().remove(currEvent.getEventId());
                    CurrentUserAccount.getInstance().GetCurrentUserEventList().remove(currEvent);
                    CurrentUserAccount.getInstance().GetEventRepository().remove(currEvent);
                    RepositoryFactory.
                            GetRepositoryInstance(RepositoryFactory.RepositoryType.UserRepository)
                            .update(CurrentUserAccount.getInstance().getCurrentUser());
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };


    private class CustomList extends ArrayAdapter<Contact>{
        ArrayList<Contact> members;

        public CustomList(Context context, int resource, ArrayList<Contact> list) {
            super(context, resource, list);
            members = list;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = CurrentEventActivity.this.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.member_list, parent, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.nameOfContact);
            txtTitle.setText(members.get(position).getContactName());
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
