package com.hdlight.wallpaperapps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hdlight.wallpaperapps.databinding.FavoriteAdapterBinding;
import com.hdlight.wallpaperapps.model.Like;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    ArrayList<Like> likeArrayList;
    Context context;

    public FavoriteAdapter(ArrayList<Like> likeArrayList, Context context) {
        this.likeArrayList = likeArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FavoriteAdapterBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .load(likeArrayList.get(position).getImage())
                .placeholder(new ColorDrawable(Color.GRAY))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.imgDownload);
    }

    @Override
    public int getItemCount() {
        return likeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FavoriteAdapterBinding binding;

        public ViewHolder(FavoriteAdapterBinding favoriteAdapterBinding) {
            super(favoriteAdapterBinding.getRoot());
            binding = favoriteAdapterBinding;

        }
    }
}
