package com.example.doanandroid.reponsitories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.doanandroid.network.ApiClient;
import com.example.doanandroid.network.ApiService;
import com.example.doanandroid.responses.TVShowReponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowsReponsitory {
    public ApiService apiService;

    public MostPopularTVShowsReponsitory(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowReponse> getMostPopuLarTVShows(int page){
        MutableLiveData<TVShowReponse> data = new MutableLiveData<>();
        apiService.getMostPopularTVShow(page).enqueue(new Callback<TVShowReponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowReponse> call,@NonNull Response<TVShowReponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowReponse> call,@NonNull Throwable t) {
                data.setValue(null);

            }
        });
        return data;
    }
}
