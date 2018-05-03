package com.dev.firdous.startupguidance.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.firdous.startupguidance.R;
import com.dev.firdous.startupguidance.models.StartupStoryModel;
import com.dev.firdous.startupguidance.ui.adapters.StartupStoryAdapter;

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
    private RecyclerView recyclerView;
    private StartupStoryAdapter adapter;
    private List<StartupStoryModel> startupStories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_startup_story, container, false);

        getActivity().setTitle("Startup stories");

        recyclerView = view.findViewById(R.id.recyclerView_startup_stories);
        adapter = new StartupStoryAdapter(getContext(), getStartupStories());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    public List<StartupStoryModel> getStartupStories(){
        startupStories = new ArrayList<>();

        StartupStoryModel startupStoryModel1 = new StartupStoryModel();
        startupStoryModel1.setStoryTitle("The billion dollar Mu Sigma story");
        startupStoryModel1.setAuthorName("Team YourStory");

        StartupStoryModel startupStoryModel2 = new StartupStoryModel();
        startupStoryModel2.setStoryTitle("Rotimatic: Rotis at a click of a button");
        startupStoryModel2.setAuthorName("Jubin Mehta");

        StartupStoryModel startupStoryModel4 = new StartupStoryModel();
        startupStoryModel4.setStoryTitle("Meet Oravel's 19-year-old founder Ritesh Agarwal");
        startupStoryModel4.setAuthorName("Jubin Mehta");

        StartupStoryModel startupStoryModel5 = new StartupStoryModel();
        startupStoryModel5.setStoryTitle("Conned by a travel agent, Rikant Pitti co-founded a multi crore empire - EaseMyTrip story");
        startupStoryModel5.setAuthorName("Aditya Bhushan Dwivedi");

        StartupStoryModel startupStoryModel3 = new StartupStoryModel();
        startupStoryModel3.setStoryTitle("Shared cab service Cubito launches in Bangalore");
        startupStoryModel3.setAuthorName("Jubin Mehta");

        startupStories.add(startupStoryModel1);
        startupStories.add(startupStoryModel2);
        startupStories.add(startupStoryModel3);
        startupStories.add(startupStoryModel4);
        startupStories.add(startupStoryModel5);

        return startupStories;
    }

}
