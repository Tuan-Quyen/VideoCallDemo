package com.nhat910.videocalldemo.holder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhat910.videocalldemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeUserHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.itemUserHome_imgName)
    public TextView imgName;
    @BindView(R.id.itemUserHome_tvUser)
    public TextView tvUser;
    @BindView(R.id.itemUserHome_imgOnline)
    public ImageView imgOnline;
    @BindView(R.id.itemUserHome_tvLastMessage)
    public TextView tvLastMessage;
    @BindView(R.id.itemUserHome_cl)
    public ConstraintLayout clBtn;
    @BindView(R.id.itemUserHome_btnUnread)
    public FrameLayout frUnread;
    @BindView(R.id.itemUserHome_tvUnreadCount)
    public TextView tvUnread;

    public HomeUserHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
