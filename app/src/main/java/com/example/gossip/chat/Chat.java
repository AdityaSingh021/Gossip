package com.example.gossip.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.example.gossip.BlankFragment;
import com.example.gossip.ContactChats;
import com.example.gossip.HomeActivity;
import com.example.gossip.MainActivity;
import com.example.gossip.MemoryData;
import com.example.gossip.R;
import com.example.gossip.ReportDialog;
import com.example.gossip.StartRandomChat;
import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    //    public boolean check;
    private int check1;

    ValueEventListener valLis1;
    private ImageView back;
    boolean State;
    ShimmerFrameLayout shimmerFrameLayout;
    private EditText message;
    private String mobile;
    private String myUserNameInOthersContact;
    private String otherUserFcmToken;
    private final List<chatList> chatLists = new ArrayList<>();
    String getUserMobile = "";
    String chatKey;
    private int generatedChatKey;
    private RecyclerView chattingRecyclerView;
    private chatAdapter chatAdapter;
    private boolean check = true;
    ValueEventListener mValueEventListener;

    //    MessagesList finalCurrentItem;
//    ValueEventListener listener;
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseReference != null && mValueEventListener != null) {
            databaseReference.removeEventListener(mValueEventListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mobile = MainActivity.mobile;
        int[] check1 = {0};
        final TextView name = findViewById(R.id.name_chat);
        message = findViewById(R.id.message);
        final ImageView sendButton = findViewById(R.id.sendBtn);
        TextView Status = findViewById(R.id.Status);
        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);
//        TextView info=findViewById(R.id.information);
//        databaseReference.child("Users").child(mobile).child("State").setValue(1);
        // data from message adapter
        SharedPreferences sharedPreferences1= getApplicationContext().getSharedPreferences("Notifications",MODE_PRIVATE);
        State=sharedPreferences1.getBoolean("Status",true);
        final String getName = getIntent().getStringExtra("name");
        name.setText(getName);

        MainActivity.cond=true;
//        new SimpleTooltip.Builder(getApplicationContext())
//                .anchorView(sendButton)
//                .text("Click here to match with people")
//                .gravity(Gravity.END)
//                .animated(true)
//                .transparentOverlay(false)
//                .build()
//                .show();
        CircleImageView profilePic=findViewById(R.id.profilePic);
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");
        //get user mobile from memory
        getUserMobile = MemoryData.getData(Chat.this);

        back=findViewById(R.id.back);
        mobile=getUserMobile;
        ImageView options = findViewById(R.id.options);
        ImageView camera = findViewById(R.id.camera);
        chatAdapter = new chatAdapter(chatLists, Chat.this, MemoryData.getData(getApplicationContext()));
        chattingRecyclerView.setAdapter(chatAdapter);
        RelativeLayout topBar = findViewById(R.id.topBar);
        shimmerFrameLayout=findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
//        Toast.makeText(getApplicationContext(),getMobile,Toast.LENGTH_SHORT).show();
//        String profilePicFromNotif=getIntent().getStringExtra("profilePic");

        final int[] var = {0};
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final int minimumHeight = 0;
        valLis1=databaseReference.child("Users").child(getMobile).child("State").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    if((long)(snapshot.getValue())==Long.valueOf(0)){
                        Status.setText("Offline");
                        Status.setTextColor(Color.parseColor("#cb7d67"));
                    }else{
                        Status.setText("Online");
                        Status.setTextColor(Color.parseColor("#94E8B4"));
                    }
                }catch (Exception e){};


//                if(var[0] ==0){
//
//                    myUserNameInOthersContact=snapshot.child(getMobile).child("contacts").child(getUserMobile).child("Name").getValue(String.class);
//                    otherUserFcmToken=snapshot.child("Users").child(getMobile).child("fcmToken").getValue(String.class);
//                    String dp=snapshot.child(getMobile).child("profilePic").getValue(String.class);
//                    if(dp!=null){
//                        Picasso.get()
//                                .load(dp)
//                                .placeholder(R.drawable.default_user) // Placeholder image while loading
//                                .error(R.drawable.default_user) // Error image if the URL is invalid
//                                .into(profilePic);
//                    }
//                    var[0] =1;
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        chattingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            private boolean isScrolling = false;
//
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                ViewGroup.MarginLayoutParams layoutParams1 = (ViewGroup.MarginLayoutParams) chattingRecyclerView.getLayoutParams();
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) topBar.getLayoutParams();
//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    // The user has started scrolling
//                    layoutParams.setMarginStart(10);
//                    layoutParams.setMarginEnd(10);
//                    layoutParams1.topMargin=0;
//                    topBar.setLayoutParams(layoutParams);
//                    topBar.animate().translationY(-topBar.getHeight()).setDuration(200);
//
//                    isScrolling = true;
//                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    // The user has stopped scrolling
//                    layoutParams.setMarginStart(0);
//                    layoutParams.setMarginEnd(0);
//                    layoutParams1.topMargin=200;
//                    topBar.setLayoutParams(layoutParams);
//                    topBar.animate().translationY(0).setDuration(200);
//                    isScrolling = false;
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                // Handle scrolling events here
//                if (isScrolling) {
//
//                    // User is currently scrolling
//                    // Perform any necessary actions
//                }
//            }
//        });


        final float originalTranslationY = topBar.getTranslationY();
        final int scrollThreshold = 200; // Adjust this threshold as needed

        final AtomicInteger scrollOffset = new AtomicInteger(0);
        final AtomicBoolean isControlsVisible = new AtomicBoolean(true);

//        chattingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                // Check if the RecyclerView is no longer scrolling
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    int currentOffset = scrollOffset.get();
//                    if (currentOffset < scrollThreshold && isControlsVisible.get()) {
//                        // Scrolling up, hide the RelativeLayout
//                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) topBar.getLayoutParams();
//                        layoutParams.setMarginStart(10);
//                        layoutParams.setMarginEnd(10);
//
//                        topBar.setLayoutParams(layoutParams);
//                        topBar.animate().translationY(-topBar.getHeight()).setDuration(200);
//                        isControlsVisible.set(false);
//                    } else if (currentOffset > -scrollThreshold && !isControlsVisible.get()) {
//                        // Scrolling down, show the RelativeLayout
//                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) topBar.getLayoutParams();
//                        layoutParams.setMarginStart(0);
//                        layoutParams.setMarginEnd(0);
//                        topBar.setLayoutParams(layoutParams);
//                        topBar.animate().translationY(0).setDuration(200);
//                        isControlsVisible.set(true);
//                    }
//                    scrollOffset.set(0); // Reset the scroll offset
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                scrollOffset.addAndGet(dy); // Accumulate the scroll offset
//            }
//        });

        final boolean[] cond = {true};

        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
        chattingRecyclerView.hasFixedSize();
        mValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatKey.isEmpty()) {
                    // generate chat key
                    chatKey = "1";
                    if (snapshot.child(mobile).hasChild("chat")) {
                        DatabaseReference newMessageRef = databaseReference.child(mobile).child("chat").push();
                        chatKey = newMessageRef.getKey();
//                            chatKey = String.valueOf(snapshot.child(mobile).child("chat").getChildrenCount() + 1);
                    }
                }
                if(var[0] ==0){

                    if(cond[0]) {
                        databaseReference.child("Users").child(mobile).child("State").setValue(1);
                        cond[0] =false;
                    }
                    myUserNameInOthersContact=snapshot.child(getMobile).child("contacts").child(getUserMobile).child("Name").getValue(String.class);
                    otherUserFcmToken=snapshot.child("Users").child(getMobile).child("fcmToken").getValue(String.class);
                    String dp=snapshot.child(getMobile).child("profilePic").getValue(String.class);
                    if(dp!=null){
                        Picasso.get()
                                .load(dp)
                                .placeholder(R.drawable.default_user) // Placeholder image while loading
                                .error(R.drawable.default_user) // Error image if the URL is invalid
                                .into(profilePic);
                    }
                    var[0] =1;
                }

//                }


                if (snapshot.child(mobile).hasChild("chat")) {
                    if (snapshot.child(mobile).child("chat").child(chatKey).hasChild("messages")) {
                        chatLists.clear();
                        for (DataSnapshot messagesSnapshot : snapshot.child(mobile).child("chat").child(chatKey).child("messages").getChildren()) {
                            if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")) {
                                final String messageTimeStamp = messagesSnapshot.child("timestamp").getValue(String.class);
                                final String Mobile = messagesSnapshot.child("mobile").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);


                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimeStamp));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                chatList chatList = new chatList(Mobile, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                chatLists.add(chatList);


//                                    if(check || Long.parseLong(messageTimeStamp)>Long.parseLong(MemoryData.getLastMsgTs(Chat.this,chatKey))){
                                check = false;
                                MemoryData.saveLastMsgTs(messageTimeStamp, chatKey, getApplicationContext());
                                chatAdapter.updateChatList(chatLists);
                                chattingRecyclerView.scrollToPosition(chatLists.size() - 1);

//                                    }
                            }

                        }
                    }
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Inside",Toast.LENGTH_SHORT).show();
                ReportDialog reportDialog=new ReportDialog();
                reportDialog.openCenteredDialog(Chat.this,"User reported.","Report user");
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTxtMessage = message.getText().toString();

                // get current Timestamp

                sendMessage(getTxtMessage,getMobile,getUserMobile,chatKey,message);
            }
        });
    }
    public void sendMessage(String getTxtMessage,String getMobile,String getUserMobile,String chatKey,EditText message){
        Log.i("adityaaa","-1");
        final String currentTimeStamp = String.valueOf(System.currentTimeMillis());
//                Toast.makeText(getApplicationContext(),currentTimeStamp,Toast.LENGTH_SHORT).show();
        final int[] check1={0};
        final String[] state = {"1"};
        final String[] ChatId={chatKey};
        final boolean[] condition = {true};
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i("adityaaa","0");
                if(snapshot.child(getMobile).child("Notif").exists()){
                    state[0] =snapshot.child(getMobile).child("Notif").getValue(String.class);
                }




                if (chatKey!=null) {

                    Log.i("adityaaa","2");
//                            DatabaseReference ChatKey = databaseReference.child(getMobile).child("chat").push();
                    ChatId[0] = chatKey;

//                            int count= (int) (snapshot.child(getMobile).child("chat").getChildrenCount()+1);
//                            Toast.makeText(getApplicationContext(),"yesss",Toast.LENGTH_SHORT).show();

                    databaseReference.child(getMobile).child("contacts").child(getUserMobile).child("ChatId").setValue(ChatId[0]);
                    databaseReference.child(getUserMobile).child("contacts").child(getMobile).child("ChatId").setValue(ChatId[0]);
                    databaseReference.child(getMobile).child("chat").child(ChatId[0]).child("user_1").setValue(getMobile);
                    databaseReference.child(getMobile).child("chat").child(ChatId[0]).child("user_2").setValue(getUserMobile);



//                            if(databaseReference.child(getMobile).child("contacts").chi)
//                            databaseReference.child(getMobile).child("contacts").child(getUserMobile).child("Name").setValue(getUserMobile);
                    databaseReference.child(getMobile).child("contacts").child(getUserMobile).child("profilePic").setValue("");
                    DatabaseReference newMessageRef = databaseReference.child(getMobile).child("chat").child(ChatId[0]).child("messages").push();
                    String messageId = newMessageRef.getKey();
                    newMessageRef.child("timestamp").setValue(currentTimeStamp);
                    newMessageRef.child("msg").setValue(getTxtMessage);
                    newMessageRef.child("mobile").setValue(getUserMobile);
                    DatabaseReference newMessageRef1 = databaseReference.child(getUserMobile).child("chat").child(ChatId[0]).child("messages").child(messageId);
//                    String messageId1 = newMessageRef.getKey();
                    newMessageRef1.child("timestamp").setValue(currentTimeStamp);
                    newMessageRef1.child("msg").setValue(getTxtMessage);
                    newMessageRef1.child("mobile").setValue(getUserMobile);
//                            databaseReference.child(getMobile).child("chat").child(String.valueOf(count)).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);
//                            databaseReference.child(getMobile).child("chat").child(String.valueOf(count)).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                }else{
//                    Toast.makeText(getApplicationContext(),"2", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot snapshot1 : snapshot.child(getMobile).child("chat").getChildren()) {
                        Log.i("adityaaa","1");
                        if (snapshot1.hasChild("messages") && (snapshot1.child("user_1").getValue().equals(getUserMobile) || snapshot1.child("user_2").getValue().equals(getUserMobile))) {
                            Log.i("adityaaa","2");
                            check1[0] = 1;
                            DatabaseReference newMessageRef = databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").push();
                            String messageId = newMessageRef.getKey();
                            ChatId[0]=snapshot1.getKey();

                            databaseReference.child(getUserMobile).child("chat").child(snapshot1.getKey()).child("user_1").setValue(getUserMobile);
                            databaseReference.child(getUserMobile).child("chat").child(snapshot1.getKey()).child("user_2").setValue(getMobile);
                            Log.i("ooppoo",ChatId[0]);
                            newMessageRef.child("timestamp").setValue(currentTimeStamp);
                            newMessageRef.child("msg").setValue(getTxtMessage);
                            newMessageRef.child("mobile").setValue(getUserMobile);

                            DatabaseReference newMessageRef1=databaseReference.child(getUserMobile).child("chat").child(ChatId[0]).child("messages").child(messageId);
                            newMessageRef1.child("timestamp").setValue(currentTimeStamp);
                            newMessageRef1.child("msg").setValue(getTxtMessage);
                            newMessageRef1.child("mobile").setValue(getUserMobile);
//                            condition[0] =false;
//                                databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);;
//                                databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                            break;
                        }
                    }
                }





//                if(ChatId==null){

//                }


                if(state[0].equals("1")) sendNotification(getTxtMessage,ChatId[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        if(ChatId[0]!=null && condition[0]){
//            Toast.makeText(getApplicationContext(),"end",Toast.LENGTH_SHORT).show();
//            DatabaseReference newMessageRef = databaseReference.child(getUserMobile).child("chat").child(ChatId[0]).child("messages").push();
//            String messageId = newMessageRef.getKey();
//            databaseReference.child(getUserMobile).child("chat").child(ChatId[0]).child("user_1").setValue(getUserMobile);
//            databaseReference.child(getUserMobile).child("chat").child(ChatId[0]).child("user_2").setValue(getMobile);
//
//            newMessageRef.child("timestamp").setValue(currentTimeStamp);
//            newMessageRef.child("msg").setValue(getTxtMessage);
//            newMessageRef.child("mobile").setValue(getUserMobile);
//        }

//                databaseReference.child(mobile).child("chat").child(chatKey).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);
//                databaseReference.child(mobile).child("chat").child(chatKey).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);


        message.setText("");
    }

    void sendNotification(String message,String chatKey) {
        try {
            JSONObject jsonObject =new JSONObject();
            JSONObject notification0bj = new JSONObject();
            notification0bj.put("title", myUserNameInOthersContact);
            notification0bj.put("body", message);
            JSONObject dataObj = new JSONObject();
            dataObj.put("Mobile", mobile);
            dataObj.put("ChatKey", chatKey);
            dataObj.put("Name", myUserNameInOthersContact);
            jsonObject.put("notification", notification0bj);
            jsonObject.put("data", dataObj);
            jsonObject.put("to", otherUserFcmToken);
            Log.i("otherUserFcm",chatKey);
            callApi(jsonObject);
        } catch (Exception e) {
        }
    }

    void callApi(JSONObject json0bject) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(json0bject.toString(), JSON);
        Request request= new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAA1GnhXLI:APA91bEAHSCi3Rg4u0AYaEH3akoJqz_-48CA5IhKnMlUpBRMUbHXk5XTle9OB0A8LprGuh44HynZG66r8cVv4dpRRJWuOUS2gl3xxJbqN8NSQ0NlCYHmbZcutgutDAaYa_vYiPpyNAJU").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("TestLog","failed");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("TestLog","Success");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valLis1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.removeEventListener(valLis1);
    }
}