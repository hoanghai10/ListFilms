package com.example.doanandroid.network;

import com.example.doanandroid.responses.TVShowDetailsReponse;
import com.example.doanandroid.responses.TVShowReponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TVShowReponse> getMostPopularTVShow(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsReponse> getTVShowDetails(@Query("q")String tvShowId);
}
