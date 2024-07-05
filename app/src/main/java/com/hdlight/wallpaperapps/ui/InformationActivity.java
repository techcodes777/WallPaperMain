package com.hdlight.wallpaperapps.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityInformationBinding;

public class InformationActivity extends AppCompatActivity {

    ActivityInformationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}