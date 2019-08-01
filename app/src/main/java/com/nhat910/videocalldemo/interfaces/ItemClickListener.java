package com.nhat910.videocalldemo.interfaces;

public interface ItemClickListener {
    interface OnItemClick {
        void onItemClick(int position);
    }

    interface OnPopUpClick {
        void onItemClickPopUp(String type);
    }
}
