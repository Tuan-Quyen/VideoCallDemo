package com.nhat910.videocalldemo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.holder.PopUpMenuViewHolder;
import com.nhat910.videocalldemo.interfaces.ItemClickListener;

import java.util.List;

public class PopUpMenuAdapter extends RecyclerView.Adapter<PopUpMenuViewHolder> {
    private List<String> _menuList;
    private ItemClickListener.OnItemClick listener;

    public PopUpMenuAdapter(List<String> _menuList) {
        this._menuList = _menuList;
    }

    public void setListener(ItemClickListener.OnItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PopUpMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_popup_menu, viewGroup, false);
        return new PopUpMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopUpMenuViewHolder popUpMenuViewHolder, int i) {
        popUpMenuViewHolder.tvMenu.setText(_menuList.get(i));
        popUpMenuViewHolder.btnMenu.setOnClickListener(v -> {
            if(listener != null){
                listener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _menuList != null ? _menuList.size() : 0;
    }
}
