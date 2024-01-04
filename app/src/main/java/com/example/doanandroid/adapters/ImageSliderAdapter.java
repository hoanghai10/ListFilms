package com.example.doanandroid.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanandroid.R;
import com.example.doanandroid.databinding.ItemContainerSliderImageBinding;
import com.example.doanandroid.databinding.ItemContainerTvShowBinding;


public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>{
    private String[] sliderImage;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(String[] sliderImage) {
        this.sliderImage = sliderImage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSliderImageBinding sliderImageBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_slider_image, parent,false
        );
        return new ImageSliderViewHolder(sliderImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.bindSliderImage(sliderImage[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImage.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerSliderImageBinding itemContainerSliderImageBinding;

        public ImageSliderViewHolder(ItemContainerSliderImageBinding itemContainerSliderImageBinding){
            super(itemContainerSliderImageBinding.getRoot());
            this.itemContainerSliderImageBinding = itemContainerSliderImageBinding;
        }
        public void bindSliderImage(String imageURL){
            itemContainerSliderImageBinding.setImageURL(imageURL);
        }

    }

}
