package com.hdlight.wallpaperapps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SharedPref {
    private Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    public SharedPref(Context context2) {
        this.context = context2;
        SharedPreferences sharedPreferences2 = context2.getSharedPreferences("settings", 0);
        this.sharedPreferences = sharedPreferences2;
        this.editor = sharedPreferences2.edit();
    }

    public static void imageSharePref(Context context,String image,int position) {
        SharedPreferences sp = context.getSharedPreferences("wallpaper",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("position", position);
        editor.putString("image",image);
        editor.apply();
    }


    public static void loginSharePref(Context context,String mobile,String password,Boolean yes){
        SharedPreferences sp = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("mobile",mobile);
        editor.putString("password",password);
        editor.putBoolean("true",yes);
        editor.apply();

    }
    public static void passSharePref(Context context,String passVerify){
        SharedPreferences sp = context.getSharedPreferences("pass",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("passVerify",passVerify);
        editor.apply();
    }



    private static  void checkExitedRecord(Context context) {
        SharedPreferences sp = context.getSharedPreferences("wallpaper",Context.MODE_PRIVATE);
        if (sp.contains("image")) {
//            binding.etUserName.setText(sp.getString("username",""));
//            binding.etPassword.setText(sp.getString("password",""));
            Toast.makeText(context, "Hello W", Toast.LENGTH_SHORT).show();

        } else {
//            binding.txtMessage.setText("Record not found");
//            binding.txtMessage.setTextColor(Color.RED);
        }
    }





    public void saveGif(String str, String str2) {
        this.editor.putString("path", str);
        this.editor.putString("gif_name", str2);
        this.editor.apply();
    }

    public String getPath() {
        return this.sharedPreferences.getString("path", "0");
    }

    public String getGifName() {
        return this.sharedPreferences.getString("gif_name", "0");
    }

    public Integer getDisplayPosition(int i) {
        return Integer.valueOf(this.sharedPreferences.getInt("display_position", i));
    }

    public void updateDisplayPosition(int i) {
        this.editor.putInt("display_position", i);
        this.editor.apply();
    }

    public Integer getWallpaperColumns() {
        return Integer.valueOf(this.sharedPreferences.getInt("wallpaper_columns", 2));
    }

    public void updateWallpaperColumns(int i) {
        this.editor.putInt("wallpaper_columns", i);
        this.editor.apply();
    }

    public void setDefaultSortWallpaper() {
        this.editor.putInt("sort_act", 0);
        this.editor.apply();
    }

    public Integer getCurrentSortWallpaper() {
        return Integer.valueOf(this.sharedPreferences.getInt("sort_act", 0));
    }

    public void updateSortWallpaper(int i) {
        this.editor.putInt("sort_act", i);
        this.editor.apply();
    }

    public Boolean getIsDarkTheme() {
        return Boolean.valueOf(this.sharedPreferences.getBoolean("theme", true));
    }

    public void setIsDarkTheme(Boolean bool) {
        this.editor.putBoolean("theme", bool.booleanValue());
        this.editor.apply();
    }

    public Boolean getIsNotification() {
        return Boolean.valueOf(this.sharedPreferences.getBoolean("noti", true));
    }

    public void setIsNotification(Boolean bool) {
        this.editor.putBoolean("noti", bool.booleanValue());
        this.editor.apply();
    }

    public Integer getAppOpenToken() {
        return Integer.valueOf(this.sharedPreferences.getInt("app_open_token", 0));
    }

    public void updateAppOpenToken(int i) {
        this.editor.putInt("app_open_token", i);
        this.editor.apply();
    }
}
