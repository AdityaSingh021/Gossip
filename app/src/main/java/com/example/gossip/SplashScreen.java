package com.example.gossip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity or the next activity
                if (!MemoryData.getData(getApplicationContext()).isEmpty()) {
                    Intent i = new Intent(getApplicationContext(), BottomNavigationPage.class);
                    Bundle bundle = new Bundle();
                    startActivity(i);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                finish(); // Close the splash screen activity
            }
        }, 1000);
    }
}