package com.nhat910.videocalldemo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.holder.ChatReceiveHolder;
import com.nhat910.videocalldemo.holder.ChatSenderHolder;
import com.nhat910.videocalldemo.others.Constant;
import com.nhat910.videocalldemo.utils.AppUtils;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<QBChatMessage> qbChatMessages;

    public ChatAdapter(ArrayList<QBChatMessage> qbChatMessages) {
        this.qbChatMessages = qbChatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        if (qbChatMessages.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId())) {
            return Constant.CHAT_SEND_TYPE;
        } else {
            return Constant.CHAT_RECEIVE_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case Constant.CHAT_SEND_TYPE:
                return new ChatSenderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_send_chat, viewGroup, false));
            case Constant.CHAT_RECEIVE_TYPE:
                return new ChatReceiveHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_receive_chat, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {
            case Constant.CHAT_SEND_TYPE:
                ChatSenderHolder senderHolder = (ChatSenderHolder) viewHolder;
                senderHolder.tvContent.setText(qbChatMessages.get(senderHolder.getAdapterPosition()).getBody());
                senderHolder.tvTime.setText(AppUtils.convertTime(viewHolder.itemView.getContext(), qbChatMessages.get(senderHolder.getAdapterPosition()).getDateSent()));
                break;
            case Constant.CHAT_RECEIVE_TYPE:
                ChatReceiveHolder receiveHolder = (ChatReceiveHolder) viewHolder;
                receiveHolder.tvContent.setText(qbChatMessages.get(receiveHolder.getAdapterPosition()).getBody());
                receiveHolder.tvTime.setText(AppUtils.convertTime(viewHolder.itemView.getContext(), qbChatMessages.get(receiveHolder.getAdapterPosition()).getDateSent()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return qbChatMessages != null ? qbChatMessages.size() : 0;
    }
}
