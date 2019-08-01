package com.nhat910.videocalldemo.ui.main;

import com.nhat910.videocalldemo.ui.base.MvpView;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;

public interface MainContract extends MvpView {
    interface MainView extends MvpView{
        void inComingCall(QBRTCSession qbrtcSession);

        void closeReceiveCallFragment();

        void createRoomChatSuccess(QBChatDialog qbChatDialog);

        void loadError(String error);
    }

    interface MainPresenter{
        void addSignaling(QBRTCClient qbrtcClient);

        void createPrivateDialog(Integer opponentId);
    }
}
