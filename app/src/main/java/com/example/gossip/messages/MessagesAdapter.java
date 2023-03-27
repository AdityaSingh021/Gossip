package com.example.gossip.messages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gossip.HomeActivity;
import com.example.gossip.MemoryData;
import com.example.gossip.R;
import com.example.gossip.chat.Chat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ConcurrentModificationException;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private List<MessagesList> messagesLists;
    private  final Context context;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list2=messagesLists.get(position);

        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getlastMessage());

        if(list2.getUnseenMessages()==0){
            holder.unseenMessages.setVisibility(View.GONE);
//            Toast.makeText(context.getApplicationContext(),String.valueOf(list2.getUnseenMessages()),Toast.LENGTH_SHORT).show();

            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        }
        else{
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUnseenMessages()+"");

            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        }
        holder.rootLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Chat")
                        .setMessage("Are you sure you want to delete this chat?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child(MemoryData.getData(context.getApplicationContext())).child("chat").child(list2.getChatKey()).removeValue();
                                // User clicked yes button
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return false;
            }
        });
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                list2.setUnseenMessages();
//                updateData();
                list2.setUnseenMessages();
                updateData(messagesLists);
                Intent intent=new Intent(context, Chat.class);
                intent.putExtra("mobile",list2.getMobile());
                intent.putExtra("name",list2.getName());
                intent.putExtra("chat_key",list2.getChatKey() );
                context.startActivity(intent);
            }
        });
    }
    public void updateData(List<MessagesList> messagesLists){
        this.messagesLists=messagesLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView profilePic;
        private TextView name,lastMessage,unseenMessages;
        private RelativeLayout rootLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic =itemView.findViewById(R.id.profilePic);
            name =itemView.findViewById(R.id.name);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            unseenMessages=itemView.findViewById(R.id.unseenMessages);
            rootLayout=itemView.findViewById(R.id.rootLayout);
        }
    }
}
