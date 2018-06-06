package com.dev.shehzadi.startupguidance.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.ChatModel;
import com.dev.shehzadi.startupguidance.models.UserModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterViewHolder>{

    private List<ChatModel> chats;
    private Context context;
    private UserModel thisUser, otherUser;

    public ChatAdapter(Context context, List<ChatModel> chats, UserModel thisUser, UserModel otherUser) {
        this.chats = chats;
        this.context = context;
        this.thisUser = thisUser;
        this.otherUser = otherUser;
    }

    @NonNull
    @Override
    public ChatAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem_chat, parent, false);
        return new ChatAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapterViewHolder holder, int position) {

        ChatModel chat = chats.get(position);

        TextView tvChatMessage = null, tvUserFirstLetter = null;
        char userFirstLetter = ' ';

        if (chat.getFromUid().equals(otherUser.getUid())) {
            tvChatMessage = holder.tvChatOther;
            tvUserFirstLetter = holder.tvOtherUserFirstLetter;
            userFirstLetter = otherUser.getFullName().charAt(0);

            holder.tvChatOther.setVisibility(View.VISIBLE);
            holder.tvOtherUserFirstLetter.setVisibility(View.VISIBLE);
            holder.tvChatMine.setVisibility(View.GONE);
            holder.tvThisUserFirstLetter.setVisibility(View.GONE);
        }

        if (chat.getFromUid().equals(thisUser.getUid())) {
            tvChatMessage = holder.tvChatMine;
            tvUserFirstLetter = holder.tvThisUserFirstLetter;
            userFirstLetter = thisUser.getFullName().charAt(0);

            holder.tvChatMine.setVisibility(View.VISIBLE);
            holder.tvThisUserFirstLetter.setVisibility(View.VISIBLE);
            holder.tvChatOther.setVisibility(View.GONE);
            holder.tvOtherUserFirstLetter.setVisibility(View.GONE);
        }

        tvChatMessage.setText(chat.getChatMessage());
        tvUserFirstLetter.setText("" + userFirstLetter);

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ChatAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tvChatOther, tvChatMine, tvOtherUserFirstLetter, tvThisUserFirstLetter;
        View itemView;

        public ChatAdapterViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            tvChatOther = itemView.findViewById(R.id.textView_chatMessage_other);
            tvChatMine = itemView.findViewById(R.id.textView_chatMessage_mine);
            tvOtherUserFirstLetter = itemView.findViewById(R.id.textView_userAlphabet_other);
            tvThisUserFirstLetter = itemView.findViewById(R.id.textView_userAlphabet_mine);
        }
    }
}
