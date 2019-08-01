package com.nhat910.videocalldemo.ui.receivecall;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.interfaces.ReceiveCallListener;
import com.nhat910.videocalldemo.others.Constant;
import com.nhat910.videocalldemo.ui.base.BaseFragment;
import com.nhat910.videocalldemo.utils.AppUtils;
import com.quickblox.users.model.QBUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveCallFragment extends BaseFragment implements ReceiveCallContract.ReceiveCallView {
    @BindView(R.id.fragReceiveCall_imgName)
    TextView imgName;
    @BindView(R.id.fragReceiveCall_tvCalling)
    TextView tvCallingName;

    ReceiveCallPresenterImp presenterImp;

    ReceiveCallListener listener;

    public static ReceiveCallFragment newInstance(Integer callerId,ReceiveCallListener listener) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.CALLER_ID, callerId);
        ReceiveCallFragment fragment = new ReceiveCallFragment();
        fragment.setListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    private void setListener(ReceiveCallListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive_call, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        presenterImp = new ReceiveCallPresenterImp();
        presenterImp.onAttach(this);
        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterImp.onDetach();
    }

    private void initView() {
        if (getArguments() != null) {
            presenterImp.getOpponentInfo((Integer) getArguments().getSerializable(Constant.CALLER_ID));
        }
    }


    @OnClick({R.id.fragReceiveCall_btnAccept, R.id.fragReceiveCall_btnReject})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.fragReceiveCall_btnReject:
                if(listener != null){
                    listener.onRejectCallListener();
                }
                break;
            case R.id.fragReceiveCall_btnAccept:
                if(listener != null){
                    listener.onAcceptCallListener();
                }
                break;
        }
    }

    @Override
    public void loadSuccess(QBUser qbUser) {
        tvCallingName.setText(qbUser.getFullName());
        imgName.setText(String.valueOf(qbUser.getFullName().charAt(0)).toUpperCase());
    }

    @Override
    public void loadFailed(String message) {
        AppUtils.showDialogMessage(getContext(), getString(R.string.error), message, null);
    }
}
