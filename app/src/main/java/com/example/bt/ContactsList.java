package com.example.bt;

import android.database.Cursor;
import android.provider.ContactsContract;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;


public class ContactsList extends AppCompatActivity {

    private ListView contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        contacts = (ListView)findViewById(R.id.contactsList);
        getContacts();
    }

    public void getContacts()
    {
        Cursor cur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);

        String [] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME , ContactsContract.CommonDataKinds.Phone.NUMBER /*, ContactsContract.CommonDataKinds.Phone._ID*/};
        //int [] to = {android.R.id.text1,android.R.id.text2};
        int [] to = {R.id.contact_name,R.id.contact_number};

        //android.R.layout.simple_list_item_2
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this,R.layout.contact_list_view , cur, from, to, 0);
        contacts.setAdapter(sca);
        contacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}
