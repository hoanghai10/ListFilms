package com.example.doanandroid.responses;

import com.example.doanandroid.models.TVShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowReponse {
    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalPages;

    @SerializedName("tv_shows")
    private List<TVShow> tv_shows;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<TVShow> getTv_shows() {
        return tv_shows;
    }
}

