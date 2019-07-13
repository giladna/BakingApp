package com.udacity.giladna.bakingapp.utilities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.udacity.giladna.bakingapp.utilities.RecipesAPI.RECEPIES_URL;


public class NetworkClient {

    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(RECEPIES_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}