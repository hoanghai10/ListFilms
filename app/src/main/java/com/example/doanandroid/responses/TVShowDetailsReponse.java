package com.example.doanandroid.responses;

import com.example.doanandroid.models.TVShowDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsReponse {
    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}
