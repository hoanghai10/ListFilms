package com.example.doanandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Index;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.doanandroid.R;
import com.example.doanandroid.adapters.TVShowsAdapter;
import com.example.doanandroid.databinding.ActivityMainBinding;
import com.example.doanandroid.listeners.TVShowListener;
import com.example.doanandroid.models.TVShow;
import com.example.doanandroid.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowListener {

    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowsViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        doInitialization();
    }
    private void doInitialization(){
        activityMainBinding.TVShowRcv.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows, this);
        activityMainBinding.TVShowRcv.setAdapter(tvShowsAdapter);
        activityMainBinding.TVShowRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.TVShowRcv.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage +=1 ;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        activityMainBinding.imgWatchlist.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),WatchlistActivity.class)));

        getMostPopularTVShows();
    }

    private void getMostPopularTVShows(){
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, mostPopularTVShowsReponse ->{
            toggleLoading();
            if (mostPopularTVShowsReponse != null ){
                totalAvailablePages = mostPopularTVShowsReponse.getTotalPages();
                if (mostPopularTVShowsReponse.getTv_shows() != null){
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTVShowsReponse.getTv_shows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount,tvShows.size());
                }
            }
        });
    }
    private void toggleLoading(){
        if(currentPage ==1 ){
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            } else {
                activityMainBinding.setIsLoading(true);
            }
        } else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            } else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }
}