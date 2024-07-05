package com.hdlight.wallpaperapps.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}