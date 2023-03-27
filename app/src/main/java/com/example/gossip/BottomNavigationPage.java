package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;


public class BottomNavigationPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    public static BottomNavigationView navigationView;
    public static ViewGroup.MarginLayoutParams initialLayoutParams;
    public static int width;
    public static com.google.android.material.bottomappbar.BottomAppBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_page);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);

        bottomNavigationView.setBackground(null);
        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);
        bottomBar=findViewById(R.id.bottomBar);
        initialLayoutParams = (ViewGroup.MarginLayoutParams) bottomBar.getLayoutParams();
        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) bottomBar.getBackground();
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);


        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        float screenDensity = displayMetrics.density;
        float radiusDp = 12f; // replace this value with the radius of the curved corners in dp

        float radiusPx = radiusDp * screenDensity;
        float bottomCornerRadiusPx = (screenHeight - screenWidth + 2 * radiusPx) / 2;







        bottomBarBackground.setShapeAppearanceModel(
                bottomBarBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED,bottomCornerRadiusPx/12)
                        .setTopLeftCorner(CornerFamily.ROUNDED,bottomCornerRadiusPx/12)
                        .setBottomLeftCorner(CornerFamily.ROUNDED,bottomCornerRadiusPx/8)
                        .setBottomRightCorner(CornerFamily.ROUNDED,bottomCornerRadiusPx/8)
                        .build());

    }
    BlankFragment blankFragment=new BlankFragment();
    HomeFragment homeFragment=new HomeFragment();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, blankFragment).commit();
                return true;

            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                return true;
        }

        return false;
    }
//    private void switchFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_layout, fragment);
//        transaction.commit();
//        width=bottomBar.getWidth();
//    }
}