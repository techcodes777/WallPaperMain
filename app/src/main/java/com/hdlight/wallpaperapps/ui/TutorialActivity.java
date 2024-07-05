package com.hdlight.wallpaperapps.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.adapter.TutorialAdapter;
import com.hdlight.wallpaperapps.databinding.ActivityTutorialBinding;
import com.hdlight.wallpaperapps.utils.Utils;

public class TutorialActivity extends BaseActivity {
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    ActivityTutorialBinding binding;
    TutorialAdapter tutorialAdapter;

    public String single_choice_selected;
    int pos = 0;

    int images[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3};

    String title[] = {"  a trending wallpaper in the world", "  a trending wallpaper in the world", "  a trending wallpaper in the world"};

    String detail[] = {"A collection of the top Trending wallpapers and backgrounds available for download for free.", "A collection of the top Trending wallpapers and backgrounds available for download for free.", "A collection of the top Trending wallpapers and backgrounds available for download for free."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.FullScreenUIMode(TutorialActivity.this);

//        Utils.statusBarColor(TutorialActivity.this,getResources().getColor(R.color.black));
//        String[] stringArray = getResources().getStringArray(R.array.array);
//        single_choice_selected = stringArray[0];
//        Toast.makeText(this, "Success" + single_choice_selected, Toast.LENGTH_SHORT).show();
        binding.txtSkip.setOnClickListener(this);
        binding.txtNext.setOnClickListener(this);

        tutorialAdapter = new TutorialAdapter(this, images, title, detail);
        binding.viewPager.setAdapter(tutorialAdapter);
        binding.indicator.setViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

                pos = position;
                if (pos == 0) {
                    binding.txtSkip.setVisibility(View.VISIBLE);
                } else if (pos == 1) {
                    binding.txtSkip.setVisibility(View.VISIBLE);
                } else if (pos == 2) {
                    binding.txtSkip.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                binding.viewPager.setCurrentItem(currentPage++, true);
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txtNext:
                if (pos == 0) {
                    binding.viewPager.setCurrentItem(pos + 1);
                } else if (pos == 1) {
                    binding.viewPager.setCurrentItem(pos + 1);
                } else if (pos == 2) {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
                break;
            case R.id.txtSkip:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    public static void setLightStatusBar(View view, Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.BLACK);

        }
    }
}