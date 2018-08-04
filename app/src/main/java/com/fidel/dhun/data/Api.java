package com.fidel.dhun.data;

import com.fidel.dhun.data.Tag.Top;
import com.fidel.dhun.data.api.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by fidel on 11/3/17.
 */

public interface Api {
    @GET("?method=artist.getinfo&api_key=3234faa9421d814259be98f90f9318c4&format=json")
    Call<Example> getArtist(@Query("artist") String artistName);
    @GET("?method=artist.gettoptags&api_key=3234faa9421d814259be98f90f9318c4&format=json")
    Call<Top> getTags(@Query("artist") String artistName);
}
