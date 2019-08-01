package com.nhat910.videocalldemo.ui.auth.signup;

import com.nhat910.videocalldemo.ui.base.MvpView;
import com.quickblox.users.model.QBUser;

public interface SignUpContract extends MvpView {
    interface SignUpView extends MvpView {
        void signUpSuccess(QBUser qbUser);

        void signUpError(String errorMessage);
    }

    interface SignUpPresenter {
        void signUpAuth(String fullName, String userName, String password, String email);
    }
}
