package com.example.gossip.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gossip.BlankFragment;
import com.example.gossip.ContactChats;
import com.example.gossip.HomeActivity;
import com.example.gossip.MemoryData;
import com.example.gossip.R;
import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
//    public boolean check;
    private int check1;
    private String mobile;
    private final List<chatList> chatLists=new ArrayList<>();
    String getUserMobile="";
    String chatKey;
    private int generatedChatKey;
    private RecyclerView chattingRecyclerView;
    private chatAdapter chatAdapter;
    private boolean check=true;
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
        mobile= BlankFragment.mobile;
        int[] check1 = {0};
        final TextView name=findViewById(R.id.name_chat);
        final EditText message=findViewById(R.id.message);
        final ImageView sendButton=findViewById(R.id.sendBtn);
        TextView Status=findViewById(R.id.Status);
        chattingRecyclerView=findViewById(R.id.chattingRecyclerView);
//        databaseReference.child(mobile).child("Status").setValue(1);
        // data from message adapter
        final String getName=getIntent().getStringExtra("name");
        name.setText(getName);
//        new SimpleTooltip.Builder(getApplicationContext())
//                .anchorView(sendButton)
//                .text("Click here to match with people")
//                .gravity(Gravity.END)
//                .animated(true)
//                .transparentOverlay(false)
//                .build()
//                .show();
        chatKey=getIntent().getStringExtra("chat_key");
        final String getMobile=getIntent().getStringExtra("mobile");
        //get user mobile from memory
        getUserMobile= MemoryData.getData(Chat.this);

        ImageView options=findViewById(R.id.options);
        ImageView camera=findViewById(R.id.camera);
        chatAdapter=new chatAdapter(chatLists,Chat.this,MemoryData.getData(getApplicationContext()));
        chattingRecyclerView.setAdapter(chatAdapter);
        RelativeLayout topBar=findViewById(R.id.topBar);
//        databaseReference.child(getMobile).child("Status").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if((long)(snapshot.getValue())==Long.valueOf(0)){
//                    Status.setText("Offline");
//                    Status.setTextColor(Color.parseColor("#cb7d67"));
//                }else{
//                    Status.setText("Online");
//                    Status.setTextColor(Color.parseColor("#94E8B4"));
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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




        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
        chattingRecyclerView.hasFixedSize();
        mValueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(chatKey.isEmpty()) {
                        // generate chat key
                        chatKey = "1";
                        if (snapshot.child(mobile).hasChild("chat")) {
                            chatKey = String.valueOf(snapshot.child(mobile).child("chat").getChildrenCount() + 1);
                        }
                    }


                    if(snapshot.child(mobile).hasChild("chat")){
                        if(snapshot.child(mobile).child("chat").child(chatKey).hasChild("messages")){
                            chatLists.clear();
                            for(DataSnapshot messagesSnapshot : snapshot.child(mobile).child("chat").child(chatKey).child("messages").getChildren()){
                                if(messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")){
                                    final String messageTimeStamp=messagesSnapshot.getKey();
                                    final String Mobile=messagesSnapshot.child("mobile").getValue(String.class);
                                    final String getMsg=messagesSnapshot.child("msg").getValue(String.class);


                                    Timestamp timestamp=new Timestamp(Long.parseLong(messageTimeStamp));
                                    Date date=new Date(timestamp.getTime());
                                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                    chatList chatList=new chatList(Mobile,getName,getMsg,simpleDateFormat.format(date),simpleTimeFormat.format(date));
                                    chatLists.add(chatList);


                                    if(check || Long.parseLong(messageTimeStamp)>Long.parseLong(MemoryData.getLastMsgTs(Chat.this,chatKey))){
                                        check=false;
                                        MemoryData.saveLastMsgTs(messageTimeStamp,chatKey,getApplicationContext());
                                        chatAdapter.updateChatList(chatLists);
                                        chattingRecyclerView.scrollToPosition(chatLists.size()-1);


                                    }
                                }

                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
////                        snapshot.child(getUserMobile).child("contacts")
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                Intent i=new Intent(getApplicationContext(), ContactChats.class);
                startActivity(i);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTxtMessage=message.getText().toString();

                // get current Timestamp
                final  String currentTimeStamp=String.valueOf(System.currentTimeMillis());
//                Toast.makeText(getApplicationContext(),currentTimeStamp,Toast.LENGTH_SHORT).show();

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.child(getMobile).child("chat").getChildren()){
//                            Log.i("adityaaa",getMobile+"   "+getUserMobile);
                            if(snapshot1.hasChild("messages") && (snapshot1.child("user_1").getValue().equals(getUserMobile) || snapshot1.child("user_2").getValue().equals(getUserMobile))){
//                                Log.i("ooppoo","pppppppp");
                                check1[0] =1;
                                databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);;
                                databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                                break;
                            }
                        }
                        if(check1[0] ==0) {
//                            Log.i("ooppoo","ppppooooooo");
                            int count= (int) (snapshot.child(getMobile).child("chat").getChildrenCount()+1);
//                            Toast.makeText(getApplicationContext(),"yesss",Toast.LENGTH_SHORT).show();
                            databaseReference.child(getMobile).child("chat").child(String.valueOf(count)).child("user_1").setValue(getMobile);
                            databaseReference.child(getMobile).child("chat").child(String.valueOf(count)).child("user_2").setValue(getUserMobile);
//                            if(databaseReference.child(getMobile).child("contacts").chi)
                            databaseReference.child(getMobile).child("contacts").child(getUserMobile).child("Name").setValue(getUserMobile);
                            databaseReference.child(getMobile).child("contacts").child(getUserMobile).child("profilePic").setValue("");
                            databaseReference.child(getMobile).child("chat").child(String.valueOf(count)).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);
                            databaseReference.child(getMobile).child("chat").child(String.valueOf(count)).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference.child(mobile).child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child(mobile).child("chat").child(chatKey).child("user_2").setValue(getMobile);

                databaseReference.child(mobile).child("chat").child(chatKey).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);
                databaseReference.child(mobile).child("chat").child(chatKey).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                message.setText("");
            }
        });
    }
}
//messageEditText     =      message