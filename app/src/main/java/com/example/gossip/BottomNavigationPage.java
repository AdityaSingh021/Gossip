package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.siddydevelops.customlottiedialogbox.CustomLottieDialog;

//import me.ibrahimsn.lib.OnItemSelectedListener;
//import me.ibrahimsn.lib.SmoothBottomBar;


public class BottomNavigationPage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public BlankFragment blankFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);

//        bottomNavigationView = findViewById(R.id.bottom_nav);
//        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.Home);
//        blankFragment=new BlankFragment();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////        Fragment selectedFragment = null;
//
//        switch (item.getItemId()) {
//            case R.id.Home:
//                // Handle item 1 selection
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new BlankFragment())
//                        .commit();
////                selectedFragment = new BlankFragment();
//                break;
//            case R.id.RandomChat:
//                // Handle item 2 selection
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new RandomChat())
//                        .commit();
//                break;
//            case R.id.Notification:
//                // Handle item 3 selection
////                selectedFragment = new NotificationFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new NotificationFragment())
//                        .commit();
//                break;
//        }
//
////        if (selectedFragment != null) {
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.fragment_container, selectedFragment)
////                    .commit();
////            return true;
////        }
//
//        return false;
//    }


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        return false;
//    }
}