package com.hdlight.wallpaperapps.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

public class CustomVolleyRequest {
    private static Context context;
    private static CustomVolleyRequest customVolleyRequest;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;

    private CustomVolleyRequest(Context context2) {
        context = context2;
        RequestQueue requestQueue2 = getRequestQueue();
        this.requestQueue = requestQueue2;
        this.imageLoader = new ImageLoader(requestQueue2, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            public Bitmap getBitmap(String str) {
                return this.cache.get(str);
            }

            public void putBitmap(String str, Bitmap bitmap) {
                this.cache.put(str, bitmap);
            }
        });
    }

    public static synchronized CustomVolleyRequest getInstance(Context context2) {
        CustomVolleyRequest customVolleyRequest2;
        synchronized (CustomVolleyRequest.class) {
            if (customVolleyRequest == null) {
                customVolleyRequest = new CustomVolleyRequest(context2);
            }
            customVolleyRequest2 = customVolleyRequest;
        }
        return customVolleyRequest2;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            RequestQueue requestQueue2 = new RequestQueue(new DiskBasedCache(context.getCacheDir(), 10485760), new BasicNetwork((BaseHttpStack) new HurlStack()));
            this.requestQueue = requestQueue2;
            requestQueue2.start();
        }
        return this.requestQueue;
    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader() {
        return this.imageLoader;
    }
}
