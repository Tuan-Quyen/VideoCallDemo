package com.nhat910.videocalldemo.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nhat910.videocalldemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatReceiveHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.itemReceiveChat_tvContent)
    public TextView tvContent;
    @BindView(R.id.itemReceiveChat_tvTime)
    public TextView tvTime;

    public ChatReceiveHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

