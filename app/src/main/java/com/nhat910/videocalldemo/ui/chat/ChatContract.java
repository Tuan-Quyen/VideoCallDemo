package com.nhat910.videocalldemo.ui.chat;

import com.nhat910.videocalldemo.ui.base.MvpView;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.util.ArrayList;

public interface ChatContract {
    interface ChatView extends MvpView {
        void loadSuccess(ArrayList<QBChatMessage> qbChatMessages);

        void loadError(String errorMessage);

        void receivedMessage(QBChatMessage qbChatMessage);

        void sendMessage(QBChatMessage qbChatMessage);

        void prepareToCall(ArrayList<Integer> userCall, QBRTCTypes.QBConferenceType qbConferenceType);
    }

    interface ChatPresenter{
        void getHistoryMessage(QBChatDialog qbChatDialog);

        void inComingMessage(QBChatDialog qbChatDialog);

        void sendMessage(QBChatDialog qbChatDialog,String message);

        void leaveDialog(QBChatDialog qbChatDialog);

        void startCall(Integer id);
    }
}
