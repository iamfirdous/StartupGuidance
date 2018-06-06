package com.dev.shehzadi.startupguidance.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.UserModel;
import com.dev.shehzadi.startupguidance.ui.activities.ChatActivity;

import java.util.List;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.ChatUsersViewHolder>{

    private List<UserModel> users;
    private Context context;

    public ChatUsersAdapter(Context context, List<UserModel> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem_chatuser, parent, false);
        return new ChatUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatUsersViewHolder holder, int position) {

        UserModel user = users.get(position);

        String photoLocation = user.getPhotoLocation();
        String guideName = user.getFullName();
        float rating = user.getRatingForGuidance();

        if(!TextUtils.isEmpty(photoLocation)){
            Glide.with(context)
                    .load(photoLocation)
                    .into(holder.ivChatUser);
        }

        if (!TextUtils.isEmpty(guideName))
            holder.tvGuideName.setText(guideName);
        else
            holder.tvGuideName.setText("-");

        if (!TextUtils.isEmpty(rating + ""))
            holder.tvRating.setText("" + rating);
        else
            holder.tvRating.setText("0.0");

        holder.itemView.setOnClickListener(view -> {
            Intent viewStartupStory = new Intent(context, ChatActivity.class);
            viewStartupStory.putExtra("user", user);
            context.startActivity(viewStartupStory);
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ChatUsersViewHolder extends RecyclerView.ViewHolder{

        ImageView ivChatUser;
        TextView tvGuideName, tvRating;
        View itemView;

        public ChatUsersViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            ivChatUser = itemView.findViewById(R.id.imageView_userPhoto_chatList);
            tvGuideName = itemView.findViewById(R.id.textView_name_chatList);
            tvRating = itemView.findViewById(R.id.textView_rating_chatList);
        }
    }
}
