package com.nhat910.videocalldemo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.adapters.HomeUserAdapter;
import com.nhat910.videocalldemo.interfaces.ItemClickListener;
import com.nhat910.videocalldemo.others.Constant;
import com.nhat910.videocalldemo.others.MenuPopupDialog;
import com.nhat910.videocalldemo.ui.auth.AuthActivity;
import com.nhat910.videocalldemo.ui.base.BaseFragment;
import com.nhat910.videocalldemo.ui.chat.ChatFragment;
import com.nhat910.videocalldemo.utils.AppUtils;
import com.nhat910.videocalldemo.utils.SharedPrefUtils;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements HomeContract.HomeView, ItemClickListener.OnItemClick, ItemClickListener.OnPopUpClick {
    HomePresenterImp presenterImp;
    @BindView(R.id.fragHome_rvUser)
    RecyclerView rvUser;
    HomeUserAdapter homeUserAdapter;
    List<QBUser> _data;
    List<QBChatDialog> _dataDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        presenterImp = new HomePresenterImp();
        presenterImp.onAttach(this);
        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        presenterImp.onDetach();
        super.onDestroy();
    }

    private void initView() {
        _data = new ArrayList<>();
        _dataDialog = new ArrayList<>();
        presenterImp.getAllUser();
        presenterImp.inComingMessage();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        rvUser.setLayoutManager(linearLayoutManager);
        homeUserAdapter = new HomeUserAdapter(_data, _dataDialog);
        homeUserAdapter.setListener(this);
        rvUser.setAdapter(homeUserAdapter);
    }

    @OnClick({R.id.fragHome_btnRefresh, R.id.fragHome_btnMore})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.fragHome_btnRefresh:
                _data.clear();
                _dataDialog.clear();
                presenterImp.getAllUser();
                break;
            case R.id.fragHome_btnMore:
                MenuPopupDialog menuPopupDialog = new MenuPopupDialog(this.getContext());
                menuPopupDialog.showAsDropDown(view);
                menuPopupDialog.setItemClickListener(this);
                break;
        }
    }

    @Override
    public void loadUserSuccess(List<QBUser> _data) {
        this._data.addAll(_data);
    }

    @Override
    public void loadError(String errorMessage) {
        AppUtils.showDialogMessage(getContext(), getString(R.string.error), errorMessage, null);
    }

    @Override
    public void loadQBChatDialog(List<QBChatDialog> _dataDialog) {
        this._dataDialog.clear();
        this._dataDialog.addAll(_dataDialog);
        homeUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void createChatSuccess(QBChatDialog qbChatDialog) {
        addFragment(ChatFragment.newInstance(qbChatDialog, false, null), true);
    }

    @Override
    public void logOutSuccess() {
        SharedPrefUtils.setString(getContext(), Constant.USER_ID, null);
        SharedPrefUtils.setString(getContext(), Constant.USER_PASS, null);
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onItemClick(int position) {
        presenterImp.createPrivateChat(_data.get(position));
    }

    @Override
    public void onItemClickPopUp(String type) {
        switch (type) {
            case Constant.LOG_OUT:
                presenterImp.logOutUser();
                break;
        }
    }
}
