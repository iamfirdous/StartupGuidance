package com.dev.shehzadi.startupguidance.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.StartupStoryModel;

import static com.dev.shehzadi.startupguidance.utils.Util.NON_HYPHENATED_PATTERN;
import static com.dev.shehzadi.startupguidance.utils.Util.getFormattedDate;

public class ViewStartupStoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewStartupStoryViewHolder holder;

    private StartupStoryModel startupStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_startup_story);

        toolbar = findViewById(R.id.toolbar_viewStartupStory);
        holder = new ViewStartupStoryViewHolder();

        startupStory = (StartupStoryModel) getIntent().getSerializableExtra("startupStory");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Startup story");
        }

        if (!TextUtils.isEmpty(startupStory.getPhotoLocation())) {
            Glide.with(this)
                    .load(startupStory.getPhotoLocation())
                    .into(holder.ivStartupStoryImage);
        }

        holder.tvTitle.setText(startupStory.getStoryTitle());
        holder.tvStoryTitle.setText(startupStory.getStoryTitle());
        holder.tvDescription.setText(startupStory.getDescription());
        holder.tvStory.setText(startupStory.getStory());
        holder.tvAuthorAndDate.setText("Author: " + startupStory.getAuthorName() +
                                       "\nPosted On: " +getFormattedDate(startupStory.getPostedOn(), NON_HYPHENATED_PATTERN));
    }

    class ViewStartupStoryViewHolder {

        ImageView ivStartupStoryImage;
        TextView tvTitle, tvDescription, tvStoryTitle, tvStory, tvAuthorAndDate;

        public ViewStartupStoryViewHolder() {
            ivStartupStoryImage = findViewById(R.id.imageView_photo_viewStartupStory);
            tvTitle = findViewById(R.id.textView_title_viewStartupStory);
            tvDescription = findViewById(R.id.textView_description_viewStartupStory);
            tvStoryTitle = findViewById(R.id.textView_storyTitle_viewStartupStory);
            tvStory = findViewById(R.id.textView_story_viewStartupStory);
            tvAuthorAndDate = findViewById(R.id.textView_authorAndDate_viewStartupStory);
        }
    }
}
