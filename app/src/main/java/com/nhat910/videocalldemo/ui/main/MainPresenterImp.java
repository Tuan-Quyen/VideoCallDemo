package com.nhat910.videocalldemo.ui.main;

import android.os.Bundle;
import android.util.Log;

import com.nhat910.videocalldemo.ui.base.BasePresenter;
import com.nhat910.videocalldemo.utils.AppUtils;
import com.nhat910.videocalldemo.utils.WebrtcSessionManagement;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;

import java.util.Map;

public class MainPresenterImp extends BasePresenter<MainContract.MainView> implements MainContract.MainPresenter, QBRTCClientSessionCallbacks {
    @Override
    public void addSignaling(QBRTCClient qbrtcClient) {
        if (QBChatService.getInstance().isLoggedIn()) {
            QBChatService.getInstance().getVideoChatWebRTCSignalingManager().addSignalingManagerListener((qbSignaling, b) -> qbrtcClient.addSignaling(qbSignaling));
            QBRTCConfig.setDebugEnabled(true);
            qbrtcClient.addSessionCallbacksListener(this);
            qbrtcClient.prepareToProcessCalls();
        }
    }

    @Override
    public void createPrivateDialog(Integer opponentId) {
        getView().showLoading();
        QBUsers.getUser(opponentId).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                createRoomChat(qbUser);
            }

            @Override
            public void onError(QBResponseException e) {
                getView().loadError(e.getMessage());
                getView().hideLoading();
            }
        });

    }

    private void createRoomChat(QBUser qbUser) {
        QBChatDialog qbChatDialog = DialogUtils.buildPrivateDialog(qbUser.getId());
        qbChatDialog.setName(qbUser.getFullName());
        QBRestChatService.createChatDialog(qbChatDialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                getView().createRoomChatSuccess(qbChatDialog);
                getView().hideLoading();
            }

            @Override
            public void onError(QBResponseException e) {
                getView().loadError(e.getMessage());
                getView().hideLoading();
            }
        });
    }

    @Override
    public void onReceiveNewSession(QBRTCSession qbrtcSession) {
        getView().inComingCall(qbrtcSession);
    }

    @Override
    public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {
    }

    @Override
    public void onSessionStartClose(QBRTCSession qbrtcSession) {
    }

    @Override
    public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {
    }

    @Override
    public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
        Log.e("Reject", "rejected");
    }

    @Override
    public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
        Log.e("Accepted", "accept");
    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
        Log.e("Receive hang up", qbrtcSession.getSessionID());

    }

    @Override
    public void onSessionClosed(QBRTCSession qbrtcSession) {
        if (getView() != null && !AppUtils.isVideoHangUp)
            getView().closeReceiveCallFragment();
    }
}
