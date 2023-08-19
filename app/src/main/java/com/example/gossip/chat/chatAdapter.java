package com.example.gossip.chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.example.gossip.AccountSettings;
import com.example.gossip.BlankFragment;
import com.example.gossip.Container;
import com.example.gossip.Login;
import com.example.gossip.MainActivity;
import com.example.gossip.MemoryData;
import com.example.gossip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {
//    private ProfilePic_data
    private List<chatList> chatLists;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");;

    private final Context context;
    private String userMobile;

    public chatAdapter(List<chatList> charLists, Context context,String userMobile) {
        this.chatLists = charLists;
        this.context = context;
        this.userMobile= userMobile;
    }

    @NonNull
    @Override
    public chatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout,null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull chatAdapter.MyViewHolder holder, int position) {


        chatList list2=chatLists.get(position);
//        Log.i("myPhoneis",list2.getMobile());

        if(!userMobile.isEmpty() && list2.getMobile().equals(userMobile)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);
            holder.imageByMe.setVisibility(View.VISIBLE);
            if(list2.isImage()) {
                if(MemoryData.profilePictureExists(list2.getMessageId(),context)){
                    holder.imageByMe.setImageBitmap(MemoryData.getProfilePicture(list2.getMessageId(),context));
                }else{
                    Glide.with(context)
                            .load(list2.getMessage())
                            .error(R.drawable.black_rect)
                            .into(holder.imageByMe);
                    MemoryData.downloadImageAndSave(list2.getMessage(),list2.getMessageId(),context);
                }
//                Toast.makeText(context,"image", Toast.LENGTH_SHORT).show();

                holder.myMessage.setVisibility(View.GONE);
            }else {
                holder.imageByMe.setVisibility(View.GONE);
                holder.myMessage.setVisibility(View.VISIBLE);
                holder.myMessage.setText(list2.getMessage());
            }
            holder.myTime.setText(list2.getDate()+"  "+list2.getTime());
//            Log.i("datChecking",list2.getDate());
        }else{
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.imageByoppo.setVisibility(View.VISIBLE);
            if(list2.isImage()) {
                Glide.with(context)
                        .load(list2.getMessage())
                        .error(R.drawable.black_rect)
                        .into(holder.imageByoppo);
                holder.oppoMessage.setVisibility(View.GONE);
            }else {
                holder.oppoMessage.setVisibility(View.VISIBLE);
                holder.imageByoppo.setVisibility(View.GONE);
                holder.oppoMessage.setText(list2.getMessage());
            }
//            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate()+"  "+list2.getTime());
        }
        holder.myMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openDeleteDialog(context,list2);
                return true;
            }
        });
        holder.imageByMe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openDeleteDialog(context,list2);
                return true;
            }
        });
        holder.imageByoppo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openDeleteDialog(context,list2);
                return false;
            }
        });
        holder.imageByMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list2.isImage()) showImage(list2.getMessage());

            }
        });

        holder.imageByoppo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list2.isImage()) showImage(list2.getMessage());
            }
        });

        holder.oppoMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openDeleteDialog(context,list2);
                return true;
            }
        });


    }
    @Override
    public int getItemCount() {
        return chatLists.size();
    }
    public void updateChatList(List<chatList> chatLists){
        this.chatLists=chatLists;
        notifyDataSetChanged();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout oppoLayout,myLayout;
        private TextView oppoMessage , myMessage;
        private TextView oppoTime,myTime;
        private ImageView imageByoppo,imageByMe;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oppoLayout=itemView.findViewById(R.id.oppoLayout);
            myLayout=itemView.findViewById(R.id.MyLayout);
            oppoMessage=itemView.findViewById(R.id.oppoMessage);
            myMessage=itemView.findViewById(R.id.myMessage);
            oppoTime=itemView.findViewById(R.id.oppoMsgTime);
            myTime=itemView.findViewById(R.id.myMsgTime);
            imageByoppo=itemView.findViewById(R.id.ImageByOpp);
            imageByMe=itemView.findViewById(R.id.imageByMe);
        }
    }
    public void openDeleteDialog(Context context,chatList list2){
        Dialog dialog = new Dialog(context);

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.temp);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        TextView delete=dialog.findViewById(R.id.Delete);
        TextView cancel=dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list2.getChatId().isEmpty()){
                    databaseReference.child(Container.getMobile()).child("chat").child(list2.getChatId()).child("messages").child(list2.getMessageId()).removeValue();
                    chatLists.remove(list2);
                    try{
                        MemoryData.deleteProfilePicture(list2.getMessageId(),context);
                    }catch(Exception e){};

                    updateChatList(chatLists);
                }
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public void showImage(String imageUrl) {
        // Create a Dialog instance
        Dialog dialog = new Dialog(context);

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.profile_image_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageView myImage=dialog.findViewById(R.id.MyImage);
        Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.black_rect)
                .into(myImage);
        // Show the Dialog
        dialog.show();
    }
}
