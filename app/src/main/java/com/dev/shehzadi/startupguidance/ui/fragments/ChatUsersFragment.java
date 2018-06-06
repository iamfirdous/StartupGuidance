package com.dev.shehzadi.startupguidance.ui.fragments;


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
import com.dev.shehzadi.startupguidance.models.UserModel;
import com.dev.shehzadi.startupguidance.ui.activities.HomeActivity;
import com.dev.shehzadi.startupguidance.ui.adapters.ChatUsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
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
public class ChatUsersFragment extends Fragment {


    public ChatUsersFragment() {
        // Required empty public constructor
    }

    private View view;
    private FrameLayout frameLayoutProgressBar;
    private RecyclerView recyclerView;

    private ChatUsersAdapter adapter;
    private List<UserModel> users;

    private boolean loaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        getActivity().setTitle("Select a guide to chat");

        FloatingActionButton fab = ((HomeActivity)getActivity()).getFAB();
        fab.setVisibility(View.GONE);

        frameLayoutProgressBar = view.findViewById(R.id.frameLayout_progressBar_chatList);
        recyclerView = view.findViewById(R.id.recyclerView_users_chatList);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        users = new ArrayList<>();
        adapter = new ChatUsersAdapter(getContext(), users);

        recyclerView.setAdapter(adapter);

        initView();

        return view;
    }

    public void initView(){
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!loaded)
                    frameLayoutProgressBar.setVisibility(View.VISIBLE);
                users.clear();
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    UserModel user = eventSnapshot.getValue(UserModel.class);
                    if (user.isSuperUser() && user.isGivingGuidance() && !user.getUid().equals(FirebaseAuth.getInstance().getUid())){
                        users.add(user);
                    }
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
