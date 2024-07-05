package com.hdlight.wallpaperapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.ui.WallPaperImageActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.ViewHolder> {

    private ArrayList<String> list;
    private Context context;
    public static final String IMAGES = "images";
    public static final String POSITION = "position";

    public WallPaperAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(list.get(position))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
//            Bundle bundle = new Bundle();
//            bundle.putString(POSITION, String.valueOf(position));
            int pos = holder.getAdapterPosition();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
            context.startActivity(new Intent(context, WallPaperImageActivity.class).putExtra(IMAGES,list.get(position)).putExtra(POSITION,pos));
            Log.e("WallPaperAdapter", "onBindViewHolder: " + position);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgItem);

        }
    }

}
