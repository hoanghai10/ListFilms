package com.example.doanandroid.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.doanandroid.database.TVShowsDatabase;
import com.example.doanandroid.models.TVShow;

import java.util.List;

import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {
    private TVShowsDatabase tvShowsDatabase;
    public WatchlistViewModel(@NonNull Application application){
        super(application);
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }
    public Flowable<List<TVShow>> loadWatchlist(){
        return tvShowsDatabase.tvShowDao().getWatchlist();
    }

}
