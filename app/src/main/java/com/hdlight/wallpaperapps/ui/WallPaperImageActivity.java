package com.hdlight.wallpaperapps.ui;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.hdlight.wallpaperapps.BuildConfig;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityFullWallPaperImageBinding;
import com.hdlight.wallpaperapps.utils.Utils;
import com.jackandphantom.blurimage.BlurImage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.favre.lib.dali.Dali;
import es.dmoral.toasty.Toasty;

public class WallPaperImageActivity extends BaseActivity implements View.OnClickListener {

    public static final String IMAGES = "images";
    public static final String POSITION = "position";

    ImageView imgFullImage;
    public String single_choice_selected;
    MaterialAlertDialogBuilder builder;
    AlertDialog progressDialog;
    private ViewGroup root;
    ActivityFullWallPaperImageBinding binding;
    String image;
    int position;
    byte[] byteArray;
    Bitmap bitmap;
    RelativeLayout relativeProgressBar;
    LinearLayout linearBottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Utils.FullScreenWpUIMode(WallPaperImageActivity.this);
        binding = ActivityFullWallPaperImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.WHITE);
        image = getIntent().getStringExtra(IMAGES);
        position = getIntent().getIntExtra(POSITION, position);
//       getWindow().setNavigationBarColor(Color.);
        binding.imgShare.setOnClickListener(this);
        binding.imgDown.setOnClickListener(this);
        downloadImage(image);

        Uri myUri = Uri.parse(image);
        Log.e("Uri", "onCreate: " + myUri);
//        Dali.create(this).load().blurRadius(20)
//                .downScale(2).concurrent().reScale().skipCache().into(binding.imageView);
        Glide.with(this)
                .asBitmap()
                .load(image)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        BlurImage.with(getApplicationContext()).load(resource).intensity(25).Async(true).into(binding.imageView);
//                        Toast.makeText(WallPaperImageActivity.this, resource.toString(), Toast.LENGTH_SHORT).show();
//                        bitmap = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        String img = "," + image;
        Log.e("Good", "onCreate: " + image);
        String base64Image = img.split(",")[1];
        Log.e("Good", "onCreate: " + base64Image);
//        byte[] byteArray1;
//      byteArray1 = Base64.decode(base64Image, Base64.DEFAULT);
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
//        Bitmap b=StringToBitMap(image);
//        Glide.with(this)
//                        .load(image)
//                                .into(binding.imageView);
//
//      BlurImage.with(getApplicationContext()).load(R.id.imageView).intensity(20).Async(true).into(binding.imageView);
        try {
//            byte [] encodeByte = Base64.decode(image,Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            Log.e("WPImgActivity", "onCreate: " + e.getMessage());
        }
        Log.e("FUll", "onCreate: " + position);
        imgFullImage = findViewById(R.id.imgFullImage);
        root = findViewById(R.id.wallPaper);
        Glide.with(getApplicationContext()).load(image).into(imgFullImage);
        binding.cardViewWallPaper.setOnClickListener(this);
//        binding.imgBack.setOnClickListener(this);
//        binding.txtApply.setOnClickListener(this);


        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionExternalMemory == PackageManager.PERMISSION_DENIED){

            Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show();

        }

    }

    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);

            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardViewWallPaper:
                startActivity(new Intent(WallPaperImageActivity.this, DownloadWallPaperActivity.class).putExtra(IMAGES, image).putExtra(POSITION, position));
                break;
            case R.id.imgShare:
                BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imgFullImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageandText(bitmap);
                break;
            case R.id.imgDown:
                showBottomSheetDialog();
                break;

//            case R.id.imgBack:
//                onBackPressed();
//                break;
//            case R.id.txtApply:
////                dialogOptionSetWallpaper();
//                break;
        }
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_wp_dialog);

        RelativeLayout setWp = bottomSheetDialog.findViewById(R.id.setWp);
        RelativeLayout setLockScreen = bottomSheetDialog.findViewById(R.id.setLockScreen);
        RelativeLayout setBoth = bottomSheetDialog.findViewById(R.id.setBoth);
        RelativeLayout setDownload = bottomSheetDialog.findViewById(R.id.setDownload);
        relativeProgressBar = bottomSheetDialog.findViewById(R.id.relativeProgressBar);
        linearBottomSheetDialog = bottomSheetDialog.findViewById(R.id.linearBottomSheetDialog);

        setDownload.setOnClickListener(v -> {

            relativeProgressBar.setVisibility(View.VISIBLE);
            linearBottomSheetDialog.setVisibility(View.INVISIBLE);

            verifyPermissions();



        });


        bottomSheetDialog.show();
    }




    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image");

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

    public void dialogOptionSetWallpaper() {
        String[] stringArray = getResources().getStringArray(R.array.dialog_set_wallpaper);

        this.single_choice_selected = stringArray[0];


        builder = new MaterialAlertDialogBuilder(WallPaperImageActivity.this, R.style.DialogTheme);
        builder.setTitle(R.string.dialog_set_title);
        builder.setSingleChoiceItems((CharSequence[]) stringArray, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                setBackground();
            }
        });
        builder.setPositiveButton(R.string.dialog_option_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setBackground();
            }
        });
        builder.setNegativeButton(R.string.dialog_option_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

//        new androidx.appcompat.app.AlertDialog.Builder(this,R.style.DialogTheme).setTitle((int) R.string.dialog_set_title)
//                .setSingleChoiceItems((CharSequence[]) stringArray, 0, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
//
//            public final void onClick(DialogInterface dialogInterface, int i) {
//                FullWallPaperImageActivity.this.applyActivityNotificationDetail(dialogInterface, i);
////                FullWallPaperImageActivity.this.lambda$dialogOptionSetWallpaper$7$ActivityNotificationDetail(stringArray, dialogInterface, i);
//            }
//        }).setPositiveButton((int) R.string.dialog_option_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
//
//            public final void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        }).setNegativeButton((int) R.string.dialog_option_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).show();
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        builder.setView(R.id.progressBar);
        builder.setCancelable(false);
        builder.show();
//        progressDialog = builder.create();
//        progressDialog.show();
    }

    public void lambda$dialogOptionSetWallpaper$7$ActivityNotificationDetail(String[] strArr, DialogInterface dialogInterface, int i) {
        this.single_choice_selected = strArr[i];
    }

    public void applyActivityNotificationDetail(DialogInterface dialogInterface, int i) {
        Toasty.normal(getApplicationContext(), (int) R.string.snack_bar_applying, 0).show();
        setBackground();
    }

    private void setBackground() {
//        showLoading();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                binding.progressBar.setVisibility(View.VISIBLE);
                Bitmap bitmap = ((BitmapDrawable) imgFullImage.getDrawable()).getBitmap();
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    manager.setBitmap(bitmap);
                } catch (IOException e) {
                    Toast.makeText(WallPaperImageActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                Log.d("Handler", "Running Handler");
            }
        }, 500);
    }

    //Check Permission image storage
    public Boolean verifyPermissions() {
        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {

            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
//            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, 5000);

            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            return false;
        }





        return true;
    }

    public void downloadImage(String imageURL) {

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";
        final File dir = new File(dirPath);
        final String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
        Glide.with(this)
                .load(imageURL)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @com.google.firebase.database.annotations.Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        WallPaperImageActivity.this.bitmap = bitmap;
//                        Toast.makeText(DownloadWallPaperActivity.this, "Saving Image...", Toast.LENGTH_SHORT).show();
                        Log.e("WPImageActivity", "onResourceReady: ");
                    }

                    @Override
                    public void onLoadCleared(@com.google.firebase.database.annotations.Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@com.google.firebase.database.annotations.Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
//                        Toast.makeText(DownloadWallPaperActivity.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                        Log.e("WPImageActivity", "onLoadFailed: ");
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

        if (imageFile.exists()) {
            Toast.makeText(this, "already download", Toast.LENGTH_SHORT).show();
        } else {
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            saveImageDownloadData();

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            relativeProgressBar.setVisibility(View.GONE);
            linearBottomSheetDialog.setVisibility(View.VISIBLE);

            Toast.makeText(WallPaperImageActivity.this, "Download Successfully", Toast.LENGTH_LONG).show();
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


}