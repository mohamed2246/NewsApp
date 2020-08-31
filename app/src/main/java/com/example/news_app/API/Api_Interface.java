package com.example.news_app.API;

import com.example.news_app.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api_Interface {

    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
            @Query("apiKey") String apiKey
            ) ;

    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") String name,
            @Query("language") String lang,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey

    );


}
