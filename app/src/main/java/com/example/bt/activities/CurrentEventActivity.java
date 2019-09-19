package com.example.bt.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v7.app.AppCompatActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bt.R;
import com.example.bt.activities.fragments.RepeatDialog;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.app.LocalDateTimeConverter;
import com.example.bt.data.Repositories.RepositoryFactory;
import com.example.bt.models.Contact;
import com.example.bt.models.Event;
import com.example.bt.models.Item;
import com.example.bt.models.User;
import com.example.bt.viewmodels.CurrentEventActivityViewModel;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CurrentEventActivity extends AppCompatActivity implements RepeatDialog.repeatDialogListener{

    private static final int CONTACT_LIST = 1;

    private CurrentEventActivityViewModel mCurrentEventViewModel;
    private TextView mEventName, mEventDate, mEventTime, repeat, repeatType;
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
    private EditText enterItem;
    private ImageButton addItem;
    private String item = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_event);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mCurrentEventViewModel = new ViewModelProvider(this).get(CurrentEventActivityViewModel.class);

        mEventName = findViewById(R.id.eventName);
        mEventDate = findViewById(R.id.currDate);
        mEventTime = findViewById(R.id.currTime);
        itemsListView = (ListView)findViewById(R.id.itemsList);
        takenItemsListView = (ListView)findViewById(R.id.takenItemsList);
        mParticipants = findViewById(R.id.participants);
        mDeleteEventBtn = (Button)findViewById(R.id.btnDeleteEvent);
        alarmBtn = findViewById(R.id.alarm);
        enterItem = (EditText)findViewById(R.id.enterItem);
        addItem = (ImageButton)findViewById(R.id.addItemBtn);
        repeat = findViewById(R.id.repeatBtn);
        repeatType = findViewById(R.id.repeatType);

        enterItem.addTextChangedListener(itemWatcher);

        eventId = getIntent().getExtras().getString("eventId");
        //repeat.setText(currEvent.getRepeatType());

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item !=  null) {
                    currEvent.addToList(item);
                    nonTakenItems.add(currEvent.getItems().get(currEvent.getItems().size()-1));
                    //adapter.add(item);
                    enterItem.setText("");
                    CurrentUserAccount.getInstance().GetEventRepository().update(currEvent);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(CurrentEventActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(CurrentEventActivity.this, ContactsListActivity.class);
                intent.putExtra("contacts", (Serializable) currEvent.getParticipants());
                startActivityForResult(intent, CONTACT_LIST);
            }
        });

        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(CurrentEventActivity.this, SetAlarmActivity.class);
                intent.putExtra("title" , currEvent.getName());
                intent.putExtra("eventId" , eventId);
                startActivity(intent);
            }
        });

        //currEvent = CurrentUserAccount.getInstance().GetEventIfPresent(eventId);
        mCurrentEventViewModel.getEvent(eventId).observe(this, new Observer<Event>()
        {
            @Override
            public void onChanged(Event event)
            {
                currEvent = event;
                repeatType.setText(currEvent.getRepeatType());

                populateItemLists();
                populateTextView();
                setAdapters();
            }
        });


//        populateItemLists();
//        populateTextView();
//        setAdapters();

        takenItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                Item curr = takenItems.get(position);
                builder.setMessage(curr.getTakenBy().getContactName())
                        .setMessage("This item taken by" + curr.getTakenBy().getContactName())
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
                CurrentUserAccount.getInstance().GetEventRepository().update(currEvent);
            }
        });

        itemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final Item currItem = nonTakenItems.get(position);
                builder.setTitle("Delete Item")
                        .setMessage("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nonTakenItems.remove(currItem);
                                currEvent.removeFromList(currItem);
                                adapter.notifyDataSetChanged();
                                CurrentUserAccount.getInstance().GetEventRepository().update(currEvent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if it is the contact list activity with an OK result
        if (requestCode == CONTACT_LIST) {
            if (resultCode == RESULT_OK)
            {
                ArrayList<Contact> contacts = (ArrayList)data.getSerializableExtra("contacts");
                currEvent.setParticipants(contacts);
                CurrentUserAccount.getInstance().GetEventRepository().update(currEvent);
            }
        }
    }

    private void openDialog(){
        RepeatDialog dialog = new RepeatDialog();
        dialog.show(getSupportFragmentManager(),"repeat dialog");
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

    @Override
    public void applyChoice(boolean none ,boolean daily, boolean weekly, boolean monyhly) {
        if (none){
            Log.d("return from dialog", "none is true");
            repeatType.setText("None");
            currEvent.setRepeat(false);
        }
        if(daily){
            Log.d("return from dialog", "daily is true");
            repeatType.setText("Daily");
            currEvent.setRepeat(true);
            currEvent.setRepeatType("Daily");
        }
        if(weekly){
            Log.d("return from dialog", "weekly is true");
            repeatType.setText("Weekly");
            currEvent.setRepeat(true);
            currEvent.setRepeatType("Weekly");
        }
        if(monyhly){
            Log.d("return from dialog", "monthly is true");
            repeatType.setText("Monthly");
            currEvent.setRepeat(true);
            currEvent.setRepeatType("Monthly");
        }
    }
}
