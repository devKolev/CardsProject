package com.devkolev.cards.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devkolev.cards.R;
import com.devkolev.cards.adapter.ContactListAdapter;
import com.devkolev.cards.model.Phone_contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends BaseTabFragment {

    private static final int LAYOUT = R.layout.fragment_contacts;

    RecyclerView rv;
    ContactListAdapter adapter;
    List<Phone_contact> phone_contacts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone_contacts = new ArrayList<>();

    }

    public static ContactsFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_contacts));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        rv = view.findViewById(R.id.contactsRecyclerView);

        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ContactListAdapter(phone_contacts);
        rv.setAdapter(adapter);


        getContacts();
//        Toast.makeText(context, "Name: " + phone_contacts.get(0).getName()
//                + " Number: " + phone_contacts.get(0).getPhone(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "Name: " + phone_contacts.get(1).getName()
//                + " Number: " + phone_contacts.get(1).getPhone(), Toast.LENGTH_SHORT).show();
        return view;
    }

    private void getContacts() {


        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        //Запускаем цикл обработчик для каждого контакта:
        if (cursor.getCount() > 0) {


            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                //Получаем имя:
                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                            Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    //и соответствующий ему номер:
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    }
                }

                phone_contacts.add(new Phone_contact(name, phoneNumber));
                adapter.notifyDataSetChanged();
            }

        }
    }
}
