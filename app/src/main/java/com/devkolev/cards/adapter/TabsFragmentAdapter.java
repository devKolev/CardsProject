package com.devkolev.cards.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.devkolev.cards.fragment.BaseTabFragment;
import com.devkolev.cards.fragment.ContactsFragment;
import com.devkolev.cards.fragment.MainFragment;

import java.util.HashMap;
import java.util.Map;

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, BaseTabFragment> tabs;
    private Context context;

    public TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;

            tabs = new HashMap<>();
            tabs.put(0, MainFragment.getInstance(context));
            tabs.put(1, ContactsFragment.getInstance(context));


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
