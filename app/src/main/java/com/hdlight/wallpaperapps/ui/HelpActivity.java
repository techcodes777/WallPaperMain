package com.hdlight.wallpaperapps.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hdlight.wallpaperapps.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {

    ActivityHelpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}