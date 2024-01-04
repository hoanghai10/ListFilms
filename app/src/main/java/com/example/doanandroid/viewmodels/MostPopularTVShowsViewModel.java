package com.example.doanandroid.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.doanandroid.reponsitories.MostPopularTVShowsReponsitory;
import com.example.doanandroid.responses.TVShowReponse;

public class MostPopularTVShowsViewModel extends ViewModel {
    private MostPopularTVShowsReponsitory mostPopularTVShowsReponsitory;

    public MostPopularTVShowsViewModel(){
        mostPopularTVShowsReponsitory = new MostPopularTVShowsReponsitory();

    }

    public LiveData<TVShowReponse> getMostPopularTVShows(int page){
        return mostPopularTVShowsReponsitory.getMostPopuLarTVShows(page);
    }

}
