package com.example.bt;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class contactsList extends AppCompatActivity {

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
        //startManagingCursor(cur);

        String [] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME , ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID};
        int [] to = {android.R.id.text1,android.R.id.text2};

        SimpleCursorAdapter sca = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cur,from,to);
        contacts.setAdapter(sca);
        contacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}
