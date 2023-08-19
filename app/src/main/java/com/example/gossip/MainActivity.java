package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gossip.Status.StatusState;
import com.example.gossip.chat.Chat;
import com.example.gossip.messages.MessagesList;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    public static List<MessagesList> retrievedList;
    public static String firstTime;

    public static boolean cond;
    private MyFragmentAdapter myFragmentAdapter;
    private ViewPager2 viewPager;
    public static String myName;
    public static String mobile;
    public static List<StatusState> statusState;
    static DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");

    com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.BottomNav);
        viewPager = findViewById(R.id.FragmentCntnr);
        myFragmentAdapter=new MyFragmentAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(myFragmentAdapter);
        byte[] key="12345678123456781234567812345678".getBytes();
        bytesToHex(key);
        String temp=bytesToHex(key);;
        Log.i("mySecretKey",temp);
        firstTime=getIntent().getStringExtra("FirstTime");
        mobile=getIntent().getStringExtra("mobile");
        if(mobile==null) mobile=MemoryData.getData(getApplicationContext());
//        if(firstTime!=null && mobile!=null){
//            databaseReference.child(mobile).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    Toast.makeText(getApplicationContext(),"pehli pehli baar",Toast.LENGTH_SHORT).show();
//                    for(DataSnapshot Contact:snapshot.getChildren()){
//                        for(DataSnapshot messages:Contact.child("messages").getChildren()){
//                            String LastMessageIfNotSaved= String.valueOf(messages.child("msg"));
//                            String savedChatId= messages.getKey();
//                            MemoryData.saveLastMsgTs(messages.child("timestamp").getValue(String.class),savedChatId,getApplicationContext());
////                            MemoryData.saveLastMsgTs(LastMessageIfNotSaved, savedChatId, getContext());
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        try {
//            String temp=generateRandomKey();
//            Log.i("mySecretKey",temp);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }




        try{
            if(Container.redirect) {
                Container.redirect=false;
                String userMobile = getIntent().getExtras().getString("mobile");
                String Name = getIntent().getExtras().getString("name");
                String ChatKey = getIntent().getExtras().getString("chat_key");
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("mobile", userMobile);
                intent.putExtra("name", Name);
                intent.putExtra("chat_key", ChatKey);
                startActivity(intent);
            }
        }catch (Exception e){};

//        AppLifecycleObserver appLifecycleObserver = new AppLifecycleObserver();
//        registerActivityLifecycleCallbacks(appLifecycleObserver);
//        viewPager.setCurrentItem(1,true);
        getFCMToken();
//        BlankFragment blankFragment=new BlankFragment();
//        RandomChat randomChat=new RandomChat();
//        NotificationFragment notificationFragment=new NotificationFragment();
//        ProfileFragment profileFragment=new ProfileFragment();
//        replace(randomChat,false);
//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
////                    databaseReference.child("Users").child(mobile).child("State").setValue(1);
//                    // Connected to Firebase servers
//                } else {
////                    databaseReference.child("Users").child(mobile).child("State").setValue(0);
//                    // Disconnected from Firebase servers
//                }
//            }

//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Handle onCancelled
//            }
//        });


        SharedPreferences sharedPreferences = getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
        myName=sharedPreferences.getString("name","User@1a8e");
        Container.setMyName(myName);

        Container.setMobile(mobile);

        try{
            retrievedList = MemoryData.retrieveMessageList(getApplicationContext());
        }catch (Exception e){};

//        databaseReference.child("Users").child(mobile).child("State").setValue(1);
//        statusState=MemoryData.getStatusState(getApplicationContext());
//        Intent serviceIntent = new Intent(this, MyBackgroundService.class);
//        startService(serviceIntent);

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
                    viewPager.setCurrentItem(0,false);
//                    replace(randomChat,false);
                    return true;
                }
                if(id==R.id.RandomChat){
                    viewPager.setCurrentItem(1,false);
//                    replace(blankFragment,true);

                    return true;
                }
                if(id==R.id.Notification){
                    viewPager.setCurrentItem(2,false);
//                    replace(notificationFragment,false);
                    return true;
                }
                else{
                    viewPager.setCurrentItem(3,false);
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

    public static void sendRequest(String userName, Context context, TextView warning, Dialog dialog){
//        String userName=searchUserName.getText().toString();
        if(!userName.isEmpty() && !userName.equals(myName)){
            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

//                    databaseReference.child("Users").child(mobile).child("State").setValue(1);
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
//                                        Toast.makeText(context,"Request sent",Toast.LENGTH_SHORT).show();
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
        if(cond) databaseReference.child("Users").child(mobile).child("State").setValue(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if(cond) databaseReference.child("Users").child(mobile).child("State").setValue(0);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cond=true;
//        final int delayMillis = 2000; // Delay in milliseconds (2 seconds)
//
//        Thread backgroundThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(delayMillis); // Sleep for the specified delay
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                // This code will be executed after the specified delay
//                databaseReference.child("Users").child(mobile).child("State").setValue(1);
//            }
//        });
//
//        backgroundThread.start();
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

    public String generateAESKey() {
        try {
            // Create a KeyGenerator instance for AES encryption with a key length of 256 bits
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256); // Key length in bits

            // Generate and return a random AES key as a hexadecimal string
            SecretKey secretKey = keyGenerator.generateKey();
//            Byte[] se
            return bytesToHex(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

//     Convert byte array to hexadecimal string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}