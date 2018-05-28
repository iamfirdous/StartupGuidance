package com.dev.shehzadi.startupguidance.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.EventModel;
import com.dev.shehzadi.startupguidance.ui.activities.AddEventActivity;
import com.dev.shehzadi.startupguidance.ui.activities.HomeActivity;
import com.dev.shehzadi.startupguidance.ui.adapters.EventAdapter;

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
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<EventModel> events;

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

        recyclerView = view.findViewById(R.id.recyclerView_event);
        adapter = new EventAdapter(getContext(), getEvents());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    public List<EventModel> getEvents(){
        events = new ArrayList<>();

        EventModel eventModel1 = new EventModel();
        eventModel1.setMaxRegistrationCount(R.drawable.business_meet);
        eventModel1.setEventTitle("Business Meet");
        eventModel1.setEventDate(new Date(2018, 4, 16));

        EventModel eventModel2 = new EventModel();
        eventModel2.setMaxRegistrationCount(R.drawable.skill_development);
        eventModel2.setEventTitle("Skill Development Meet");
        eventModel2.setEventDate(new Date(2018, 4, 16));

        EventModel eventModel3 = new EventModel();
        eventModel3.setMaxRegistrationCount(R.drawable.webdevfinal);
        eventModel3.setEventTitle("Web Development workshop");
        eventModel3.setEventDate(new Date(2018, 4, 16));

        EventModel eventModel4 = new EventModel();
        eventModel4.setMaxRegistrationCount(R.drawable.skill_development);
        eventModel4.setEventTitle("Skill Development Meet");
        eventModel4.setEventDate(new Date(2018, 4, 16));

        EventModel eventModel5 = new EventModel();
        eventModel5.setMaxRegistrationCount(R.drawable.business_meet);
        eventModel5.setEventTitle("Business Meet");
        eventModel5.setEventDate(new Date(2018, 4, 16));

        events.add(eventModel1);
        events.add(eventModel2);
        events.add(eventModel3);
        events.add(eventModel4);
        events.add(eventModel5);

        return events;
    }

}
