package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    public static String myName;
    public static String mobile;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");

    com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.BottomNav);
        getFCMToken();
        BlankFragment blankFragment=new BlankFragment();
        RandomChat randomChat=new RandomChat();
        NotificationFragment notificationFragment=new NotificationFragment();
        ProfileFragment profileFragment=new ProfileFragment();
        replace(blankFragment,false);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
        myName=sharedPreferences.getString("name","User@1a8e");
        mobile=MemoryData.getData(getApplicationContext());
//        databaseReference.child()
        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.Home) {
                    replace(blankFragment,true);
                    return true;
                }
                if(id==R.id.RandomChat){
                    replace(randomChat,false);
                    return true;
                }
                if(id==R.id.Notification){
                    replace(notificationFragment,false);
                    return true;
                }
                else{
                    replace(profileFragment,false);
                    return true;
                }
            }
        });

//        bottomNavigationView.setSelectedItemId(R.id.Home);
    }
    public void replace(Fragment fragment,boolean flag){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        if(flag){
            ft.add(R.id.FragmentCntnr,fragment).commit();
        }else{
            ft.replace(R.id.FragmentCntnr,fragment).commit();
        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.FragmentCntnr, fragment)
//                .commit();
    }
    void getFCMToken (){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.i ( "My token", token) ;
                databaseReference.child("Users").child(mobile).child("fcmToken").setValue(token);
            }
        });
    }
}