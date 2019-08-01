package com.nhat910.videocalldemo.ui.home;

import com.nhat910.videocalldemo.ui.base.MvpView;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.util.List;

public interface HomeContract extends MvpView {
    interface HomeView extends MvpView {
        void loadUserSuccess(List<QBUser> _data);

        void loadError(String errorMessage);

        void loadQBChatDialog(List<QBChatDialog> _dataDialog);

        void createChatSuccess(QBChatDialog qbChatDialog);

        void logOutSuccess();
    }

    interface HomePresenter {
        void getAllUser();

        void createPrivateChat(QBUser qbUser);

        void logOutUser();

        void inComingMessage();
    }
}
