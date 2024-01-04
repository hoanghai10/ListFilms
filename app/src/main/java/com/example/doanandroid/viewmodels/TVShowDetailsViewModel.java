package com.example.doanandroid.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.example.doanandroid.database.TVShowsDatabase;
import com.example.doanandroid.models.TVShow;
import com.example.doanandroid.reponsitories.TVShowDetailsReponsitory;
import com.example.doanandroid.responses.TVShowDetailsReponse;

import io.reactivex.Completable;

public class TVShowDetailsViewModel extends AndroidViewModel {
    private TVShowDetailsReponsitory tvShowDetailsReponsitory;
    private TVShowsDatabase tvShowsDatabase;

    public TVShowDetailsViewModel(@NonNull Application application){
        super(application);
        tvShowDetailsReponsitory = new TVShowDetailsReponsitory();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public MutableLiveData<TVShowDetailsReponse> getTVShowDetails(String tvShowId){
        return tvShowDetailsReponsitory.getTVShowDetails(tvShowId);
    }
    public Completable addToWatchList(TVShow tvShow) {
        return tvShowsDatabase.tvShowDao().addToWatchlist(tvShow);
    }

}
