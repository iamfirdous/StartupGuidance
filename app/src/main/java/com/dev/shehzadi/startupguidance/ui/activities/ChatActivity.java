package com.dev.shehzadi.startupguidance.ui.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.ChatModel;
import com.dev.shehzadi.startupguidance.models.UserModel;
import com.dev.shehzadi.startupguidance.ui.adapters.ChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.dev.shehzadi.startupguidance.utils.Util.getTimeStamp;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageView ivButtonSend;

    private UserModel otherUser, thisUser;

    private ChatAdapter adapter;
    private List<ChatModel> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        otherUser = (UserModel) getIntent().getSerializableExtra("user");
        setTitle(otherUser.getFullName());

        thisUser = new UserModel();
        thisUser.setUid(FirebaseAuth.getInstance().getUid());
        thisUser.setFullName(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        recyclerView = findViewById(R.id.recyclerView_messagesList_chat);
        etMessage = findViewById(R.id.editText_message);
        ivButtonSend = findViewById(R.id.imageButton_send);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        chats = new ArrayList<>();
        adapter = new ChatAdapter(this, chats, thisUser, otherUser);

        recyclerView.setAdapter(adapter);

        loadChats();

        ivButtonSend.setOnClickListener(view -> {
            ChatModel chat = new ChatModel();
            chat.setFromUid(thisUser.getUid());
            chat.setToUid(otherUser.getUid());

            chat.setChatMessage(etMessage.getText().toString().trim());

            if (!TextUtils.isEmpty(chat.getChatMessage())) {
                chat.setChatTime(getTimeStamp());
                DatabaseReference thisUserChatReference = FirebaseDatabase
                        .getInstance()
                        .getReference("Chats")
                        .child(thisUser.getUid())
                        .child(otherUser.getUid())
                        .push();
                chat.setChatId(thisUserChatReference.getKey());
                thisUserChatReference.setValue(chat);

                DatabaseReference otherUserChatReference = FirebaseDatabase
                        .getInstance()
                        .getReference("Chats")
                        .child(otherUser.getUid())
                        .child(thisUser.getUid())
                        .child(chat.getChatId());
                otherUserChatReference.setValue(chat);
                etMessage.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    private void loadChats() {
        DatabaseReference chatsReference = FirebaseDatabase
                .getInstance()
                .getReference("Chats")
                .child(thisUser.getUid())
                .child(otherUser.getUid());

        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot chatSnapshot: dataSnapshot.getChildren()) {
                    chats.add(chatSnapshot.getValue(ChatModel.class));
                }
                recyclerView.scrollToPosition(chats.size() - 1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
