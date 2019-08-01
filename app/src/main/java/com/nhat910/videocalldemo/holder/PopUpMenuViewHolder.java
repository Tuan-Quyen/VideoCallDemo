package com.nhat910.videocalldemo.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nhat910.videocalldemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopUpMenuViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.itemPopupMenu_tv)
    public TextView tvMenu;
    @BindView(R.id.itemPopupMenu_btn)
    public FrameLayout btnMenu;

    public PopUpMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
