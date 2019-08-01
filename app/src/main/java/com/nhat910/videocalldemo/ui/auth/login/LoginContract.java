package com.nhat910.videocalldemo.ui.auth.login;

import com.nhat910.videocalldemo.ui.base.MvpView;
import com.quickblox.users.model.QBUser;

public interface LoginContract {
    interface LoginView extends MvpView {
        void loginSuccess(QBUser qbUser);

        void loginError(String errorMessage);
    }

    interface LoginPresenter {
        void loginAuth(String userName, String password);

        void getUserWithId(String id, String password);
    }
}
