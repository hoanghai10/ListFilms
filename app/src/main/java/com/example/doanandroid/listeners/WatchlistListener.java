package com.example.doanandroid.listeners;

import com.example.doanandroid.models.TVShow;

public interface WatchlistListener {
    void onTVShowClicked(TVShow tvShow);
    void removeTVShowFromWatchlist(TVShow tvShow, int position);
}

