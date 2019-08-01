package com.nhat910.videocalldemo.ui.auth.signup;

import android.os.Bundle;

import com.nhat910.videocalldemo.ui.base.BasePresenter;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignUpPresenterImp extends BasePresenter<SignUpContract.SignUpView> implements SignUpContract.SignUpPresenter {
    @Override
    public void signUpAuth(String fullName, String userName, String password, String email) {
        getView().showLoading();
        QBUser qbUser = new QBUser(userName, password, email);
        qbUser.setFullName(fullName);
        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                getView().signUpSuccess(qbUser);
                getView().hideLoading();
            }

            @Override
            public void onError(QBResponseException e) {
                getView().signUpError(e.getMessage());
                getView().hideLoading();
            }
        });
    }
}
