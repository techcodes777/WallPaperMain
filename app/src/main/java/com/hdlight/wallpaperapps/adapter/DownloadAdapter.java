package com.hdlight.wallpaperapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hdlight.wallpaperapps.databinding.DownloadAdpaterBinding;
import com.hdlight.wallpaperapps.model.Download;
import com.hdlight.wallpaperapps.ui.WallPaperImageActivity;

import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    ArrayList<Download> downloadArrayList;
    Context context;

    public static String IMAGE = "images";

    public DownloadAdapter(ArrayList<Download> downloadArrayList) {
        this.downloadArrayList = downloadArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DownloadAdpaterBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.binding.imgDownload.getContext())
                .load(downloadArrayList.get(position).getImage())
                .placeholder(new ColorDrawable(Color.GRAY))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.imgDownload);


        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, WallPaperImageActivity.class).putExtra(IMAGE,downloadArrayList.get(position).getImage())));

    }

    @Override
    public int getItemCount() {
        return downloadArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        DownloadAdpaterBinding binding;

        public ViewHolder(DownloadAdpaterBinding downloadAdpaterBinding) {
            super(downloadAdpaterBinding.getRoot());
            binding = downloadAdpaterBinding;
            context = binding.imgDownload.getContext();
        }
    }
}
