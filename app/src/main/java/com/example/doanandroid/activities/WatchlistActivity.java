package com.example.doanandroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.doanandroid.R;
import com.example.doanandroid.adapters.WatchlistAdapter;
import com.example.doanandroid.databinding.ActivityWatchlistBinding;
import com.example.doanandroid.listeners.TVShowListener;
import com.example.doanandroid.listeners.WatchlistListener;
import com.example.doanandroid.models.TVShow;
import com.example.doanandroid.viewmodels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchlistBinding activityWatchlistBinding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchlistBinding = DataBindingUtil.setContentView(this,R.layout.activity_watchlist);
        doInitialization();

    }
    private void doInitialization(){
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchlistBinding.imageBack.setOnClickListener(view -> onBackPressed());
        watchlist = new ArrayList<>();
    }

    private  void loadWatchlist(){
        activityWatchlistBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchlistBinding.setIsLoading(false);
                    if(watchlist.size()>0){
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist, (TVShowListener) this);
                }));

    }
    @Override
    protected void onResume(){
        super.onResume();
        loadWatchlist();
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {

    }

    @Override
    public void removeTVShowFromWatchlist(TVShow tvShow, int position) {

    }
}