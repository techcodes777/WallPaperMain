package com.hdlight.wallpaperapps.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.utils.SharedPref;
import com.hdlight.wallpaperapps.utils.Tools;
import com.hdlight.wallpaperapps.utils.Utils;

public class SplashActivity extends BaseActivity {

    //    ImageView imageView2;
    ImageView img_splash;
    LottieAnimationView progressBar;
    SharedPref sharedPref;
    String url = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.transparentStatusBarNavigation(this);
        setContentView(R.layout.activity_splash);

//        Tools.getRtlDirection(this);
        this.sharedPref = new SharedPref(this);

//        firebaseAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();


//        this.img_splash = (ImageView) findViewById(R.id.imgsplash);
//
//        if (this.sharedPref.getIsDarkTheme().booleanValue()) {
//            this.img_splash.setBackgroundColor(getResources().getColor(R.color.colorBackgroundDark));
//        } else {
//            this.img_splash.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
//        }

        this.progressBar = (LottieAnimationView) findViewById(R.id.progressBar);
        Log.e("SplashActivity", "onCreate: ");


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Splash", "onStart: " + firebaseAuth);

        SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);

        if (sp.contains("true")){

            if (sp.contains("mobile")){
                Toast.makeText(this, "SuccessFully Login", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public final void run() {
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                    }
                }, 3000);
            }

        }else {
//            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (user != null){
                user.reload();
            }
//            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            SharedPreferences pass = context.getSharedPreferences("pass", Context.MODE_PRIVATE);

            String name = null;
            if (pass.contains("passVerify")){
               name  = pass.getString("passVerify","");
            }

            Log.e("status", "onStart: " + name );
            if (name != null){
                switch(name) {
                    case "0":
//                        Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                        if (user != null){
//                            Toast.makeText(this, "Already Register Logged In|" + user.getUid(), Toast.LENGTH_SHORT).show();
                            if (user.getUid() == null){
//                                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                            }

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                public final void run() {
                                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                                    finish();
                                }
                            }, 3000);

                            Log.e("userId", "onStart: " + user.getUid());

                        }
                        break;
                    default:
                        break;
                }
            }else {
                loadResources();
            }

        }

    }
    
    @Override
    protected void onResume() {
        super.onResume();

        Log.e("Splash", "onResume: " + firebaseAuth);

        SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        if (sp.contains("true")){

            if (sp.contains("mobile")){
//                Toast.makeText(this, "SuccessFully Login", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public final void run() {

                    }
                }, 3000);
            }

        }

    }

    public void launchMainScreen() {
        startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
        finish();
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            public final void run() {
//                finish();
//            }
//        }, 2000);
    }


    private void loadResources() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public final void run() {
                SplashActivity.this.loadActivitySplash();
            }
        }, 3000);
    }

    public /* synthetic */ void loadActivitySplash() {
        launchMainScreen();
    }

}