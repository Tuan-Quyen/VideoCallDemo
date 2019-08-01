package com.nhat910.videocalldemo.ui.receivecall;

import android.os.Bundle;

import com.nhat910.videocalldemo.ui.base.BasePresenter;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class ReceiveCallPresenterImp extends BasePresenter<ReceiveCallContract.ReceiveCallView> implements ReceiveCallContract.ReceiveCallPresenter {
    @Override
    public void getOpponentInfo(Integer opponentId) {
        QBUsers.getUser(opponentId).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                if (getView() != null)
                    getView().loadSuccess(qbUser);
            }

            @Override
            public void onError(QBResponseException e) {
                getView().loadFailed(e.getMessage());
            }
        });
    }
}
