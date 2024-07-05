package com.hdlight.wallpaperapps.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.adapter.ViewPagerAdapterSlider;
import com.hdlight.wallpaperapps.adapter.WallPaperAdapter;
import com.hdlight.wallpaperapps.helper.CustomVolleyRequest;
import com.hdlight.wallpaperapps.ui.MainActivity;
import com.hdlight.wallpaperapps.utils.Config;
import com.hdlight.wallpaperapps.utils.SliderUtils;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class TrendingFragment extends Fragment {

    SharedPreferences sharedPreferences;
    Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference reference;
    private WallPaperAdapter wallPaperAdapter;
    private ArrayList<String> list;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        this.sharedPreferences = defaultSharedPreferences;
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("images");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        getData();



        return view;
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressBar.setVisibility(View.GONE);
                list = new ArrayList<>();
                for (DataSnapshot shot : snapshot.getChildren()) {
                    String data = shot.getValue().toString();
                    list.add(data);
                }
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                wallPaperAdapter = new WallPaperAdapter(list, context);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(wallPaperAdapter);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                },1000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                Toast.makeText(context, "Error :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}