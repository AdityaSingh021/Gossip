package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gossip.chat.Chat;
import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class HomeActivity extends AppCompatActivity {


    private ImageView imageView;
    private ImageView imageView2;
    private RecyclerView messagesRecyclerView ;
    private RelativeLayout relativeLayout;
    public static String mobile;
    private String name;
    private String lastMessage="";
    private int unseenMessages=0;
    com.example.gossip.EarthView global;
    private String chatKey="";
    private boolean dataSet=false;
    private MessagesAdapter messagesAdapter;
    private ImageView add;
    private int i=1;
    public static String newUser_mobile;
    private TreeSet<String> set;
    private final List<MessagesList> messagesLists=new ArrayList<>();
    private DatabaseReference databaseReference;

    Bitmap temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
        mobile=getIntent().getStringExtra("mobile");
//        Log.i("my mobile no",mobile);
        imageView=findViewById(R.id.left_bottom_nav);
        relativeLayout=findViewById(R.id.relativeLayout);
        imageView2=findViewById(R.id.right_bottom_nav);
        name=getIntent().getStringExtra("name");
        messagesRecyclerView=findViewById(R.id.messagesRecyclerView);
        add=findViewById(R.id.add);
        messagesRecyclerView.setHasFixedSize(true);
        EarthView earthView = findViewById(R.id.global);
//        earthView.startRollAnimation();
        earthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earthView.startJumpAnimation();
            }
        });
        set=new TreeSet<String>();
        global=findViewById(R.id.global);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter=new MessagesAdapter(messagesLists,HomeActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);
        final int[] prevScrollY = {0};

//        class RoundOutlineProvider extends ViewOutlineProvider {
//            private float mRadius;
//
//            public RoundOutlineProvider(float radius) {
//                mRadius = radius;
//            }
//
//            @Override
//            public void getOutline(View view, Outline outline) {
//                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), mRadius);
//            }
//        }

        float a=relativeLayout.getTranslationY();
        messagesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int scrollY = recyclerView.computeVerticalScrollOffset();
                int delta = scrollY;
//                Toast.makeText(getApplicationContext(),String.valueOf(scrollY),Toast.LENGTH_SHORT).show();

                float x=relativeLayout.getTranslationY() + delta;
//                if(x<a+200){
                    x=200;
                    if(delta==0) x=0;

                    // Set up a value animator for the RelativeLayout
                    ValueAnimator anim = ValueAnimator.ofFloat(relativeLayout.getTranslationY(), x);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float value = (float) valueAnimator.getAnimatedValue();
                            relativeLayout.setTranslationY(value);
                        }
                    });
                    anim.setDuration(200);
                    anim.start();
//                }

                // Update the previous scroll position
//                prevScrollY[0] = scrollY;
            }
        });


// Get a reference to the "messages" node in your database
//        DatabaseReference messagesRef = rootRef.child("messages");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this,addContact.class);
                i.putExtra("mobile",mobile);
                startActivity(i);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {

//                Chat.check=false;
//                messagesLists.clear();
//                set.clear();
//                unseenMessages=0;
//                lastMessage="";
//                chatKey="";

//                for(DataSnapshot dataSnapshot: snapshot.child(mobile).child("contacts").getChildren()){



//                    final String getmobile=dataSnapshot.getKey();
                    dataSet=true;
//                    if(!getmobile.equals(mobile) ){
                        Log.i("mno","2");
//                        final String getName=dataSnapshot.child("Name").getValue(String.class);
//                        Toast.makeText(getApplicationContext(),getName,Toast.LENGTH_SHORT).show();




                        // Deleting values from chats


//                        databaseReference.child(mobile).child("chat").removeValue();


                        //
                        databaseReference.child(mobile).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCounts= (int) snapshot.getChildrenCount();
//                                Toast.makeText(getApplicationContext(),String.valueOf(getChatCounts),Toast.LENGTH_SHORT).show();
                                if(getChatCounts>0){
                                    messagesLists.clear();
                                    set.clear();
                                    unseenMessages=0;
                                    lastMessage="";
                                    chatKey="";
                                    Log.i("mno","4");
//                                    messagesLists.clear();

                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
//                                        Log.i("mno","5");
                                        final String getKey=dataSnapshot1.getKey();
                                        chatKey=getKey;
                                        final String getmobile=snapshot.child(chatKey).child("user_2").getValue(String.class);

//                                        assert getmobile != null;
                                        String getName=snapshot1.child(mobile).child("contacts").child(getmobile).child("Name").getValue(String.class);
//                                        Toast.makeText(getApplicationContext(),getName+"   2",Toast.LENGTH_SHORT).show();
                                        if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                            final String getUserOne=dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo=dataSnapshot1.child("user_2").getValue(String.class);
                                            if(getUserOne.equals(mobile) && getUserTwo.equals(getmobile) || getUserOne.equals(getmobile) && getUserTwo.equals(mobile)){
                                                if(dataSnapshot1.child("messages").getChildrenCount()==0){
//                                                    Toast.makeText(getApplicationContext(),String.valueOf(messagesLists.size())+"   "+chatKey,Toast.LENGTH_SHORT).show();
//                                                    if(!set.contains(chatKey)){
//                                                        MessagesList messagesList = new MessagesList(getName, getmobile, "", temp,0, chatKey);
//                                                        messagesLists.add(messagesList);
//                                                        set.add(chatKey);
                                                        Log.i("if0","aaaaaa");
//                                                        break;
//                                                    }
                                                }else{



                                                    for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                                        final Long getMessageKey= Long.valueOf(chatDataSnapshot.getKey());
                                                        final Long getLastSeenMessage= Long.valueOf(MemoryData.getLastMsgTs(HomeActivity.this,getKey));
                                                        Log.i("myTest",getLastSeenMessage.toString()+"    "+getMessageKey.toString()+"     "+chatKey.toString());

                                                        lastMessage=chatDataSnapshot.child("msg").getValue(String.class);
                                                        Log.i("XYZ2",lastMessage+"   "+String.valueOf(getMessageKey));
                                                        if(getMessageKey>getLastSeenMessage){
                                                            unseenMessages++;
                                                        }else unseenMessages=0;
                                                    }

                                                    Log.i("chatKey",String.valueOf(chatKey)+"    "+String.valueOf(unseenMessages));
//                                                    MessagesList messagesList = new MessagesList(getName, getmobile, lastMessage,temp, unseenMessages, chatKey);
//                                                    messagesLists.add(messagesList);

                                                }

//                                                    set.add(chatKey);
//                                                }


                                            }
                                        }


                                    }


                                }

                                messagesAdapter.updateData(messagesLists);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
//                    }

//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                messagesLists.clear();
            }
        });

    }
}