package com.nhat910.videocalldemo.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.adapters.ChatAdapter;
import com.nhat910.videocalldemo.interfaces.ReceiveChatListener;
import com.nhat910.videocalldemo.others.Constant;
import com.nhat910.videocalldemo.ui.base.BaseFragment;
import com.nhat910.videocalldemo.ui.videocall.VideoCallActivity;
import com.nhat910.videocalldemo.utils.AppUtils;
import com.nhat910.videocalldemo.utils.WebrtcSessionManagement;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ChatFragment extends BaseFragment implements ChatContract.ChatView {
    @BindView(R.id.fragChat_tvOppnentNameTitle)
    TextView tvOppnentTitle;
    @BindView(R.id.fragChat_rv)
    RecyclerView rvChat;
    @BindView(R.id.fragChat_etMessage)
    EditText etMessage;
    @BindView(R.id.fragChat_btnSend)
    ImageView btnSend;
    @BindView(R.id.fragChat_btnCall)
    ImageView btnCall;
    @BindView(R.id.fragChat_topBar)
    LinearLayout llTopBar;

    ChatPresenterImp presenterImp;
    QBChatDialog qbChatDialog;
    ChatAdapter chatAdapter;
    ArrayList<QBChatMessage> _data;
    ReceiveChatListener listener;
    boolean isVideo;

    public static ChatFragment newInstance(QBChatDialog qbChatDialog, Boolean isVideo,@Nullable ReceiveChatListener listener) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.DATA_SEND_CHAT_DIALOG, qbChatDialog);
        args.putBoolean(Constant.CHAT_IN_VIDEO, isVideo);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    private void setListener(ReceiveChatListener listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        presenterImp = new ChatPresenterImp();
        presenterImp.onAttach(this);
        initView();
        return view;
    }

    private void initView() {
        _data = new ArrayList<>();
        if (getArguments() != null) {
            qbChatDialog = (QBChatDialog) getArguments().getSerializable(Constant.DATA_SEND_CHAT_DIALOG);
            isVideo = getArguments().getBoolean(Constant.CHAT_IN_VIDEO);
            if (isVideo) {
                llTopBar.setVisibility(View.INVISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                btnCall.setVisibility(View.GONE);
            }
            if (qbChatDialog != null) {
                tvOppnentTitle.setText(qbChatDialog.getName());
                presenterImp.getHistoryMessage(qbChatDialog);
                presenterImp.inComingMessage(qbChatDialog);
            }
        }
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(_data);
        rvChat.setAdapter(chatAdapter);
    }

    @Override
    public void onDestroy() {
        presenterImp.leaveDialog(qbChatDialog);
        presenterImp.onDetach();
        super.onDestroy();
    }

    @OnClick({R.id.fragChat_btnBack, R.id.fragChat_btnSend, R.id.fragChat_btnCall})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.fragChat_btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.fragChat_btnSend:
                if (!TextUtils.isEmpty(etMessage.getText().toString()))
                    presenterImp.sendMessage(qbChatDialog, etMessage.getText().toString());
                break;
            case R.id.fragChat_btnCall:
                for (int i = 0; i < qbChatDialog.getOccupants().size(); i++) {
                    if (!qbChatDialog.getOccupants().get(i).equals(QBChatService.getInstance().getUser().getId())) {
                        presenterImp.startCall(qbChatDialog.getOccupants().get(i));
                    }
                }
                break;
        }
    }

    @OnTextChanged(R.id.fragChat_etMessage)
    public void OnTextChange(CharSequence charSequence) {
        if (charSequence.length() == 0 && !isVideo) {
            btnCall.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
        } else {
            btnSend.setVisibility(View.VISIBLE);
            btnCall.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadSuccess(ArrayList<QBChatMessage> qbChatMessages) {
        Collections.reverse(qbChatMessages);
        _data.addAll(qbChatMessages);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadError(String errorMessage) {
        AppUtils.showDialogMessage(getContext(), getString(R.string.error), errorMessage, null);
    }

    @Override
    public void receivedMessage(QBChatMessage qbChatMessage) {
        _data.add(0, qbChatMessage);
        chatAdapter.notifyDataSetChanged();
        if(listener != null){
            listener.receiveChat(qbChatMessage);
        }
    }

    @Override
    public void sendMessage(QBChatMessage qbChatMessage) {
        _data.add(0, qbChatMessage);
        chatAdapter.notifyDataSetChanged();
        etMessage.setText("");
    }

    @Override
    public void prepareToCall(ArrayList<Integer> userCall, QBRTCTypes.QBConferenceType qbConferenceType) {
        QBRTCSession qbrtcSession = QBRTCClient.getInstance(getContext()).createNewSessionWithOpponents(userCall, qbConferenceType);
        WebrtcSessionManagement.INSTANCE.setCurrentSession(qbrtcSession);
        qbrtcSession.startCall(new HashMap<String, String>());
        Intent intent = new Intent(getActivity(), VideoCallActivity.class);
        intent.putExtra(Constant.DATA_SEND_CHAT_DIALOG, qbChatDialog);
        startActivity(intent);
    }
}
