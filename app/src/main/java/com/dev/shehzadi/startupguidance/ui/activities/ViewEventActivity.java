package com.dev.shehzadi.startupguidance.ui.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.EventModel;
import com.dev.shehzadi.startupguidance.models.LocationModel;
import com.google.firebase.auth.FirebaseAuth;

import static com.dev.shehzadi.startupguidance.utils.Util.NON_HYPHENATED_PATTERN;
import static com.dev.shehzadi.startupguidance.utils.Util.getFormattedDate;

public class ViewEventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewEventViewHolder holder;

    private EventModel event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        toolbar = findViewById(R.id.toolbar_viewEvent);
        holder = new ViewEventViewHolder();

        event = (EventModel) getIntent().getSerializableExtra("event");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(event.getEventTitle());
        }

        if (FirebaseAuth.getInstance().getUid().equals(event.getEventOrganizerUid())) {
            holder.fabEdit.setVisibility(View.VISIBLE);

            holder.fabEdit.setOnClickListener(view -> {
                Intent viewEvent = new Intent(this, AddEventActivity.class);
                viewEvent.putExtra("event", event);
                startActivity(viewEvent);
            });
        }

        if (!TextUtils.isEmpty(event.getPhotoLocation())) {
            Glide.with(this)
                    .load(event.getPhotoLocation())
                    .into(holder.ivEventImage);
        }

        holder.tvDescription.setText(event.getDescription());
        holder.tvEventDate.setText(getFormattedDate(event.getEventDate(), NON_HYPHENATED_PATTERN));

        LocationModel location = event.getLocation();
        String locationStr = location.getAddressLine1() + " " +
                             location.getAddressLine2() + " " +
                             location.getCity() + " " +
                             location.getState() + " " +
                             location.getPinCode() + " " +
                             location.getLandmark();
        holder.tvLocation.setText(locationStr);

        holder.tvMaxRegCount.setText("" + event.getMaxRegistrationCount());
        holder.tvTotalRegs.setText("" + event.getTotalRegistrationCount());
        holder.tvRegLastDate.setText(getFormattedDate(event.getRegistrationLastDate(), NON_HYPHENATED_PATTERN));
        holder.tvRegFee.setText("" + event.getRegistrationFee());

    }

    class ViewEventViewHolder {

        ImageView ivEventImage;
        TextView tvDescription, tvEventDate, tvLocation, tvMaxRegCount,
                tvTotalRegs, tvRegLastDate, tvRegFee;
        FloatingActionButton fabEdit;

        public ViewEventViewHolder() {
            ivEventImage = findViewById(R.id.imageView_eventBanner_viewEvent);
            tvDescription = findViewById(R.id.textView_description_viewEvent);
            tvEventDate = findViewById(R.id.textView_eventDate_viewEvent);
            tvLocation = findViewById(R.id.textView_location_viewEvent);
            tvMaxRegCount = findViewById(R.id.textView_maxReg_viewEvent);
            tvTotalRegs = findViewById(R.id.textView_totalReg_viewEvent);
            tvRegLastDate = findViewById(R.id.textView_lastDate_viewEvent);
            tvRegFee = findViewById(R.id.textView_regFee_viewEvent);
            fabEdit = findViewById(R.id.fab_edit_viewEvent);

            fabEdit.setVisibility(View.GONE);
        }
    }
}
