package com.example.gossip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Popular_HashTags_Content_Adapter extends RecyclerView.Adapter<Popular_HashTags_Content_Adapter.TextViewHolder> {

    private List<Popular_HashTags_Item> PopularItems;

    public Popular_HashTags_Content_Adapter(List<Popular_HashTags_Item> textItemList) {
        this.PopularItems = textItemList;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_popular_hashtag_content, parent, false);
        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        Popular_HashTags_Item item=PopularItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return PopularItems.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView image;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.HashTag_image);
            textView = itemView.findViewById(R.id.HashTag_name); // Replace with the actual TextView ID
        }

        public void bind(Popular_HashTags_Item textItem) {
            textView.setText(textItem.getImage_info());
            image.setImageResource(textItem.getImage());
        }
    }
}