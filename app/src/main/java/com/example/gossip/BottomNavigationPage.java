package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.siddydevelops.customlottiedialogbox.CustomLottieDialog;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class BottomNavigationPage extends AppCompatActivity {
    public static BottomNavigationView navigationView;
    public static ViewGroup.MarginLayoutParams initialLayoutParams;
    public static int width;
    public static CustomLottieDialog customLottieDialog;
    public static com.google.android.material.bottomappbar.BottomAppBar bottomBar;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"closing..",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_page);
        SmoothBottomBar smoothBottomBar=findViewById(R.id.bottomBar);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new BlankFragment());
        fragmentTransaction.commit();
        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {


//                customLottieDialog = new CustomLottieDialog(getApplicationContext(), "LO05");
//                customLottieDialog.setLottieBackgroundColor("#000000");
//                customLottieDialog.setDialogLayoutDimensions(200,200);
//                customLottieDialog.setLoadingText("");
//                customLottieDialog.show();
                if(i==0){
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout,new BlankFragment());
                    fragmentTransaction.commit();
                }
                if(i==1){
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout,new HomeFragment());
                }
                if(i==2){
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout,new RandomChat());
//                    customLottieDialog.dismiss();
                    fragmentTransaction.commit();
                }
                return false;
            }
        });
//        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
//
//        bottomNavigationView.setBackground(null);
//        navigationView = findViewById(R.id.bottom_nav);
//        navigationView.setOnNavigationItemSelectedListener(this);
//        bottomBar=findViewById(R.id.bottomBar);
//        initialLayoutParams = (ViewGroup.MarginLayoutParams) bottomBar.getLayoutParams();
//        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) bottomBar.getBackground();
//        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
//
////        Menu menu = bottomNavigationView.getMenu();
////
////        MenuItem menuItem = menu.findItem(R.id.navigation_profile);
////        menuItem.setActionView(R.layout.profile_img_bottom_nav);
////
////        View actionView = menuItem.getActionView();
////        ImageView profileImage = actionView.findViewById(R.id.profile_image);
////
////        Glide.with(this)
////                .load("https://images.unsplash.com/photo-1661956600684-97d3a4320e45?ixlib=rb-4.0.3&ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80")
////                .into(profileImage);
//        Resources res = getResources();
//        DisplayMetrics displayMetrics = res.getDisplayMetrics();
//
//        int screenWidth = displayMetrics.widthPixels;
//        int screenHeight = displayMetrics.heightPixels;
//
//        float screenDensity = displayMetrics.density;
//        float radiusDp = 12f; // replace this value with the radius of the curved corners in dp
//
//        float radiusPx = radiusDp * screenDensity;
//        float bottomCornerRadiusPx = (screenHeight - screenWidth + 2 * radiusPx) / 2;
//
//
//
//
//
//
//
//        bottomBarBackground.setShapeAppearanceModel(
//                bottomBarBackground.getShapeAppearanceModel()
//                        .toBuilder()
//                        .setTopRightCorner(CornerFamily.ROUNDED,bottomCornerRadiusPx/12)
//                        .setTopLeftCorner(CornerFamily.ROUNDED,bottomCornerRadiusPx/12)
//                        .setBottomLeftCorner(CornerFamily.ROUNDED,bottomCornerRadiusPx/8)
//                        .setBottomRightCorner(CornerFamily.ROUNDED,bottomCornerRadiusPx/8)
//                        .build());
//
//    }
//    BlankFragment blankFragment=new BlankFragment();
//    HomeFragment homeFragment=new HomeFragment();
//    RandomChat randomChat=new RandomChat();
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.navigation_profile:
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, blankFragment).commit();
//                return true;
//
//            case R.id.Menu:
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,randomChat).commit();
//                return true;
//        }
//
//        return false;
    }
//    private void switchFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_layout, fragment);
//        transaction.commit();
//        width=bottomBar.getWidth();
//    }
}