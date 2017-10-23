package com.devkolev.cards.adapter;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devkolev.cards.R;
import com.devkolev.cards.model.Phone_contact;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.CardViewHolder> {

    List<Phone_contact> phone_contacts;

    public ContactListAdapter(List<Phone_contact> phone_contacts){
        this.phone_contacts = phone_contacts;

    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_card_item, parent, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.contact_name.setText(phone_contacts.get(position).getName());
        holder.phone_number.setText(phone_contacts.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return phone_contacts.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView contact_name;
        TextView phone_number;
        ImageView contact_image;


        public CardViewHolder(View itemView) {
            super(itemView);


            cv = itemView.findViewById(R.id.cv);
            contact_name = itemView.findViewById(R.id.contact_name);
            phone_number = itemView.findViewById(R.id.contact_phone_number);
            contact_image = itemView.findViewById(R.id.contact_image);

        }
    }
}
