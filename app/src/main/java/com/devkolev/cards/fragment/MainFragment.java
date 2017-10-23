package com.devkolev.cards.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devkolev.cards.App;
import com.devkolev.cards.R;
import com.devkolev.cards.adapter.CardListAdapter;
import com.devkolev.cards.model.CardDTO;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BaseTabFragment {

    private static final int LAYOUT = R.layout.fragment_main;

    CardListAdapter adapter;
    private Realm mRealm;
    RecyclerView rv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        RealmResults<CardDTO> results = mRealm.where(CardDTO.class).findAll();
        mRealm.beginTransaction();
        results.deleteAllFromRealm();
        mRealm.commitTransaction();

        mRealm.close();
        rv.setAdapter(null);
    }

    public static MainFragment getInstance(Context context) {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_main));

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        rv = view.findViewById(R.id.recycleView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CardListAdapter(mRealm.where(CardDTO.class).findAll(), context);
        rv.setAdapter(adapter);


        get("3", "posts");
        get("1", "comments");
        get("6", "photos");
        get("4", "todos");
        get("10", "users");


        return view;
    }


    private void get(String key, String type_get) {


        App.getApi().getPost(type_get, key).enqueue(new Callback<List<CardDTO>>() {
            @Override
            public void onResponse(Call<List<CardDTO>> call, Response<List<CardDTO>> response) {

                mRealm.beginTransaction();
                mRealm.insert(response.body());
                mRealm.commitTransaction();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CardDTO>> call, Throwable t) {
                Toast.makeText(context, "Bad bad bad", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
