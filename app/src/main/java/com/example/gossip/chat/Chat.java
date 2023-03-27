package com.example.gossip.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gossip.BlankFragment;
import com.example.gossip.HomeActivity;
import com.example.gossip.MemoryData;
import com.example.gossip.R;
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
        chattingRecyclerView=findViewById(R.id.chattingRecyclerView);

        // data from message adapter
        final String getName=getIntent().getStringExtra("name");
        name.setText(getName);
        chatKey=getIntent().getStringExtra("chat_key");
        final String getMobile=getIntent().getStringExtra("mobile");


        //get user mobile from memory
        getUserMobile= MemoryData.getData(Chat.this);

        chatAdapter=new chatAdapter(chatLists,Chat.this);
        chattingRecyclerView.setAdapter(chatAdapter);


        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
        chattingRecyclerView.hasFixedSize();




        mValueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("llkkjj","aaaaaa");
                    if(chatKey.isEmpty()) {
                        // generate chat key
                        chatKey = "1";
                        if (snapshot.child(mobile).hasChild("chat")) {
                            chatKey = String.valueOf(snapshot.child(mobile).child("chat").getChildrenCount() + 1);
//                            Toast.makeText(getApplicationContext(),chatKey,Toast.LENGTH_SHORT).show();
                        }
                    }


                    if(snapshot.child(mobile).hasChild("chat")){
                        if(snapshot.child(mobile).child("chat").child(chatKey).hasChild("messages")){
                            chatLists.clear();
                            for(DataSnapshot messagesSnapshot : snapshot.child(mobile).child("chat").child(chatKey).child("messages").getChildren()){
                                if(messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")){
                                    final String messageTimeStamp=messagesSnapshot.getKey();
                                    final String getMobile=messagesSnapshot.child("mobile").getValue(String.class);
                                    final String getMsg=messagesSnapshot.child("msg").getValue(String.class);


                                    Timestamp timestamp=new Timestamp(Long.parseLong(messageTimeStamp));
                                    Date date=new Date(timestamp.getTime());
                                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                    chatList chatList=new chatList(getMobile,getName,getMsg,simpleDateFormat.format(date),simpleTimeFormat.format(date));
                                    chatLists.add(chatList);

//                                    if(check || Long.parseLong(messageTimeStamp)>Long.parseLong(MemoryData.getLastMsgTs(Chat.this,chatKey))){

                                        check=false;
                                        MemoryData.saveLastMsgTs(messageTimeStamp,chatKey,getApplicationContext());

                                        chatAdapter.updateChatList(chatLists);
                                        chattingRecyclerView.scrollToPosition(chatLists.size()-1);


//                                    }
                                }

                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTxtMessage=message.getText().toString();

                // get current Timestamp
                final  String currentTimeStamp=String.valueOf(System.currentTimeMillis()).substring(0,10);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.child(getMobile).child("chat").getChildren()){
                            Log.i("adityaaa",getMobile+"   "+getUserMobile);
                            if(snapshot1.hasChild("messages") && (snapshot1.child("user_1").getValue().equals(getUserMobile) | snapshot1.child("user_2").getValue().equals(getUserMobile))){
//                                Log.i("ooppoo","pppppppp");
                                check1[0] =1;
                                databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);;
                                databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                                break;
                            }
                        }
                        if(check1[0] ==0) {
                            Log.i("ooppoo","ppppooooooo");
                            int count= (int) (snapshot.child(getMobile).child("chat").getChildrenCount()+1);

                            databaseReference.child(getMobile).child("chat").child(String.valueOf(count)).child("user_1").setValue(getMobile);
                            databaseReference.child(getMobile).child("chat").child(String.valueOf(count)).child("user_2").setValue(getUserMobile);
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