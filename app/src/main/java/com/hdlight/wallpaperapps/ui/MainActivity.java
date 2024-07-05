package com.hdlight.wallpaperapps.ui;

import static java.lang.String.format;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.adapter.ViewPagerAdapter;
import com.hdlight.wallpaperapps.databinding.ActivityMainBinding;
import com.hdlight.wallpaperapps.fragment.DoubleFragment;
import com.hdlight.wallpaperapps.fragment.TrendingFragment;
import com.hdlight.wallpaperapps.fragment.WallpaperFragment;
import com.hdlight.wallpaperapps.model.ScrimInsetsFrameLayout;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    ImageView imgMenu;
    DrawerLayout drawerLayout;
    ScrimInsetsFrameLayout mLayout;
    TabLayout tab;
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String userId;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setLightStatusBar(binding.drawerLayout, MainActivity.this);
        tab = findViewById(R.id.tab);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        if (userId != null){
            userId = user.getUid();
        }

        viewPager = findViewById(R.id.view_pager);
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(viewPagerAdapter);
//        tab.setupWithViewPager(viewPager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        //Init and attach
        firebaseAuth = FirebaseAuth.getInstance();
//Call signOut()
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mLayout = (ScrimInsetsFrameLayout) findViewById(R.id.containers);
//        drawerLayout.closeDrawer(mLayout);

        imgMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(mLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

    }

    private void setupViewPager(ViewPager viewPager) {
//
//        String normalBefore= "First Part Not Bold ";
//        Spannable sb = new SpannableString( normalBefore );
//        sb.setSpan(new StyleSpan(Typeface.BOLD),
//                sb.length() - normalBefore.length(), sb.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);




        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        pagerAdapter.addFragment(new TrendingFragment(), "HOME");
        pagerAdapter.addFragment(new DoubleFragment(), "CATEGORIES");
        pagerAdapter.addFragment(new WallpaperFragment(), "PREMIUM");
        viewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    public void checkProfileStatus(){
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                context.startActivity(new Intent(context,MainActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("MainActivity", "onPause: " );
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void relativeHome(View view){
        closeDrawer(drawerLayout);
    }

    public void relativeLike(View view){
        closeDrawer(drawerLayout);
        startActivity(new Intent(this,FavoriteWallPaperActivity.class));
    }


    public void relativeLogout(View view){
//        closeDrawer(drawerLayout);

        firebaseAuth.signOut();
        SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();

        SharedPreferences spR = context.getSharedPreferences("pass", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = spR.edit();
        editor1.clear();
        editor1.apply();

        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();

        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
    }


    public void relativeSettings(View view)
    {
        closeDrawer(drawerLayout);
        startActivity(new Intent(MainActivity.this,SettingActivity.class));
    }

    public void relativeHelp(View view){
        closeDrawer(drawerLayout);
        startActivity(new Intent(MainActivity.this,HelpActivity.class));
    }

    public void relativeInformation(View view){
        closeDrawer(drawerLayout);
        startActivity(new Intent(MainActivity.this,InformationActivity.class));
    }



}