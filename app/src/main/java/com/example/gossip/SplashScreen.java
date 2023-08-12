package com.example.gossip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.gossip.chat.Chat;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                 Start the main activity or the next activity
                if(getIntent().getExtras()!=null) {
                    Log.i("checking","yes");
                    //from notification
                    String userMobile = getIntent().getExtras().getString("Mobile");
                    String Name = getIntent().getExtras().getString("Name");
                    String ChatKey = getIntent().getExtras().getString("ChatKey");
                    Intent intent = new Intent(getApplicationContext(), Chat.class);
                    intent.putExtra("mobile", userMobile);
                    intent.putExtra("name", Name);
                    intent.putExtra("chat_key", ChatKey);
                    startActivity(intent);
                }else{
                    if (!MemoryData.getData(getApplicationContext()).isEmpty()) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        BlankFragment blankFragment=new BlankFragment();
                        Bundle bundle = new Bundle();
                        startActivity(i);
                        Animatoo.INSTANCE.animateZoom(SplashScreen.this);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(),Start_Screen_1.class);
                        startActivity(intent);
                    }
                }

                finish(); // Close the splash screen activity
            }
        }, 1500);
    }
}