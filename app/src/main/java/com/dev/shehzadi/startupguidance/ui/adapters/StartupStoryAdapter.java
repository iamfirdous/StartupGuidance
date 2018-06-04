package com.dev.shehzadi.startupguidance.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.StartupStoryModel;
import com.dev.shehzadi.startupguidance.ui.activities.ViewStartupStoryActivity;

import java.util.List;

public class StartupStoryAdapter extends RecyclerView.Adapter<StartupStoryAdapter.StartupStoryViewHolder>{

    private List<StartupStoryModel> startupStories;
    private Context context;

    public StartupStoryAdapter(Context context, List<StartupStoryModel> startupStories) {
        this.startupStories = startupStories;
        this.context = context;
    }

    @NonNull
    @Override
    public StartupStoryAdapter.StartupStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem_startup_story, parent, false);
        return new StartupStoryAdapter.StartupStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StartupStoryAdapter.StartupStoryViewHolder holder, int position) {

        StartupStoryModel startupStory = startupStories.get(position);

        String photoLocation = startupStory.getPhotoLocation();
        String storyTitle = startupStory.getStoryTitle();
        String authorName = startupStory.getAuthorName();

        if(!TextUtils.isEmpty(photoLocation)){
            Glide.with(context)
                    .load(photoLocation)
                    .into(holder.ivStartupStoryImg);
        }
        else{
            Bitmap bmp = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_story_black_512)).getBitmap();
            holder.ivStartupStoryImg.setImageBitmap(bmp);
        }

        if (!TextUtils.isEmpty(storyTitle))
            holder.tvTitle.setText(storyTitle);
        else
            holder.tvTitle.setText("-");

        if (!TextUtils.isEmpty(authorName))
            holder.tvAuthor.setText(authorName);
        else
            holder.tvAuthor.setText("-");

        holder.itemView.setOnClickListener(view -> {
            Intent viewStartupStory = new Intent(context, ViewStartupStoryActivity.class);
            viewStartupStory.putExtra("startupStory", startupStory);
            context.startActivity(viewStartupStory);
        });

    }

    @Override
    public int getItemCount() {
        return startupStories.size();
    }

    class StartupStoryViewHolder extends RecyclerView.ViewHolder{

        ImageView ivStartupStoryImg;
        TextView tvTitle, tvAuthor;
        View itemView;

        public StartupStoryViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            ivStartupStoryImg = itemView.findViewById(R.id.imageView_startupStory_logo);
            tvTitle = itemView.findViewById(R.id.textView_startupStory_title);
            tvAuthor = itemView.findViewById(R.id.textView_startupStory_author);
        }
    }
}
