package com.example.gossip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {

    private List<TextItem> textItemList;

    public TextAdapter(List<TextItem> textItemList) {
        this.textItemList = textItemList;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hashtags_item, parent, false);
        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        TextItem textItem = textItemList.get(position);
        holder.bind(textItem);
    }

    @Override
    public int getItemCount() {
        return textItemList.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.HashTag); // Replace with the actual TextView ID
        }

        public void bind(TextItem textItem) {
            textView.setText(textItem.getHashTag());
        }
    }
}