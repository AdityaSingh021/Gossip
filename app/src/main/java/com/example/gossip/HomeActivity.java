package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView messagesRecyclerView ;
    private String mobile;
    private String name;
    private String lastMessage="";
    private int unseenMessages=0;
    private String chatKey="";
    private boolean dataSet=false;
    private MessagesAdapter messagesAdapter;
    private FloatingActionButton add;
    private int i=1;
    public static String newUser_mobile;
    private TreeSet<String> set;
    private final List<MessagesList> messagesLists=new ArrayList<>();
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
        mobile=getIntent().getStringExtra("mobile");
        name=getIntent().getStringExtra("name");
        messagesRecyclerView=findViewById(R.id.messagesRecyclerView);
        add=findViewById(R.id.add);
        messagesRecyclerView.setHasFixedSize(true);
        set=new TreeSet<String>();
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter=new MessagesAdapter(messagesLists,HomeActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this,addContact.class);
                startActivity(i);
            }
        });



//        int i=1;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(getApplicationContext(),String.valueOf(messagesLists.size()),Toast.LENGTH_SHORT).show();
                messagesLists.clear();
                set.clear();
                unseenMessages=0;
                lastMessage="";
                chatKey="";
                for(DataSnapshot dataSnapshot: snapshot.child("Users").getChildren()){



                    final String getmobile=dataSnapshot.getKey();
                    dataSet=true;
                    if(!getmobile.equals(mobile) ){
//                        Log.i("mno","2");
                        final String getName=dataSnapshot.child("Name").getValue(String.class);
//                        Toast.makeText(getApplicationContext(),getName,Toast.LENGTH_SHORT).show();


                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCounts= (int) snapshot.getChildrenCount();
//                                Toast.makeText(getApplicationContext(),String.valueOf(getChatCounts),Toast.LENGTH_SHORT).show();
                                if(getChatCounts>0){
//                                    Log.i("mno","4");
//                                    messagesLists.clear();

                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
//                                        Log.i("mno","5");
                                        final String getKey=dataSnapshot1.getKey();
                                        chatKey=getKey;
//                                        Log.i("XYZ",chatKey);
                                        if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
//                                            Log.i("mno","6");

                                            final String getUserOne=dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo=dataSnapshot1.child("user_2").getValue(String.class);
//                                            Log.i("XYZ1",getUserOne+"   "+getUserTwo+"    "+getmobile);

                                            if(getUserOne.equals(mobile) && getUserTwo.equals(getmobile) || getUserOne.equals(getmobile) && getUserTwo.equals(mobile)){
//                                                Log.i("mno","7");
//                                                Log.i("xyz","asdf");

                                                for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
//                                                    Log.i("mno","8");


                                                    final Long getMessageKey= Long.valueOf(chatDataSnapshot.getKey());
                                                    final Long getLastSeenMessage= Long.valueOf(MemoryData.getLastMsgTs(HomeActivity.this,getKey));


                                                    lastMessage=chatDataSnapshot.child("msg").getValue(String.class);
//                                                    Log.i("XYZ2",lastMessage);
                                                    if(getMessageKey>getLastSeenMessage){
                                                        unseenMessages++;
                                                    }

                                                }
//                                                Log.i("mno","9");
//                                                Log.i("checking","1111111111111111");
                                                if(!set.contains(chatKey)){
                                                    MessagesList messagesList = new MessagesList(getName, getmobile, lastMessage, unseenMessages, chatKey);
                                                    messagesLists.add(messagesList);
                                                    set.add(chatKey);
                                                }




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
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                messagesLists.clear();
            }
        });

    }
}