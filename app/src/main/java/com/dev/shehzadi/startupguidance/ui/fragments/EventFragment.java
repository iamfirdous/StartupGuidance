package com.dev.shehzadi.startupguidance.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.EventModel;
import com.dev.shehzadi.startupguidance.ui.activities.AddEventActivity;
import com.dev.shehzadi.startupguidance.ui.activities.HomeActivity;
import com.dev.shehzadi.startupguidance.ui.adapters.EventAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    public EventFragment() {
        // Required empty public constructor
    }

    private View view;
    private FrameLayout frameLayoutProgressBar;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<EventModel> events;

    private boolean loaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event, container, false);

        getActivity().setTitle("Upcoming events");

        ((HomeActivity)getActivity()).getFAB().setOnClickListener(view -> {
            Intent addEventIntent = new Intent(getActivity(), AddEventActivity.class);
            startActivity(addEventIntent);
        });

        frameLayoutProgressBar = view.findViewById(R.id.frameLayout_progressBar_event);
        recyclerView = view.findViewById(R.id.recyclerView_event);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        events = new ArrayList<>();
        adapter = new EventAdapter(getContext(), events);

        recyclerView.setAdapter(adapter);

        initView();

        return view;
    }

    public void initView(){
        DatabaseReference eventsReference = FirebaseDatabase.getInstance().getReference("Events");

        eventsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!loaded)
                    frameLayoutProgressBar.setVisibility(View.VISIBLE);
                events.clear();
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    events.add(eventSnapshot.getValue(EventModel.class));
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
