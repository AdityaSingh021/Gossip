package com.example.gossip.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gossip.MemoryData;
import com.example.gossip.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    String getUserMobile="";
    String chatKey;
    private int generatedChatKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final TextView name=findViewById(R.id.name_chat);
        final EditText message=findViewById(R.id.message);
        final Button sendButton=findViewById(R.id.sendBtn);

        // data from message adapter
        final String getName=getIntent().getStringExtra("name");
        chatKey=getIntent().getStringExtra("chat_key");
        final String getMobile=getIntent().getStringExtra("mobile");


        //get user mobile from memory
        getUserMobile= MemoryData.getData(Chat.this);

        name.setText(getName);


        if(chatKey.isEmpty()){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // generate chat key
                    chatKey="1";
                    if(snapshot.hasChild("chat")){
                        chatKey= String.valueOf(snapshot.child("chat").getChildrenCount()+1);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTxtMessage=message.getText().toString();

                // get current Timestamp
                final  String currentTimeStamp=String.valueOf(System.currentTimeMillis()).substring(0,10);

                MemoryData.saveLastMsgTs(currentTimeStamp,chatKey,Chat.this);
                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                message.setText("");
            }
        });
    }
}
//messageEditText     =      message