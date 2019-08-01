package com.nhat910.videocalldemo.ui.chat;

import android.os.Bundle;
import android.util.Log;

import com.nhat910.videocalldemo.ui.base.BasePresenter;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

public class ChatPresenterImp extends BasePresenter<ChatContract.ChatView> implements ChatContract.ChatPresenter {
    @Override
    public void getHistoryMessage(QBChatDialog qbChatDialog) {
        getView().showLoading();
        try {
            QBChatService.getInstance().enterActiveState();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(50);
        QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                getView().loadSuccess(qbChatMessages);
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
    public void inComingMessage(QBChatDialog qbChatDialog) {
        if (qbChatDialog != null) {
            QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
            incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
                @Override
                public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                    if (s.equals(qbChatDialog.getDialogId()) && getView() != null) {
                        getView().receivedMessage(qbChatMessage);
                    }
                }

                @Override
                public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

                }
            });
        }
    }

    @Override
    public void sendMessage(QBChatDialog qbChatDialog, String message) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
        qbChatMessage.setBody(message);
        qbChatMessage.setDateSent(System.currentTimeMillis() / 1000);
        qbChatMessage.setSaveToHistory(true);
        qbChatDialog.sendMessage(qbChatMessage, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                getView().sendMessage(qbChatMessage);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("failed",e.getMessage());
            }
        });

    }

    @Override
    public void leaveDialog(QBChatDialog qbChatDialog) {
        try {
            QBChatService.getInstance().enterInactiveState();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startCall(Integer opponentId) {
        QBRTCTypes.QBConferenceType qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
        ArrayList<Integer> userCall = new ArrayList<>();
        userCall.add(opponentId);
        getView().prepareToCall(userCall,qbConferenceType);
    }
}
