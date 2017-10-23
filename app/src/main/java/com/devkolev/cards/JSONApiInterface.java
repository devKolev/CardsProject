package com.devkolev.cards;

import com.devkolev.cards.model.CardDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONApiInterface {

    @GET("{posts}/")
    Call<List<CardDTO>> getPost(
            @Path("posts") String type_get,
            @Query("id") String post_id);

}
