package com.nhat910.videocalldemo.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nhat910.videocalldemo.ui.base.BasePresenter;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class HomePresenterImp extends BasePresenter<HomeContract.HomeView> implements HomeContract.HomePresenter {

    @Override
    public void getAllUser() {
        getView().showLoading();
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                for (int i = 0; i < qbUsers.size(); i++) {
                    if (qbUsers.get(i).getId().equals(QBChatService.getInstance().getUser().getId()))
                        qbUsers.remove(qbUsers.get(i));
                }
                getQBDialog(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {
                getView().loadError(e.getMessage());
                getView().hideLoading();
            }
        });


    }

    private void getQBDialog(@Nullable ArrayList<QBUser> qbUsers) {
        QBRequestGetBuilder qbDialogRequestBuilder = new QBRequestGetBuilder();
        qbDialogRequestBuilder.ctn("occupants_ids", QBChatService.getInstance().getUser().getId());
        QBRestChatService.getChatDialogs(null, qbDialogRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                if (qbUsers != null) {
                    getView().loadUserSuccess(qbUsers);
                    getView().hideLoading();
                }
                getView().loadQBChatDialog(qbChatDialogs);
            }

            @Override
            public void onError(QBResponseException e) {
                getView().loadError(e.getMessage());
                getView().hideLoading();
            }
        });
    }


    @Override
    public void inComingMessage() {
        QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                getQBDialog(null);
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

            }
        });
    }

    @Override
    public void createPrivateChat(QBUser qbUser) {
        getView().showLoading();
        QBChatDialog qbChatDialog = DialogUtils.buildPrivateDialog(qbUser.getId());
        qbChatDialog.setName(qbUser.getFullName());
        QBRestChatService.createChatDialog(qbChatDialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                qbChatDialog.initForChat(QBChatService.getInstance());
                getQBDialog(null);
                getView().createChatSuccess(qbChatDialog);
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
    public void logOutUser() {
        boolean isLoggedIn = QBChatService.getInstance().isLoggedIn();
        if (!isLoggedIn) {
            getView().loadError("Error");
            return;
        }
        QBChatService.getInstance().logout(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                getView().logOutSuccess();
            }

            @Override
            public void onError(QBResponseException e) {
                getView().loadError(e.getMessage());
            }
        });
    }
}
