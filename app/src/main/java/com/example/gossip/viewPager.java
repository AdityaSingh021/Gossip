package com.example.gossip;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class viewPager extends PagerAdapter {
    Context context;
    int images[]={
            R.drawable.group_1
            ,R.drawable.message,
            R.drawable.anonymous
    };
    int headings[]={
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three
    };
    int description[]={
            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three
    };
    public viewPager(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.fragment_start_screen_child1,container,false);
//        Toast.makeText(container.getContext(), String.valueOf(position),Toast.LENGTH_SHORT).show();
        ImageView img1=(ImageView) view.findViewById(R.id.image);
        TextView heading=(TextView) view.findViewById(R.id.heading);
        TextView desc=(TextView) view.findViewById(R.id.description);
        img1.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
         container.removeView((LinearLayout)object);
    }
}
