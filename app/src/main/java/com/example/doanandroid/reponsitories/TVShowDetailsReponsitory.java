package com.example.doanandroid.reponsitories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.doanandroid.network.ApiClient;
import com.example.doanandroid.network.ApiService;
import com.example.doanandroid.responses.TVShowDetailsReponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailsReponsitory {
    private ApiService apiService;

    public TVShowDetailsReponsitory(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }
    public MutableLiveData<TVShowDetailsReponse> getTVShowDetails(String tvShowId){
        MutableLiveData<TVShowDetailsReponse> data = new MutableLiveData<>();
        Log.d("ss", "getTVShowDetails: id = " + tvShowId);
        apiService.getTVShowDetails(tvShowId).enqueue(new Callback<TVShowDetailsReponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowDetailsReponse> call,@NonNull Response<TVShowDetailsReponse> response) {
                data.setValue(response.body());
                Log.d("checkDAtazzzzzzzzzzzzzz", "onResponse: + " + response.body().getTvShowDetails().getEpisodes());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowDetailsReponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
