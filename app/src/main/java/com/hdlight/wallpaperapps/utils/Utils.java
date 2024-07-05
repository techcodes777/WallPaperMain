package com.hdlight.wallpaperapps.utils;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.view.WindowCompat;

import com.hdlight.wallpaperapps.R;

public class Utils {

    public static void FullScreenUIMode(Activity context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = context.getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
            }
            int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            context.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }


    public static void FullScreenWpUIMode(Activity context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = context.getWindow();
//                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
//                context.getWindow().setStatusBarColor(Color.BLACK);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                window.setStatusBarColor(context.getResources().getColor(R.color.black));
//                int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//                context.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
//                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                context.getWindow().setNavigationBarColor(Color.BLACK);
            }

            int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            context.getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        }
    }

    //hide keyboard
    public static void hideKeyBoardFromView(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view, so we can grab the correct window
        // token from it.
        View view = ((Activity) context).getCurrentFocus();
        // If no view currently has focus, create a new one, just so we can grab
        // a window token from it
        if (view == null) {
            view = new View(context);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void statusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
    }
}
