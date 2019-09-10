package com.example.bt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt.models.Contact;

import java.util.ArrayList;


public class ContactsList extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    private ListView contacts;
    private ListView selectedContacts;
    private TextView check;
    private ArrayList<Contact> contactsList = new ArrayList<Contact>() ;
    private ArrayList<Contact> arraySelectedContactsList = new ArrayList<Contact>() ;
    private ArrayList <String> names = new ArrayList<String>();
    private ContactsAdapter selectedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        contacts = (ListView)findViewById(R.id.contactsList);
        selectedContacts = (ListView)findViewById(R.id.selectedContactsList);
        getContacts();
    }


    public void getContacts()
    {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            Cursor cur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            cur.moveToFirst();

            while (cur.moveToNext()){
                String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String memberName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                if (!names.isEmpty()){
                    if (!names.contains(memberName)){
                        names.add(memberName);
                        Contact newContact = new Contact(memberName,phoneNumber);
                        contactsList.add(newContact);
                    }
                }
                else {
                    names.add(memberName);
                    Contact newContact = new Contact(memberName,phoneNumber);
                    contactsList.add(newContact);
                }
            }
            ContactsAdapter adapter = new ContactsAdapter(this, contactsList);
            selectedAdapter = new ContactsAdapter(this, arraySelectedContactsList);
            contacts.setAdapter(adapter);
            selectedContacts.setAdapter(selectedAdapter);
            contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contact selectedContact = contactsList.get(position);
                    if (!arraySelectedContactsList.isEmpty()){
                        if (arraySelectedContactsList.contains(selectedContact)){
                            Toast.makeText(ContactsList.this, "This contact already selected", Toast.LENGTH_LONG).show();
                        }
                        else {
                            arraySelectedContactsList.add(selectedContact);
                        }
                    }
                    else {
                        arraySelectedContactsList.add(selectedContact);
                    }
                    selectedAdapter.notifyDataSetChanged();
                    /*Cursor tempCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                    tempCur.moveToPosition(position);
                    String phoneNumber = tempCur.getString(tempCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String memberName = tempCur.getString(tempCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));*/
                    //tempCur.close();
                }
            });

            /*
            String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER , ContactsContract.CommonDataKinds.Phone._ID};
            int[] to = {R.id.contact_name, R.id.contact_number};

            //android.R.layout.simple_list_item_2
            SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.contact_list_view, cur, from, to);
            contacts.setAdapter(sca);

            cur.moveToFirst();
            Cursor cur2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while(cur.moveToNext()){
                String currContactName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                cur2.moveToFirst();
                while(cur2.moveToNext()){
                    String check = cur2.getString(cur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    if (currContactName.equals(check)){
                        int pos = cur2.getPosition();

                    }
                }
            }
            contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor tempCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                    tempCur.moveToPosition(position);
                    String phoneNumber = tempCur.getString(tempCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String memberName = tempCur.getString(tempCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    check.setText(memberName);
                    tempCur.close();
                }
            });*/
            contacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
