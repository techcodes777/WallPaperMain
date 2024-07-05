package com.hdlight.wallpaperapps.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.anjlab.android.iab.v3.Constants;
import com.hdlight.wallpaperapps.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Tools {
    private Context context;

    public static void getRtlDirection(Activity activity) {
    }

    public Tools(Context context2) {
        this.context = context2;
    }

    public static void getTheme(Context context2) {
        if (new SharedPref(context2).getIsDarkTheme().booleanValue()) {
            context2.setTheme(R.style.AppDarkTheme);
        } else {
            context2.setTheme(R.style.AppTheme);
        }
    }

    public static void resetAppOpenAdToken(Context context2) {
        SharedPref sharedPref = new SharedPref(context2);
        sharedPref.updateAppOpenToken(0);
        Log.d("AppOpenAdsToken", "Reset app open token : " + sharedPref.getAppOpenToken());
    }

//    public static AdRequest getAdRequest(Activity activity) {
//        return new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, GDPR.getBundleAd(activity)).build();
//    }
//
//    public static AdSize getAdSize(Activity activity) {
//        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        defaultDisplay.getMetrics(displayMetrics);
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, (int) (((float) displayMetrics.widthPixels) / displayMetrics.density));
//    }

    public static void setSystemBarColor(Activity activity, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(activity.getResources().getColor(i));
        }
    }

    public static void changeMenuIconColor(Menu menu, int i) {
        for (int i2 = 0; i2 < menu.size(); i2++) {
            Drawable icon = menu.getItem(i2).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public static void darkNavigationStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.colorToolbarDark));
            activity.getWindow().getDecorView().setSystemUiVisibility(8192);
        }
    }

    public static void darkNavigation(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.colorToolbarDark));
        }
    }

    public static void lightNavigation(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT >= 26) {
            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.colorPrimary));
            activity.getWindow().getDecorView().setSystemUiVisibility(16);
        }
    }

    public static void lightToolbar(Activity activity, Toolbar toolbar) {
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
    }

    public static void darkToolbar(Activity activity, Toolbar toolbar) {
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.colorToolbarDark));
    }

    public static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, 201326592, false);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(1792);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, 201326592, false);
            activity.getWindow().setStatusBarColor(0);
            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.colorToolbarDark));
        }
    }

    public static void transparentStatusBarNavigation(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, 201326592, false);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(1792);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, 201326592, false);
            activity.getWindow().setStatusBarColor(0);
            activity.getWindow().setNavigationBarColor(0);
        }
    }

    public static void setWindowFlag(Activity activity, int i, boolean z) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (z) {
            attributes.flags = i | attributes.flags;
        } else {
            attributes.flags = (i ^ -1) & attributes.flags;
        }
        window.setAttributes(attributes);
    }

    public static String withSuffix(long j) {
        if (j < 1000) {
            return "" + j;
        }
        double d = (double) j;
        int log = (int) (Math.log(d) / Math.log(1000.0d));
        double pow = Math.pow(1000.0d, (double) log);
        Double.isNaN(d);
        return String.format("%.1f%c", new Object[]{Double.valueOf(d / pow), Character.valueOf("KMGTPE".charAt(log - 1))});
    }

    public static long timeStringtoMilis(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void saveImage(Context context2, byte[] bArr, String str, String str2) {
        File file;
        try {
            if (Build.VERSION.SDK_INT >= 30) {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + context2.getString(R.string.app_name));
            } else {
                file = new File(Environment.getExternalStorageDirectory() + "/" + context2.getString(R.string.app_name));
            }
            boolean z = true;
            if (!file.exists()) {
                z = file.mkdirs();
            }
            if (z) {
                File file2 = new File(file, str);
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(bArr);
                fileOutputStream.flush();
                fileOutputStream.close();
                ContentValues contentValues = new ContentValues();
                contentValues.put("title", file2.getName());
                contentValues.put("_display_name", file2.getName());
                contentValues.put(Constants.RESPONSE_DESCRIPTION, "");
                contentValues.put("mime_type", str2);
                contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
                contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
                contentValues.put("_data", file2.getAbsolutePath());
                context2.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareImage(Context context2, byte[] bArr, String str, String str2) {
        File file;
        try {
            if (Build.VERSION.SDK_INT >= 30) {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + context2.getString(R.string.app_name));
            } else {
                file = new File(Environment.getExternalStorageDirectory() + "/" + context2.getString(R.string.app_name));
            }
            boolean z = true;
            if (!file.exists()) {
                z = file.mkdirs();
            }
            if (z) {
                File file2 = new File(file, str);
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(bArr);
                fileOutputStream.flush();
                fileOutputStream.close();
                ContentValues contentValues = new ContentValues();
                contentValues.put("title", file2.getName());
                contentValues.put("_display_name", file2.getName());
//                contentValues.put(Constants.RESPONSE_DESCRIPTION, "");
                contentValues.put("mime_type", str2);
                contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
                contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
                contentValues.put("_data", file2.getAbsolutePath());
                context2.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + file2.getAbsolutePath()));
                context2.startActivity(Intent.createChooser(intent, "Share Image"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWallpaperFromOtherApp(Context context2, byte[] bArr, String str, String str2) {
        File file;
        try {
            if (Build.VERSION.SDK_INT >= 30) {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + context2.getString(R.string.app_name));
            } else {
                file = new File(Environment.getExternalStorageDirectory() + "/" + context2.getString(R.string.app_name));
            }
            boolean z = true;
            if (!file.exists()) {
                z = file.mkdirs();
            }
            if (z) {
                File file2 = new File(file, str);
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(bArr);
                fileOutputStream.flush();
                fileOutputStream.close();
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
                Intent intent = new Intent("android.intent.action.ATTACH_DATA");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.parse("file://" + file2.getAbsolutePath()), "image/*");
                intent.putExtra("mimeType", "image/*");
                context2.startActivity(Intent.createChooser(intent, "Set as:"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setGifWallpaper(Context context2, byte[] bArr, String str, String str2) {
        SharedPref sharedPref = new SharedPref(context2);
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context2.getString(R.string.app_name));
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.exists()) {
                File file2 = new File(file, str);
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(bArr);
                fileOutputStream.flush();
                fileOutputStream.close();
                ContentValues contentValues = new ContentValues();
                contentValues.put("title", file2.getName());
                contentValues.put("_display_name", file2.getName());
                contentValues.put(Constants.RESPONSE_DESCRIPTION, "");
                contentValues.put("mime_type", str2);
                contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
                contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
                contentValues.put("_data", file2.getAbsolutePath());
                context2.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Constant.gifPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + context2.getString(R.string.app_name);
                Constant.gifName = file2.getName();
                try {
                    WallpaperManager.getInstance(context2).clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
//                intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(context2, SetGIFAsWallpaperService.class));
                context2.startActivity(intent);
                sharedPref.saveGif(Constant.gifPath, Constant.gifName);
                Log.d("GIF_PATH", Constant.gifPath);
                Log.d("GIF_NAME", Constant.gifName);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void scanFile(Context context2, Uri uri) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(uri);
        context2.sendBroadcast(intent);
    }

    public static String createName(String str) {
        return str.substring(str.lastIndexOf(47) + 1);
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        long length = file.length();
        if (length <= 2147483647L) {
            int i = (int) length;
            byte[] bArr = new byte[i];
            int i2 = 0;
            FileInputStream fileInputStream = new FileInputStream(file);
            while (i2 < i) {
                try {
                    int read = fileInputStream.read(bArr, i2, i - i2);
                    if (read < 0) {
                        break;
                    }
                    i2 += read;
                } catch (Throwable th) {
                    fileInputStream.close();
                    throw th;
                }
            }
            fileInputStream.close();
            if (i2 >= i) {
                return bArr;
            }
            throw new IOException("Could not completely read file " + file.getName());
        }
        throw new IOException("File is too large!");
    }

    public static int dpToPx(Context context2, int i) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) i, context2.getResources().getDisplayMetrics()));
    }

    public static boolean isNetworkAvailable(Activity activity) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            for (Network networkInfo : connectivityManager.getAllNetworks()) {
                if (connectivityManager.getNetworkInfo(networkInfo).getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else if (!(connectivityManager == null || (allNetworkInfo = connectivityManager.getAllNetworkInfo()) == null)) {
            for (NetworkInfo networkInfo2 : allNetworkInfo) {
                if (networkInfo2.getState() == NetworkInfo.State.CONNECTED) {
                    Log.d("Network", "NETWORKNAME: " + networkInfo2.getTypeName());
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isConnect(Context context2) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context2.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return false;
            }
            if (activeNetworkInfo.isConnected() || activeNetworkInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0032, code lost:
        if (r6 != null) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0034, code lost:
        r6.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0044, code lost:
        if (r6 != null) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0047, code lost:
        return r0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String getJSONString(String r6) {
        /*
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch:{ Exception -> 0x003f, all -> 0x003a }
            r1.<init>(r6)     // Catch:{ Exception -> 0x003f, all -> 0x003a }
            java.net.URLConnection r6 = r1.openConnection()     // Catch:{ Exception -> 0x003f, all -> 0x003a }
            java.net.HttpURLConnection r6 = (java.net.HttpURLConnection) r6     // Catch:{ Exception -> 0x003f, all -> 0x003a }
            int r1 = r6.getResponseCode()     // Catch:{ Exception -> 0x0038 }
            r2 = 200(0xc8, float:2.8E-43)
            if (r1 != r2) goto L_0x0032
            java.io.InputStream r1 = r6.getInputStream()     // Catch:{ Exception -> 0x0038 }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0038 }
            r2.<init>()     // Catch:{ Exception -> 0x0038 }
        L_0x001d:
            int r3 = r1.read()     // Catch:{ Exception -> 0x0038 }
            r4 = -1
            if (r3 == r4) goto L_0x0028
            r2.write(r3)     // Catch:{ Exception -> 0x0038 }
            goto L_0x001d
        L_0x0028:
            byte[] r1 = r2.toByteArray()     // Catch:{ Exception -> 0x0038 }
            java.lang.String r2 = new java.lang.String     // Catch:{ Exception -> 0x0038 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x0038 }
            r0 = r2
        L_0x0032:
            if (r6 == 0) goto L_0x0047
        L_0x0034:
            r6.disconnect()
            goto L_0x0047
        L_0x0038:
            r1 = move-exception
            goto L_0x0041
        L_0x003a:
            r6 = move-exception
            r5 = r0
            r0 = r6
            r6 = r5
            goto L_0x0049
        L_0x003f:
            r1 = move-exception
            r6 = r0
        L_0x0041:
            r1.printStackTrace()     // Catch:{ all -> 0x0048 }
            if (r6 == 0) goto L_0x0047
            goto L_0x0034
        L_0x0047:
            return r0
        L_0x0048:
            r0 = move-exception
        L_0x0049:
            if (r6 == 0) goto L_0x004e
            r6.disconnect()
        L_0x004e:
            goto L_0x0050
        L_0x004f:
            throw r0
        L_0x0050:
            goto L_0x004f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.wallpapers.papers.utils.Tools.getJSONString(java.lang.String):java.lang.String");
    }

    public static void downloadImageLegacy(Activity activity, String str, String str2) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str2));
            DownloadManager.Request notificationVisibility = request.setAllowedNetworkTypes(3).setAllowedOverRoaming(false).setTitle(str).setMimeType("image/jpeg").setNotificationVisibility(1);
            String str3 = Environment.DIRECTORY_PICTURES;
            notificationVisibility.setDestinationInExternalPublicDir(str3, File.separator + str + ".jpg");
            ((DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
            Toast.makeText(activity, "Image download started.", Toast.LENGTH_SHORT).show();
        } catch (Exception unused) {
            Toast.makeText(activity, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
