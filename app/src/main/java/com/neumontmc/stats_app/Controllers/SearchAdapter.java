package com.neumontmc.stats_app.Controllers;

import com.bumptech.glide.Glide;
import com.neumontmc.api.Models.User;
import com.neumontmc.stats_app.R;
import com.neumontmc.stats_app.View.UserStats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    ArrayList<User> users;
    Context context;
    Glide glide;
    public SearchAdapter(Context ct, ArrayList<User> users) {
        context = ct;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTV.setText(users.get(position).getUsername());
        holder.uuidTV.setText(users.get(position).getUuid().getUUID());
        glide.with(holder.nameTV.getContext())
                .load("https://api.neumontmc.com/images/bust/" + users.get(position).getUsername() + ".png")
                .placeholder(R.drawable.ic_menu_close_clear_cancel)
                .into(holder.userImage);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserStats.class);
                intent.putExtra("name", users.get(position).getUsername());
                intent.putExtra("uuid", users.get(position).getUuid().getUUID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, uuidTV;
        ImageView userImage;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.cardNameTextView);
            uuidTV = itemView.findViewById(R.id.cardUuidTextView);
            userImage = itemView.findViewById(R.id.cardUserImageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }


}

