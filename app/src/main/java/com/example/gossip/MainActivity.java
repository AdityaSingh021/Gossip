package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gossip.Status.StatusState;
import com.example.gossip.messages.MessagesList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<MessagesList> retrievedList;

    public static boolean cond;
    private MyFragmentAdapter myFragmentAdapter;
    private ViewPager2 viewPager;
    public static String myName;
    public static String mobile;
    public static List<StatusState> statusState;
    static DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");

    com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.BottomNav);
        viewPager = findViewById(R.id.FragmentCntnr);
        myFragmentAdapter=new MyFragmentAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(myFragmentAdapter);
//        viewPager.setCurrentItem(1,true);
        getFCMToken();
//        BlankFragment blankFragment=new BlankFragment();
//        RandomChat randomChat=new RandomChat();
//        NotificationFragment notificationFragment=new NotificationFragment();
//        ProfileFragment profileFragment=new ProfileFragment();
//        replace(randomChat,false);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
        myName=sharedPreferences.getString("name","User@1a8e");
        Container.setMyName(myName);
        mobile=MemoryData.getData(getApplicationContext());
        Container.setMobile(mobile);

        retrievedList = MemoryData.retrieveMessageList(getApplicationContext());
//        statusState=MemoryData.getStatusState(getApplicationContext());
        Intent serviceIntent = new Intent(this, MyBackgroundService.class);
        startService(serviceIntent);

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
                    viewPager.setCurrentItem(0,true);
//                    replace(randomChat,false);
                    return true;
                }
                if(id==R.id.RandomChat){
                    viewPager.setCurrentItem(1,true);
//                    replace(blankFragment,true);

                    return true;
                }
                if(id==R.id.Notification){
                    viewPager.setCurrentItem(2,true);
//                    replace(notificationFragment,false);
                    return true;
                }
                else{
                    viewPager.setCurrentItem(3,true);
//                    replace(profileFragment,false);
                    return true;
                }
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Update the selected item in the BottomNavigationView
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });


//        bottomNavigationView.setSelectedItemId(R.id.Home);
    }
//    public void replace(Fragment fragment,boolean flag){
//        FragmentManager fm=getSupportFragmentManager();
//        FragmentTransaction ft= fm.beginTransaction();
//        if(flag){
//            ft.add(R.id.FragmentCntnr,fragment).commit();
//        }else{
//            ft.replace(R.id.FragmentCntnr,fragment).commit();
//        }
////        getSupportFragmentManager().beginTransaction()
////                .replace(R.id.FragmentCntnr, fragment)
////                .commit();
//    }
    void getFCMToken (){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.i ( "My token", token) ;
                databaseReference.child("Users").child(mobile).child("fcmToken").setValue(token);
            }
        });
    }

    public static void sendRequest(TextView searchUserName, Context context, TextView warning, Dialog dialog){
        String userName=searchUserName.getText().toString();
        if(!userName.isEmpty() && !userName.equals(myName)){
            databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final int[] check = {0};
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        if(snapshot1.child("Name").getValue().equals(userName)){
                            databaseReference.child(mobile).child("contacts").child(snapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Toast.makeText(context,"Already a friend.",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                    }else{
                                        databaseReference.child(snapshot1.getKey()).child("FrndReq").child(mobile).child("Name").setValue(myName);

                                        databaseReference.child(mobile).child("ProfilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                databaseReference.child(snapshot1.getKey()).child("FrndReq").child(mobile).child("profilePic").setValue(snapshot.getValue(String.class));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });



                                        check[0] =1;
                                        Toast.makeText(context,"Request sent",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            dialog.dismiss();
                            break;
                        }
                    }
                    if(check[0]==0){
                        warning.setVisibility(View.VISIBLE);
                        warning.setText("Warning : User does not exist.");
                        searchUserName.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else Toast.makeText(context,"Enter a Valid user name",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cond)
        databaseReference.child("Users").child(mobile).child("State").setValue(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cond=true;
        databaseReference.child("Users").child(mobile).child("State").setValue(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        databaseReference.child("Users").child(mobile).child("State").setValue(0);
    }

    @Override
    public void finish() {
        super.finish();
//        databaseReference.child("Users").child(mobile).child("State").setValue(0);
    }
    //    onFinish
}