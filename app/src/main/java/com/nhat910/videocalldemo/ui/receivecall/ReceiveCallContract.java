package com.nhat910.videocalldemo.ui.receivecall;

import com.nhat910.videocalldemo.ui.base.MvpView;
import com.quickblox.users.model.QBUser;

public interface ReceiveCallContract {
    interface ReceiveCallView extends MvpView {
        void loadSuccess(QBUser qbUser);

        void loadFailed(String message);
    }

    interface ReceiveCallPresenter{
        void getOpponentInfo(Integer opponentId);
    }
}
