package com.example.gossip.messages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
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

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.gossip.HomeActivity;
import com.example.gossip.MainActivity;
import com.example.gossip.MemoryData;
import com.example.gossip.R;
import com.example.gossip.chat.Chat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private List<MessagesList> messagesLists;
    private List<MessagesList> filteredData;
    private  final Context context;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
        this.filteredData = new ArrayList<>(messagesLists);
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list2=messagesLists.get(position);
        Bitmap bitmap=MemoryData.getProfilePicture(list2.getMobile(), context.getApplicationContext());
//        Log.i("myList",list2.getMobile()+" "+position+"   "+messagesLists.size());
//        if(bitmap!=null){
//            Log.i("myList","andar hu");
//            holder.profilePic.setImageBitmap(bitmap);
////            Toast.makeText(context.getApplicationContext(),list2.getMobile()+" "+list2.getLastMessage(),Toast.LENGTH_SHORT).show();
//        }
//        bitmap=null;
//        for(int i=0;i<messagesLists.size();i++){
//            Log.i("hdmi345",messagesLists.get(i).getMobile()+" "+messagesLists.get(i).getProfilePic()+" "+i);
//        }


        if(bitmap!=null){
//            Log.i("hdmi345",position+"  "+list2.getMobile()+"  "+list2.getLastNode());
            holder.profilePic.setImageBitmap(bitmap);
        }else{
            holder.profilePic.setImageResource(R.drawable.default_user);
        }
        if(list2.getName()!=null && list2.getName().isEmpty()) list2.setName(list2.getMobile());
        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getlastMessage());


        long currentTimeStamp=System.currentTimeMillis();
        long lastNodeTime= list2.getLastNode();
        long timeDifferenceMillis = currentTimeStamp - lastNodeTime;


        if (timeDifferenceMillis < TimeUnit.DAYS.toMillis(1)) {
            // Format the timestamp of the last message
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String lastMessageTime = sdf.format(new Date(list2.getLastNode()));
            holder.time.setText(lastMessageTime);
//            System.out.println("Last message was sent/received at " + lastMessageTime);
        }else {
            // Convert milliseconds to days
            long days = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis);

            // Display the time difference in days
            String timeDifference = days + " day" + (days > 1 ? "s" : "") + " ago";
            holder.time.setText(timeDifference);
        }
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
                                databaseReference.child(MemoryData.getData(context.getApplicationContext())).child("chat").child(list2.getChatKey()).child("messages").removeValue();
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
                MainActivity.cond=false;
//                list2.setUnseenMessages();
//                updateData();

//                holder.unseenMessages.setVisibility(View.INVISIBLE);
                if(list2.getUnseenMessages()>0){
                    list2.setUnseenMessages();
//                    list2.setProfilePic(bitmap);
                    updateData(messagesLists);
//                    MemoryData.storeMessageList(v.getContext(), messagesLists);
                }
                Intent intent=new Intent(context, Chat.class);
                intent.putExtra("mobile",list2.getMobile());
                intent.putExtra("name",list2.getName());
                intent.putExtra("chat_key",list2.getChatKey());
//                intent.putExtra("profilePic",bitmap);
//                intent.putExtra("profilePic",list2.getProfilePic());

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
        private CircleImageView profilePic;
        private TextView name,lastMessage,unseenMessages;
        private RelativeLayout rootLayout;
        private TextView time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic =itemView.findViewById(R.id.profilePic);
            name =itemView.findViewById(R.id.name);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            unseenMessages=itemView.findViewById(R.id.unseenMessages);
            rootLayout=itemView.findViewById(R.id.rootLayout);
            time=itemView.findViewById(R.id.time);

        }
    }

    public void filter(String query,List<MessagesList> messagesLists) {
        filteredData.clear();
        if (TextUtils.isEmpty(query)) {
            updateData(messagesLists);
        } else {
            query = query.toLowerCase().trim();
            for (MessagesList item : messagesLists) {
                // Convert the item name to lowercase and check if it contains the query
//                Toast.makeText(context,,Toast.LENGTH_SHORT).show();
                if (item.getName().toLowerCase().contains(query)) {
                    filteredData.add(item);
                }
            }
            updateData(filteredData);
        }

    }
}
