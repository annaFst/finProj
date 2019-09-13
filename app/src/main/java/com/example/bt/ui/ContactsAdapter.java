package com.example.bt.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bt.R;
import com.example.bt.models.Contact;

import java.util.List;

public class ContactsAdapter extends BaseAdapter {
    Context mContext;
    List<Contact> listOfContacts;

    public ContactsAdapter(Context mContext, List<Contact> mContact) {
        this.mContext = mContext;
        this.listOfContacts = mContact;
    }

    @Override
    public int getCount() {
        return listOfContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.contact_list_view,null);

        TextView contact_name = (TextView) view.findViewById(R.id.contact_name);
        TextView contact_number = (TextView) view.findViewById(R.id.contact_number);

        contact_name.setText(listOfContacts.get(position).getContactName());
        contact_number.setText(listOfContacts.get(position).getPhoneNumber());

        view.setTag(listOfContacts.get(position).getContactName());
        return view;
    }
}