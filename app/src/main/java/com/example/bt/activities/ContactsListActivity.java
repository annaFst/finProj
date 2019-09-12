package com.example.bt.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt.R;
import com.example.bt.models.Contact;
import com.example.bt.ui.ContactsAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ContactsListActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private ListView contacts;
    private ListView selectedContacts;
    private TextView check;
    private List<Contact> contactsList = new ArrayList<>() ;
    private List<Contact> arraySelectedContactsList = new ArrayList<>() ;
    private List<String> names = new ArrayList<>();
    private ContactsAdapter selectedAdapter;
    private Button Done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        contacts = (ListView)findViewById(R.id.contactsList);
        selectedContacts = (ListView)findViewById(R.id.selectedContactsList);
        Done = (Button)findViewById(R.id.DoneBtn);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent();
                goBack.putExtra("contacts", (Serializable) arraySelectedContactsList);
                setResult(RESULT_OK, goBack);

                finish();
            }
        });

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
                            Toast.makeText(ContactsListActivity.this, "This contact already selected", Toast.LENGTH_LONG).show();
                        }
                        else {
                            arraySelectedContactsList.add(selectedContact);
                        }
                    }
                    else {
                        arraySelectedContactsList.add(selectedContact);
                    }
                    selectedAdapter.notifyDataSetChanged();
                }
            });

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
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
