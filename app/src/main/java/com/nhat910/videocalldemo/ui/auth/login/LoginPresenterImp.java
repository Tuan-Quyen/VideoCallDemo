package com.nhat910.videocalldemo.ui.auth.login;

import android.os.Bundle;

import com.nhat910.videocalldemo.ui.base.BasePresenter;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class LoginPresenterImp extends BasePresenter<LoginContract.LoginView> implements LoginContract.LoginPresenter {
    @Override
    public void loginAuth(String userName, String password) {
        getView().showLoading();
        QBUser qbUserOrigin = new QBUser(userName, password);
        QBUsers.signIn(qbUserOrigin).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                QBChatService.getInstance().login(qbUserOrigin, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        getView().loginSuccess(qbUserOrigin);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        getView().loginError(e.getMessage());
                    }
                });
                getView().hideLoading();
            }

            @Override
            public void onError(QBResponseException e) {
                getView().loginError(e.getMessage());
                getView().hideLoading();
            }
        });
    }

    @Override
    public void getUserWithId(String id, String pass) {
        getView().showLoading();
        QBUsers.getUser(Integer.parseInt(id)).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                qbUser.setPassword(pass);
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        getView().loginSuccess(qbUser);
                        getView().hideLoading();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        getView().loginError(e.getMessage());
                        getView().hideLoading();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                getView().loginError(e.getMessage());
                getView().hideLoading();
            }
        });
    }
}
