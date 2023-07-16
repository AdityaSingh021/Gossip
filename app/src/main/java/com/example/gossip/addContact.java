package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addContact extends AppCompatActivity {
    private int check=0;
    private DatabaseReference databaseReference;
    private String chatCount;
    private DatabaseReference databaseReference1;
    int getChatCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        String mobile=MemoryData.getData(getApplicationContext());

        EditText newUser_mobile=findViewById(R.id.newUser);
        EditText newUser_name=findViewById(R.id.newUserName);
        Button button=findViewById(R.id.goBack);
        String my_name=MemoryData.getName(getApplicationContext());
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(mobile).child("contacts").child(newUser_mobile.getText().toString()).child("Name").setValue(newUser_name.getText().toString());
//                databaseReference.child(mobile).child("contacts").child(newUser_mobile.getText().toString()).child("profilePic").setValue();
                databaseReference.child(newUser_mobile.getText().toString()).child("contacts").child(mobile).child("Name").setValue(my_name);
                databaseReference1=databaseReference;
                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        getChatCounts= (int) snapshot.getChildrenCount()+1;
                        chatCount=String.valueOf(getChatCounts);
                        Log.i("hhhhhh",chatCount+"  "+getChatCounts);

                        databaseReference1.child(mobile).child("chat").child(chatCount).child("messages").setValue("");
                        databaseReference1.child(mobile).child("chat").child(chatCount).child("user_1").setValue(mobile);
                        databaseReference1.child(mobile).child("chat").child(chatCount).child("user_2").setValue(newUser_mobile.getText().toString());
                        databaseReference1.child(mobile).child("contacts").child(newUser_mobile.getText().toString()).child("profilePic").setValue(snapshot.child(newUser_mobile.getText().toString()).child("profilePic").getValue(String.class));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                finish();
            }
        });




    }
}