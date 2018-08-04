package com.fidel.dhun.data;

/**
 * Created by fidel on 11/3/17.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://ws.audioscrobbler.com/2.0/";

    public static Api getArtist() {

        return ApiClient.getClient(BASE_URL ).create(Api.class);
    }
}