package com.nhat910.videocalldemo.others;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.adapters.PopUpMenuAdapter;
import com.nhat910.videocalldemo.interfaces.ItemClickListener;
import com.nhat910.videocalldemo.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuPopupDialog extends PopupWindow implements ItemClickListener.OnItemClick {
    @BindView(R.id.popupMenu_rv)
    RecyclerView rvDialogSearch;
    private List<String> _menuList;
    private ItemClickListener.OnPopUpClick listener;

    public void setItemClickListener(ItemClickListener.OnPopUpClick listener) {
        this.listener = listener;
    }

    public MenuPopupDialog(Context context) {
        super(context);
        OnCreate(context);
    }

    private void OnCreate(Context context) {
        View layoutInflater = LayoutInflater.from(context).inflate(R.layout.dialog_popup_menu, null);
        setContentView(layoutInflater);
        ButterKnife.bind(this, layoutInflater);
        initView(context);
    }

    private void initView(Context context){
        _menuList = AppUtils.grenerateHomeMenu();
        this.setOutsideTouchable(true);
        PopUpMenuAdapter menuAdapter = new PopUpMenuAdapter(_menuList);
        rvDialogSearch.setLayoutManager(new LinearLayoutManager(context));
        rvDialogSearch.setAdapter(menuAdapter);
        menuAdapter.setListener(this);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void onItemClick(int position) {
        if (listener != null) {
            listener.onItemClickPopUp(_menuList.get(position));
        }
        dismiss();
    }
}
