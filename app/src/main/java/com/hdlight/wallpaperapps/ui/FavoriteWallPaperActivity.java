package com.hdlight.wallpaperapps.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.adapter.FavoriteAdapter;
import com.hdlight.wallpaperapps.databinding.ActivityFavoriteWallPaperBinding;
import com.hdlight.wallpaperapps.model.Like;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteWallPaperActivity extends BaseActivity  {

    ActivityFavoriteWallPaperBinding binding;

    FavoriteAdapter favoriteAdapter;
    ArrayList<Like> likeArrayList;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteWallPaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        likeArrayList = new ArrayList<>();
        list = new ArrayList<>();

        loadData();

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        binding.recyclerView.setHasFixedSize(true);
        favoriteAdapter = new FavoriteAdapter(likeArrayList,this);
        binding.recyclerView.setAdapter(favoriteAdapter);
        favoriteAdapter.notifyDataSetChanged();

        binding.imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("image", null);
//        Toast.makeText(this, "json" + json, Toast.LENGTH_SHORT).show();
        Log.e("json", "loadData: " + json);
        Type type = new TypeToken<ArrayList<Like>>() {}.getType();
        likeArrayList = gson.fromJson(json, type);


        if (likeArrayList == null) {
            likeArrayList = new ArrayList<>();
        }
    }
}