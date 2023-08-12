package com.example.gossip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.cuberto.liquid_swipe.LiquidPager;

public class Start_Screen_1 extends AppCompatActivity {
//    public static LiquidPager pager;

    RelativeLayout mainRL;
    RelativeLayout getStarted;
    // declare viewPager
    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    ViewPager mSLideViewPager;
    RelativeLayout next;
    viewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen1);
//        next = findViewById(R.id.next);
//        Button skip = findViewById(R.id.skip);
        dot1=findViewById(R.id.dot1);
        dot2=findViewById(R.id.dot2);
        dot3=findViewById(R.id.dot3);
        mainRL=findViewById(R.id.mainRL);
        getStarted=findViewById(R.id.getStarted);
        dot1.setBackgroundResource(R.drawable.start_screen_dots_red);
//        getStarted.setVisibility(View.INVISIBLE);

//        mainRL.setBackgroundColor(#);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });

//        pager = findViewById(R.id.pager);
        if (!MemoryData.getData(this).isEmpty()) {
            Intent i = new Intent(Start_Screen_1.this, MainActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putString("mobile",MemoryData.getData(this));
//            bundle.putString("name",MemoryData.getData(this));
//            i.putExtra("mobile",MemoryData.getData(this));
//            i.putExtra("name",MemoryData.getName(this));
//            BlankFragment frag=new BlankFragment();
//            frag.setArguments(bundle);
//            Intent i=new Intent(getApplicationContext(),BottomNavigationPage.class);
//            Fragmentclass frag=new Fragmentclass();
            startActivity(i);
            finish();
        }


//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getitem(0) < 3)
////                    Toast.makeText(getApplicationContext(), "aaaaa",Toast.LENGTH_SHORT).show();
//                    mSLideViewPager.setCurrentItem(getitem(1),true);
//
//            }
//        });
//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        mSLideViewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager = new viewPager(this);
        mSLideViewPager.setAdapter(viewPager);
        mSLideViewPager.addOnPageChangeListener(viewListener);
        // calling the constructor of viewPager class
//        viewPager = new viewPager(getSupportFragmentManager(), 1);
//        pager.setAdapter(viewPager);
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        int previousPosition = 0;

        @Override
        public void onPageSelected(int position) {
            int s = 0;
            int e = 0;
            switch (position) {
                case 0:
//                    next.setVisibility(View.VISIBLE);
//                    getStarted.setVisibility(View.INVISIBLE);
//                    s = Color.parseColor("#303030");
//                    e = s = Color.parseColor("#303030");
                    dot1.setBackgroundResource(R.drawable.start_screen_dots_red);
                    dot2.setBackgroundResource(R.drawable.start_screen_dots);
                    dot3.setBackgroundResource(R.drawable.start_screen_dots);
                    break;
                case 1:

//                    next.setVisibility(View.VISIBLE);
//                    getStarted.setVisibility(View.INVISIBLE);
//                    s = Color.parseColor("#303030");
//                    e = Color.parseColor("#608595");
                    dot1.setBackgroundResource(R.drawable.start_screen_dots);
                    dot2.setBackgroundResource(R.drawable.start_screen_dots_red);
                    dot3.setBackgroundResource(R.drawable.start_screen_dots);
                    break;
                case 2:
//                    next.setVisibility(View.INVISIBLE);
//                    getStarted.setVisibility(View.VISIBLE);
//                    s = Color.parseColor("#608595");
//                    e = Color.parseColor("#303030");
                    dot1.setBackgroundResource(R.drawable.start_screen_dots);
                    dot2.setBackgroundResource(R.drawable.start_screen_dots);
                    dot3.setBackgroundResource(R.drawable.start_screen_dots_red);
                    break;

            }
//            int startColor = s;
//            int endColor = e;
//            if (position != previousPosition) { // Check if the position has changed
//                ValueAnimator animator = ValueAnimator.ofArgb(startColor, endColor);
//                animator.setDuration(1500);
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animator) {
//
//                        int currentColor = (int) animator.getAnimatedValue();
//                        mainRL.setBackgroundColor(currentColor);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setStatusBarColor(currentColor);
//                            getWindow().setNavigationBarColor(currentColor);
//                        }
//                    }
//                });
//                animator.start();
//            }
//            previousPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return mSLideViewPager.getCurrentItem() + i;
    }

//    public void setUpindicator(int positionn){
//
//    }

}