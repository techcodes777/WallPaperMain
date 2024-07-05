package com.hdlight.wallpaperapps.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.helper.CustomVolleyRequest;
import com.hdlight.wallpaperapps.model.Wallpaper;
import com.hdlight.wallpaperapps.utils.SliderUtils;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapterSlider extends PagerAdapter {
    private Activity activity;
//    AdapterCategory adapterCategory;
    String category_id;
    /* access modifiers changed from: private */
    public Context context;
    private ImageLoader imageLoader;
    List<Wallpaper> items = new ArrayList();
    private LayoutInflater layoutInflater;
    /* access modifiers changed from: private */
    public List<SliderUtils> sliderImg;
    SliderUtils sliderUtils;
    String table;

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public ViewPagerAdapterSlider(List list, Context context2) {
        this.sliderImg = list;
        this.context = context2;
    }

    public int getCount() {
        return this.sliderImg.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, final int i) {
        LayoutInflater layoutInflater2 = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutInflater = layoutInflater2;
        View inflate = layoutInflater2.inflate(R.layout.custom_layout, (ViewGroup) null);
        final SliderUtils sliderUtils2 = this.sliderImg.get(i);
        TextView textView = (TextView) inflate.findViewById(R.id.textSlider);
        textView.setText(this.sliderImg.get(i).getSliderName());
        textView.setAllCaps(true);
        ImageLoader imageLoader2 = CustomVolleyRequest.getInstance(this.context).getImageLoader();
        this.imageLoader = imageLoader2;
        imageLoader2.get(sliderUtils2.getSliderImageUrl(), ImageLoader.getImageListener((ImageView) inflate.findViewById(R.id.imageView), R.drawable.slider_loading, R.drawable.slider_loading));
//        inflate.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                if (sliderUtils2.getType() == 1) {
//                    Intent intent = new Intent("android.intent.action.VIEW");
//                    intent.setData(Uri.parse(((SliderUtils) ViewPagerAdapterSlider.this.sliderImg.get(i)).getUrl()));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    ViewPagerAdapterSlider.this.context.startActivity(intent);
//                }
//                if (sliderUtils2.getType() == 2) {
////                    Intent intent2 = new Intent(ViewPagerAdapterSlider.this.context, ActivityCategoryDetailsSlider.class);
////                    intent2.putExtra("cat_id", sliderUtils2.getCatId());
////                    intent2.putExtra("cat_name", sliderUtils2.getCatName());
////                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    ViewPagerAdapterSlider.this.context.startActivity(intent2);
//                }
//                if (sliderUtils2.getType() == 3) {
//////                    Intent intent3 = new Intent(ViewPagerAdapterSlider.this.context, ActivitySearch.class);
////                    intent3.putExtra(DBHelper.TAGS, ((SliderUtils) ViewPagerAdapterSlider.this.sliderImg.get(i)).getWallName());
////                    intent3.putExtra(Constant.EXTRA_OBJC, "wallpaper");
////                    intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    ViewPagerAdapterSlider.this.context.startActivity(intent3);
//                }
//            }
//        });
        ((ViewPager) viewGroup).addView(inflate, 0);
        return inflate;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ((ViewPager) viewGroup).removeView((View) obj);
    }
}
