package com.dev.shehzadi.startupguidance.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.EventModel;
import com.dev.shehzadi.startupguidance.models.StartupStoryModel;
import com.dev.shehzadi.startupguidance.ui.activities.AddEventActivity;
import com.dev.shehzadi.startupguidance.ui.activities.AddStartupStoryActivity;
import com.dev.shehzadi.startupguidance.ui.activities.HomeActivity;
import com.dev.shehzadi.startupguidance.ui.adapters.StartupStoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartupStoryFragment extends Fragment {


    public StartupStoryFragment() {
        // Required empty public constructor
    }

    private View view;
    private FrameLayout frameLayoutProgressBar;
    private RecyclerView recyclerView;

    private StartupStoryAdapter adapter;
    private List<StartupStoryModel> startupStories;

    private boolean loaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_startup_story, container, false);

        getActivity().setTitle("Startup stories");

        FloatingActionButton fab = ((HomeActivity)getActivity()).getFAB();
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(view -> {
            Intent addStartupStoryIntent = new Intent(getActivity(), AddStartupStoryActivity.class);
            startActivity(addStartupStoryIntent);
        });

        frameLayoutProgressBar = view.findViewById(R.id.frameLayout_progressBar_startupStory);
        recyclerView = view.findViewById(R.id.recyclerView_startup_stories);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        startupStories = new ArrayList<>();
        adapter = new StartupStoryAdapter(getContext(), startupStories);

        recyclerView.setAdapter(adapter);

        initView();

        return view;
    }

    public void initView(){
        DatabaseReference eventsReference = FirebaseDatabase.getInstance().getReference("StartupStories");

        eventsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!loaded)
                    frameLayoutProgressBar.setVisibility(View.VISIBLE);
                startupStories.clear();
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    startupStories.add(eventSnapshot.getValue(StartupStoryModel.class));
                }
                adapter.notifyDataSetChanged();
                frameLayoutProgressBar.setVisibility(View.GONE);
                loaded = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
