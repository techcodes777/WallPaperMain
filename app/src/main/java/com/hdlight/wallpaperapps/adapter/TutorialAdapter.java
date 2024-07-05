package com.hdlight.wallpaperapps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hdlight.wallpaperapps.R;

public class TutorialAdapter extends PagerAdapter {

    Context context;
    int images[];
    String title[];
    String detail[];
    LayoutInflater layoutInflater;

    public TutorialAdapter(Context context, int[] images, String[] title, String[] detail) {
        this.context = context;
        this.images = images;
        this.title = title;
        this.detail = detail;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view  == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.adapter_tutorial, container, false);
        ImageView Img = (ImageView) itemView.findViewById(R.id.img);
        TextView lblTitle = (TextView) itemView.findViewById(R.id.txtMainTitle);
        TextView lblSubTitle = (TextView) itemView.findViewById(R.id.txtSubTitle);
        ImageView imgSunday = (ImageView) itemView.findViewById(R.id.imgSunday);
        ImageView imgNovember = (ImageView) itemView.findViewById(R.id.imgNovember);

        lblSubTitle.setText(detail[position]);
        Img.setImageResource(images[position]);
        String name = "SEARCH";
//        SpannableStringBuilder longDescription = new SpannableStringBuilder();
////        longDescription.append("SEARCH");
//        int start = longDescription.length();
//        longDescription.append(name);
//        longDescription.setSpan(new ForegroundColorSpan(Color.WHITE), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        longDescription.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        String sourceString = "<b>" + name + "</b> " + title[position];
        lblTitle.setText(Html.fromHtml(sourceString));
//        lblTitle.setText(longDescription) ;
        if(position == 1){
            imgSunday.setVisibility(View.GONE);
            imgNovember.setVisibility(View.GONE);

        }else if (position == 2){
            imgSunday.setVisibility(View.GONE);
            imgNovember.setVisibility(View.GONE);
        }else {
            imgSunday.setVisibility(View.VISIBLE);
            imgNovember.setVisibility(View.VISIBLE);
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
