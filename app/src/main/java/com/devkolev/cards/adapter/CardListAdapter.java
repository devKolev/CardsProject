package com.devkolev.cards.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.devkolev.cards.App;
import com.devkolev.cards.R;
import com.devkolev.cards.model.CardDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {


    private Realm mRealm;
    private static String count = null;
    private Context context;

    private RealmResults<CardDTO> cardDTO;


    public CardListAdapter(RealmResults<CardDTO> cardDTO, Context context) {
        this.cardDTO = cardDTO;
        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int position) {
        setData(holder, position);


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.editText.getText().length() != 0) {

                    switch (Integer.parseInt(holder.title.getHint().toString())) {
                        case 1:
                            count = "posts";
                            break;
                        case 2:
                            count = "comments";
                            break;
                        case 3:
                            count = "todos";
                            break;
                        case 4:
                            count = "photos";
                            break;
                        case 5:
                            count = "users";
                            break;
                    }
                    App.getApi().getPost(count, holder.editText.getText().toString()).enqueue(new Callback<List<CardDTO>>() {
                        @Override
                        public void onResponse(Call<List<CardDTO>> call, final Response<List<CardDTO>> response) {

                            //Получение новых данных и их замена в БД
                            mRealm.beginTransaction();
                            setData(holder, position);
                            try {
                                cardDTO.get(position).setTitle(response.body().get(0).getTitle());
                                cardDTO.get(position).setBody(response.body().get(0).getBody());
                                cardDTO.get(position).setEmail(response.body().get(0).getEmail());
                                cardDTO.get(position).setCompleted(response.body().get(0).getCompleted());
                                cardDTO.get(position).setUrl(response.body().get(0).getUrl());

                                cardDTO.get(position).setUsername(response.body().get(0).getUsername());
                                cardDTO.get(position).setName(response.body().get(0).getName());
                                cardDTO.get(position).setPhone(response.body().get(0).getPhone());
                                cardDTO.get(position).setWebsite(response.body().get(0).getWebsite());
                                cardDTO.get(position).getAddress().setStreet(response.body().get(0).getAddress().getStreet());
                                cardDTO.get(position).getCompany().setName(response.body().get(0).getCompany().getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mRealm.commitTransaction();

                            //Установка нового изображения
                            Picasso.with(context)
                                    .load(cardDTO.get(position).getUrl())
                                    .into(holder.image);

                            //Очистка editText
                            holder.editText.getText().clear();


                        }

                        @Override
                        public void onFailure(Call<List<CardDTO>> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void setData(CardViewHolder holder, int position) {


        String thumbUrl = cardDTO.get(position).getThumbnailUrl();
        Integer userId = cardDTO.get(position).getUserId();
        String username = cardDTO.get(position).getUsername();
        Integer postId = cardDTO.get(position).getPostId();
        String body = cardDTO.get(position).getBody();


        if (userId != null && body != null) {                                        // ПОКАЗЫВАЕМ ПОСТ

            holder.title.setText(cardDTO.get(position).getTitle());
            holder.body.setText(cardDTO.get(position).getBody());
            holder.title.setHint("1");
        } else if (postId != null) {                                                 // ПОКАЗЫВАЕМ КОМЕМЕНТАРИЙ
            holder.title.setText("ИМЯ: " + cardDTO.get(position).getName());
            holder.body.setText(cardDTO.get(position).getBody());
            holder.commentUserName.setVisibility(View.VISIBLE);
            holder.commentUserName.setText("EMAIL: " + cardDTO.get(position).getEmail());
            holder.title.setHint("2");
        } else if (thumbUrl != null) {                                               // ПОКАЗЫВАЕМ ИЗОБРАЖЕНИЕ
            holder.title.setText(cardDTO.get(position).getTitle());
            holder.title.setHint("4");
            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(cardDTO.get(position).getUrl())
                    .into(holder.image);
        } else if (username != null) {                                              // ПОКАЗЫВАЕМ ПОЛЬЗОВАТЕЛЬЯ
            holder.title.setText(cardDTO.get(position).getUsername());
            holder.body.setText("Имя: " + cardDTO.get(position).getName() +
                    "\nEmail: " + cardDTO.get(position).getEmail() +
                    "\nУлица: " + cardDTO.get(position).getAddress().getStreet() +
                    "\nТелефон: " + cardDTO.get(position).getPhone() +
                    "\nWebSite: " + cardDTO.get(position).getWebsite() +
                    "\nCompany Name: " + cardDTO.get(position).getCompany().getName());
            holder.title.setHint("5");

        } else {                                                                     // показываем ТоДо
            holder.title.setText("ЗАДАЧА: " + cardDTO.get(position).getTitle());
            if (cardDTO.get(position).getCompleted() == true) {
                holder.body.setText("Выполнено");
            } else {
                holder.body.setText("В процессе");
            }
            holder.title.setHint("3");
        }
    }

    @Override
    public int getItemCount() {
        return cardDTO.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView body;
        Button button;
        EditText editText;
        ImageView image;
        TextView commentUserName;

        public CardViewHolder(View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            button = itemView.findViewById(R.id.button_add);
            editText = itemView.findViewById(R.id.edit_text);
            image = itemView.findViewById(R.id.image);
            commentUserName = itemView.findViewById(R.id.commentUserName);

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRealm.close();
    }
}
