package com.example.doanandroid.activities;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanandroid.R;
import com.example.doanandroid.adapters.EpisodesAdapter;
import com.example.doanandroid.adapters.ImageSliderAdapter;
import com.example.doanandroid.databinding.ActivityTvshowDetailsBinding;
import com.example.doanandroid.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.doanandroid.models.TVShow;
import com.example.doanandroid.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;

    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShow tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_tvshow_details);
        doInitalization();
    }

    private void doInitalization(){
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        getTVShowDetails();
    }

    private void getTVShowDetails(){
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowDetailsReponse -> {
                    activityTvshowDetailsBinding.setIsLoading(false);
                    if(tvShowDetailsReponse.getTvShowDetails() != null ){
                        if(tvShowDetailsReponse.getTvShowDetails().getPictures() != null){
                            loadImageSlider(tvShowDetailsReponse.getTvShowDetails().getPictures());
                        }

                        activityTvshowDetailsBinding.setTVShowImageURL(
                                tvShowDetailsReponse.getTvShowDetails().getImagePath()
                        );
                        activityTvshowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailsReponse.getTvShowDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );

                        // alo cắm thửu lị xong build thửu đi

                        activityTvshowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.textReadMore.setOnClickListener(view -> {
                            Log.d("getTVShowDetails", activityTvshowDetailsBinding.textReadMore.getText().toString());
                            if (activityTvshowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                                activityTvshowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvshowDetailsBinding.textDescription.setEllipsize(null);
                                activityTvshowDetailsBinding.textReadMore.setText(R.string.read_less);
                            } else { // nó cứ chạy vào else là sao nhỉ
                                activityTvshowDetailsBinding.textDescription.setMaxLines(4);
                                activityTvshowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvshowDetailsBinding.textReadMore.setText(R.string.read_more);
                            }
                        });
                        activityTvshowDetailsBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailsReponse.getTvShowDetails().getRating())
                                )
                        );
                        if(tvShowDetailsReponse.getTvShowDetails().getGenres() != null){
                            activityTvshowDetailsBinding.setGenre(tvShowDetailsReponse.getTvShowDetails().getGenres()[0]);
                        } else {
                            activityTvshowDetailsBinding.setGenre("N/A");
                        }
                        activityTvshowDetailsBinding.setRuntime(tvShowDetailsReponse.getTvShowDetails().getRuntime()+ " Min");
                        activityTvshowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.buttomWebsite.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsReponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityTvshowDetailsBinding.buttomWebsite.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.buttomEpisodes.setVisibility(View.VISIBLE );
                        activityTvshowDetailsBinding.buttomEpisodes.setOnClickListener(view -> {
                            if (episodesBottomSheetDialog == null){
                                episodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TVShowDetailsActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodesContainer),
                                        false
                                );// nó đ hiện list episodes là sao nhỉ
                                episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                layoutEpisodesBottomSheetBinding.episodesRcv.setAdapter(
                                        new EpisodesAdapter(tvShowDetailsReponse.getTvShowDetails().getEpisodes())
                                );
                                layoutEpisodesBottomSheetBinding.episodesRcv.setLayoutManager(new LinearLayoutManager(this));
                                Log.d("TAG", "[list = " + tvShowDetailsReponse.getTvShowDetails().getEpisodes()+ "]");
                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", tvShow.getName() )
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> episodesBottomSheetDialog.dismiss());
                            }

                            // ----- Optional section start ----- //
                            FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if (frameLayout != null){
                                BottomSheetBehavior<View> bottomSheetBehavior =BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                            // ----- Optional section end ----- //

                            episodesBottomSheetDialog.show();
                        });

                        activityTvshowDetailsBinding.imageWatchlist.setOnClickListener(v -> new CompositeDisposable().add(tvShowDetailsViewModel.addToWatchList(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added);
                                    Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                                })

                        ));

                        loadBasicTVShowDetails();
                    }
                }
        );
    }
    public void loadImageSlider(String[] sliderImages){
        activityTvshowDetailsBinding.slideViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.slideViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailsBinding.slideViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.ViewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityTvshowDetailsBinding.slideViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count){
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i =0;i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_slider_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailsBinding.LayoutSliderIndicators.addView(indicators[i]);
        }
        activityTvshowDetailsBinding.LayoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }
    private void setCurrentSliderIndicator(int position){
        int childCount = activityTvshowDetailsBinding.LayoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) activityTvshowDetailsBinding.LayoutSliderIndicators.getChildAt(i);
            if(i == position){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            }else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }
    private void loadBasicTVShowDetails(){
        activityTvshowDetailsBinding.setTVShowName(tvShow.getName());
        activityTvshowDetailsBinding.setNetworkCountry(
                tvShow.getNetwork() + "( " +
                        tvShow.getCountry() + ")"
        );
        activityTvshowDetailsBinding.setStatus(tvShow.getStatus());
        activityTvshowDetailsBinding.setStartedDate(tvShow.getStart_date());
        activityTvshowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }
}