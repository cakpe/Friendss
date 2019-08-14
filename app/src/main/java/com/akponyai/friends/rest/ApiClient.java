package com.akponyai.friends.rest;

import android.app.Activity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient extends Activity {
    //you then have to create a friends directory in xampp/htdoc. This will be our base folder location
    public static final String BASE_URL = "https://10.0.2.2/friends";
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
