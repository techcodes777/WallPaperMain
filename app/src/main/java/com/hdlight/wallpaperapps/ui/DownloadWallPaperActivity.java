package com.hdlight.wallpaperapps.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityDownloadWallPaperBinding;
import com.hdlight.wallpaperapps.model.Download;
import com.hdlight.wallpaperapps.model.Like;
import com.hdlight.wallpaperapps.utils.Constant;
import com.hdlight.wallpaperapps.utils.PreferenceManager;
import com.hdlight.wallpaperapps.utils.SharedPref;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DownloadWallPaperActivity extends BaseActivity {

    ActivityDownloadWallPaperBinding binding;
    public static final String IMAGES = "images";
    public static final String POSITION = "position";
    int position;
    PreferenceManager preferenceManager;

    ImageView imgDownloadWallPaper;
    Bitmap bitmap;
    String image;
    DatabaseReference likeDatabaseReference;
    Boolean testClick = false;
    String imageKey;
    int pos;
    int po;
    private ArrayList<Like> likeArrayList;
    private ArrayList<Download> downloadArrayList;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDownloadWallPaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imgDownloadWallPaper = findViewById(R.id.imgWallPaperDownload);
        image = getIntent().getStringExtra(IMAGES);
        position = getIntent().getIntExtra(POSITION, position);
        Log.e("Activity", "onCreate: " + image);

        Glide.with(getApplicationContext()).load(image).into(binding.imgWallPaperDownload);

        likeDatabaseReference = FirebaseDatabase.getInstance().getReference("likes");
        binding.imgBack.setOnClickListener(this);
        binding.relativeDownloadWP.setOnClickListener(this);
        binding.relativeShare.setOnClickListener(this);
        binding.checkLike.setOnClickListener(this);

        downloadImage(image);
        verifyPermissions();
        preferenceManager = PreferenceManager.getInstance(DownloadWallPaperActivity.this);
//        likeDatabaseReference.removeValue();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        checkExitedRecord();

//        String image = preferenceManager.getString("image");
//        if (!image.equalsIgnoreCase("")){
//
////            binding.checkLike.setChecked(true);
//
//        }
//            pos = Integer.parseInt(preferenceManager.getString("position"));
//
//            if (pos == position){
//                Toast.makeText(this, "jemis" + pos + position, Toast.LENGTH_SHORT).show();
//                binding.checkLike.setChecked(true);
//
//            }else {
//                binding.checkLike.setChecked(false);
//
//            }
//            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        likeArrayList = new ArrayList<Like>();
        downloadArrayList = new ArrayList<Download>();
        loadJsonFavoriteData();
        Log.e("onCreate", "onCreate: ");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.relativeDownloadWP:
                downloadArrayList.add(new Download(image));
                saveImage(bitmap, image);
                break;
            case R.id.relativeShare:
                BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imgWallPaperDownload.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                binding.imgWallPaperDownload.getDrawingCache();
                binding.imgWallPaperDownload.setDrawingCacheEnabled(true);
                shareImageandText(bitmap);
                break;
            case R.id.checkLike:

                if (binding.checkLike.isChecked()) {
                    SharedPref.imageSharePref(DownloadWallPaperActivity.this, image, position);
//                    likeArrayList.add(new Like(position,image));
//                    preferenceManager.setString("image", image);
//                    preferenceManager.setString("position", String.valueOf(position));
//                    Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show();

                    likeArrayList.add(new Like(image,position));
                    saveAddFavoriteData();
                } else {
                    preferenceManager.setClear("image", String.valueOf(position));
                    Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();

                    likeArrayList.clear();
//                    saveRemoveFavoriteData();
                }
                break;
        }
    }



    private void shareImageandText(Bitmap bitmap) {
            Uri uri = getmageToShare(bitmap);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // putting uri of image to be shared
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            // adding text to share
            intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image" + " - " + Uri.parse("https://www.geeksforgeeks.org"));
            // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            // setting type to image
            intent.setType("image/png");
            // calling startactivity() to share
            startActivity(Intent.createChooser(intent, "Share With"));
    }

    // Retrieving the url to share
    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.hdlight.wallpaperapps", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    //Check Permission image storage
    public Boolean verifyPermissions() {
        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {

            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, 5000);
            return false;
        }
        return true;
    }

    private void saveStorageFileImages(Bitmap image, File storageDir, String imageFileName) {

        boolean successDirCreated = true;
        if (!storageDir.exists()) {
            successDirCreated = storageDir.mkdir();
        }
        if (successDirCreated) {
            File imageFile = new File(storageDir, imageFileName);
            String savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                Log.e("DownloadWallPaperActivity", "saveImages: ");
//                Toast.makeText(DownloadWallPaperActivity.this, "Image Saved!" + savedImagePath, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
//                Toast.makeText(DownloadWallPaperActivity.this, "Error while saving image!", Toast.LENGTH_SHORT).show();
                Log.e("DownloadWallPaperActivity", "saveImages: ");
                e.printStackTrace();
            }

        } else {
//            Toast.makeText(this, "Failed to make folder!", Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadImage(String imageURL) {

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";
        final File dir = new File(dirPath);
        final String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
        Glide.with(this)
                .load(imageURL)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        DownloadWallPaperActivity.this.bitmap = bitmap;
//                        Toast.makeText(DownloadWallPaperActivity.this, "Saving Image...", Toast.LENGTH_SHORT).show();
                        saveStorageFileImages(bitmap, dir, fileName);
                        Log.e("DownloadWallPaperActivity", "onResourceReady: ");
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
//                        Toast.makeText(DownloadWallPaperActivity.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                        Log.e("DownloadWallPaperActivity", "onLoadFailed: ");
                    }
                });

    }


    //Save image in Phone Albums
    private String saveImage(Bitmap image, String imageURL) {
        String savedImagePath = null;

        final String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
        String imageFileName = "JPEG_" + fileName + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/WallPaper");
//        boolean success = true;
            File imageFile = new File(storageDir, imageFileName);

            if (imageFile.exists()){
                Toast.makeText(this, "already download", Toast.LENGTH_SHORT).show();
            }
            else {
                savedImagePath = imageFile.getAbsolutePath();
                try {
                    OutputStream fOut = new FileOutputStream(imageFile);
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                saveImageDownloadData();

                // Add the image to the system gallery
                galleryAddPic(savedImagePath);
                Toast.makeText(DownloadWallPaperActivity.this, "Download Successfully", Toast.LENGTH_LONG).show();
            }

            return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    //Save image in SharedPreferences
    private void saveImageDownloadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("download preferences", MODE_PRIVATE);
        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String old = sharedPreferences.getString("download", null);
        // creating a new variable for gson.
        Gson gson = new Gson();

        if (old == null) {
            // getting data from gson and storing it in a string.
            String json = gson.toJson(downloadArrayList);
            Log.e("Download", "saveImageDownloadData: " + json);
            // below line is to save data in shared
            // prefs in the form of string.
            editor.putString("download", json);
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Download> list;
            list = gson.fromJson(old, new TypeToken<ArrayList<Download>>() {
            }.getType());
            for (int i = 0; i < downloadArrayList.size(); i++) {
                list.add(downloadArrayList.get(i));
            }
//            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
            String json = gson.toJson(list);
            Log.e("Download", "saveImageDownloadData: " + json);
            // below line is to save data in shared
            // prefs in the form of string.
            editor.putString("download", json);
        }
        editor.apply();
        // below line is to apply changes
        // and save data in shared prefs.
        // after saving data we are displaying a toast message.
        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    //Favorite data store in SharedPreferences
    private void saveAddFavoriteData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String oldFavorite = sharedPreferences.getString("image", null);
        Gson gson = new Gson();

        if (oldFavorite == null) {
            // getting data from gson and storing it in a string.
            String json = gson.toJson(likeArrayList);
            Log.e("image", "saveImageFavoriteData: " + json);
            // below line is to save data in shared
            // prefs in the form of string.
            editor.putString("image", json);
            editor.putString("position",json);

            Log.e("AddFavorite", "saveAddFavoriteData: " + json);
            Toast.makeText(this, "null favorite" , Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Like> list;
            list = gson.fromJson(oldFavorite, new TypeToken<ArrayList<Like>>() {
            }.getType());
            for (int i = 0; i < likeArrayList.size(); i++) {
                list.add(likeArrayList.get(i));
            }
            String json = gson.toJson(list);
//            Toast.makeText(this, "not null favorite" + json, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "not null favorite", Toast.LENGTH_SHORT).show();
            Log.e("image", "saveImageFavoriteData: " + json);
            // below line is to save data in shared
            // prefs in the form of string.
            editor.putString("image", json);
            editor.putString("position",json);
        }
        editor.apply();

//        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    private void saveRemoveFavoriteData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        String oldFavorite = sharedPreferences.getString("image",null);

        if (oldFavorite != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(oldFavorite);
            editor.commit();
            Toast.makeText(this, "remove", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadJsonFavoriteData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("position", null);
//        Toast.makeText(this, "json" + json, Toast.LENGTH_SHORT).show();
        Log.e("json", "loadData: " + json);
        Type type = new TypeToken<ArrayList<Like>>() {}.getType();
        likeArrayList = gson.fromJson(json, type);

        if (likeArrayList != null){

            for (int i = 0; i < likeArrayList.size(); i++) {
//                pos =  likeArrayList.get(i).getPosition();
//            Toast.makeText(this, "Position" + pos, Toast.LENGTH_SHORT).show();

                Log.e("Po", "loadJson: " + po);
                Log.e("Position", "loadJsonFavoriteData: " + pos);

            }
        }

        if (likeArrayList == null) {
            likeArrayList = new ArrayList<>();
        }
    }

}