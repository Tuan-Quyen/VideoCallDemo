package com.nhat910.videocalldemo.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nhat910.videocalldemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatSenderHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.itemSendChat_tvContent)
    public TextView tvContent;
    @BindView(R.id.itemSendChat_tvTime)
    public TextView tvTime;

    public ChatSenderHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
