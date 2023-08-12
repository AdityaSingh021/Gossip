package com.example.gossip.Status;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gossip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    private List<StatusModel> statusList;
    private Context context;

    public StatusAdapter(List<StatusModel> statusList,Context context) {
        this.statusList = statusList;
        this.context=context;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status_layout, parent, false);
        return new StatusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        StatusModel status = statusList.get(position);

        // Bind data to views in the item_status_layout
        holder.statusName.setText(status.getName());
        if(status.isState()){
            holder.statusState.setImageResource(R.drawable.status_cicrcle);
//            Toast.makeText(context,"grey "+status.getMobile(),Toast.LENGTH_SHORT).show();
        }
        // Use Picasso or Glide to load the image into the ImageView
        Glide.with(context)
                .load(status.getImageUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        RoundedBitmapDrawable circularDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), ((BitmapDrawable) resource).getBitmap());
                        circularDrawable.setCircular(true);
                        holder.statusImage.setImageDrawable(circularDrawable);

                        return true;
                    }
                })
                .into(holder.statusImage);
//        Picasso.get().load(status.getImageUrl()).into(holder.statusImage);
//        Log.i("MyImage",status.getImageUrl()+"aaaaaa");
        holder.statusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,StatusGallery.class);
                i.putExtra("Mobile",status.getMobile());
                i.putExtra("Name",status.getName());
                i.putExtra("Index", position);
                context.startActivity(i);
                Animatoo.INSTANCE.animateZoom(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView statusImage;
        TextView statusName;
        ImageView statusState;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            statusImage = itemView.findViewById(R.id.statusImage);
            statusName = itemView.findViewById(R.id.statusName);
            statusState=itemView.findViewById(R.id.StatusState);
        }
    }
}

