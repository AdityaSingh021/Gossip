package com.example.gossip.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gossip.MemoryData;
import com.example.gossip.R;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {
    private List<chatList> chatLists;
    private final Context context;
    private String userMobile;

    public chatAdapter(List<chatList> charLists, Context context) {
        this.chatLists = charLists;
        this.context = context;
        this.userMobile= MemoryData.getData(context);
    }

    @NonNull
    @Override
    public chatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull chatAdapter.MyViewHolder holder, int position) {


        chatList list2=chatLists.get(position);

        if(list2.getMobile().equals(userMobile)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate()+"  "+list2.getTime());
        }else{
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate()+"  "+list2.getTime());
        }

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }
    public void updateChatList(List<chatList> chatLists){
        this.chatLists=chatLists;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout oppoLayout,myLayout;
        private TextView oppoMessage , myMessage;
        private TextView oppoTime,myTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oppoLayout=itemView.findViewById(R.id.oppoLayout);
            myLayout=itemView.findViewById(R.id.MyLayout);
            oppoMessage=itemView.findViewById(R.id.oppoMessage);
            myMessage=itemView.findViewById(R.id.myMessage);
            oppoTime=itemView.findViewById(R.id.oppoMsgTime);
            myTime=itemView.findViewById(R.id.myMsgTime);
        }
    }
}
